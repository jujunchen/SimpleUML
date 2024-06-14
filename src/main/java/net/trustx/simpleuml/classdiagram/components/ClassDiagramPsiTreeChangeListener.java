 package net.trustx.simpleuml.classdiagram.components;

 import com.intellij.psi.PsiClass;
 import com.intellij.psi.PsiFile;
 import com.intellij.psi.PsiJavaFile;
 import com.intellij.psi.PsiTreeChangeAdapter;
 import com.intellij.psi.PsiTreeChangeEvent;




 public class ClassDiagramPsiTreeChangeListener
   extends PsiTreeChangeAdapter
 {
   private ClassDiagramComponentPanel classDiagramComponentPanel;

   public ClassDiagramPsiTreeChangeListener(ClassDiagramComponentPanel classDiagramComponentPanel) {
     this.classDiagramComponentPanel = classDiagramComponentPanel;
   }



   public void childrenChanged(PsiTreeChangeEvent event) {
     reloadClasses(event);
   }



   public void childAdded(PsiTreeChangeEvent event) {
     reloadClasses(event);
   }



   public void childMoved(PsiTreeChangeEvent event) {
     reloadClasses(event);
   }



   public void childRemoved(PsiTreeChangeEvent event) {
     reloadClasses(event);
   }



   public void childReplaced(PsiTreeChangeEvent event) {
     reloadClasses(event);
   }



   public void propertyChanged(PsiTreeChangeEvent event) {
     reloadClasses(event);
   }






   private void reloadClasses(PsiTreeChangeEvent event) {
     try {
       PsiFile[] psiFiles = new PsiFile[0];
       reloadClasses(psiFiles);

       PsiFile psiFile = event.getFile();
       reloadClasses(psiFile);
     }
     catch (Throwable t) {

       t.printStackTrace();
     }
   }



   private void reloadClasses(PsiFile[] psiFiles) {
     for (int i = 0; i < psiFiles.length; i++) {

       PsiFile psiFile = psiFiles[i];
       if (psiFile instanceof PsiJavaFile) {

         PsiJavaFile psiJavaFile = (PsiJavaFile)psiFile;
         PsiClass[] psiClasses = psiJavaFile.getClasses();
         this.classDiagramComponentPanel.getClassDiagramComponent().reloadClasses(psiClasses);
       }
     }
   }



   public void reloadClasses(PsiFile psiFile) {
     if (psiFile != null)
     {
       reloadClasses(new PsiFile[] { psiFile });
     }
   }
 }


