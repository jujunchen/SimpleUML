 package net.trustx.simpleuml.classdiagram.actions;

 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.actionSystem.ToggleAction;
 import javax.swing.ImageIcon;
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponent;



 public class ToggleUsesAction
   extends ToggleAction
 {
   private ClassDiagramComponent classDiagramComponent;

   public ToggleUsesAction(ClassDiagramComponent classDiagramComponent) {
     super("Toggle Associations", "Toggle Associations", new ImageIcon(ToggleExtendsAction.class.getResource("/net/trustx/simpleuml/icons/toggleUsesConnector.png")));
     this.classDiagramComponent = classDiagramComponent;
   }



   public boolean isSelected(AnActionEvent event) {
     return this.classDiagramComponent.getComponentSettings().isPaintUses();
   }



   public void setSelected(AnActionEvent event, boolean b) {
     this.classDiagramComponent.getComponentSettings().setPaintUses(b);
   }
 }


