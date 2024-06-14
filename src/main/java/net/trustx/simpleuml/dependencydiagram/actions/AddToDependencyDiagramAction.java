 package net.trustx.simpleuml.dependencydiagram.actions;
 
 import com.intellij.openapi.actionSystem.AnAction;
 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.actionSystem.DataKeys;
 import com.intellij.openapi.actionSystem.Presentation;
 import com.intellij.openapi.project.Project;
 import com.intellij.openapi.wm.ToolWindow;
 import com.intellij.openapi.wm.ToolWindowManager;
 import net.trustx.simpleuml.components.DiagramComponent;
 import net.trustx.simpleuml.dependencydiagram.components.DependencyDiagramComponentPanel;
 import net.trustx.simpleuml.plugin.UMLToolWindowPlugin;
 import net.trustx.simpleuml.util.UMLUtils;
 

 public class AddToDependencyDiagramAction
   extends AnAction
 {
   private DependencyDiagramComponentPanel dependencyDiagramComponentPanel;
   
   public AddToDependencyDiagramAction(DependencyDiagramComponentPanel dependencyDiagramComponentPanel) {
     super(UMLUtils.stripFileType(dependencyDiagramComponentPanel.getDiagramName()));
     this.dependencyDiagramComponentPanel = dependencyDiagramComponentPanel;
   }
 
 
   
   public void actionPerformed(AnActionEvent e) {
     UMLToolWindowPlugin umlToolWindowPlugin = UMLToolWindowPlugin.getUMLToolWindowPlugin(this.dependencyDiagramComponentPanel.getProject());
     umlToolWindowPlugin.showDiagramComponent((DiagramComponent)this.dependencyDiagramComponentPanel);
     addSelectedFilesToDiagram(e);
   }
 
 
   
   private void addSelectedFilesToDiagram(AnActionEvent event) {
     Project project = (Project)DataKeys.PROJECT.getData(event.getDataContext());
     ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("simpleUML");
     
     this.dependencyDiagramComponentPanel.addPsiElements(event);
     toolWindow.activate(null);
   }
 
 
   
   public void update(AnActionEvent event) {
     Presentation presentation = event.getPresentation();
     Project project = (Project)DataKeys.PROJECT.getData(event.getDataContext());
     
     boolean available = isAvailable(event);
     presentation.setEnabled(available);
     presentation.setVisible(available);
     if (!available) {
       return;
     }
 
 
     
     ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("simpleUML");
     if (toolWindow == null) {
       
       presentation.setEnabled(false);
       presentation.setVisible(false);
       return;
     } 
     presentation.setEnabled(toolWindow.isAvailable());
     presentation.setVisible(true);
   }
 
 
   
   private boolean isAvailable(AnActionEvent event) {
     Project project = (Project)DataKeys.PROJECT.getData(event.getDataContext());
     
     if (project == null)
     {
       return false;
     }
     
     return this.dependencyDiagramComponentPanel.canAdd(event);
   }
 }


