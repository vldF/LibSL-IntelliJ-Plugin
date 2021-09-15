package org.jetbrains.research.libslplugin

import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import org.antlr.intellij.adaptor.lexer.RuleIElementType
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import org.jetbrains.research.libsl.LibSLParser
import org.jetbrains.research.libslplugin.psi.AutomatonSubtree
import org.jetbrains.research.libslplugin.psi.FunctionSubtree
import org.jetbrains.research.libslplugin.psi.IdentifierNode

class LibslFindUsagesProvider : FindUsagesProvider {
    override fun canFindUsagesFor(psiElement: PsiElement): Boolean =
        psiElement is IdentifierNode || psiElement is AutomatonSubtree || psiElement is FunctionSubtree

    override fun getHelpId(psiElement: PsiElement): String? = null

    override fun getType(element: PsiElement): String {
        return when(((element as? ANTLRPsiNode)?.parent?.node?.elementType as? RuleIElementType)?.ruleIndex) {
            LibSLParser.RULE_functionDecl -> "function"
            LibSLParser.RULE_automatonDecl -> "automaton"
            LibSLParser.RULE_variableDecl -> "variable"
            LibSLParser.RULE_parameter -> "parameter"
            LibSLParser.RULE_typeIdentifier -> "type"

            else -> ""
        }
    }

    override fun getDescriptiveName(element: PsiElement): String = element.text

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String = element.text

    override fun getWordsScanner(): WordsScanner = DefaultWordsScanner(
        LibslParserDefinition.createLexer(null),
        LibslParserDefinition.identifierTokenSet,
        LibslParserDefinition.commentTokens,
        LibslParserDefinition.stringLiteralElements
    )
}