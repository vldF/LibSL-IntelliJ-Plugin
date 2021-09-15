package org.jetbrains.research.libslplugin.psi

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.PsiReferenceBase
import org.antlr.intellij.adaptor.psi.ScopeNode

abstract class LibslElementRef(element: IdentifierNode) : PsiReferenceBase<IdentifierNode>(element, TextRange(0, element.text.length)) {
    override fun resolve(): PsiElement? {
        return (myElement.context as? ScopeNode)?.resolve(myElement)
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        return myElement.setName(newElementName)
    }

    override fun isReferenceTo(element: PsiElement): Boolean {
        val refName = myElement.name
        val elementName = (element as? PsiNameIdentifierOwner)?.nameIdentifier?.text
        if (refName == elementName && element::class.java == myElement::class.java) {
            return true
        }

        return false
    }
}