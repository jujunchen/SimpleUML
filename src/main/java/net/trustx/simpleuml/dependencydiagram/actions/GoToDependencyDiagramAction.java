 package net.trustx.simpleuml.dependencydiagram.actions;
 
 import com.intellij.openapi.actionSystem.AnAction;
 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.actionSystem.DataKeys;
 import com.intellij.openapi.actionSystem.Presentation;
 import com.intellij.psi.PsiElement;
 import net.trustx.simpleuml.components.DiagramComponent;
 import net.trustx.simpleuml.dependencydiagram.components.DependencyDiagramComponentPanel;
 import net.trustx.simpleuml.plugin.UMLToolWindowPlugin;
 import net.trustx.simpleuml.util.UMLUtils;
 
 

 public class GoToDependencyDiagramAction
   extends AnAction
 {
   private DependencyDiagramComponentPanel dependencyDiagramComponentPanel;
   
   public GoToDependencyDiagramAction(DependencyDiagramComponentPanel dependencyDiagramComponentPanel) {
     super(UMLUtils.stripFileType(dependencyDiagramComponentPanel.getDiagramName()));
     this.dependencyDiagramComponentPanel = dependencyDiagramComponentPanel;
   }
 
 
   
   public void actionPerformed(AnActionEvent event) {
     PsiElement psiElement = (PsiElement)DataKeys.PSI_ELEMENT.getData(event.getDataContext());
     
     if (psiElement != null) {
       
       UMLToolWindowPlugin umlToolWindowPlugin = UMLToolWindowPlugin.getUMLToolWindowPlugin(this.dependencyDiagramComponentPanel.getProject());
       umlToolWindowPlugin.showDiagramComponent((DiagramComponent)this.dependencyDiagramComponentPanel);
       this.dependencyDiagramComponentPanel.containsHighlight(psiElement);
     } 
   }
 
 
   
   public void update(AnActionEvent event) {
     PsiElement psiElement = (PsiElement)DataKeys.PSI_ELEMENT.getData(event.getDataContext());
     Presentation presentation = event.getPresentation();
     
     if (psiElement != null && this.dependencyDiagramComponentPanel.contains(psiElement)) {
       
       presentation.setEnabled(true);
       presentation.setVisible(true);
     }
     else {
       
       presentation.setEnabled(false);
       presentation.setVisible(false);
     } 
   }
 }


