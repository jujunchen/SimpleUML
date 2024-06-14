 package net.trustx.simpleuml.classdiagram.configuration;



 public class ClassesPanel
   extends ClassesPanelUI
 {
   private boolean initialised = false;

   public void applySettingsToUI(ClassDiagramSettings classDiagramSettings) {
     getFieldsExpandedBox().setSelected(classDiagramSettings.isDefaultFieldsExpanded());
     getConstructorsExpandedBox().setSelected(classDiagramSettings.isDefaultContructorsExpanded());
     getMethodsExpandedBox().setSelected(classDiagramSettings.isDefaultMethodsExpanded());

     getShowParamtersBox().setSelected(classDiagramSettings.isShowParameters());
     getShowLongModifiersBox().setSelected(classDiagramSettings.isLongModifier());

     int cB = classDiagramSettings.getCompartmentBehaviour();
     if (cB == 1) {

       getCompartmentShowRadioButton().setSelected(true);
     }
     else if (cB == 4) {

       getCompartmentHideConditionalRadioButton().setSelected(true);
     }
     else if (cB == 2) {

       getCompartmentHideRadioButton().setSelected(true);
     }

     int cE = classDiagramSettings.getExtendsBehaviour();
     if (cE == 1) {

       getExtendsShowRadioButton().setSelected(true);
     }
     else if (cE == 4) {

       getExtendsHideConditionalRadioButton().setSelected(true);
     }
     else if (cE == 2) {

       getExtendsHideRadioButton().setSelected(true);
     }

     int cI = classDiagramSettings.getImplementsBehaviour();
     if (cI == 1) {

       getImplementsShowRadioButton().setSelected(true);
     }
     else if (cI == 4) {

       getImplementsHideConditionalRadioButton().setSelected(true);
     }
     else if (cI == 2) {

       getImplementsHideRadioButton().setSelected(true);
     }

     this.initialised = true;
   }



   public String getDisplayName() {
     return "Classes";
   }



   public boolean isModified(ClassDiagramSettings classDiagramSettings) {
     if (!this.initialised)
     {
       return false;
     }

     return (getFieldsExpandedBox().isSelected() != classDiagramSettings.isDefaultFieldsExpanded() || getConstructorsExpandedBox().isSelected() != classDiagramSettings.isDefaultContructorsExpanded() || getMethodsExpandedBox().isSelected() != classDiagramSettings.isDefaultMethodsExpanded() || getShowParamtersBox().isSelected() != classDiagramSettings.isShowParameters() || getShowLongModifiersBox().isSelected() != classDiagramSettings.isLongModifier() || (getCompartmentShowRadioButton().isSelected() && classDiagramSettings.getCompartmentBehaviour() != 1) || (getCompartmentHideConditionalRadioButton().isSelected() && classDiagramSettings.getCompartmentBehaviour() != 4) || (getCompartmentHideRadioButton().isSelected() && classDiagramSettings.getCompartmentBehaviour() != 2) || (getExtendsShowRadioButton().isSelected() && classDiagramSettings.getExtendsBehaviour() != 1) || (getExtendsHideConditionalRadioButton().isSelected() && classDiagramSettings.getExtendsBehaviour() != 4) || (getExtendsHideRadioButton().isSelected() && classDiagramSettings.getExtendsBehaviour() != 2) || (getImplementsShowRadioButton().isSelected() && classDiagramSettings.getImplementsBehaviour() != 1) || (getImplementsHideConditionalRadioButton().isSelected() && classDiagramSettings.getImplementsBehaviour() != 4) || (getImplementsHideRadioButton().isSelected() && classDiagramSettings.getImplementsBehaviour() != 2));
   }




   public void apply(ClassDiagramSettings classDiagramSettings) {
     if (!this.initialised) {
       return;
     }


     classDiagramSettings.setDefaultFieldsExpanded(getFieldsExpandedBox().isSelected());
     classDiagramSettings.setDefaultContructorsExpanded(getConstructorsExpandedBox().isSelected());
     classDiagramSettings.setDefaultMethodsExpanded(getMethodsExpandedBox().isSelected());

     classDiagramSettings.setShowParameters(getShowParamtersBox().isSelected());
     classDiagramSettings.setLongModifier(getShowLongModifiersBox().isSelected());

     if (getCompartmentShowRadioButton().isSelected()) {

       classDiagramSettings.setCompartmentBehaviour(1);
     }
     else if (getCompartmentHideConditionalRadioButton().isSelected()) {

       classDiagramSettings.setCompartmentBehaviour(4);
     }
     else if (getCompartmentHideRadioButton().isSelected()) {

       classDiagramSettings.setCompartmentBehaviour(2);
     }

     if (getExtendsShowRadioButton().isSelected()) {

       classDiagramSettings.setExtendsBehaviour(1);
     }
     else if (getExtendsHideConditionalRadioButton().isSelected()) {

       classDiagramSettings.setExtendsBehaviour(4);
     }
     else if (getExtendsHideRadioButton().isSelected()) {

       classDiagramSettings.setExtendsBehaviour(2);
     }

     if (getImplementsShowRadioButton().isSelected()) {

       classDiagramSettings.setImplementsBehaviour(1);
     }
     else if (getImplementsHideConditionalRadioButton().isSelected()) {

       classDiagramSettings.setImplementsBehaviour(4);
     }
     else if (getImplementsHideRadioButton().isSelected()) {

       classDiagramSettings.setImplementsBehaviour(2);
     }
   }




   public void reset(ClassDiagramSettings classDiagramSettings) {
     applySettingsToUI(classDiagramSettings);
   }
 }


