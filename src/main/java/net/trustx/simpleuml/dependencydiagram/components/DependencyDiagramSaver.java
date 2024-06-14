 package net.trustx.simpleuml.dependencydiagram.components;

 import java.util.Iterator;
 import java.util.Map;
 import net.trustx.simpleuml.components.DiagramSaver;
 import org.jdom.Document;
 import org.jdom.Element;




 public class DependencyDiagramSaver
   implements DiagramSaver
 {
   private DependencyDiagramComponentPanel dependencyDiagramComponentPanel;

   public DependencyDiagramSaver(DependencyDiagramComponentPanel dependencyDiagramComponentPanel) {
     this.dependencyDiagramComponentPanel = dependencyDiagramComponentPanel;
   }



   public Document saveToDocument() {
     Element root = new Element("Dependencydiagram");

     Element settingsElement = new Element("settings");
     root.addContent(settingsElement);


     Map map = this.dependencyDiagramComponentPanel.getComponentSettings().getSettingsMap();
     Iterator<String> keyIterator = map.keySet().iterator();
     while (keyIterator.hasNext()) {

       String key = keyIterator.next();

       Element entryElement = new Element("option");
       entryElement = entryElement.setAttribute("name", key);
       entryElement = entryElement.setAttribute("value", map.get(key).toString());
       settingsElement.addContent(entryElement);
     }

     Element classElement = new Element("class");
     classElement.setAttribute("name", this.dependencyDiagramComponentPanel.getPsiDependencyClassName());
     root.addContent(classElement);

     return new Document(root);
   }
 }


