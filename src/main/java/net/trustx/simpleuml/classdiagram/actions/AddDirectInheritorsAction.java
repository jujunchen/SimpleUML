 package net.trustx.simpleuml.classdiagram.actions;
 
 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.psi.PsiClass;
 import com.intellij.psi.PsiJavaCodeReferenceElement;
 import com.intellij.psi.PsiManager;
 import com.intellij.psi.PsiReferenceList;
 import com.intellij.psi.search.PsiSearchHelper;
 import com.intellij.psi.search.searches.ClassInheritorsSearch;
 import java.util.HashMap;
 import java.util.LinkedList;
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponent;
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponentPanel;
 import net.trustx.simpleuml.classdiagram.components.PsiClassComponent;
 import net.trustx.simpleuml.util.UMLUtils;

 
 
 
 public class AddDirectInheritorsAction
   extends AddClassesAction
 {
   private ClassDiagramComponent classDiagramComponent;
   private PsiClassComponent psiClassComponent;
   
   public AddDirectInheritorsAction(PsiClassComponent psiClassComponent) {
     super(((ClassDiagramComponent)psiClassComponent.getDiagramPane()).getProject(), "Direct Inheritors");
     this.psiClassComponent = psiClassComponent;
     this.classDiagramComponent = (ClassDiagramComponent)psiClassComponent.getDiagramPane();
   }
 
 
   
   public void executeCommand(HashMap commandProperties, boolean first, boolean last) {
//     PsiSearchHelper psh = PsiManager.getInstance(this.classDiagramComponent.getProject()).getSearchHelper();
     PsiClass psiClass = this.psiClassComponent.getPsiClass();
     PsiClass[] psiClasses = ClassInheritorsSearch.search(psiClass, false).toArray((PsiClass[]) new PsiClass[0]);
 
     
     LinkedList<PsiClass> psiClassesToAdd = (LinkedList)commandProperties.get("AddClassesAction.Classes");
     if (psiClassesToAdd == null) {
       
       psiClassesToAdd = new LinkedList();
       commandProperties.put("AddClassesAction.Classes", psiClassesToAdd);
     } 
     
     for (int i = 0; i < psiClasses.length; i++) {
       
       PsiClass inheritor = psiClasses[i];
       
       if (inheritor.getQualifiedName() != null && !this.classDiagramComponent.containsClass(inheritor.getQualifiedName())) {
         
         boolean add = (containsClass(inheritor.getExtendsList(), psiClass) || containsClass(inheritor.getImplementsList(), psiClass));
         
         if (add)
         {
           psiClassesToAdd.add(inheritor);
         }
       } 
     } 
     
     if (last)
     {
       addClassesToDiagram(psiClassesToAdd);
     }
   }
   
   private boolean containsClass(PsiReferenceList psiReferenceElements, PsiClass psiClass) {
     PsiJavaCodeReferenceElement[] elements = UMLUtils.getReferenceElements(psiReferenceElements);
     for (int j = 0; j < elements.length; j++) {
       
       PsiJavaCodeReferenceElement psiReferenceElement = elements[j];
       PsiClass possibleInheritor = (PsiClass)psiReferenceElement.resolve();
       if (possibleInheritor != null) {
         
         String qualifiedName = possibleInheritor.getQualifiedName();
         if (qualifiedName != null)
         {
           if (qualifiedName.equals(psiClass.getQualifiedName()))
           {
             return true; }  } 
       } 
     } 
     return false;
   }
 
 
   
   public void update(AnActionEvent e) {
     e.getPresentation().setEnabled((this.psiClassComponent.getPsiClass() != null));
   }
 
 
   
   public ClassDiagramComponentPanel getClassDiagramComponentPanel() {
     return this.classDiagramComponent.getClassDiagramComponentPanel();
   }
 }


