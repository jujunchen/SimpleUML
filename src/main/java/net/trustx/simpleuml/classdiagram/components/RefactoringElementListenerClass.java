 package net.trustx.simpleuml.classdiagram.components;

 import com.intellij.psi.PsiClass;
 import com.intellij.psi.PsiElement;
 import com.intellij.refactoring.listeners.RefactoringElementListener;
 import java.util.Iterator;
 import java.util.LinkedList;
 import net.trustx.simpleuml.gef.components.FigureComponentDragHandler;



 public class RefactoringElementListenerClass
   implements RefactoringElementListener
 {
   private ClassDiagramComponent classDiagramComponent;
   private LinkedList psiClassNameList;
   private String oldPrefix;

   public RefactoringElementListenerClass(ClassDiagramComponent classDiagramComponent, PsiClass oldClass) {
     this.classDiagramComponent = classDiagramComponent;

     this.psiClassNameList = new LinkedList();


     this.oldPrefix = oldClass.getQualifiedName();
     this.psiClassNameList.add(oldClass.getQualifiedName());


     PsiClass[] psiClasses = oldClass.getInnerClasses();
     for (int i = 0; i < psiClasses.length; i++) {

       PsiClass psiClass = psiClasses[i];
       this.psiClassNameList.add(psiClass.getQualifiedName());
     }
   }



   public void elementRenamed(PsiElement newPsiClassElement) {
     if (this.oldPrefix != null && newPsiClassElement != null && newPsiClassElement instanceof PsiClass) {

       String newPrefix = ((PsiClass)newPsiClassElement).getQualifiedName();

       for (Iterator<String> iterator = this.psiClassNameList.iterator(); iterator.hasNext(); ) {

         String qualifiedOldName = iterator.next();
         String qualifiedNewName = getNewName(qualifiedOldName, this.oldPrefix, newPrefix);
         updateComponent(this.classDiagramComponent, qualifiedOldName, qualifiedNewName);
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



   public static String getNewName(String oldName, String oldPrefix, String newPrefix) {
     if (oldName.startsWith(oldPrefix))
     {
       return newPrefix + oldName.substring(oldPrefix.length());
     }
     return null;
   }



   public static void updateComponent(ClassDiagramComponent classDiagramComponent, String qualifiedOldName, String qualifiedNewName) {
     PsiClassComponent psiClassComponent = classDiagramComponent.getPsiClassComponent(qualifiedOldName);

     if (psiClassComponent != null && qualifiedNewName != null) {

       classDiagramComponent.removeFigureComponent(psiClassComponent);

       PsiClassComponent classComponent = new PsiClassComponent(classDiagramComponent, qualifiedNewName);

       FigureComponentDragHandler dragHandler = new FigureComponentDragHandler(classDiagramComponent, classDiagramComponent.getClassDiagramComponentPanel().getScrollPane());
       dragHandler.install(classComponent);

       classComponent.getToggleFieldButton().setSelected(psiClassComponent.getToggleFieldButton().isSelected());
       classComponent.getToggleConstructorButton().setSelected(psiClassComponent.getToggleConstructorButton().isSelected());
       classComponent.getToggleMethodButton().setSelected(psiClassComponent.getToggleMethodButton().isSelected());
       classComponent.setPinned(psiClassComponent.isPinned());
       classComponent.setColor(psiClassComponent.getColor());
       classComponent.rebuildComponent(false);

       classDiagramComponent.addFigureComponent(classComponent, true);

       classComponent.setPosX(psiClassComponent.getPosX());
       classComponent.setPosY(psiClassComponent.getPosY());
     }
   }
 }


