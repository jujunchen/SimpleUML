 package net.trustx.simpleuml.classdiagram.actions;

 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.actionSystem.Presentation;
 import com.intellij.psi.PsiClass;
 import java.util.HashMap;
 import java.util.LinkedList;
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponent;
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponentPanel;
 import net.trustx.simpleuml.classdiagram.components.PsiClassComponent;





 public class AddInnerClassesAction
   extends AddClassesAction
 {
   private final ClassDiagramComponent classDiagramComponent;
   private PsiClassComponent psiClassComponent;

   public AddInnerClassesAction(PsiClassComponent psiClassComponent) {
     super(((ClassDiagramComponent)psiClassComponent.getDiagramPane()).getProject(), "Inner Classes");
     this.classDiagramComponent = (ClassDiagramComponent)psiClassComponent.getDiagramPane();
     this.psiClassComponent = psiClassComponent;
   }



   public void executeCommand(HashMap commandProperties, boolean first, boolean last) {
     PsiClass psiClass = this.psiClassComponent.getPsiClass();
     PsiClass[] psiClasses = psiClass.getInnerClasses();
     LinkedList<PsiClass> psiClassesToAdd = (LinkedList)commandProperties.get("AddClassesAction.Classes");
     if (psiClassesToAdd == null) {

       psiClassesToAdd = new LinkedList();
       commandProperties.put("AddClassesAction.Classes", psiClassesToAdd);
     }

     for (int i = 0; i < psiClasses.length; i++) {

       PsiClass inner = psiClasses[i];

       if (inner.getQualifiedName() != null && !this.classDiagramComponent.containsClass(inner.getQualifiedName()))
       {
         psiClassesToAdd.add(inner);
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
     PsiClass[] psiClasses = psiClass.getInnerClasses();
     presentation.setEnabled((psiClasses.length > 0));
   }



   public ClassDiagramComponentPanel getClassDiagramComponentPanel() {
     return this.classDiagramComponent.getClassDiagramComponentPanel();
   }
 }


