 package net.trustx.simpleuml.sequencediagram.actions;

 import com.intellij.openapi.actionSystem.AnAction;
 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.actionSystem.DataKeys;
 import com.intellij.openapi.project.Project;
 import net.trustx.simpleuml.components.DiagramComponent;
 import net.trustx.simpleuml.plugin.UMLToolWindowPlugin;
 import net.trustx.simpleuml.sequencediagram.components.SequenceDiagramComponent;
















 public class AddReturnOnSequenceAction
   extends AnAction
 {
   public void actionPerformed(AnActionEvent event) {
     Project project = (Project)DataKeys.PROJECT.getData(event.getDataContext());
     UMLToolWindowPlugin umlToolWindowPlugin = UMLToolWindowPlugin.getUMLToolWindowPlugin(project);
     DiagramComponent dc = umlToolWindowPlugin.getSelectedDiagramComponent();
     if (dc instanceof SequenceDiagramComponent) {

       SequenceDiagramComponent seq = (SequenceDiagramComponent)dc;
       seq.getModel().addReturn();
       seq.changesMade();
     }
   }
 }


