package org.jetbrains.research.libslplugin

import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

object LibslFileType : LanguageFileType(LibslLanguage, false) {
    override fun getName(): String = "LibSL file"

    override fun getDescription(): String = "Library specification language file"

    override fun getDefaultExtension(): String = "lsl"

    override fun getIcon(): Icon = IconLoader.getIcon("/icons/jar-gray.png", this::class.java)

}