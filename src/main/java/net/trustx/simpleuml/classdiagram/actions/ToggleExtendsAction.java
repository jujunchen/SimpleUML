 package net.trustx.simpleuml.classdiagram.actions;

 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.actionSystem.ToggleAction;
 import javax.swing.ImageIcon;
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponent;



 public class ToggleExtendsAction
   extends ToggleAction
 {
   private ClassDiagramComponent classDiagramComponent;

   public ToggleExtendsAction(ClassDiagramComponent classDiagramComponent) {
     super("Toggle Extends", "Toggle Extends", new ImageIcon(ToggleExtendsAction.class.getResource("/net/trustx/simpleuml/icons/toggleExtendsConnector.png")));
     this.classDiagramComponent = classDiagramComponent;
   }



   public boolean isSelected(AnActionEvent event) {
     return this.classDiagramComponent.getComponentSettings().isPaintExtends();
   }



   public void setSelected(AnActionEvent event, boolean b) {
     this.classDiagramComponent.getComponentSettings().setPaintExtends(b);
   }
 }


