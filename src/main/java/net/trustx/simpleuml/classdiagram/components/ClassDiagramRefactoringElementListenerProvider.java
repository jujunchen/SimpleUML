 package net.trustx.simpleuml.classdiagram.components;

 import com.intellij.psi.PsiClass;
 import com.intellij.psi.PsiElement;
 import com.intellij.psi.PsiPackage;
 import com.intellij.refactoring.listeners.RefactoringElementListener;
 import com.intellij.refactoring.listeners.RefactoringElementListenerProvider;


 public class ClassDiagramRefactoringElementListenerProvider
   implements RefactoringElementListenerProvider
 {
   private final ClassDiagramComponent classDiagramComponent;

   public ClassDiagramRefactoringElementListenerProvider(ClassDiagramComponent classDiagramComponent) {
     this.classDiagramComponent = classDiagramComponent;
   }



   public RefactoringElementListener getListener(PsiElement changedElement) {
     if (changedElement instanceof PsiClass)
     {
       return new RefactoringElementListenerClass(this.classDiagramComponent, (PsiClass)changedElement);
     }
     if (changedElement instanceof PsiPackage)
     {
       return new RefactoringElementListenerPackage(this.classDiagramComponent, (PsiPackage)changedElement);
     }


     return new RefactoringElementListener() {
         public void elementRenamed(PsiElement psiElement) {}

         public void elementMoved(PsiElement psiElement) {}
       };
   }
 }


