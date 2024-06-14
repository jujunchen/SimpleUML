 package net.trustx.simpleuml.components;

 import com.intellij.openapi.components.ProjectComponent;
 import com.intellij.openapi.project.Project;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.List;
 import org.jdom.Element;


 public class DiagramSettingsWorkspace
   implements ProjectComponent
 {
   public static final String IMAGE_PATH = "SaveAsImageDialog.path";
   public static final String IMAGE_TYPE = "SaveAsImageDialog.type";
   private static HashMap diagramSettingsWorkspaceMap;
   private HashMap settingsListenerMap;
   private HashMap settingsMap;

   public static synchronized DiagramSettingsWorkspace getDiagramSettingsWorkspace(Project project) {
     return (DiagramSettingsWorkspace)getDiagramSettingsWorkspaceMap().get(project);
   }



   public static HashMap getDiagramSettingsWorkspaceMap() {
     if (diagramSettingsWorkspaceMap == null)
     {
       diagramSettingsWorkspaceMap = new HashMap<Object, Object>();
     }
     return diagramSettingsWorkspaceMap;
   }



   public DiagramSettingsWorkspace(Project project) {
     this.settingsMap = new HashMap<Object, Object>();
     this.settingsListenerMap = new HashMap<Object, Object>();
     getDiagramSettingsWorkspaceMap().put(project, this);
   }



   public void addSettingsListener(SettingsListenerWorkspace listener) {
     this.settingsListenerMap.put(listener, listener);
   }



   public void removeSettingsListener(SettingsListenerWorkspace listener) {
     this.settingsListenerMap.remove(listener);
   }



   public void notifyListeners() {
     Iterator<SettingsListenerWorkspace> iter = this.settingsListenerMap.values().iterator();
     while (iter.hasNext()) {

       SettingsListenerWorkspace settingsListener = iter.next();
       if (settingsListener != null)
       {
         settingsListener.settingsChanged(this);
       }
     }
   }



   public String getComponentName() {
     return "simpleUML.DiagramSettingsWorkspace";
   }




   public void initComponent() {}




   public void disposeComponent() {}




   public void projectOpened() {}




   public void projectClosed() {}



   public void readExternal(Element element) {
     this.settingsMap = new HashMap<Object, Object>();
     List<Element> entries = element.getChildren("option");
     for (int i = 0; i < entries.size(); i++) {

       Element entry = entries.get(i);
       String name = entry.getAttribute("name").getValue();
       String value = entry.getAttribute("value").getValue();
       this.settingsMap.put(name, value);
     }
   }



   public void writeExternal(Element element) {
     Iterator<String> keyIterator = this.settingsMap.keySet().iterator();
     while (keyIterator.hasNext()) {

       String key = keyIterator.next();

       Element entryElement = new Element("option");
       entryElement = entryElement.setAttribute("name", key);
       entryElement = entryElement.setAttribute("value", this.settingsMap.get(key).toString());
       element.addContent(entryElement);
     }
   }



   public Object get(Object key) {
     return this.settingsMap.get(key);
   }



   public Object put(Object key, Object value) {
     return this.settingsMap.put(key, value);
   }



   public Object remove(Object key) {
     return this.settingsMap.remove(key);
   }
 }


