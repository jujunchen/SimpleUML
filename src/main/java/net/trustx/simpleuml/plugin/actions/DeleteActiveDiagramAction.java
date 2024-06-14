 package net.trustx.simpleuml.plugin.actions;

 import com.intellij.openapi.actionSystem.AnAction;
 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.actionSystem.CommonDataKeys;
 import com.intellij.openapi.actionSystem.DataKeys;
 import com.intellij.openapi.project.Project;
 import com.intellij.openapi.ui.Messages;
 import net.trustx.simpleuml.components.DiagramComponent;
 import net.trustx.simpleuml.plugin.UMLToolWindowPlugin;
 import net.trustx.simpleuml.util.UMLUtils;




 public class DeleteActiveDiagramAction
   extends AnAction
 {
   public DeleteActiveDiagramAction() {
     super("Delete");
   }



   public void actionPerformed(AnActionEvent event) {
     Project project = (Project) CommonDataKeys.PROJECT.getData(event.getDataContext());
     if (project == null) {
       return;
     }


     UMLToolWindowPlugin umlToolWindowPlugin = UMLToolWindowPlugin.getUMLToolWindowPlugin(project);
     if (umlToolWindowPlugin == null) {
       return;
     }


     DiagramComponent diagramComponent = umlToolWindowPlugin.getSelectedDiagramComponent();
     if (diagramComponent != null) {

       int response = Messages.showYesNoDialog(project, "Delete diagram \"" + UMLUtils.stripFileType(diagramComponent.getDiagramName()) + "\"?", "Question", Messages.getQuestionIcon());

       if (response == 0)
       {
         umlToolWindowPlugin.deleteRemoveDiagramFromUI(diagramComponent.getFolderURL(), diagramComponent.getDiagramName());
       }
     }
   }
 }


