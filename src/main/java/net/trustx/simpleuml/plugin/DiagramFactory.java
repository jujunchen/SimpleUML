 package net.trustx.simpleuml.plugin;

 import com.intellij.openapi.project.Project;
 import com.intellij.openapi.ui.Messages;
 import com.intellij.openapi.vfs.VirtualFile;
 import com.intellij.openapi.vfs.VirtualFileManager;
 import java.io.IOException;
 import java.io.InputStreamReader;
 import java.io.Reader;
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponent;
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponentPanel;
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponentSettings;
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramLoader;
 import net.trustx.simpleuml.classdiagram.configuration.ClassDiagramSettings;
 import net.trustx.simpleuml.components.DiagramComponent;
 import net.trustx.simpleuml.components.DiagramLoader;
 import net.trustx.simpleuml.dependencydiagram.components.DependencyDiagramComponent;
 import net.trustx.simpleuml.dependencydiagram.components.DependencyDiagramComponentPanel;
 import net.trustx.simpleuml.dependencydiagram.components.DependencyDiagramComponentSettings;
 import net.trustx.simpleuml.dependencydiagram.components.DependencyDiagramLoader;
 import net.trustx.simpleuml.dependencydiagram.configuration.DependencyDiagramSettings;
 import net.trustx.simpleuml.packagediagram.components.PackageDiagramComponent;
 import net.trustx.simpleuml.packagediagram.components.PackageDiagramComponentPanel;
 import net.trustx.simpleuml.packagediagram.components.PackageDiagramComponentSettings;
 import net.trustx.simpleuml.packagediagram.components.PackageDiagramLoader;
 import net.trustx.simpleuml.packagediagram.configuration.PackageDiagramSettings;
 import net.trustx.simpleuml.sequencediagram.components.SequenceDiagramComponent;
 import net.trustx.simpleuml.sequencediagram.components.SequenceDiagramLoader;
 import org.jdom.Document;
 import org.jdom.Element;
 import org.jdom.JDOMException;
 import org.jdom.input.SAXBuilder;



































 public class DiagramFactory
 {
   public static final String CLASSDIAGRAM_KEY = "Classdiagram";
   public static final String PACKAGEDIAGRAM_KEY = "Packagediagram";
   public static final String DEPENDENCYDIAGRAM_KEY = "Dependencydiagram";
   public static final String SEQUENCEDIAGRAM_KEY = "Sequencediagram";
   public static final String GENERAL_KEY = "General";
   private static final String[] SUPPORTED_DIAGRAM_TYPES = new String[] { "Classdiagram", "Dependencydiagram", "Packagediagram", "Sequencediagram" };












   public static DiagramComponent createDiagramComponent(Project project, String folderURL, String name) throws UnknownDiagramTypeException {
     Document doc;
     SequenceDiagramLoader sequenceDiagramLoader;
     try {
       doc = loadFile(folderURL, name);
     }
     catch (Exception ex) {

       Messages.showMessageDialog(project, "File format is not supported", "ERROR", Messages.getErrorIcon());
       return null;
     }

     Element root = doc.getRootElement();
     String nodeName = root.getName();
     DiagramLoader diagramLoader = null;

     if ("Classdiagram".equalsIgnoreCase(nodeName)) {

       diagramLoader = ClassDiagramLoader.getInstance();
     }
     else if ("Packagediagram".equals(nodeName)) {

       diagramLoader = PackageDiagramLoader.getInstance();
     }
     else if ("Dependencydiagram".equals(nodeName)) {

       diagramLoader = DependencyDiagramLoader.getInstance();
     }
     else if ("Sequencediagram".equals(nodeName)) {

       sequenceDiagramLoader = SequenceDiagramLoader.getInstance();
     }


     if (sequenceDiagramLoader != null)
     {
       return sequenceDiagramLoader.loadFromDocument(project, doc, folderURL, name);
     }

     throw new UnknownDiagramTypeException("Unknown DiagramType: " + nodeName, nodeName);
   }














   public static DiagramComponent createNewDiagramComponent(Project project, String folderURL, String name, String type) throws UnknownDiagramTypeException {
     if ("Classdiagram".equalsIgnoreCase(type)) {

       UMLToolWindowPlugin umlToolWindowPlugin = UMLToolWindowPlugin.getUMLToolWindowPlugin(project);
       ClassDiagramComponent classDiagramComponent = new ClassDiagramComponent((ClassDiagramSettings)umlToolWindowPlugin.getConfigurationFactory().getDiagramSettings("Classdiagram"), new ClassDiagramComponentSettings(), project);
       ClassDiagramComponentPanel classDiagramComponentPanel = new ClassDiagramComponentPanel(classDiagramComponent, folderURL, name);
       umlToolWindowPlugin.addDiagramComponent((DiagramComponent)classDiagramComponentPanel);
       umlToolWindowPlugin.showDiagramComponent((DiagramComponent)classDiagramComponentPanel);
       return (DiagramComponent)classDiagramComponentPanel;
     }
     if ("Packagediagram".equalsIgnoreCase(type)) {

       UMLToolWindowPlugin umlToolWindowPlugin = UMLToolWindowPlugin.getUMLToolWindowPlugin(project);
       PackageDiagramComponent packageDiagramComponent = new PackageDiagramComponent((PackageDiagramSettings)umlToolWindowPlugin.getConfigurationFactory().getDiagramSettings("Packagediagram"), new PackageDiagramComponentSettings(), project);
       PackageDiagramComponentPanel packageDiagramComponentPanel = new PackageDiagramComponentPanel(packageDiagramComponent, folderURL, name);
       umlToolWindowPlugin.addDiagramComponent((DiagramComponent)packageDiagramComponentPanel);
       umlToolWindowPlugin.showDiagramComponent((DiagramComponent)packageDiagramComponentPanel);
       return (DiagramComponent)packageDiagramComponentPanel;
     }
     if ("Dependencydiagram".equalsIgnoreCase(type)) {

       UMLToolWindowPlugin umlToolWindowPlugin = UMLToolWindowPlugin.getUMLToolWindowPlugin(project);
       DependencyDiagramComponent dependencyDiagramComponent = new DependencyDiagramComponent((DependencyDiagramSettings)umlToolWindowPlugin.getConfigurationFactory().getDiagramSettings("Dependencydiagram"), new DependencyDiagramComponentSettings(), project);
       DependencyDiagramComponentPanel dependencyDiagramComponentPanel = new DependencyDiagramComponentPanel(dependencyDiagramComponent, folderURL, name);
       umlToolWindowPlugin.addDiagramComponent((DiagramComponent)dependencyDiagramComponentPanel);
       umlToolWindowPlugin.showDiagramComponent((DiagramComponent)dependencyDiagramComponentPanel);
       return (DiagramComponent)dependencyDiagramComponentPanel;
     }
     if ("Sequencediagram".equalsIgnoreCase(type)) {

       UMLToolWindowPlugin umlToolWindowPlugin = UMLToolWindowPlugin.getUMLToolWindowPlugin(project);
       SequenceDiagramComponent s = new SequenceDiagramComponent(folderURL, name, project);
       umlToolWindowPlugin.addDiagramComponent((DiagramComponent)s);
       umlToolWindowPlugin.showDiagramComponent((DiagramComponent)s);
       return (DiagramComponent)s;
     }

     throw new UnknownDiagramTypeException("Unknown DiagramType: " + type, type);
   }



   private static Document loadFile(String folderURL, String name) throws IOException, JDOMException {
     VirtualFile child = VirtualFileManager.getInstance().findFileByUrl(folderURL + "/" + name);
     if (child == null) return null;

     SAXBuilder builder = new SAXBuilder();
     Reader reader = new InputStreamReader(child.getInputStream());
     return builder.build(reader);
   }














   public static String[] getSupportedDiagramTypes() {
     return SUPPORTED_DIAGRAM_TYPES;
   }
 }


