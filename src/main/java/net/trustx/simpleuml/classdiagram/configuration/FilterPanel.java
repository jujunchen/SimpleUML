 package net.trustx.simpleuml.classdiagram.configuration;
 
 import java.awt.BorderLayout;
 import java.awt.GridLayout;
 import javax.swing.BorderFactory;
 import javax.swing.JPanel;
 

 public class FilterPanel
   extends JPanel
 {
   private boolean initialised;
   private ModifierFilterPanel filterPanelField;
   private ModifierFilterPanel filterPanelConstructor;
   private ModifierFilterPanel filterPanelMethods;
   
   public FilterPanel() {
     super(new BorderLayout());
     
     JPanel filterPanel = new JPanel(new GridLayout(0, 3));
     
     this.filterPanelField = new ModifierFilterPanel();
     this.filterPanelField.setBorder(BorderFactory.createTitledBorder("Fields"));
     filterPanel.add(this.filterPanelField);
     
     this.filterPanelConstructor = new ModifierFilterPanel();
     this.filterPanelConstructor.setBorder(BorderFactory.createTitledBorder("Constructors"));
     filterPanel.add(this.filterPanelConstructor);
     
     this.filterPanelMethods = new ModifierFilterPanel();
     this.filterPanelMethods.setBorder(BorderFactory.createTitledBorder("Methods"));
     filterPanel.add(this.filterPanelMethods);
     
     add(filterPanel, "North");
   }
 
 
   
   public void applySettingsToUI(ClassDiagramSettings classDiagramSettings) {
     int[] hideFields = classDiagramSettings.getHideFieldList();
     this.filterPanelField.getHideFilterBox().setValues(hideFields);
     int[] showFields = classDiagramSettings.getShowFieldList();
     this.filterPanelField.getShowFilterBox().setValues(showFields);
     
     int[] hideConstructors = classDiagramSettings.getHideConstructorList();
     this.filterPanelConstructor.getHideFilterBox().setValues(hideConstructors);
     int[] showConstructors = classDiagramSettings.getShowConstructorList();
     this.filterPanelConstructor.getShowFilterBox().setValues(showConstructors);
     
     int[] hideMethods = classDiagramSettings.getHideMethodList();
     this.filterPanelMethods.getHideFilterBox().setValues(hideMethods);
     int[] showMethods = classDiagramSettings.getShowConstructorList();
     this.filterPanelMethods.getShowFilterBox().setValues(showMethods);
     
     this.initialised = true;
   }
 
 
   
   public String getDisplayName() {
     return "Filter";
   }
 
 
   
   public boolean isModified(ClassDiagramSettings classDiagramSettings) {
     if (!this.initialised)
     {
       return false;
     }
     
     return (!equalsArray(this.filterPanelField.getShowFilterBox().getValues(), classDiagramSettings.getShowFieldList()) || !equalsArray(this.filterPanelField.getHideFilterBox().getValues(), classDiagramSettings.getHideFieldList()) || !equalsArray(this.filterPanelConstructor.getShowFilterBox().getValues(), classDiagramSettings.getShowConstructorList()) || !equalsArray(this.filterPanelConstructor.getHideFilterBox().getValues(), classDiagramSettings.getHideConstructorList()) || !equalsArray(this.filterPanelMethods.getShowFilterBox().getValues(), classDiagramSettings.getShowMethodList()) || !equalsArray(this.filterPanelMethods.getHideFilterBox().getValues(), classDiagramSettings.getHideMethodList()));
   }
 
 
 
   
   private boolean equalsArray(int[] array1, int[] array2) {
     if (array1.length != array2.length)
     {
       return false;
     }
     
     for (int i = 0; i < array1.length; i++) {
       
       if (array1[i] != array2[i])
       {
         return false;
       }
     } 
     
     return true;
   }
 
 
   
   public void apply(ClassDiagramSettings classDiagramSettings) {
     if (!this.initialised) {
       return;
     }
 
     
     classDiagramSettings.setShowFieldList(this.filterPanelField.getShowFilterBox().getValues());
     classDiagramSettings.setHideFieldList(this.filterPanelField.getHideFilterBox().getValues());
     
     classDiagramSettings.setShowConstructorList(this.filterPanelConstructor.getShowFilterBox().getValues());
     classDiagramSettings.setHideConstructorList(this.filterPanelConstructor.getHideFilterBox().getValues());
     
     classDiagramSettings.setShowMethodList(this.filterPanelMethods.getShowFilterBox().getValues());
     classDiagramSettings.setHideMethodList(this.filterPanelMethods.getHideFilterBox().getValues());
   }
 
 
   
   public void reset(ClassDiagramSettings classDiagramSettings) {
     applySettingsToUI(classDiagramSettings);
   }
 }


