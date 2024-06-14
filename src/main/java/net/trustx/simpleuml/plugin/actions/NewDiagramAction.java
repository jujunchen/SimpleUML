 package net.trustx.simpleuml.plugin.actions;
 
 import com.intellij.openapi.actionSystem.AnAction;
 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.actionSystem.DataKeys;
 import com.intellij.openapi.project.Project;
 import com.intellij.openapi.ui.Messages;
 import net.trustx.simpleuml.components.DiagramComponent;
 import net.trustx.simpleuml.file.UMLLocationChooser;
 import net.trustx.simpleuml.plugin.DiagramFactory;
 import net.trustx.simpleuml.plugin.UMLToolWindowPlugin;
 import net.trustx.simpleuml.plugin.UnknownDiagramTypeException;
 import net.trustx.simpleuml.util.UMLUtils;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class NewDiagramAction
   extends AnAction
 {
   public NewDiagramAction() {
     super("New Diagram...");
   }
 
 
   
   public void actionPerformed(AnActionEvent event) {
     Project project = (Project)DataKeys.PROJECT.getData(event.getDataContext());
     
     UMLLocationChooser umlLocationChooser = new UMLLocationChooser(project, null, null, null, true);
     umlLocationChooser.setModal(true);
     umlLocationChooser.show();
     
     if (umlLocationChooser.getExitCode() == 1) {
       return;
     }
 
     
     UMLToolWindowPlugin umlToolWindowPlugin = UMLToolWindowPlugin.getUMLToolWindowPlugin(project);
     
     String folderURL = umlLocationChooser.getFolderURL();
     String diagramName = umlLocationChooser.getDiagramName();
 
 
 
     
     try {
       if (umlToolWindowPlugin.isDiagramComponentOnUI(folderURL, diagramName)) {
         
         handleDiagramExistsOnUI(diagramName, umlToolWindowPlugin, folderURL, event, project, umlLocationChooser);
       }
       else if (umlToolWindowPlugin.isDiagramComponentOnDisk(folderURL, diagramName)) {
         
         handleDiagramExistsOnDisk(diagramName, project, folderURL, umlToolWindowPlugin, umlLocationChooser, event);
       }
       else {
         
         DiagramComponent diagramComponent = DiagramFactory.createNewDiagramComponent(project, folderURL, diagramName, umlLocationChooser.getDiagramType());
         umlToolWindowPlugin.showDiagramComponent(diagramComponent);
         diagramComponent.addPsiElements(event);
       } 
       
       umlToolWindowPlugin.saveDiagramComponents();
     }
     catch (UnknownDiagramTypeException ex) {
       
       UMLUtils.showInternalErrorMessageDialog(project, "Internal Error", "Internal Error in simpleUML.", (Throwable)ex);
     } 
   }
 
 
 
 
 
   
   private void handleDiagramExistsOnDisk(String diagramName, Project project, String folderURL, UMLToolWindowPlugin umlToolWindowPlugin, UMLLocationChooser umlLocationChooser, AnActionEvent event) throws UnknownDiagramTypeException {
     String displayName = UMLUtils.stripFileType(diagramName);
     String[] values = { "Load " + displayName, "Overwrite " + displayName, "Load and Add elements to " + displayName };
     
     int response = Messages.showChooseDialog("Diagram already exists on disk.", "Choose", values, values[0], Messages.getQuestionIcon());
 
     
     if (response == 0) {
       
       DiagramComponent diagramComponent = DiagramFactory.createDiagramComponent(project, folderURL, diagramName);
       umlToolWindowPlugin.showDiagramComponent(diagramComponent);
     }
     else if (response == 1) {
       
       DiagramComponent diagramComponent = DiagramFactory.createNewDiagramComponent(project, folderURL, diagramName, umlLocationChooser.getDiagramType());
       umlToolWindowPlugin.showDiagramComponent(diagramComponent);
       diagramComponent.addPsiElements(event);
     }
     else if (response == 2) {
       
       DiagramComponent diagramComponent = DiagramFactory.createDiagramComponent(project, folderURL, diagramName);
       umlToolWindowPlugin.showDiagramComponent(diagramComponent);
       diagramComponent.addPsiElements(event);
     } 
   }
 
 
 
   
   private void handleDiagramExistsOnUI(String diagramName, UMLToolWindowPlugin umlToolWindowPlugin, String folderURL, AnActionEvent event, Project project, UMLLocationChooser umlLocationChooser) throws UnknownDiagramTypeException {
     String displayName = UMLUtils.stripFileType(diagramName);
     String[] values = { "Add elements to " + displayName, "Overwrite " + displayName };
     
     int response = Messages.showChooseDialog("Diagram is already open.", "Choose", values, values[0], Messages.getQuestionIcon());
 
     
     if (response == 0) {
       
       DiagramComponent existingDiagramComponent = umlToolWindowPlugin.getDiagramComponent(folderURL, diagramName);
       umlToolWindowPlugin.showDiagramComponent(existingDiagramComponent);
       existingDiagramComponent.addPsiElements(event);
     }
     else if (response == 1) {
       
       umlToolWindowPlugin.deleteRemoveDiagramFromUI(folderURL, diagramName);
       
       DiagramComponent diagramComponent = DiagramFactory.createNewDiagramComponent(project, folderURL, diagramName, umlLocationChooser.getDiagramType());
       umlToolWindowPlugin.showDiagramComponent(diagramComponent);
       diagramComponent.addPsiElements(event);
     } 
   }
 }


