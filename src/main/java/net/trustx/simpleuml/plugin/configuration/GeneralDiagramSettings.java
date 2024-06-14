 package net.trustx.simpleuml.plugin.configuration;

 import java.util.HashMap;
 import java.util.HashSet;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;
 import javax.swing.JComponent;
 import javax.swing.JTabbedPane;
 import net.trustx.simpleuml.file.UMLFileManager;
 import org.jdom.Element;
























 public class GeneralDiagramSettings
   implements DiagramSettings
 {
   public static final long BIRDVIEW_UPDATE_MIN = 100L;
   public static final long BIRDVIEW_UPDATE_MAX = 1209600000L;
   private HashSet settingsListenerSet;
   private long birdViewUpdateDelay;
   private UMLFileManager umlFileManager;
   private GeneralPanel generalPanel;

   public GeneralDiagramSettings(UMLFileManager umlFileManager) {
     this.umlFileManager = umlFileManager;
     this.settingsListenerSet = new HashSet();
     this.generalPanel = new GeneralPanel(this);
     initDefaultValues();
   }



   private void initDefaultValues() {
     this.birdViewUpdateDelay = 2000L;
   }



   public UMLFileManager getUmlFileManager() {
     return this.umlFileManager;
   }



   public void setUmlFileManager(UMLFileManager umlFileManager) {
     this.umlFileManager = umlFileManager;
   }



   public void addSettingsListener(GeneralDiagramSettingsListener listener) {
     this.settingsListenerSet.add(listener);
   }



   public void removeSettingsListener(GeneralDiagramSettingsListener listener) {
     this.settingsListenerSet.remove(listener);
   }



   public void notifyListeners() {
     for (Iterator<GeneralDiagramSettingsListener> iterator = this.settingsListenerSet.iterator(); iterator.hasNext(); ) {

       GeneralDiagramSettingsListener listener = iterator.next();
       listener.settingsChanged(this);
     }
   }



   public void saveToElement(Element parent) {
     parent.addContent(ConfigurationUtils.getOptionElement("birdViewUpdateDelay", Long.toString(this.birdViewUpdateDelay)));

     Map settingsMap = this.umlFileManager.getSettingsMap();
     Iterator<String> keyIterator = settingsMap.keySet().iterator();
     while (keyIterator.hasNext()) {

       String key = keyIterator.next();
       Element entryElement = ConfigurationUtils.getOptionElement(key, settingsMap.get(key).toString());
       parent.addContent(entryElement);
     }
   }



   public void readFromElement(Element parent) {
     HashMap<Object, Object> settingsMap = new HashMap<Object, Object>();
     List elementList = parent.getChildren("option");
     for (Iterator<Element> iterator = elementList.iterator(); iterator.hasNext(); ) {

       Element child = iterator.next();
       String name = child.getAttribute("name").getValue();
       if (name == null) {
         continue;
       }

       String value = child.getAttributeValue("value");

       settingsMap.put(name, value);

       if (name.equals("birdViewUpdateDelay")) {
         this.birdViewUpdateDelay = ConfigurationUtils.getLong(value, this.birdViewUpdateDelay, 100L, 1209600000L);
       }
     }
     this.umlFileManager.setSettingsMap(settingsMap);
   }



   public JComponent createComponent() {
     JTabbedPane tabbedPane = new JTabbedPane();

     this.generalPanel.applySettingsToUI(this);
     tabbedPane.addTab(this.generalPanel.getDisplayName(), this.generalPanel);

     return tabbedPane;
   }



   public boolean isModified() {
     return this.generalPanel.isModified(this);
   }



   public void apply() {
     this.generalPanel.apply(this);
     notifyListeners();
   }



   public void reset() {
     this.generalPanel.reset(this);
   }




   public void disposeUIResources() {}



   public String getKey() {
     return "General";
   }



   public long getBirdViewUpdateDelay() {
     return this.birdViewUpdateDelay;
   }



   public void setBirdViewUpdateDelay(long birdViewUpdateDelay) {
     this.birdViewUpdateDelay = birdViewUpdateDelay;
   }
 }


