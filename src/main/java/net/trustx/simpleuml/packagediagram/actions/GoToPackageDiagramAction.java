 package net.trustx.simpleuml.packagediagram.actions;
 
 import com.intellij.openapi.actionSystem.AnAction;
 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.actionSystem.DataKeys;
 import com.intellij.openapi.actionSystem.Presentation;
 import com.intellij.psi.PsiElement;
 import net.trustx.simpleuml.components.DiagramComponent;
 import net.trustx.simpleuml.packagediagram.components.PackageDiagramComponentPanel;
 import net.trustx.simpleuml.plugin.UMLToolWindowPlugin;
 import net.trustx.simpleuml.util.UMLUtils;
 
 
 

 
 public class GoToPackageDiagramAction
   extends AnAction
 {
   private PackageDiagramComponentPanel packageDiagramComponentPanel;
   
   public GoToPackageDiagramAction(PackageDiagramComponentPanel packageDiagramComponentPanel) {
     super(UMLUtils.stripFileType(packageDiagramComponentPanel.getDiagramName()));
     this.packageDiagramComponentPanel = packageDiagramComponentPanel;
   }
 
 
   
   public void actionPerformed(AnActionEvent event) {
     PsiElement psiElement = (PsiElement)DataKeys.PSI_ELEMENT.getData(event.getDataContext());
     
     if (psiElement != null) {
       
       UMLToolWindowPlugin umlToolWindowPlugin = UMLToolWindowPlugin.getUMLToolWindowPlugin(this.packageDiagramComponentPanel.getProject());
       umlToolWindowPlugin.showDiagramComponent((DiagramComponent)this.packageDiagramComponentPanel);
       this.packageDiagramComponentPanel.containsHighlight(psiElement);
     } 
   }
 
 
   
   public void update(AnActionEvent event) {
     PsiElement psiElement = (PsiElement)DataKeys.PSI_ELEMENT.getData(event.getDataContext());
     Presentation presentation = event.getPresentation();
     
     if (psiElement != null && this.packageDiagramComponentPanel.contains(psiElement)) {
       
       presentation.setEnabled(true);
       presentation.setVisible(true);
     }
     else {
       
       presentation.setEnabled(false);
       presentation.setVisible(false);
     } 
   }
 }


