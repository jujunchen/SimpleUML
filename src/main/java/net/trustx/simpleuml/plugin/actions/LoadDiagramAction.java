 package net.trustx.simpleuml.plugin.actions;
 
 import com.intellij.openapi.actionSystem.AnAction;
 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.actionSystem.DataKeys;
 import com.intellij.openapi.project.Project;
 import com.intellij.openapi.vfs.VirtualFile;
 import com.intellij.openapi.vfs.VirtualFileManager;
 import java.util.HashSet;
 import java.util.Set;
 import javax.swing.ImageIcon;
 import net.trustx.simpleuml.components.DiagramComponent;
 import net.trustx.simpleuml.file.KnownFilesChooser;
 import net.trustx.simpleuml.plugin.DiagramFactory;
 import net.trustx.simpleuml.plugin.UMLToolWindowPlugin;
 import net.trustx.simpleuml.plugin.UnknownDiagramTypeException;
 import net.trustx.simpleuml.util.UMLUtils;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class LoadDiagramAction
   extends AnAction
 {
   public LoadDiagramAction() {
     super("Load Diagram", "Loads a diagram from disk", new ImageIcon(LoadDiagramAction.class.getResource("/net/trustx/simpleuml/icons/loadDiagramIcon.png")));
   }
 
 
   
   public void actionPerformed(AnActionEvent event) {
     Project project = (Project)DataKeys.PROJECT.getData(event.getDataContext());
     UMLToolWindowPlugin umlToolWindowPlugin = UMLToolWindowPlugin.getUMLToolWindowPlugin(project);
     Set diagramsNotOnUI = umlToolWindowPlugin.getKnownDiagramComponentsNotOnUI();
     String[] availableValues = (String[])diagramsNotOnUI.toArray((Object[])new String[diagramsNotOnUI.size()]);
     
     Set<VirtualFile> treeSet = new HashSet();
     
     for (int i = 0; i < availableValues.length; i++) {
       
       String availableValue = availableValues[i];
       VirtualFile vf = VirtualFileManager.getInstance().findFileByUrl(availableValue);
       if (vf != null)
       {
         treeSet.add(vf);
       }
     } 
     
     KnownFilesChooser knownFilesChooser = new KnownFilesChooser(project, treeSet);
     knownFilesChooser.setCrossClosesWindow(true);
     knownFilesChooser.setModal(true);
     knownFilesChooser.show();
     if (knownFilesChooser.getExitCode() == 1) {
       return;
     }
 
     
     VirtualFile selectedFile = knownFilesChooser.getSelectedFile();
     
     if (selectedFile != null) {
       
       VirtualFile parent = selectedFile.getParent();
       if (parent == null)
         return; 
       if (umlToolWindowPlugin.isDiagramComponentOnUI(parent.getUrl(), selectedFile.getName())) {
         
         DiagramComponent diagramComponent = umlToolWindowPlugin.getDiagramComponent(parent.getUrl(), selectedFile.getName());
         umlToolWindowPlugin.showDiagramComponent(diagramComponent);
         
         return;
       } 
       
       try {
         DiagramComponent diagramComponent = DiagramFactory.createDiagramComponent(project, parent.getUrl(), selectedFile.getName());
         umlToolWindowPlugin.showDiagramComponent(diagramComponent);
       }
       catch (UnknownDiagramTypeException ex) {
         
         UMLUtils.showInternalErrorMessageDialog(project, "Internal Error", "Internal Error in simpleUML.", (Throwable)ex);
       } 
     } 
   }
 }


