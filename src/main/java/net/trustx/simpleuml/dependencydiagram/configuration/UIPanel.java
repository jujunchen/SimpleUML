 package net.trustx.simpleuml.dependencydiagram.configuration;
 
 import javax.swing.JPanel;
 
 

 public class UIPanel
   extends JPanel
 {
   public void applySettingsToUI(DependencyDiagramSettings dependencyDiagramSettings) {}
   
   public String getDisplayName() {
     return "UI";
   }
 
 
   
   public boolean isModified(DependencyDiagramSettings dependencyDiagramSettings) {
     return false;
   }
 
 
 
   
   public void apply(DependencyDiagramSettings dependencyDiagramSettings) {}
 
 
   
   public void reset(DependencyDiagramSettings dependencyDiagramSettings) {
     applySettingsToUI(dependencyDiagramSettings);
   }
 }


