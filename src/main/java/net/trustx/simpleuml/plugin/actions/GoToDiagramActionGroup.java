 package net.trustx.simpleuml.plugin.actions;

 import com.intellij.openapi.actionSystem.ActionGroup;
 import com.intellij.openapi.actionSystem.AnAction;
 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.actionSystem.DataKeys;
 import com.intellij.openapi.actionSystem.Presentation;
 import com.intellij.openapi.project.Project;
 import java.util.ArrayList;
 import net.trustx.simpleuml.components.DiagramComponent;
 import net.trustx.simpleuml.plugin.UMLToolWindowPlugin;


 public class GoToDiagramActionGroup
   extends ActionGroup
 {
   public AnAction[] getChildren(AnActionEvent event) {
     if (event == null)
     {
       return new AnAction[0];
     }

     Project project = (Project)DataKeys.PROJECT.getData(event.getDataContext());
     if (project == null)
     {
       return new AnAction[0];
     }
     UMLToolWindowPlugin umlToolWindowPlugin = UMLToolWindowPlugin.getUMLToolWindowPlugin(project);
     if (umlToolWindowPlugin == null)
     {
       return new AnAction[0];
     }
     DiagramComponent[] components = umlToolWindowPlugin.getDiagramComponentsOnUI();

     ArrayList<AnAction> actionList = new ArrayList();

     for (int i = 0; i < components.length; i++) {

       AnAction goToDiagramAction = components[i].getGoToDiagramComponentAction();
       actionList.add(goToDiagramAction);
     }

     return actionList.<AnAction>toArray(new AnAction[actionList.size()]);
   }



   public void update(AnActionEvent event) {
     Presentation presentation = event.getPresentation();
     Project project = (Project)DataKeys.PROJECT.getData(event.getDataContext());

     if (project == null || UMLToolWindowPlugin.getUMLToolWindowPlugin(project) == null) {

       presentation.setEnabled(false);
       presentation.setVisible(false);
     }
   }
 }


