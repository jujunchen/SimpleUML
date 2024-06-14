 package net.trustx.simpleuml.classdiagram.configuration;

 import java.awt.Color;
 import java.awt.Dimension;
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




 public class ClassDiagramSettings
   implements DiagramSettings
 {
   public static final String[] FONT_SIZES = new String[] { "6", "7", "8", "9", "10", "11", "12", "13", "14", "16", "18", "20", "24", "28", "32", "48", "64" };

   private UIPanel uiPanel;

   private ClassesPanel classesPanel;

   private FilterPanel filterPanel;

   public static final int SHOW = 1;

   public static final int HIDE = 2;

   public static final int HIDE_CONDITIONAL = 4;

   private HashSet settingsListenerSet;

   private Font diagramTitleFont;

   private Font diagramFont;

   private boolean defaultFieldsExpanded;

   private boolean defaultContructorsExpanded;

   private boolean defaultMethodsExpanded;

   private boolean showParameters;

   private boolean showTooltip;

   private boolean showReturnValues;

   private boolean longModifier;

   private int implementsBehaviour;

   private int extendsBehaviour;

   private int compartmentBehaviour;

   private Border defaultDiagramComponentBorder;

   private Border highlightDiagramComponentBorder;

   private Border selectedDiagramComponentBorder;
   private Border selectedPinnedDiagramComponentBorder;
   private Border pinnedDiagramComponentBorder;
   private Color interfaceBackgroundColor;
   private Color abstractClassBackgroundColor;
   private Color classBackgroundColor;
   private Color diagramBackgroundColor;
   private boolean useAntialiasedConnectors;
   private Color quickSourceLinkColor;
   private Color quickDiagramLinkColor;
   private boolean drawDecorations;
   private int[] hideFieldList;
   private int[] showFieldList;
   private int[] hideConstructorList;
   private int[] showConstructorList;
   private int[] hideMethodList;
   private int[] showMethodList;
   private Dimension minimumFigureSize;
   private JTabbedPane tabbedPane;

   public ClassDiagramSettings() {
     this.settingsListenerSet = new HashSet();
     this.classesPanel = new ClassesPanel();
     this.uiPanel = new UIPanel();
     this.filterPanel = new FilterPanel();
     this.tabbedPane = new JTabbedPane();

     initDefaultValues();
   }






   private void initDefaultValues() {
     this.diagramTitleFont = new Font("SansSerif", 1, 12);
     this.diagramFont = new Font("SansSerif", 0, 10);

     this.defaultFieldsExpanded = false;
     this.defaultContructorsExpanded = false;
     this.defaultMethodsExpanded = false;

     this.showParameters = true;
     this.showTooltip = true;
     this.showReturnValues = true;

     this.longModifier = true;

     this.implementsBehaviour = 1;
     this.extendsBehaviour = 1;

     this.compartmentBehaviour = 1;

     this.useAntialiasedConnectors = true;

     this.defaultDiagramComponentBorder = (Border)new BevelBorder2D(0, 2);
     this.highlightDiagramComponentBorder = BorderFactory.createLineBorder(Color.red, 2);
     this.selectedDiagramComponentBorder = (Border)new SelectionBorder(this.defaultDiagramComponentBorder);
     this.pinnedDiagramComponentBorder = (Border)new BevelBorder2D(1, 2);
     this.selectedPinnedDiagramComponentBorder = (Border)new SelectionBorder(this.pinnedDiagramComponentBorder);







     this.interfaceBackgroundColor = new Color(156, 231, 158);
     this.abstractClassBackgroundColor = new Color(231, 227, 156);
     this.classBackgroundColor = new Color(156, 177, 231);

     this.diagramBackgroundColor = new Color(255, 255, 255);

     this.quickSourceLinkColor = Color.BLUE;
     this.quickDiagramLinkColor = new Color(127, 0, 0);

     this.drawDecorations = false;

     this.hideFieldList = new int[0];
     this.showFieldList = new int[0];

     this.hideConstructorList = new int[0];
     this.showConstructorList = new int[0];

     this.hideMethodList = new int[0];
     this.showMethodList = new int[0];

     this.minimumFigureSize = new Dimension(0, 0);
   }




   public void saveToElement(Element parent) {
     parent.addContent(ConfigurationUtils.getOptionElement("diagramTitleFont", ConfigurationUtils.getStringFromFont(this.diagramTitleFont)));
     parent.addContent(ConfigurationUtils.getOptionElement("diagramFont", ConfigurationUtils.getStringFromFont(this.diagramFont)));

     parent.addContent(ConfigurationUtils.getOptionElement("defaultFieldsExpanded", Boolean.valueOf(this.defaultFieldsExpanded).toString()));
     parent.addContent(ConfigurationUtils.getOptionElement("defaultContructorsExpanded", Boolean.valueOf(this.defaultContructorsExpanded).toString()));
     parent.addContent(ConfigurationUtils.getOptionElement("defaultMethodsExpanded", Boolean.valueOf(this.defaultMethodsExpanded).toString()));

     parent.addContent(ConfigurationUtils.getOptionElement("showParameters", Boolean.valueOf(this.showParameters).toString()));
     parent.addContent(ConfigurationUtils.getOptionElement("showTooltip", Boolean.valueOf(this.showTooltip).toString()));
     parent.addContent(ConfigurationUtils.getOptionElement("showReturnValues", Boolean.valueOf(this.showReturnValues).toString()));

     parent.addContent(ConfigurationUtils.getOptionElement("longModifier", Boolean.valueOf(this.longModifier).toString()));

     parent.addContent(ConfigurationUtils.getOptionElement("implementsBehaviour", Integer.toString(this.implementsBehaviour)));
     parent.addContent(ConfigurationUtils.getOptionElement("extendsBehaviour", Integer.toString(this.extendsBehaviour)));
     parent.addContent(ConfigurationUtils.getOptionElement("compartmentBehaviour", Integer.toString(this.compartmentBehaviour)));

     parent.addContent(ConfigurationUtils.getOptionElement("interfaceBackgroundColor", ConfigurationUtils.getStringFromColor(this.interfaceBackgroundColor)));
     parent.addContent(ConfigurationUtils.getOptionElement("abstractClassBackgroundColor", ConfigurationUtils.getStringFromColor(this.abstractClassBackgroundColor)));
     parent.addContent(ConfigurationUtils.getOptionElement("classBackgroundColor", ConfigurationUtils.getStringFromColor(this.classBackgroundColor)));
     parent.addContent(ConfigurationUtils.getOptionElement("diagramBackgroundColor", ConfigurationUtils.getStringFromColor(this.diagramBackgroundColor)));

     parent.addContent(ConfigurationUtils.getOptionElement("useAntialiasedConnectors", Boolean.valueOf(this.useAntialiasedConnectors).toString()));

     parent.addContent(ConfigurationUtils.getOptionElement("quickSourceLinkColor", ConfigurationUtils.getStringFromColor(this.quickSourceLinkColor)));
     parent.addContent(ConfigurationUtils.getOptionElement("quickDiagramLinkColor", ConfigurationUtils.getStringFromColor(this.quickDiagramLinkColor)));

     parent.addContent(ConfigurationUtils.getOptionElement("drawDecorations", Boolean.valueOf(this.drawDecorations).toString()));

     parent.addContent(ConfigurationUtils.getOptionElement("hideFieldList", ConfigurationUtils.getStringFromArray(this.hideFieldList)));
     parent.addContent(ConfigurationUtils.getOptionElement("showFieldList", ConfigurationUtils.getStringFromArray(this.showFieldList)));

     parent.addContent(ConfigurationUtils.getOptionElement("hideConstructorList", ConfigurationUtils.getStringFromArray(this.hideConstructorList)));
     parent.addContent(ConfigurationUtils.getOptionElement("showConstructorList", ConfigurationUtils.getStringFromArray(this.showConstructorList)));

     parent.addContent(ConfigurationUtils.getOptionElement("hideMethodList", ConfigurationUtils.getStringFromArray(this.hideMethodList)));
     parent.addContent(ConfigurationUtils.getOptionElement("showMethodList", ConfigurationUtils.getStringFromArray(this.showMethodList)));

     parent.addContent(ConfigurationUtils.getOptionElement("minimumFigureSize", ConfigurationUtils.getStringFromDimension(this.minimumFigureSize)));
   }



   public void readFromElement(Element element) {
     List elementList = element.getChildren("option");
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
       }  if (name.equals("defaultFieldsExpanded")) {
         this.defaultFieldsExpanded = ConfigurationUtils.getBooleanValue(value, this.defaultFieldsExpanded); continue;
       }  if (name.equals("defaultContructorsExpanded")) {
         this.defaultContructorsExpanded = ConfigurationUtils.getBooleanValue(value, this.defaultContructorsExpanded); continue;
       }  if (name.equals("defaultMethodsExpanded")) {
         this.defaultMethodsExpanded = ConfigurationUtils.getBooleanValue(value, this.defaultMethodsExpanded); continue;
       }  if (name.equals("showParameters")) {
         this.showParameters = ConfigurationUtils.getBooleanValue(value, this.showParameters); continue;
       }  if (name.equals("showTooltip")) {
         this.showTooltip = ConfigurationUtils.getBooleanValue(value, this.showTooltip); continue;
       }  if (name.equals("showReturnValues")) {
         this.showReturnValues = ConfigurationUtils.getBooleanValue(value, this.showReturnValues); continue;
       }  if (name.equals("longModifier")) {
         this.longModifier = ConfigurationUtils.getBooleanValue(value, this.longModifier); continue;
       }  if (name.equals("implementsBehaviour")) {
         this.implementsBehaviour = ConfigurationUtils.getInt(value, 1); continue;
       }  if (name.equals("extendsBehaviour")) {
         this.extendsBehaviour = ConfigurationUtils.getInt(value, 1); continue;
       }  if (name.equals("compartmentBehaviour")) {
         this.compartmentBehaviour = ConfigurationUtils.getInt(value, 1); continue;
       }  if (name.equals("interfaceBackgroundColor")) {
         this.interfaceBackgroundColor = ConfigurationUtils.getColorFromStringObject(value, this.interfaceBackgroundColor); continue;
       }  if (name.equals("abstractClassBackgroundColor")) {
         this.abstractClassBackgroundColor = ConfigurationUtils.getColorFromStringObject(value, this.abstractClassBackgroundColor); continue;
       }  if (name.equals("classBackgroundColor")) {
         this.classBackgroundColor = ConfigurationUtils.getColorFromStringObject(value, this.classBackgroundColor); continue;
       }  if (name.equals("diagramBackgroundColor")) {
         this.diagramBackgroundColor = ConfigurationUtils.getColorFromStringObject(value, this.diagramBackgroundColor); continue;
       }  if (name.equals("useAntialiasedConnectors")) {
         this.useAntialiasedConnectors = ConfigurationUtils.getBooleanValue(value, this.useAntialiasedConnectors); continue;
       }  if (name.equals("quickSourceLinkColor")) {
         this.quickSourceLinkColor = ConfigurationUtils.getColorFromStringObject(value, this.quickSourceLinkColor); continue;
       }  if (name.equals("quickDiagramLinkColor")) {
         this.quickDiagramLinkColor = ConfigurationUtils.getColorFromStringObject(value, this.quickDiagramLinkColor); continue;
       }  if (name.equals("drawDecorations")) {
         this.drawDecorations = ConfigurationUtils.getBooleanValue(value, this.drawDecorations); continue;
       }  if (name.equals("hideFieldList")) {
         this.hideFieldList = ConfigurationUtils.getArrayFromString(value, this.hideFieldList); continue;
       }  if (name.equals("showFieldList")) {
         this.showFieldList = ConfigurationUtils.getArrayFromString(value, this.showFieldList); continue;
       }  if (name.equals("hideConstructorList")) {
         this.hideConstructorList = ConfigurationUtils.getArrayFromString(value, this.hideConstructorList); continue;
       }  if (name.equals("showConstructorList")) {
         this.showConstructorList = ConfigurationUtils.getArrayFromString(value, this.showConstructorList); continue;
       }  if (name.equals("hideMethodList")) {
         this.hideMethodList = ConfigurationUtils.getArrayFromString(value, this.hideMethodList); continue;
       }  if (name.equals("showMethodList")) {
         this.showMethodList = ConfigurationUtils.getArrayFromString(value, this.showMethodList); continue;
       }  if (name.equals("minimumFigureSize")) {
         this.minimumFigureSize = ConfigurationUtils.getDimensionFromStringObject(value, this.minimumFigureSize);
       }
     }
   }






   public JComponent createComponent() {
     this.classesPanel.applySettingsToUI(this);
     this.uiPanel.applySettingsToUI(this);
     this.filterPanel.applySettingsToUI(this);

     this.tabbedPane.addTab(this.uiPanel.getDisplayName(), this.uiPanel);
     this.tabbedPane.addTab(this.classesPanel.getDisplayName(), this.classesPanel);
     this.tabbedPane.addTab(this.filterPanel.getDisplayName(), this.filterPanel);

     return this.tabbedPane;
   }



   public boolean isModified() {
     return (this.classesPanel.isModified(this) || this.uiPanel.isModified(this) || this.filterPanel.isModified(this));
   }



   public void apply() {
     this.classesPanel.apply(this);
     this.uiPanel.apply(this);
     this.filterPanel.apply(this);
     notifyListeners();
   }



   public void reset() {
     this.classesPanel.reset(this);
     this.uiPanel.reset(this);
     this.filterPanel.reset(this);
   }




   public void disposeUIResources() {}



   public String getKey() {
     return "Classdiagram";
   }



   public void addSettingsListener(ClassDiagramSettingsListener component) {
     this.settingsListenerSet.add(component);
   }



   public void removeSettingsListener(ClassDiagramSettingsListener listener) {
     this.settingsListenerSet.remove(listener);
   }



   private void notifyListeners() {
     for (Iterator<ClassDiagramSettingsListener> iterator = this.settingsListenerSet.iterator(); iterator.hasNext(); ) {

       ClassDiagramSettingsListener listener = iterator.next();
       listener.settingsChanged(this);
     }
   }




   public Color getAbstractClassBackgroundColor() {
     return this.abstractClassBackgroundColor;
   }



   public void setAbstractClassBackgroundColor(Color abstractClassBackgroundColor) {
     this.abstractClassBackgroundColor = abstractClassBackgroundColor;
   }



   public Color getClassBackgroundColor() {
     return this.classBackgroundColor;
   }



   public void setClassBackgroundColor(Color classBackgroundColor) {
     this.classBackgroundColor = classBackgroundColor;
   }



   public ClassesPanel getClassesPanel() {
     return this.classesPanel;
   }



   public void setClassesPanel(ClassesPanel classesPanel) {
     this.classesPanel = classesPanel;
   }



   public int getCompartmentBehaviour() {
     return this.compartmentBehaviour;
   }



   public void setCompartmentBehaviour(int compartmentBehaviour) {
     this.compartmentBehaviour = compartmentBehaviour;
   }



   public boolean isDefaultContructorsExpanded() {
     return this.defaultContructorsExpanded;
   }



   public void setDefaultContructorsExpanded(boolean defaultContructorsExpanded) {
     this.defaultContructorsExpanded = defaultContructorsExpanded;
   }



   public Border getDefaultDiagramComponentBorder() {
     return this.defaultDiagramComponentBorder;
   }



   public void setDefaultDiagramComponentBorder(Border defaultDiagramComponentBorder) {
     this.defaultDiagramComponentBorder = defaultDiagramComponentBorder;
   }



   public boolean isDefaultFieldsExpanded() {
     return this.defaultFieldsExpanded;
   }



   public void setDefaultFieldsExpanded(boolean defaultFieldsExpanded) {
     this.defaultFieldsExpanded = defaultFieldsExpanded;
   }



   public boolean isDefaultMethodsExpanded() {
     return this.defaultMethodsExpanded;
   }



   public void setDefaultMethodsExpanded(boolean defaultMethodsExpanded) {
     this.defaultMethodsExpanded = defaultMethodsExpanded;
   }



   public Color getDiagramBackgroundColor() {
     return this.diagramBackgroundColor;
   }



   public void setDiagramBackgroundColor(Color diagramBackgroundColor) {
     this.diagramBackgroundColor = diagramBackgroundColor;
   }



   public Font getDiagramFont() {
     return this.diagramFont;
   }



   public void setDiagramFont(Font diagramFont) {
     this.diagramFont = diagramFont;
   }



   public Font getDiagramTitleFont() {
     return this.diagramTitleFont;
   }



   public void setDiagramTitleFont(Font diagramTitleFont) {
     this.diagramTitleFont = diagramTitleFont;
   }



   public boolean isDrawDecorations() {
     return this.drawDecorations;
   }



   public void setDrawDecorations(boolean drawDecorations) {
     this.drawDecorations = drawDecorations;
   }



   public int getExtendsBehaviour() {
     return this.extendsBehaviour;
   }



   public void setExtendsBehaviour(int extendsBehaviour) {
     this.extendsBehaviour = extendsBehaviour;
   }



   public int[] getHideConstructorList() {
     return this.hideConstructorList;
   }



   public void setHideConstructorList(int[] hideConstructorList) {
     this.hideConstructorList = hideConstructorList;
   }



   public int[] getHideFieldList() {
     return this.hideFieldList;
   }



   public void setHideFieldList(int[] hideFieldList) {
     this.hideFieldList = hideFieldList;
   }



   public int[] getHideMethodList() {
     return this.hideMethodList;
   }



   public void setHideMethodList(int[] hideMethodList) {
     this.hideMethodList = hideMethodList;
   }



   public Border getHighlightDiagramComponentBorder() {
     return this.highlightDiagramComponentBorder;
   }



   public void setHighlightDiagramComponentBorder(Border highlightDiagramComponentBorder) {
     this.highlightDiagramComponentBorder = highlightDiagramComponentBorder;
   }



   public int getImplementsBehaviour() {
     return this.implementsBehaviour;
   }



   public void setImplementsBehaviour(int implementsBehaviour) {
     this.implementsBehaviour = implementsBehaviour;
   }



   public Color getInterfaceBackgroundColor() {
     return this.interfaceBackgroundColor;
   }



   public void setInterfaceBackgroundColor(Color interfaceBackgroundColor) {
     this.interfaceBackgroundColor = interfaceBackgroundColor;
   }



   public boolean isLongModifier() {
     return this.longModifier;
   }



   public void setLongModifier(boolean longModifier) {
     this.longModifier = longModifier;
   }



   public Dimension getMinimumFigureSize() {
     return this.minimumFigureSize;
   }



   public void setMinimumFigureSize(Dimension minimumFigureSize) {
     this.minimumFigureSize = minimumFigureSize;
   }



   public Border getPinnedDiagramComponentBorder() {
     return this.pinnedDiagramComponentBorder;
   }



   public void setPinnedDiagramComponentBorder(Border pinnedDiagramComponentBorder) {
     this.pinnedDiagramComponentBorder = pinnedDiagramComponentBorder;
   }



   public Color getQuickDiagramLinkColor() {
     return this.quickDiagramLinkColor;
   }



   public void setQuickDiagramLinkColor(Color quickDiagramLinkColor) {
     this.quickDiagramLinkColor = quickDiagramLinkColor;
   }



   public Color getQuickSourceLinkColor() {
     return this.quickSourceLinkColor;
   }



   public void setQuickSourceLinkColor(Color quickSourceLinkColor) {
     this.quickSourceLinkColor = quickSourceLinkColor;
   }



   public Border getSelectedDiagramComponentBorder() {
     return this.selectedDiagramComponentBorder;
   }



   public void setSelectedDiagramComponentBorder(Border selectedDiagramComponentBorder) {
     this.selectedDiagramComponentBorder = selectedDiagramComponentBorder;
   }



   public Border getSelectedPinnedDiagramComponentBorder() {
     return this.selectedPinnedDiagramComponentBorder;
   }



   public void setSelectedPinnedDiagramComponentBorder(Border selectedPinnedDiagramComponentBorder) {
     this.selectedPinnedDiagramComponentBorder = selectedPinnedDiagramComponentBorder;
   }



   public int[] getShowConstructorList() {
     return this.showConstructorList;
   }



   public void setShowConstructorList(int[] showConstructorList) {
     this.showConstructorList = showConstructorList;
   }



   public int[] getShowFieldList() {
     return this.showFieldList;
   }



   public void setShowFieldList(int[] showFieldList) {
     this.showFieldList = showFieldList;
   }



   public int[] getShowMethodList() {
     return this.showMethodList;
   }



   public void setShowMethodList(int[] showMethodList) {
     this.showMethodList = showMethodList;
   }



   public boolean isShowParameters() {
     return this.showParameters;
   }



   public void setShowParameters(boolean showParameters) {
     this.showParameters = showParameters;
   }



   public boolean isShowReturnValues() {
     return this.showReturnValues;
   }



   public void setShowReturnValues(boolean showReturnValues) {
     this.showReturnValues = showReturnValues;
   }



   public boolean isShowTooltip() {
     return this.showTooltip;
   }



   public void setShowTooltip(boolean showTooltip) {
     this.showTooltip = showTooltip;
   }



   public UIPanel getUiPanel() {
     return this.uiPanel;
   }



   public void setUiPanel(UIPanel uiPanel) {
     this.uiPanel = uiPanel;
   }



   public boolean isUseAntialiasedConnectors() {
     return this.useAntialiasedConnectors;
   }



   public void setUseAntialiasedConnectors(boolean useAntialiasedConnectors) {
     this.useAntialiasedConnectors = useAntialiasedConnectors;
   }
 }


