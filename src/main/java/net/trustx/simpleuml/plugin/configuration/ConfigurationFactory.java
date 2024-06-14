 package net.trustx.simpleuml.plugin.configuration;

 import com.intellij.openapi.options.Configurable;
 import java.awt.BorderLayout;
 import java.awt.event.ItemEvent;
 import java.awt.event.ItemListener;
 import java.util.Collection;
 import java.util.Iterator;
 import java.util.LinkedHashMap;
 import java.util.List;
 import javax.swing.Icon;
 import javax.swing.ImageIcon;
 import javax.swing.JComboBox;
 import javax.swing.JComponent;
 import javax.swing.JPanel;
 import javax.swing.SwingUtilities;
 import net.trustx.simpleuml.classdiagram.configuration.ClassDiagramSettings;
 import net.trustx.simpleuml.dependencydiagram.configuration.DependencyDiagramSettings;
 import net.trustx.simpleuml.file.UMLFileManager;
 import net.trustx.simpleuml.packagediagram.configuration.PackageDiagramSettings;
 import net.trustx.simpleuml.plugin.UMLToolWindowPlugin;
 import org.jdom.Element;























 public class ConfigurationFactory
   implements Configurable, DiagramSettings
 {
   private UMLToolWindowPlugin umlToolWindowPlugin;
   private LinkedHashMap diagramSettingsMap;
   private ImageIcon smallImageIcon;

   public ConfigurationFactory(UMLToolWindowPlugin umlToolWindowPlugin) {
     this.umlToolWindowPlugin = umlToolWindowPlugin;

     this.diagramSettingsMap = new LinkedHashMap<Object, Object>();
     this.diagramSettingsMap.put("General", new GeneralDiagramSettings(new UMLFileManager(umlToolWindowPlugin)));
     this.diagramSettingsMap.put("Classdiagram", new ClassDiagramSettings());
     this.diagramSettingsMap.put("Dependencydiagram", new DependencyDiagramSettings());
     this.diagramSettingsMap.put("Packagediagram", new PackageDiagramSettings());


     this.smallImageIcon = new ImageIcon(getClass().getResource("net/trustx/simpleuml/icons/simpleUML.png"));
   }



   public GeneralDiagramSettings getGeneralDiagramSettings() {
     return (GeneralDiagramSettings)this.diagramSettingsMap.get("General");
   }



   public DiagramSettings getDiagramSettings(Object key) {
     return (DiagramSettings)this.diagramSettingsMap.get(key);
   }



   public String getDisplayName() {
     return "simpleUML";
   }



   public Icon getIcon() {
     return this.smallImageIcon;
   }



   public String getHelpTopic() {
     return null;
   }



   public void saveToElement(Element parent) {
     Collection settings = this.diagramSettingsMap.values();
     for (Iterator<DiagramSettings> iterator = settings.iterator(); iterator.hasNext(); ) {

       DiagramSettings diagramSettings = iterator.next();
       Element element = new Element(diagramSettings.getKey());
       parent.addContent(element);
       diagramSettings.saveToElement(element);
     }
   }



   public void readFromElement(Element element) {
     List list = element.getChildren();
     for (Iterator<Element> iterator = list.iterator(); iterator.hasNext(); ) {

       Element diagramElement = iterator.next();
       DiagramSettings settings = getDiagramSettings(diagramElement.getName());
       if (settings != null)
       {
         settings.readFromElement(diagramElement);
       }
     }
   }



   public JComponent createComponent() {
     final JPanel configPanel = new JPanel(new BorderLayout());

     Object[] values = this.diagramSettingsMap.keySet().toArray();
     final JComboBox comboBox = new JComboBox(values);
     comboBox.setSelectedIndex(0);
     comboBox.addItemListener(new ItemListener()
         {
           public void itemStateChanged(ItemEvent e)
           {
             if (e.getStateChange() == 1) {

               Object item = comboBox.getSelectedItem();
               configPanel.removeAll();
               configPanel.add(comboBox, "North");
               JComponent currentComponent = ConfigurationFactory.this.getDiagramSettings(item).createComponent();
               configPanel.add(currentComponent, "Center");



               SwingUtilities.updateComponentTreeUI(configPanel);
             }
           }
         });


     configPanel.add(comboBox, "North");

     Object selectedItem = comboBox.getSelectedItem();
     String key = selectedItem.toString();
     DiagramSettings settings = getDiagramSettings(key);
     JComponent currentComponent = settings.createComponent();

     configPanel.add(currentComponent, "Center");

     SwingUtilities.updateComponentTreeUI(configPanel);

     return configPanel;
   }




   public boolean isModified() {
     Collection settings = this.diagramSettingsMap.values();
     for (Iterator<DiagramSettings> iterator = settings.iterator(); iterator.hasNext(); ) {

       DiagramSettings diagramSettings = iterator.next();
       if (diagramSettings.isModified())
       {
         return true;
       }
     }

     return false;
   }




   public void apply() {
     Collection settings = this.diagramSettingsMap.values();
     for (Iterator<DiagramSettings> iterator = settings.iterator(); iterator.hasNext(); ) {

       DiagramSettings diagramSettings = iterator.next();
       diagramSettings.apply();
     }
   }




   public void reset() {
     Collection settings = this.diagramSettingsMap.values();
     for (Iterator<DiagramSettings> iterator = settings.iterator(); iterator.hasNext(); ) {

       DiagramSettings diagramSettings = iterator.next();
       diagramSettings.reset();
     }
   }




   public void disposeUIResources() {
     Collection settings = this.diagramSettingsMap.values();
     for (Iterator<DiagramSettings> iterator = settings.iterator(); iterator.hasNext(); ) {

       DiagramSettings diagramSettings = iterator.next();
       diagramSettings.disposeUIResources();
     }
   }



   public String getKey() {
     return "simpleUML";
   }



   public UMLToolWindowPlugin getUmlToolWindowPlugin() {
     return this.umlToolWindowPlugin;
   }
 }


