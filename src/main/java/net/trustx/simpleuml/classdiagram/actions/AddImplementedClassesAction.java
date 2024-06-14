 package net.trustx.simpleuml.classdiagram.actions;

 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.actionSystem.Presentation;
 import com.intellij.psi.PsiClass;
 import com.intellij.psi.PsiJavaCodeReferenceElement;
 import java.util.HashMap;
 import java.util.LinkedList;
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponent;
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponentPanel;
 import net.trustx.simpleuml.classdiagram.components.PsiClassComponent;
 import net.trustx.simpleuml.util.UMLUtils;






 public class AddImplementedClassesAction
   extends AddClassesAction
 {
   private final ClassDiagramComponent classDiagramComponent;
   private PsiClassComponent psiClassComponent;

   public AddImplementedClassesAction(PsiClassComponent psiClassComponent) {
     super(((ClassDiagramComponent)psiClassComponent.getDiagramPane()).getProject(), "Implemented Interfaces");
     this.classDiagramComponent = (ClassDiagramComponent)psiClassComponent.getDiagramPane();
     this.psiClassComponent = psiClassComponent;
   }



   public void executeCommand(HashMap commandProperties, boolean first, boolean last) {
     PsiClass psiClass = this.psiClassComponent.getPsiClass();
     PsiJavaCodeReferenceElement[] psiReferenceElements = UMLUtils.getReferenceElements(psiClass.getImplementsList());
     LinkedList<PsiClass> psiClassesToAdd = (LinkedList)commandProperties.get("AddClassesAction.Classes");
     if (psiClassesToAdd == null) {

       psiClassesToAdd = new LinkedList();
       commandProperties.put("AddClassesAction.Classes", psiClassesToAdd);
     }

     for (int i = 0; i < psiReferenceElements.length; i++) {

       PsiJavaCodeReferenceElement psiReferenceElement = psiReferenceElements[i];

       PsiClass extendedClass = (PsiClass)psiReferenceElement.resolve();
       if (extendedClass != null) {

         String qualifiedName = extendedClass.getQualifiedName();
         if (qualifiedName != null && 
           !this.classDiagramComponent.containsClass(qualifiedName))
         {
           psiClassesToAdd.add(extendedClass); } 
       }
     }
     if (last)
     {
       addClassesToDiagram(psiClassesToAdd);
     }
   }



   public void update(AnActionEvent e) {
     PsiClass psiClass = this.psiClassComponent.getPsiClass();
     Presentation presentation = e.getPresentation();
     if (psiClass == null) {

       presentation.setEnabled(false);
       return;
     }
     PsiJavaCodeReferenceElement[] psiReferenceElements = UMLUtils.getReferenceElements(psiClass.getImplementsList());
     presentation.setEnabled((psiReferenceElements.length > 0));
   }



   public ClassDiagramComponentPanel getClassDiagramComponentPanel() {
     return this.classDiagramComponent.getClassDiagramComponentPanel();
   }
 }


