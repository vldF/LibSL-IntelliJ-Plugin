package org.jetbrains.research.libslplugin.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.jetbrains.research.libslplugin.LibslFileType
import org.jetbrains.research.libslplugin.LibslLanguage

class LibslFileRoot(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, LibslLanguage), ScopeNode {
    override fun getContext(): ScopeNode? {
        return null
    }

    override fun resolve(element: PsiNamedElement?): PsiElement? {
        return null
    }

    override fun getFileType(): FileType = LibslFileType
}