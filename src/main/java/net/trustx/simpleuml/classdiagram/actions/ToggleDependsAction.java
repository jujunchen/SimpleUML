 package net.trustx.simpleuml.classdiagram.actions;

 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.actionSystem.ToggleAction;
 import javax.swing.ImageIcon;
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponent;



 public class ToggleDependsAction
   extends ToggleAction
 {
   private ClassDiagramComponent classDiagramComponent;

   public ToggleDependsAction(ClassDiagramComponent classDiagramComponent) {
     super("Toggle Depends", "Toggle Depends", new ImageIcon(ToggleImplementsAction.class.getResource("/net/trustx/simpleuml/icons/toggleDependsConnector.png")));
     this.classDiagramComponent = classDiagramComponent;
   }



   public boolean isSelected(AnActionEvent event) {
     return this.classDiagramComponent.getComponentSettings().isPaintDepends();
   }



   public void setSelected(AnActionEvent event, boolean b) {
     this.classDiagramComponent.getComponentSettings().setPaintDepends(b);
   }
 }


