 package net.trustx.simpleuml.plugin.actions;

 import com.intellij.openapi.actionSystem.AnAction;
 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.actionSystem.DataKeys;
 import com.intellij.openapi.project.Project;
 import com.intellij.openapi.ui.Messages;
 import net.trustx.simpleuml.components.DiagramComponent;
 import net.trustx.simpleuml.file.UMLLocationChooser;
 import net.trustx.simpleuml.plugin.UMLToolWindowPlugin;
 import net.trustx.simpleuml.util.UMLUtils;

























 public class RenameActiveDiagramAction
   extends AnAction
 {
   public RenameActiveDiagramAction() {
     super("Rename");
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


     int index = umlToolWindowPlugin.getTabbedPane().getSelectedIndex();
     if (index != -1) {


       DiagramComponent diagramComponent = umlToolWindowPlugin.getDiagramComponentAtIndex(index);


       UMLLocationChooser umlLocationChooser = new UMLLocationChooser(project, diagramComponent.getFolderURL(), diagramComponent.getDiagramName(), diagramComponent.getDiagramType(), false);

       umlLocationChooser.setModal(true);
       umlLocationChooser.show();

       if (umlLocationChooser.getExitCode() == 1) {
         return;
       }


       String newFolderURL = umlLocationChooser.getFolderURL();
       String newName = umlLocationChooser.getDiagramName();


       if (umlToolWindowPlugin.existsDiagramOnDisk(newFolderURL, newName)) {


         Messages.showMessageDialog(project, "Diagram already exists", "Error", Messages.getErrorIcon());

         return;
       }
       umlToolWindowPlugin.saveDiagramComponents();
       umlToolWindowPlugin.deleteDiagramComponent(diagramComponent.getFolderURL(), diagramComponent.getDiagramName());


       umlToolWindowPlugin.getDiagramComponentMap().remove(UMLUtils.getWholePath(diagramComponent.getFolderURL(), diagramComponent.getDiagramName()));
       umlToolWindowPlugin.getUMLFileManager().removeURL(UMLUtils.getWholePath(diagramComponent.getFolderURL(), diagramComponent.getDiagramName()));


       diagramComponent.setFolderURL(newFolderURL);
       diagramComponent.setDiagramName(newName);


       umlToolWindowPlugin.getDiagramComponentMap().put(UMLUtils.getWholePath(diagramComponent.getFolderURL(), diagramComponent.getDiagramName()), diagramComponent);


       umlToolWindowPlugin.getTabbedPane().setTitleAt(index, UMLUtils.stripFileType(newName));

       umlToolWindowPlugin.saveDiagramComponents();
     }
   }
 }


