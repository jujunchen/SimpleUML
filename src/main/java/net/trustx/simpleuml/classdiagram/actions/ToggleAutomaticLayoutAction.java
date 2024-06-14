 package net.trustx.simpleuml.classdiagram.actions;

 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.actionSystem.ToggleAction;
 import com.intellij.openapi.ui.Messages;
 import javax.swing.ImageIcon;
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponent;


 public class ToggleAutomaticLayoutAction
   extends ToggleAction
 {
   private ClassDiagramComponent classDiagramComponent;

   public ToggleAutomaticLayoutAction(ClassDiagramComponent classDiagramComponent) {
     super("Toggle Automatic Layout", "Toggle Automatic Layout", new ImageIcon(ToggleAutomaticLayoutAction.class.getResource("/net/trustx/simpleuml/icons/toggleAutomaticLayout.png")));
     this.classDiagramComponent = classDiagramComponent;
   }



   public boolean isSelected(AnActionEvent event) {
     return this.classDiagramComponent.getComponentSettings().isLayoutOnChanges();
   }



   public void setSelected(AnActionEvent event, boolean selected) {
     if (selected) {

       int response = Messages.showYesNoDialog(this.classDiagramComponent.getProject(), "This will destroy your current layout.\nDo you want to continue?", "Warning", Messages.getWarningIcon());

       if (response == 0)
       {
         this.classDiagramComponent.getComponentSettings().setLayoutOnChanges(selected);
         this.classDiagramComponent.layoutComponents(false);
       }

     } else {

       this.classDiagramComponent.getComponentSettings().setLayoutOnChanges(selected);
     }
   }
 }


