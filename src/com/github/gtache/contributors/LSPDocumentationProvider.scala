package com.github.gtache.contributors

import java.util

import com.github.gtache.PluginMain
import com.intellij.lang.documentation.DocumentationProvider
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.psi.{PsiElement, PsiManager}

class LSPDocumentationProvider extends DocumentationProvider {
  private val LOG: Logger = Logger.getInstance(classOf[LSPDocumentationProvider])

  LOG.info("HAHA")

  override def getUrlFor(element: PsiElement, originalElement: PsiElement): java.util.List[String] = {
    LOG.info("GETURL")
    new util.ArrayList[String]()
  }

  override def getDocumentationElementForLookupItem(psiManager: PsiManager, object_ : scala.Any, element: PsiElement): PsiElement = {
    LOG.info("GETDOCELEMLOOKUP")
    null
  }

  override def getDocumentationElementForLink(psiManager: PsiManager, link: String, context: PsiElement): PsiElement = {
    LOG.info("GETDOCELEM")
    null
  }

  override def getQuickNavigateInfo(element: PsiElement, originalElement: PsiElement): String = {
    LOG.info("GETQUICK")
    val editor = FileEditorManager.getInstance(element.getProject).getSelectedTextEditor
    val manager = PluginMain.getManagerForEditor(editor)
    if (manager != null) {
      manager.requestDoc(editor, element.getTextOffset)
    } else {
      ""
    }
  }

  override def generateDoc(element: PsiElement, originalElement: PsiElement): String = {
    LOG.info("GETDOC")
    val editor = FileEditorManager.getInstance(element.getProject).getSelectedTextEditor
    val manager = PluginMain.getManagerForEditor(editor)
    if (manager != null) {
      manager.requestDoc(editor, element.getTextOffset)
    } else {
      ""
    }
  }
}