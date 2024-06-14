 package net.trustx.simpleuml.sequencediagram.components;

 import com.intellij.openapi.project.Project;
 import net.trustx.simpleuml.components.DiagramComponent;
 import net.trustx.simpleuml.components.DiagramLoader;
 import net.trustx.simpleuml.plugin.UMLToolWindowPlugin;
 import org.jdom.Document;
 import org.jdom.Element;














 public class SequenceDiagramLoader
   implements DiagramLoader
 {
   private static SequenceDiagramLoader instance;

   public static SequenceDiagramLoader getInstance() {
     if (instance == null)
     {
       instance = new SequenceDiagramLoader();
     }

     return instance;
   }








   public DiagramComponent loadFromDocument(Project project, Document document, String folderURL, String name) {
     UMLToolWindowPlugin umlToolWindowPlugin = UMLToolWindowPlugin.getUMLToolWindowPlugin(project);
     SequenceDiagramComponent seq = new SequenceDiagramComponent(folderURL, name, project);
     umlToolWindowPlugin.addDiagramComponent(seq);
     Element root = document.getRootElement();
     String def = root.getChildText("Definition");
     seq.getModel().load(project, def, root.getChild("Links"));
     return seq;
   }
 }


