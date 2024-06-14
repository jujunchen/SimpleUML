 package net.trustx.simpleuml.classdiagram.actions;
 
 import com.intellij.openapi.actionSystem.AnAction;
 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.actionSystem.DataKeys;
 import com.intellij.openapi.actionSystem.Presentation;
 import com.intellij.openapi.project.Project;
 import com.intellij.openapi.wm.ToolWindow;
 import com.intellij.openapi.wm.ToolWindowManager;
 import com.intellij.psi.PsiClass;
 import com.intellij.psi.PsiElement;
 import com.intellij.psi.PsiFile;
 import com.intellij.psi.PsiJavaFile;
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponentPanel;
 import net.trustx.simpleuml.components.DiagramComponent;
 import net.trustx.simpleuml.plugin.UMLToolWindowPlugin;
 import net.trustx.simpleuml.util.UMLUtils;
 

 
 
 public class AddToClassDiagramAction
   extends AnAction
 {
   private ClassDiagramComponentPanel classDiagramComponentPanel;
   
   public AddToClassDiagramAction(ClassDiagramComponentPanel classDiagramComponentPanel) {
     super(UMLUtils.stripFileType(classDiagramComponentPanel.getDiagramName()));
     this.classDiagramComponentPanel = classDiagramComponentPanel;
   }
 
 
   
   public void actionPerformed(AnActionEvent e) {
     UMLToolWindowPlugin umlToolWindowPlugin = UMLToolWindowPlugin.getUMLToolWindowPlugin(this.classDiagramComponentPanel.getProject());
     umlToolWindowPlugin.showDiagramComponent((DiagramComponent)this.classDiagramComponentPanel);
     addSelectedFilesToDiagram(e);
   }
 
 
   
   private void addSelectedFilesToDiagram(AnActionEvent event) {
     Project project = (Project)DataKeys.PROJECT.getData(event.getDataContext());
     ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("simpleUML");
     
     PsiFile psiFile = (PsiFile)DataKeys.PSI_FILE.getData(event.getDataContext());
     
     PsiElement[] selectedElements = (PsiElement[])DataKeys.PSI_ELEMENT_ARRAY.getData(event.getDataContext());
     PsiElement selectedElement = (PsiElement)DataKeys.PSI_ELEMENT.getData(event.getDataContext());
     
     if (selectedElements != null) {
       
       this.classDiagramComponentPanel.addSelectedClasses(selectedElements, project, 3);
       toolWindow.activate(null);
     }
     else if (selectedElement != null && selectedElement instanceof PsiClass) {
       
       this.classDiagramComponentPanel.addSelectedClasses(new PsiElement[] { selectedElement }, project, 3);
       toolWindow.activate(null);
     }
     else if (psiFile != null && psiFile instanceof PsiJavaFile) {
       
       PsiJavaFile psiJavaFile = (PsiJavaFile)psiFile;
       PsiClass[] psiClasses = psiJavaFile.getClasses();
       this.classDiagramComponentPanel.addSelectedClasses((PsiElement[])psiClasses, project, 3);
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
     PsiFile psiFile = (PsiFile)DataKeys.PSI_FILE.getData(event.getDataContext());
     
     if (psiElements == null && psiElement == null && psiFile == null)
     {
       return false;
     }
     
     if (psiElement != null) {
       
       if (psiElement instanceof PsiClass || psiElement instanceof com.intellij.psi.PsiDirectory)
       {
         return true;
       }
 
       
       return (psiFile != null && psiFile instanceof PsiJavaFile);
     } 
 
     
     if (psiElements != null)
     {
       return (psiElements.length > 0);
     }
     
     return psiFile instanceof PsiJavaFile;
   }
 }


