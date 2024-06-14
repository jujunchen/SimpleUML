 package net.trustx.simpleuml.dependencydiagram.components;

 import com.intellij.openapi.project.Project;
 import com.intellij.openapi.ui.Messages;
 import com.intellij.psi.JavaPsiFacade;
 import com.intellij.psi.PsiClass;
 import com.intellij.psi.PsiElement;
 import com.intellij.psi.search.GlobalSearchScope;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import net.trustx.simpleuml.components.DiagramComponent;
 import net.trustx.simpleuml.components.DiagramLoader;
 import net.trustx.simpleuml.dependencydiagram.configuration.DependencyDiagramSettings;
 import net.trustx.simpleuml.plugin.UMLToolWindowPlugin;
 import org.jdom.Attribute;
 import org.jdom.Document;
 import org.jdom.Element;


 public class DependencyDiagramLoader
   implements DiagramLoader
 {
   private static final DependencyDiagramLoader instance = new DependencyDiagramLoader();



   public static DiagramLoader getInstance() {
     return instance;
   }








   public DiagramComponent loadFromDocument(Project project, Document document, String folderURL, String name) {
     UMLToolWindowPlugin umlToolWindowPlugin = UMLToolWindowPlugin.getUMLToolWindowPlugin(project);

     if (umlToolWindowPlugin.isDiagramComponentOnUI(folderURL, name)) {

       Messages.showMessageDialog(project, "Diagram is already open", "Information", Messages.getInformationIcon());
       return null;
     }

     DependencyDiagramComponent dependencyDiagramComponent = new DependencyDiagramComponent((DependencyDiagramSettings)umlToolWindowPlugin.getConfigurationFactory().getDiagramSettings("Dependencydiagram"), new DependencyDiagramComponentSettings(), project);
     DependencyDiagramComponentPanel dependencyDiagramComponentPanel = new DependencyDiagramComponentPanel(dependencyDiagramComponent, folderURL, name);
     boolean success = umlToolWindowPlugin.addDiagramComponent(dependencyDiagramComponentPanel);

     if (!success) {

       Messages.showMessageDialog("There already exists a diagram called \"" + name + "\"", "Error", Messages.getErrorIcon());
       return null;
     }

     dependencyDiagramComponent.setVisible(true);

     Element e = document.getRootElement();
     fillSettings(e, dependencyDiagramComponent);

     PsiClass psiClass = null;
     String className = null;

     Element classElement = e.getChild("class");
     if (classElement != null) {

       Attribute attribute = classElement.getAttribute("name");
       if (attribute != null) {

         className = attribute.getValue();
         psiClass = JavaPsiFacade.getInstance(project).findClass(className, GlobalSearchScope.allScope(project));
       }
     }
     if (psiClass != null) {

       dependencyDiagramComponentPanel.add((PsiElement)psiClass, true);


     }
     else if (className == null) {

       Messages.showMessageDialog("No class found in diagram file", "Error", Messages.getErrorIcon());
     }
     else {

       Messages.showMessageDialog("Could not find class \"" + className + "\"", "Error", Messages.getErrorIcon());
     }


     return dependencyDiagramComponentPanel;
   }



   private void fillSettings(Element root, DependencyDiagramComponent dependencyDiagramComponent) {
     Element settings = root.getChild("settings");
     Map<Object, Object> settingsMap = new HashMap<Object, Object>();
     List<Element> entries = settings.getChildren("option");
     for (int i = 0; i < entries.size(); i++) {

       Element entry = entries.get(i);
       String name = entry.getAttribute("name").getValue();
       String value = entry.getAttribute("value").getValue();
       settingsMap.put(name, value);
     }
     dependencyDiagramComponent.getComponentSettings().setSettingsMap(settingsMap);
   }
 }


