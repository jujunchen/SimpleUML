 package net.trustx.simpleuml.classdiagram.actions;

 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.actionSystem.ToggleAction;
 import javax.swing.ImageIcon;
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponent;




 public class ToggleInnerAction
   extends ToggleAction
 {
   private ClassDiagramComponent classDiagramComponent;

   public ToggleInnerAction(ClassDiagramComponent classDiagramComponent) {
     super("Toggle Inner", "Toggle Inner", new ImageIcon(ToggleImplementsAction.class.getResource("/net/trustx/simpleuml/icons/toggleInnerConnector.png")));
     this.classDiagramComponent = classDiagramComponent;
   }



   public boolean isSelected(AnActionEvent event) {
     return this.classDiagramComponent.getComponentSettings().isPaintInner();
   }



   public void setSelected(AnActionEvent event, boolean b) {
     this.classDiagramComponent.getComponentSettings().setPaintInner(b);
   }
 }


