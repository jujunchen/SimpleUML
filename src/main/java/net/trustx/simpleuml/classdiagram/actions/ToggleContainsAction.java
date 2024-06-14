 package net.trustx.simpleuml.classdiagram.actions;
 
 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.actionSystem.ToggleAction;
 import javax.swing.ImageIcon;
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponent;
 

 
 
 public class ToggleContainsAction
   extends ToggleAction
 {
   private ClassDiagramComponent classDiagramComponent;
   
   public ToggleContainsAction(ClassDiagramComponent classDiagramComponent) {
     super("Toggle Contains", "Toggle Contains", new ImageIcon(ToggleImplementsAction.class.getResource("/net/trustx/simpleuml/icons/toggleContainsConnector.png")));
     this.classDiagramComponent = classDiagramComponent;
   }
 
 
   
   public boolean isSelected(AnActionEvent event) {
     return this.classDiagramComponent.getComponentSettings().isPaintContains();
   }
 
 
   
   public void setSelected(AnActionEvent event, boolean b) {
     this.classDiagramComponent.getComponentSettings().setPaintContains(b);
   }
 }


