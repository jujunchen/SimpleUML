 package net.trustx.simpleuml.packagediagram.actions;

 import com.intellij.openapi.actionSystem.AnAction;
 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.actionSystem.DataKeys;
 import com.intellij.openapi.actionSystem.Presentation;
 import com.intellij.openapi.project.Project;
 import com.intellij.openapi.wm.ToolWindow;
 import com.intellij.openapi.wm.ToolWindowManager;
 import com.intellij.psi.PsiElement;
 import net.trustx.simpleuml.components.DiagramComponent;
 import net.trustx.simpleuml.packagediagram.components.PackageDiagramComponentPanel;
 import net.trustx.simpleuml.plugin.UMLToolWindowPlugin;
 import net.trustx.simpleuml.util.UMLUtils;





 public class AddToPackageDiagramAction
   extends AnAction
 {
   private PackageDiagramComponentPanel packageDiagramComponentPanel;

   public AddToPackageDiagramAction(PackageDiagramComponentPanel packageDiagramComponentPanel) {
     super(UMLUtils.stripFileType(packageDiagramComponentPanel.getDiagramName()));
     this.packageDiagramComponentPanel = packageDiagramComponentPanel;
   }



   public void actionPerformed(AnActionEvent e) {
     UMLToolWindowPlugin umlToolWindowPlugin = UMLToolWindowPlugin.getUMLToolWindowPlugin(this.packageDiagramComponentPanel.getProject());
     umlToolWindowPlugin.showDiagramComponent((DiagramComponent)this.packageDiagramComponentPanel);
     addSelectedFilesToDiagram(e);
   }



   private void addSelectedFilesToDiagram(AnActionEvent event) {
     Project project = (Project)DataKeys.PROJECT.getData(event.getDataContext());
     ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("simpleUML");

     PsiElement[] selectedElements = (PsiElement[])DataKeys.PSI_ELEMENT_ARRAY.getData(event.getDataContext());
     PsiElement selectedElement = (PsiElement)DataKeys.PSI_ELEMENT.getData(event.getDataContext());


     if (selectedElements != null) {

       this.packageDiagramComponentPanel.addSelectedPackages(selectedElements, project, 3);
       toolWindow.activate(null);
     }
     else if (selectedElement != null && selectedElement instanceof com.intellij.psi.PsiPackage) {

       this.packageDiagramComponentPanel.addSelectedPackages(new PsiElement[] { selectedElement }, project, 3);
       toolWindow.activate(null);
     }
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

     PsiElement[] psiElements = (PsiElement[])DataKeys.PSI_ELEMENT_ARRAY.getData(event.getDataContext());
     PsiElement psiElement = (PsiElement)DataKeys.PSI_ELEMENT.getData(event.getDataContext());

     if (psiElements == null && psiElement == null)
     {
       return false;
     }
     if (psiElement != null)
     {
       return (psiElement instanceof com.intellij.psi.PsiPackage || psiElement instanceof com.intellij.psi.PsiDirectory);
     }

     return (psiElements.length > 0);
   }
 }


