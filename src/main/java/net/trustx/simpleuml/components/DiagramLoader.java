package net.trustx.simpleuml.components;

import com.intellij.openapi.project.Project;
import org.jdom.Document;

public interface DiagramLoader {
  DiagramComponent loadFromDocument(Project paramProject, Document paramDocument, String paramString1, String paramString2);
}


