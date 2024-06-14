 package net.trustx.simpleuml.packagediagram.configuration;

 import java.awt.Color;
 import java.awt.Font;
 import java.util.HashSet;
 import java.util.Iterator;
 import java.util.List;
 import javax.swing.BorderFactory;
 import javax.swing.JComponent;
 import javax.swing.JTabbedPane;
 import javax.swing.border.Border;
 import net.trustx.simpleuml.components.border.BevelBorder2D;
 import net.trustx.simpleuml.components.border.SelectionBorder;
 import net.trustx.simpleuml.plugin.configuration.ConfigurationUtils;
 import net.trustx.simpleuml.plugin.configuration.DiagramSettings;
 import org.jdom.Element;



 public class PackageDiagramSettings
   implements DiagramSettings
 {
   public static final String[] FONT_SIZES = new String[] { "6", "7", "8", "9", "10", "11", "12", "13", "14", "16", "18", "20", "24", "28", "32", "48", "64" };

   private UIPanel uiPanel;

   private Font diagramTitleFont;

   private Font diagramFont;

   private HashSet settingsListenerSet;

   private BevelBorder2D defaultDiagramComponentBorder;

   private Border highlightDiagramComponentBorder;

   private SelectionBorder selectedDiagramComponentBorder;

   private BevelBorder2D pinnedDiagramComponentBorder;

   private SelectionBorder selectedPinnedDiagramComponentBorder;

   private int packageNameCompressionLevel;
   private Color packageBackgroundColor;
   private Color diagramBackgroundColor;
   private boolean useAntialiasedConnectors;
   private JTabbedPane tabbedPane;
   private Color interfaceBackgroundColor;
   private Color abstractClassBackgroundColor;
   private Color classBackgroundColor;

   public PackageDiagramSettings() {
     this.settingsListenerSet = new HashSet();

     this.defaultDiagramComponentBorder = new BevelBorder2D(0, 2);
     this.highlightDiagramComponentBorder = BorderFactory.createLineBorder(Color.red, 2);
     this.selectedDiagramComponentBorder = new SelectionBorder((Border)this.defaultDiagramComponentBorder);
     this.pinnedDiagramComponentBorder = new BevelBorder2D(1, 2);
     this.selectedPinnedDiagramComponentBorder = new SelectionBorder((Border)this.pinnedDiagramComponentBorder);

     this.uiPanel = new UIPanel();
     this.tabbedPane = new JTabbedPane();

     initDefaultValues();
   }



   private void initDefaultValues() {
     this.diagramTitleFont = new Font("SansSerif", 0, 12);
     this.diagramFont = new Font("SansSerif", 0, 10);

     this.packageNameCompressionLevel = 2;

     this.packageBackgroundColor = Color.LIGHT_GRAY;
     this.diagramBackgroundColor = Color.WHITE;

     this.useAntialiasedConnectors = true;

     this.interfaceBackgroundColor = new Color(156, 231, 158);
     this.abstractClassBackgroundColor = new Color(231, 227, 156);
     this.classBackgroundColor = new Color(156, 177, 231);
   }




   public void saveToElement(Element parent) {
     parent.addContent(ConfigurationUtils.getOptionElement("diagramTitleFont", ConfigurationUtils.getStringFromFont(this.diagramTitleFont)));
     parent.addContent(ConfigurationUtils.getOptionElement("diagramFont", ConfigurationUtils.getStringFromFont(this.diagramFont)));

     parent.addContent(ConfigurationUtils.getOptionElement("packageBackgroundColor", ConfigurationUtils.getStringFromColor(this.packageBackgroundColor)));
     parent.addContent(ConfigurationUtils.getOptionElement("diagramBackgroundColor", ConfigurationUtils.getStringFromColor(this.diagramBackgroundColor)));

     parent.addContent(ConfigurationUtils.getOptionElement("useAntialiasedConnectors", Boolean.valueOf(this.useAntialiasedConnectors).toString()));

     parent.addContent(ConfigurationUtils.getOptionElement("packageNameCompressionLevel", Integer.toString(this.packageNameCompressionLevel)));
   }



   public void readFromElement(Element parent) {
     List elementList = parent.getChildren("option");
     for (Iterator<Element> iterator = elementList.iterator(); iterator.hasNext(); ) {

       Element child = iterator.next();
       String name = child.getAttribute("name").getValue();
       if (name == null) {
         continue;
       }

       String value = child.getAttributeValue("value");

       if (name.equals("diagramTitleFont")) {
         this.diagramTitleFont = ConfigurationUtils.getFontFromStringObject(value, this.diagramTitleFont); continue;
       }  if (name.equals("diagramFont")) {
         this.diagramFont = ConfigurationUtils.getFontFromStringObject(value, this.diagramFont); continue;
       }  if (name.equals("packageBackgroundColor")) {
         this.packageBackgroundColor = ConfigurationUtils.getColorFromStringObject(value, this.packageBackgroundColor); continue;
       }  if (name.equals("diagramBackgroundColor")) {
         this.diagramBackgroundColor = ConfigurationUtils.getColorFromStringObject(value, this.diagramBackgroundColor); continue;
       }  if (name.equals("useAntialiasedConnectors")) {
         this.useAntialiasedConnectors = ConfigurationUtils.getBooleanValue(value, this.useAntialiasedConnectors); continue;
       }  if (name.equals("packageNameCompressionLevel")) {
         this.packageNameCompressionLevel = ConfigurationUtils.getInt(value, this.packageNameCompressionLevel);
       }
     }
   }





   public JComponent createComponent() {
     this.uiPanel.applySettingsToUI(this);

     this.tabbedPane.addTab(this.uiPanel.getDisplayName(), this.uiPanel);

     return this.tabbedPane;
   }



   public boolean isModified() {
     return this.uiPanel.isModified(this);
   }



   public void apply() {
     this.uiPanel.apply(this);
     notifyListeners();
   }



   public void reset() {
     this.uiPanel.reset(this);
   }




   public void disposeUIResources() {}



   public String getKey() {
     return "Packagediagram";
   }



   public Color getDiagramBackgroundColor() {
     return this.diagramBackgroundColor;
   }



   public void setDiagramBackgroundColor(Color diagramBackgroundColor) {
     this.diagramBackgroundColor = diagramBackgroundColor;
   }



   public void addSettingsListener(PackageDiagramSettingsListener component) {
     this.settingsListenerSet.add(component);
   }



   public void removeSettingsListener(PackageDiagramSettingsListener listener) {
     this.settingsListenerSet.remove(listener);
   }



   private void notifyListeners() {
     for (Iterator<PackageDiagramSettingsListener> iterator = this.settingsListenerSet.iterator(); iterator.hasNext(); ) {

       PackageDiagramSettingsListener listener = iterator.next();
       listener.settingsChanged(this);
     }
   }



   public Border getHighlightDiagramComponentBorder() {
     return this.highlightDiagramComponentBorder;
   }



   public Border getSelectedDiagramComponentBorder() {
     return (Border)this.selectedDiagramComponentBorder;
   }



   public Border getSelectedPinnedDiagramComponentBorder() {
     return (Border)this.selectedPinnedDiagramComponentBorder;
   }



   public Border getPinnedDiagramComponentBorder() {
     return (Border)this.pinnedDiagramComponentBorder;
   }



   public Border getDefaultDiagramComponentBorder() {
     return (Border)this.defaultDiagramComponentBorder;
   }



   public Font getDiagramTitleFont() {
     return this.diagramTitleFont;
   }



   public Font getDiagramFont() {
     return this.diagramFont;
   }



   public int getPackageNameCompressionLevel() {
     return this.packageNameCompressionLevel;
   }



   public void setPackageNameCompressionLevel(int packageNameCompressionLevel) {
     this.packageNameCompressionLevel = packageNameCompressionLevel;
   }



   public Color getPackageBackgroundColor() {
     return this.packageBackgroundColor;
   }



   public void setPackageBackgroundColor(Color packageBackgroundColor) {
     this.packageBackgroundColor = packageBackgroundColor;
   }



   public boolean isUseAntialiasedConnectors() {
     return this.useAntialiasedConnectors;
   }



   public void setUseAntialiasedConnectors(boolean useAntialiasedConnectors) {
     this.useAntialiasedConnectors = useAntialiasedConnectors;
   }



   public void setDiagramTitleFont(Font diagramTitleFont) {
     this.diagramTitleFont = diagramTitleFont;
   }



   public void setDiagramFont(Font diagramFont) {
     this.diagramFont = diagramFont;
   }



   public void setDefaultDiagramComponentBorder(BevelBorder2D defaultDiagramComponentBorder) {
     this.defaultDiagramComponentBorder = defaultDiagramComponentBorder;
   }



   public void setHighlightDiagramComponentBorder(Border highlightDiagramComponentBorder) {
     this.highlightDiagramComponentBorder = highlightDiagramComponentBorder;
   }



   public void setSelectedDiagramComponentBorder(SelectionBorder selectedDiagramComponentBorder) {
     this.selectedDiagramComponentBorder = selectedDiagramComponentBorder;
   }



   public void setPinnedDiagramComponentBorder(BevelBorder2D pinnedDiagramComponentBorder) {
     this.pinnedDiagramComponentBorder = pinnedDiagramComponentBorder;
   }



   public void setSelectedPinnedDiagramComponentBorder(SelectionBorder selectedPinnedDiagramComponentBorder) {
     this.selectedPinnedDiagramComponentBorder = selectedPinnedDiagramComponentBorder;
   }



   public Color getQuickSourceLinkColor() {
     return Color.BLUE;
   }



   public Color getInterfaceBackgroundColor() {
     return this.interfaceBackgroundColor;
   }



   public Color getAbstractClassBackgroundColor() {
     return this.abstractClassBackgroundColor;
   }



   public Color getClassBackgroundColor() {
     return this.classBackgroundColor;
   }
 }


