package org.jetbrains.research.libslplugin.psi

import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.PsiReference
import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.lexer.RuleIElementType
import org.antlr.intellij.adaptor.psi.ANTLRPsiLeafNode
import org.antlr.intellij.adaptor.psi.Trees
import org.jetbrains.research.libsl.LibSLLexer
import org.jetbrains.research.libslplugin.LibslLanguage
import org.jetbrains.research.libslplugin.LibslParserDefinition

class IdentifierNode(type: IElementType?, text: CharSequence?) : ANTLRPsiLeafNode(type, text), PsiNamedElement {
    override fun setName(name: String): PsiElement {
        val newNode = Trees.createLeafFromText(
            project,
            LibslLanguage,
            context,
            name,
            LibslParserDefinition.ID
        )

        if (newNode != null) {
            return  this.replace(newNode)
        }

        return this
    }

    override fun getReference(): PsiReference? {
        val parentType = parent.node.elementType

        return when((parentType as RuleIElementType).ruleIndex) {
            LibSLLexer.FUN -> FunctionRef(this)
            LibSLLexer.AUTOMATON -> AutomatonRef(this)
            LibSLLexer.TYPE, LibSLLexer.TYPEALIAS -> TypeRef(this)

            else -> {
                val log = Logger.getInstance(this::class.java)
                log.error("can't get reference for ${parentType::class.java}")

                null
            }
        }
    }
}