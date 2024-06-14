 package net.trustx.simpleuml.classdiagram.components;
 
 import com.intellij.psi.PsiClass;
 import com.intellij.psi.PsiElement;
 import com.intellij.psi.PsiPackage;
 import com.intellij.refactoring.listeners.RefactoringElementListener;
 import java.util.Iterator;
 import java.util.LinkedList;
 
 

 public class RefactoringElementListenerPackage
   implements RefactoringElementListener
 {
   private ClassDiagramComponent classDiagramComponent;
   private String oldPrefix;
   private LinkedList psiClassNameList;
   
   public RefactoringElementListenerPackage(ClassDiagramComponent classDiagramComponent, PsiPackage psiPackage) {
     this.classDiagramComponent = classDiagramComponent;
     
     this.psiClassNameList = new LinkedList();
     
     if (psiPackage != null) {
       
       this.oldPrefix = psiPackage.getQualifiedName();
       addPackage(psiPackage);
     } 
   }
 
 
   
   private void addPackage(PsiPackage psiPackage) {
     addClassesFromPackage(psiPackage);
     
     PsiPackage[] subPackages = psiPackage.getSubPackages();
     for (int i = 0; i < subPackages.length; i++) {
       
       PsiPackage subPackage = subPackages[i];
       addPackage(subPackage);
     } 
   }
 
 
   
   private void addClassesFromPackage(PsiPackage psiPackage) {
     PsiClass[] psiClasses = psiPackage.getClasses();
     for (int i = 0; i < psiClasses.length; i++) {
       
       PsiClass psiClass = psiClasses[i];
       this.psiClassNameList.add(psiClass.getQualifiedName());
       
       PsiClass[] innerClasses = psiClass.getInnerClasses();
       for (int j = 0; j < innerClasses.length; j++) {
         
         PsiClass innerClass = innerClasses[j];
         this.psiClassNameList.add(innerClass.getQualifiedName());
       } 
     } 
   }
 
 
   
   public void elementRenamed(PsiElement newPsiPackageElement) {
     if (this.oldPrefix != null && newPsiPackageElement != null && newPsiPackageElement instanceof PsiPackage) {
       
       String newPrefix = ((PsiPackage)newPsiPackageElement).getQualifiedName();
       
       for (Iterator<String> iterator = this.psiClassNameList.iterator(); iterator.hasNext(); ) {
         
         String qualifiedOldName = iterator.next();
         String qualifiedNewName = RefactoringElementListenerClass.getNewName(qualifiedOldName, this.oldPrefix, newPrefix);
         RefactoringElementListenerClass.updateComponent(this.classDiagramComponent, qualifiedOldName, qualifiedNewName);
       } 
     } 
     
     this.classDiagramComponent.reloadAllPsiComponents();
     this.classDiagramComponent.getConnectorUpdater().updateAllConnectors();
     this.classDiagramComponent.revalidate();
     this.classDiagramComponent.layoutContainer();
     this.classDiagramComponent.getConnectorManager().validateConnectors();
     this.classDiagramComponent.repaint();
   }
 
 
   
   public void elementMoved(PsiElement newPsiClassElement) {
     elementRenamed(newPsiClassElement);
   }
 }


