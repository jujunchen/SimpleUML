 package net.trustx.simpleuml.classdiagram.actions;

 import com.intellij.openapi.actionSystem.AnAction;
 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.actionSystem.DataKeys;
 import com.intellij.openapi.actionSystem.Presentation;
 import com.intellij.psi.PsiElement;
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponentPanel;
 import net.trustx.simpleuml.components.DiagramComponent;
 import net.trustx.simpleuml.plugin.UMLToolWindowPlugin;
 import net.trustx.simpleuml.util.UMLUtils;




 public class GoToClassDiagramAction
   extends AnAction
 {
   private ClassDiagramComponentPanel classDiagramComponentPanel;

   public GoToClassDiagramAction(ClassDiagramComponentPanel classDiagramComponentPanel) {
     super(UMLUtils.stripFileType(classDiagramComponentPanel.getDiagramName()));
     this.classDiagramComponentPanel = classDiagramComponentPanel;
   }



   public void actionPerformed(AnActionEvent event) {
     PsiElement psiElement = (PsiElement)DataKeys.PSI_ELEMENT.getData(event.getDataContext());

     if (psiElement != null) {

       UMLToolWindowPlugin umlToolWindowPlugin = UMLToolWindowPlugin.getUMLToolWindowPlugin(this.classDiagramComponentPanel.getProject());
       umlToolWindowPlugin.showDiagramComponent((DiagramComponent)this.classDiagramComponentPanel);
       this.classDiagramComponentPanel.containsHighlight(psiElement);
     }
   }



   public void update(AnActionEvent event) {
     PsiElement psiElement = (PsiElement)DataKeys.PSI_ELEMENT.getData(event.getDataContext());
     Presentation presentation = event.getPresentation();

     if (psiElement != null && this.classDiagramComponentPanel.contains(psiElement)) {

       presentation.setEnabled(true);
       presentation.setVisible(true);
     }
     else {

       presentation.setEnabled(false);
       presentation.setVisible(false);
     }
   }
 }


