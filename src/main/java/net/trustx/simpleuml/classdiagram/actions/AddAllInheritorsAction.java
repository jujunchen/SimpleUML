 package net.trustx.simpleuml.classdiagram.actions;
 
 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.psi.PsiClass;
 import com.intellij.psi.PsiManager;
 import com.intellij.psi.search.PsiSearchHelper;
 import com.intellij.psi.search.searches.ClassInheritorsSearch;
 import java.util.HashMap;
 import java.util.LinkedList;
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponent;
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponentPanel;
 import net.trustx.simpleuml.classdiagram.components.PsiClassComponent;

 
 
 public class AddAllInheritorsAction
   extends AddClassesAction
 {
   private final ClassDiagramComponent classDiagramComponent;
   private PsiClassComponent psiClassComponent;
   
   public AddAllInheritorsAction(PsiClassComponent psiClassComponent) {
     super(((ClassDiagramComponent)psiClassComponent.getDiagramPane()).getProject(), "All Inheritors");
     this.classDiagramComponent = (ClassDiagramComponent)psiClassComponent.getDiagramPane();
     this.psiClassComponent = psiClassComponent;
   }
 
 
   
   public void executeCommand(HashMap commandProperties, boolean first, boolean last) {
//     PsiSearchHelper psh = PsiManager.getInstance(this.classDiagramComponent.getProject()).getAcquire();
     PsiClass psiClass = this.psiClassComponent.getPsiClass();
     PsiClass[] psiClasses = ClassInheritorsSearch.search(psiClass, false).toArray((PsiClass[]) new PsiClass[0]);
 
     
     LinkedList<PsiClass> psiClassesToAdd = (LinkedList)commandProperties.get("AddClassesAction.Classes");
     if (psiClassesToAdd == null) {
       
       psiClassesToAdd = new LinkedList();
       commandProperties.put("AddClassesAction.Classes", psiClassesToAdd);
     } 
     
     for (int i = 0; i < psiClasses.length; i++) {
       
       PsiClass inheritor = psiClasses[i];
       
       if (inheritor.getQualifiedName() != null && !this.classDiagramComponent.containsClass(inheritor.getQualifiedName()))
       {
         psiClassesToAdd.add(inheritor);
       }
     } 
     
     if (last)
     {
       addClassesToDiagram(psiClassesToAdd);
     }
   }
 
 
 
   
   public void update(AnActionEvent e) {
     e.getPresentation().setEnabled((this.psiClassComponent.getPsiClass() != null));
   }
 
 
   
   public ClassDiagramComponentPanel getClassDiagramComponentPanel() {
     return this.classDiagramComponent.getClassDiagramComponentPanel();
   }


 }


