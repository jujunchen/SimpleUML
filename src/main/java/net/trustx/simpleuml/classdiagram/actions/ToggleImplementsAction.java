 package net.trustx.simpleuml.classdiagram.actions;

 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.actionSystem.ToggleAction;
 import javax.swing.ImageIcon;
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponent;




 public class ToggleImplementsAction
   extends ToggleAction
 {
   private ClassDiagramComponent classDiagramComponent;

   public ToggleImplementsAction(ClassDiagramComponent classDiagramComponent) {
     super("Toggle Implements", "Toggle Implements", new ImageIcon(ToggleImplementsAction.class.getResource("/net/trustx/simpleuml/icons/toggleImplementsConnector.png")));
     this.classDiagramComponent = classDiagramComponent;
   }



   public boolean isSelected(AnActionEvent event) {
     return this.classDiagramComponent.getComponentSettings().isPaintImplements();
   }



   public void setSelected(AnActionEvent event, boolean b) {
     this.classDiagramComponent.getComponentSettings().setPaintImplements(b);
   }
 }


