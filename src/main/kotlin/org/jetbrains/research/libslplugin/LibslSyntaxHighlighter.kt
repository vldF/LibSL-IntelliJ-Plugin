package org.jetbrains.research.libslplugin

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.lexer.TokenIElementType
import org.jetbrains.research.libsl.LibSLLexer

class LibslSyntaxHighlighter : SyntaxHighlighterBase() {
    private val empty = TextAttributesKey.EMPTY_ARRAY

    private val id = createTextAttributesKey("LSL_ID", DefaultLanguageHighlighterColors.IDENTIFIER)
    private val number = createTextAttributesKey("LSL_NUMBER", DefaultLanguageHighlighterColors.NUMBER)
    private val keyword = createTextAttributesKey("LSL_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
    private val string = createTextAttributesKey("LSL_STRING", DefaultLanguageHighlighterColors.STRING)
    private val lineComment = createTextAttributesKey("LSL_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
    private val blockComment = createTextAttributesKey("LSL_BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT)

    private val keywords = arrayOf(
        LibSLLexer.AUTOMATON, LibSLLexer.FUN, LibSLLexer.VAR, LibSLLexer.ACTION, LibSLLexer.TRUE, LibSLLexer.FALSE,
        LibSLLexer.REQUIRES, LibSLLexer.ENSURES, LibSLLexer.IMPORT, LibSLLexer.INCLUDE, LibSLLexer.ENUM,
        LibSLLexer.TYPE, LibSLLexer.TYPES, LibSLLexer.TYPEALIAS, LibSLLexer.STATE, LibSLLexer.FINISHSTATE,
        LibSLLexer.INITSTATE, LibSLLexer.LIBSL, LibSLLexer.SHIFT, LibSLLexer.LANGUAGE, LibSLLexer.VERSION,
        LibSLLexer.NEW, LibSLLexer.URL, LibSLLexer.LIBRARY, LibSLLexer.INCLUDE
    )

    override fun getHighlightingLexer(): Lexer = LibslParserDefinition.createLexer(null)

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        if (tokenType !is TokenIElementType) return empty
        val antlrTokenType = tokenType.antlrTokenType
        if (antlrTokenType in keywords) {
            return arrayOf(keyword)
        }

        val attributeKey = when(antlrTokenType) {
            LibSLLexer.Identifier -> id
            LibSLLexer.COMMENT -> blockComment
            LibSLLexer.LINE_COMMENT -> lineComment
            LibSLLexer.DoubleQuotedString -> string
            LibSLLexer.Digit -> number
            LibSLLexer.MINUS -> number

            else -> {
                return empty
            }
        }

        return arrayOf(attributeKey)
    }
}