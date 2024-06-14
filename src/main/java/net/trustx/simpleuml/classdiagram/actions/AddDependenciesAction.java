 package net.trustx.simpleuml.classdiagram.actions;
 
 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.psi.PsiClass;
 import com.intellij.psi.PsiElementVisitor;
 import java.util.HashMap;
 import java.util.HashSet;
 import java.util.Iterator;
 import java.util.LinkedList;
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponent;
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponentPanel;
 import net.trustx.simpleuml.classdiagram.components.PsiClassComponent;
 import net.trustx.simpleuml.util.FindReferenceVisitor;
 

 
 
 
 public class AddDependenciesAction
   extends AddClassesAction
 {
   private final ClassDiagramComponent classDiagramComponent;
   private PsiClassComponent psiClassComponent;
   
   public AddDependenciesAction(PsiClassComponent psiClassComponent) {
     super(((ClassDiagramComponent)psiClassComponent.getDiagramPane()).getProject(), "Dependencies");
     this.classDiagramComponent = (ClassDiagramComponent)psiClassComponent.getDiagramPane();
     this.psiClassComponent = psiClassComponent;
   }
 
 
   
   public void executeCommand(HashMap commandProperties, boolean first, boolean last) {
     PsiClass psiClass = this.psiClassComponent.getPsiClass();
     FindReferenceVisitor frv = new FindReferenceVisitor();
     psiClass.accept((PsiElementVisitor)frv);
     
     HashSet hashSet = frv.getList();
     LinkedList<PsiClass> psiClassesToAdd = (LinkedList)commandProperties.get("AddClassesAction.Classes");
     if (psiClassesToAdd == null) {
       
       psiClassesToAdd = new LinkedList();
       commandProperties.put("AddClassesAction.Classes", psiClassesToAdd);
     } 
     
     for (Iterator<PsiClass> iterator = hashSet.iterator(); iterator.hasNext(); ) {
       
       PsiClass depClass = iterator.next();
       if (depClass.getQualifiedName() != null && !this.classDiagramComponent.containsClass(depClass.getQualifiedName()))
       {
         psiClassesToAdd.add(depClass);
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


