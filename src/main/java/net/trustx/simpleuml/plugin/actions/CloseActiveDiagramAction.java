 package net.trustx.simpleuml.plugin.actions;

 import com.intellij.openapi.actionSystem.AnAction;
 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.actionSystem.DataKeys;
 import com.intellij.openapi.project.Project;
 import net.trustx.simpleuml.components.DiagramComponent;
 import net.trustx.simpleuml.plugin.UMLToolWindowPlugin;



 public class CloseActiveDiagramAction
   extends AnAction
 {
   public CloseActiveDiagramAction() {
     super("Close");
   }



   public void actionPerformed(AnActionEvent event) {
     Project project = (Project)DataKeys.PROJECT.getData(event.getDataContext());
     if (project == null) {
       return;
     }


     UMLToolWindowPlugin umlToolWindowPlugin = UMLToolWindowPlugin.getUMLToolWindowPlugin(project);
     if (umlToolWindowPlugin == null) {
       return;
     }


     DiagramComponent diagramComponent = umlToolWindowPlugin.getSelectedDiagramComponent();
     if (diagramComponent != null)
     {
       umlToolWindowPlugin.storeRemoveDiagramFromUI(diagramComponent.getFolderURL(), diagramComponent.getDiagramName());
     }
   }
 }


