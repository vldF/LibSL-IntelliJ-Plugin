package org.jetbrains.research.libslplugin

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory
import org.antlr.intellij.adaptor.lexer.RuleIElementType
import org.antlr.intellij.adaptor.lexer.TokenIElementType
import org.antlr.intellij.adaptor.parser.ANTLRParserAdaptor
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import org.antlr.v4.runtime.Parser
import org.antlr.v4.runtime.tree.ParseTree
import org.jetbrains.research.libsl.LibSLLexer
import org.jetbrains.research.libsl.LibSLParser
import org.jetbrains.research.libslplugin.psi.AutomatonSubtree
import org.jetbrains.research.libslplugin.psi.FunctionSubtree
import org.jetbrains.research.libslplugin.psi.LibslFileRoot

object LibslParserDefinition : ParserDefinition {
    private val file = IFileElementType(LibslLanguage)
    private var comment: TokenSet
    private var stringLiteral: TokenSet
    private var whitespaces: TokenSet
    var ID: TokenIElementType
    var identifierTokenSet: TokenSet

    init {
        PSIElementTypeFactory.defineLanguageIElementTypes(LibslLanguage, LibSLLexer.tokenNames, LibSLLexer.ruleNames)
        val tokenIElementTypes = PSIElementTypeFactory.getTokenIElementTypes(LibslLanguage)
        ID = tokenIElementTypes[LibSLParser.Identifier]
        comment = PSIElementTypeFactory.createTokenSet(LibslLanguage, LibSLParser.COMMENT, LibSLParser.LINE_COMMENT)
        whitespaces = PSIElementTypeFactory.createTokenSet(LibslLanguage, LibSLParser.WS, LibSLParser.BR)
        stringLiteral = PSIElementTypeFactory.createTokenSet(LibslLanguage, LibSLParser.DoubleQuotedString)
        identifierTokenSet = PSIElementTypeFactory.createTokenSet(LibslLanguage, LibSLParser.Identifier)
    }

    override fun getFileNodeType(): IFileElementType = file

    override fun getCommentTokens(): TokenSet = comment

    override fun getStringLiteralElements(): TokenSet = stringLiteral

    override fun getWhitespaceTokens(): TokenSet = whitespaces

    override fun createFile(viewProvider: FileViewProvider): PsiFile = LibslFileRoot(viewProvider)

    override fun createLexer(project: Project?): Lexer {
        return ANTLRLexerAdaptor(
            LibslLanguage,
            LibSLLexer(null)
        )
    }

    override fun createParser(project: Project): PsiParser {
        return object : ANTLRParserAdaptor(LibslLanguage, LibSLParser(null)) {
            override fun parse(parser: Parser?, root: IElementType?): ParseTree {
                parser as LibSLParser
                return parser.file()
            }
        }
    }

    override fun createElement(node: ASTNode): PsiElement {
        val type = node.elementType
        if (type is TokenIElementType || type !is RuleIElementType) {
            return ANTLRPsiNode(node)
        }
        return when (type.ruleIndex) {
            LibSLLexer.AUTOMATON -> AutomatonSubtree(node, type)
            LibSLLexer.FUN -> FunctionSubtree(node, type)
            else -> ANTLRPsiNode(node)
        }
    }
}