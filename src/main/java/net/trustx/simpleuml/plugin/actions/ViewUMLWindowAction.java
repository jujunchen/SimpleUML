 package net.trustx.simpleuml.plugin.actions;
 
 import com.intellij.openapi.actionSystem.ActionGroup;
 import com.intellij.openapi.actionSystem.AnAction;
 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.actionSystem.DataKeys;
 import com.intellij.openapi.actionSystem.Separator;
 import com.intellij.openapi.project.Project;
 import net.trustx.simpleuml.components.DiagramComponent;
 import net.trustx.simpleuml.plugin.UMLToolWindowPlugin;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ViewUMLWindowAction
   extends ActionGroup
 {
   private AnAction newDiagramAction = new NewDiagramAction();
   private AnAction separatorAction = (AnAction)Separator.getInstance();
 
 
 
   
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
 
     
     AnAction[] actions = new AnAction[components.length + 2];
     actions[0] = this.newDiagramAction;
     actions[1] = this.separatorAction;
 
     
     for (int i = 0; i < components.length; i++)
     {
       actions[i + 2] = components[i].getAddToDiagramComponentAction();
     }
     
     return actions;
   }
 }


