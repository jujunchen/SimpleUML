 package net.trustx.simpleuml.plugin.actions;

 import com.intellij.openapi.actionSystem.AnAction;
 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.ui.Messages;
 import java.io.IOException;
 import javax.imageio.ImageIO;
 import javax.swing.ImageIcon;
 import javax.swing.JTabbedPane;
 import net.trustx.simpleuml.components.DiagramComponent;
 import net.trustx.simpleuml.components.DiagramSettingsWorkspace;
 import net.trustx.simpleuml.components.Previewable;
 import net.trustx.simpleuml.components.SaveAsImageDialog;
 import net.trustx.simpleuml.plugin.UMLToolWindowPlugin;


























 public class SaveAsImageAction
   extends AnAction
 {
   private UMLToolWindowPlugin umlToolWindowPlugin;

   public SaveAsImageAction(UMLToolWindowPlugin umlToolWindowPlugin) {
     super("Save As Image", "Saves the currently active diagram as an image", new ImageIcon(SaveAsImageAction.class.getResource("/net/trustx/simpleuml/icons/saveAsImage.png")));
     this.umlToolWindowPlugin = umlToolWindowPlugin;
   }



   public void actionPerformed(AnActionEvent event) {
     JTabbedPane diagramTabbedPane = this.umlToolWindowPlugin.getTabbedPane();
     if (diagramTabbedPane.getTabCount() == 0) {

       Messages.showMessageDialog(this.umlToolWindowPlugin.getProject(), "No diagram available", "Information", Messages.getInformationIcon());
     } else {


       try {

         DiagramComponent diagramComponent = UMLToolWindowPlugin.getUMLToolWindowPlugin(this.umlToolWindowPlugin.getProject()).getSelectedDiagramComponent();
         if (diagramComponent instanceof Previewable)
         {
           Previewable previewable = (Previewable)diagramComponent;
           showSaveAsImageDialog(previewable);
         }

       } catch (Exception e1) {

         Messages.showMessageDialog(e1.getMessage(), "Error", Messages.getErrorIcon());
         e1.printStackTrace();
       }
     }
   }



   private void showSaveAsImageDialog(Previewable previewable) throws IOException {
     SaveAsImageDialog saveAsImageDialog = new SaveAsImageDialog(this.umlToolWindowPlugin.getProject(), previewable);
     saveAsImageDialog.setModal(true);

     String path = (String)DiagramSettingsWorkspace.getDiagramSettingsWorkspace(this.umlToolWindowPlugin.getProject()).get("SaveAsImageDialog.path");
     if (path != null)
     {
       saveAsImageDialog.setImagePath(path);
     }

     String type = (String)DiagramSettingsWorkspace.getDiagramSettingsWorkspace(this.umlToolWindowPlugin.getProject()).get("SaveAsImageDialog.type");
     if (type != null)
     {
       saveAsImageDialog.setSelectedImageType(type);
     }

     saveAsImageDialog.show();

     if (saveAsImageDialog.getExitCode() == 0) {

       DiagramSettingsWorkspace.getDiagramSettingsWorkspace(this.umlToolWindowPlugin.getProject()).put("SaveAsImageDialog.path", saveAsImageDialog.getSelectedFile().getCanonicalPath());
       DiagramSettingsWorkspace.getDiagramSettingsWorkspace(this.umlToolWindowPlugin.getProject()).put("SaveAsImageDialog.type", saveAsImageDialog.getSelectedImageType());
       ImageIO.write(saveAsImageDialog.getDiagramPreviewPanel().getBufferedImage(), saveAsImageDialog.getTypeComboBox().getSelectedItem().toString(), saveAsImageDialog.getSelectedFile());
     }
   }



   public void update(AnActionEvent event) {
     DiagramComponent selectedDiagramComponent = UMLToolWindowPlugin.getUMLToolWindowPlugin(this.umlToolWindowPlugin.getProject()).getSelectedDiagramComponent();
     event.getPresentation().setEnabled((selectedDiagramComponent != null && selectedDiagramComponent instanceof Previewable));
   }
 }


