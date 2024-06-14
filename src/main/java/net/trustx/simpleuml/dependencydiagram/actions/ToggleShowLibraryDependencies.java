 package net.trustx.simpleuml.dependencydiagram.actions;
 
 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.actionSystem.ToggleAction;
 import javax.swing.ImageIcon;
 import net.trustx.simpleuml.dependencydiagram.components.DependencyDiagramComponent;
 

 
 public class ToggleShowLibraryDependencies
   extends ToggleAction
 {
   private DependencyDiagramComponent dependencyDiagramComponent;
   
   public ToggleShowLibraryDependencies(DependencyDiagramComponent dependencyDiagramComponent) {
     super("Show Library Dependencies", "Show Library Dependencies", new ImageIcon(ToggleShowLibraryDependencies.class.getResource("/objectBrowser/showLibraryContents.png")));
     this.dependencyDiagramComponent = dependencyDiagramComponent;
   }
 
 
   
   public boolean isSelected(AnActionEvent e) {
     return this.dependencyDiagramComponent.getComponentSettings().isShowLibraryDependencies();
   }
 
 
   
   public void setSelected(AnActionEvent e, boolean state) {
     this.dependencyDiagramComponent.getComponentSettings().setShowLibraryDependencies(state);
     this.dependencyDiagramComponent.getDependencyDiagramComponentPanel().refresh();
   }
 }


