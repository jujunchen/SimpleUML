 package net.trustx.simpleuml.classdiagram.actions;

 import com.intellij.openapi.actionSystem.AnAction;
 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.ui.Messages;
 import com.intellij.psi.JavaPsiFacade;
 import com.intellij.psi.PsiClass;
 import com.intellij.psi.PsiElement;
 import com.intellij.psi.search.GlobalSearchScope;
 import javax.swing.ImageIcon;
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponentPanel;





 public class AddFullyQualifiedClassNameToDiagramAction
   extends AnAction
 {
   private ClassDiagramComponentPanel classDiagramComponentPanel;
   private String fullyQualifiedClassName;

   public AddFullyQualifiedClassNameToDiagramAction(ClassDiagramComponentPanel classDiagramComponentPanel) {
     super("Add Class", "Adds a class to the currently active diagram", new ImageIcon(AddFullyQualifiedClassNameToDiagramAction.class.getResource("/net/trustx/simpleuml/icons/addClassIcon.png")));
     this.classDiagramComponentPanel = classDiagramComponentPanel;
   }



   public void actionPerformed(AnActionEvent event) {
     this.fullyQualifiedClassName = Messages.showInputDialog("Enter fully qualified classname", "Question", Messages.getQuestionIcon(), this.fullyQualifiedClassName, null);
     if (this.fullyQualifiedClassName != null)
     {

       if (this.fullyQualifiedClassName.trim().length() == 0) {

         Messages.showMessageDialog("Name is not valid", "Error", Messages.getErrorIcon());
       }
       else {

         addFullyQualifiedClassName(this.fullyQualifiedClassName);
       }
     }
   }



   private void addFullyQualifiedClassName(String fullyQualifiedClassName) {
     try {
       PsiClass psiClass = JavaPsiFacade.getInstance(this.classDiagramComponentPanel.getProject()).findClass(fullyQualifiedClassName, GlobalSearchScope.allScope(this.classDiagramComponentPanel.getProject()));
       if (psiClass == null) {

         Messages.showMessageDialog("Class " + fullyQualifiedClassName + " not found", "Error", Messages.getErrorIcon());

         return;
       }
       if (!this.classDiagramComponentPanel.containsHighlight((PsiElement)psiClass))
       {
         this.classDiagramComponentPanel.addPsiElement((PsiElement)psiClass, true);
       }
     }
     catch (Exception ex) {

       Messages.showMessageDialog("Class " + fullyQualifiedClassName + " not found", "Error", Messages.getErrorIcon());
     }
   }
 }


