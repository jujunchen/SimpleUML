 package net.trustx.simpleuml.dependencydiagram.components;
 
 import com.intellij.openapi.progress.ProgressIndicator;
 import com.intellij.openapi.project.Project;
 import com.intellij.psi.JavaPsiFacade;
 import com.intellij.psi.PsiClass;
 import com.intellij.psi.PsiClassType;
 import com.intellij.psi.PsiElement;
 import com.intellij.psi.PsiElementVisitor;
 import com.intellij.psi.PsiField;
 import com.intellij.psi.PsiJavaCodeReferenceElement;
 import com.intellij.psi.PsiManager;
 import com.intellij.psi.PsiMethod;
 import com.intellij.psi.PsiReference;
 import com.intellij.psi.PsiReferenceList;
 import com.intellij.psi.PsiResolveHelper;
 import com.intellij.psi.PsiType;
 import com.intellij.psi.search.GlobalSearchScope;
 import com.intellij.psi.search.PsiSearchHelper;
 import com.intellij.psi.search.SearchScope;
 import com.intellij.psi.search.searches.ClassInheritorsSearch;
 import com.intellij.psi.search.searches.ReferencesSearch;
 import java.util.Collection;
 import java.util.HashSet;
 import java.util.Iterator;
 import net.trustx.simpleuml.util.FindReferenceVisitor;
 import net.trustx.simpleuml.util.UMLUtils;
 

 public class DependencyAnalyzer
 {
   private PsiClass psiClass;
   private ProgressIndicator progressIndicator;
   private Project project;
   private HashSet extendedClasses;
   private HashSet implementedClasses;
   private HashSet extendingClasses;
   private HashSet implementingClasses;
   private HashSet usedBothClasses;
   private HashSet usedClasses;
   private HashSet userClasses;
   private HashSet dependBothClasses;
   private HashSet dependingClasses;
   private HashSet dependClasses;
   private GlobalSearchScope globalSearchScope;
   
   public DependencyAnalyzer(PsiClass psiClass, DependencyDiagramComponent dependencyDiagramComponent, ProgressIndicator progressIndicator) {
     this.psiClass = psiClass;
     this.progressIndicator = progressIndicator;
     this.project = dependencyDiagramComponent.getProject();
     
     this.globalSearchScope = GlobalSearchScope.allScope(this.project);
     if (!dependencyDiagramComponent.getComponentSettings().isShowLibraryDependencies())
     {
       this.globalSearchScope = GlobalSearchScope.projectScope(this.project);
     }
     
     addExtendedImplemented();
     
     addDirectExtendorsImplementors();
     
     addDependentClasses();
     
     addClassesWeDepend();
     
     addUsed();
     
     addUsers(this.dependingClasses);
 
     
     this.usedClasses.removeAll(this.extendedClasses);
     this.usedClasses.removeAll(this.implementedClasses);
     this.usedClasses.removeAll(this.extendingClasses);
     this.usedClasses.removeAll(this.implementingClasses);
     
     this.userClasses.removeAll(this.extendedClasses);
     this.userClasses.removeAll(this.implementedClasses);
     this.userClasses.removeAll(this.extendingClasses);
     this.userClasses.removeAll(this.implementingClasses);
 
     
     this.usedBothClasses = new HashSet();
     this.usedBothClasses.addAll(this.userClasses);
     this.usedBothClasses.retainAll(this.usedClasses);
     
     this.userClasses.removeAll(this.usedBothClasses);
     this.usedClasses.removeAll(this.usedBothClasses);
 
     
     this.dependingClasses.remove(psiClass.getQualifiedName());
     this.dependingClasses.removeAll(this.extendedClasses);
     this.dependingClasses.removeAll(this.implementedClasses);
     this.dependingClasses.removeAll(this.extendingClasses);
     this.dependingClasses.removeAll(this.implementingClasses);
     this.dependingClasses.removeAll(this.usedBothClasses);
     this.dependingClasses.removeAll(this.userClasses);
     this.dependingClasses.removeAll(this.usedClasses);
 
     
     this.dependClasses.remove(psiClass.getQualifiedName());
     this.dependClasses.removeAll(this.extendedClasses);
     this.dependClasses.removeAll(this.implementedClasses);
     this.dependClasses.removeAll(this.extendingClasses);
     this.dependClasses.removeAll(this.implementingClasses);
     this.dependClasses.removeAll(this.usedBothClasses);
     this.dependClasses.removeAll(this.userClasses);
     this.dependClasses.removeAll(this.usedClasses);
 
     
     this.dependBothClasses = new HashSet();
     this.dependBothClasses.addAll(this.dependingClasses);
     this.dependBothClasses.retainAll(this.dependClasses);
     
     this.dependingClasses.removeAll(this.dependBothClasses);
     this.dependClasses.removeAll(this.dependBothClasses);
   }
 
 
   
   public PsiClass getPsiClass() {
     return this.psiClass;
   }
 
 
   
   public HashSet getExtendedClasses() {
     return this.extendedClasses;
   }
 
 
   
   public HashSet getImplementedClasses() {
     return this.implementedClasses;
   }
 
 
   
   public HashSet getExtendingClasses() {
     return this.extendingClasses;
   }
 
 
   
   public HashSet getImplementingClasses() {
     return this.implementingClasses;
   }
 
 
   
   public HashSet getUsedClasses() {
     return this.usedClasses;
   }
 
 
   
   public HashSet getUserClasses() {
     return this.userClasses;
   }
 
 
   
   public HashSet getDependingClasses() {
     return this.dependingClasses;
   }
 
 
   
   public HashSet getDependClasses() {
     return this.dependClasses;
   }
 
 
   
   public HashSet getUsedBothClasses() {
     return this.usedBothClasses;
   }
 
 
   
   public HashSet getDependBothClasses() {
     return this.dependBothClasses;
   }
 
 
   
   private void addUsers(HashSet dependingClasses) {
     this.progressIndicator.setText("Searching for user classes...");
 
     
     this.userClasses = new HashSet();
     
     String qualifiedNameOfClass = this.psiClass.getQualifiedName();
     double incr = 0.1D / dependingClasses.size();
     int n = 0;
     for (Iterator<String> iterator = dependingClasses.iterator(); iterator.hasNext(); ) {
       
       String qualifiedName = iterator.next();
       this.progressIndicator.setText2("Processing fields of " + qualifiedName);
       PsiClass psiClass = JavaPsiFacade.getInstance(this.project).findClass(qualifiedName, this.globalSearchScope);
       if (psiClass == null)
         continue; 
       PsiField[] psiFields = psiClass.getFields();
       for (int i = 0; i < psiFields.length; i++) {
         
         PsiField psiField = psiFields[i];
         PsiType psiType = psiField.getType().getDeepComponentType();
         if (psiType instanceof PsiClassType) {
           
           PsiClassType psiClassType = (PsiClassType)psiType;
           PsiClass clazz = psiClassType.resolve();
           if (clazz != null) {
             
             String qName = clazz.getQualifiedName();
             if (qName != null && 
               qName.equals(qualifiedNameOfClass))
             {
               addClassIfValid(this.userClasses, qualifiedName); } 
           } 
         } 
       }  n++;
       this.progressIndicator.setFraction(0.5D + incr * n);
     } 
     
     this.progressIndicator.setFraction(0.6D);
     this.progressIndicator.setText2("");
   }
 
 
   
   private void addClassIfValid(HashSet<String> set, String qualifiedClassName) {
     if (JavaPsiFacade.getInstance(this.project).findClass(qualifiedClassName, this.globalSearchScope) != null)
     {
       set.add(qualifiedClassName);
     }
   }
 
 
   
   private void addUsed() {
     this.progressIndicator.setText("Searching for classes used in fields...");
 
     
     this.usedClasses = new HashSet();
     PsiField[] psiFields = this.psiClass.getFields();
     for (int i = 0; i < psiFields.length; i++) {
       
       PsiField psiField = psiFields[i];
       PsiType psiType = psiField.getType();
       PsiType clazzType = psiType.getDeepComponentType();
       if (clazzType instanceof PsiClassType) {
         
         PsiClassType psiClassType = (PsiClassType)clazzType;
         PsiClass psiClass = psiClassType.resolve();
         if (psiClass != null)
         {
           addClassIfValid(this.usedClasses, psiClass.getQualifiedName());
         }
       } 
     } 
     
     this.progressIndicator.setFraction(0.5D);
   }
 
 
   
   private void addClassesWeDepend() {
     this.progressIndicator.setText("Searching for depend classes...");
     
     FindReferenceVisitor frv = new FindReferenceVisitor();
     this.psiClass.accept((PsiElementVisitor)frv);
     this.dependClasses = new HashSet();
     
     HashSet hashSet = frv.getList();
     for (Iterator<PsiClass> iterator = hashSet.iterator(); iterator.hasNext(); ) {
       
       PsiClass depClass = iterator.next();
       if (depClass.getQualifiedName() != null)
       {
         addClassIfValid(this.dependClasses, depClass.getQualifiedName());
       }
     } 
     
     this.progressIndicator.setFraction(0.4D);
   }
 
 
   
   private void addDependentClasses() {
     this.progressIndicator.setText("Searching for dependent classes...");
     
     this.dependingClasses = new HashSet();
     this.dependingClasses.addAll(findReferences((PsiElement)this.psiClass));
     
     PsiField[] fields = this.psiClass.getFields();
     
     double incr = 0.05D / fields.length;
     for (int i = 0; i < fields.length; i++) {
       
       PsiField field = fields[i];
       this.progressIndicator.setText2("Processing references to " + field.getName());
       this.dependingClasses.addAll(findReferences((PsiElement)field));
       this.progressIndicator.setFraction(0.2D + incr * (i + 1));
     } 
     
     this.progressIndicator.setFraction(0.25D);
     
     PsiMethod[] methods = this.psiClass.getMethods();
     incr = 0.05D / methods.length;
     for (int j = 0; j < methods.length; j++) {
       
       PsiMethod method = methods[j];
       this.progressIndicator.setText2("Processing references to " + method.getName());
       this.dependingClasses.addAll(findReferences((PsiElement)method));
       this.progressIndicator.setFraction(0.25D + incr * (j + 1));
     } 
     
     this.progressIndicator.setFraction(0.3D);
     this.progressIndicator.setText2("");
   }
 
 
   
   private HashSet findReferences(PsiElement psiElement) {
     HashSet classesSet = new HashSet();
//     PsiSearchHelper psh = PsiManager.getInstance(this.project).getSearchHelper();
     Collection<PsiReference> references = ReferencesSearch.search(psiElement, (SearchScope)GlobalSearchScope.allScope(this.project), false).findAll();
     for (Iterator<PsiReference> iterator = references.iterator(); iterator.hasNext(); ) {
       
       PsiReference reference = iterator.next();
       PsiElement element = reference.getElement();
       
       if (element != null) {
         
         PsiClass clazz = UMLUtils.findClass(element);
         if (clazz != null)
         {
           addClassIfValid(classesSet, clazz.getQualifiedName());
         }
       } 
     } 
     return classesSet;
   }
 
 
   
   private void addDirectExtendorsImplementors() {
     this.progressIndicator.setText("Searching for directly extending classes...");
     PsiResolveHelper psh = JavaPsiFacade.getInstance(this.project).getResolveHelper();
     
     PsiClass[] psiClasses = ClassInheritorsSearch.search(this.psiClass, false).toArray(new PsiClass[0]);
 
     
     this.extendingClasses = new HashSet();
     this.implementingClasses = new HashSet();
     
     for (int i = 0; i < psiClasses.length; i++) {
       
       PsiClass inheritor = psiClasses[i];
       
       if (inheritor.getQualifiedName() != null) {
         
         PsiJavaCodeReferenceElement[] psiReferenceElements = UMLUtils.getReferenceElements(inheritor.getExtendsList()); int j;
         for (j = 0; j < psiReferenceElements.length; j++) {
           
           PsiJavaCodeReferenceElement psiReferenceElement = psiReferenceElements[j];
           PsiClass possibleInheritor = (PsiClass)psiReferenceElement.resolve();
           if (possibleInheritor != null) {
             
             String qualifiedName = possibleInheritor.getQualifiedName();
             if (qualifiedName != null && 
               qualifiedName.equals(this.psiClass.getQualifiedName()))
             {
               addClassIfValid(this.extendingClasses, inheritor.getQualifiedName()); } 
           } 
         } 
         psiReferenceElements = UMLUtils.getReferenceElements(inheritor.getImplementsList());
         for (j = 0; j < psiReferenceElements.length; j++) {
           
           PsiJavaCodeReferenceElement psiReferenceElement = psiReferenceElements[j];
           PsiClass possibleInheritor = (PsiClass)psiReferenceElement.resolve();
           if (possibleInheritor != null) {
             
             String qualifiedName = possibleInheritor.getQualifiedName();
             if (qualifiedName != null && 
               qualifiedName.equals(this.psiClass.getQualifiedName()))
             {
               addClassIfValid(this.implementingClasses, inheritor.getQualifiedName()); } 
           } 
         } 
       } 
     } 
     this.progressIndicator.setFraction(0.2D);
   }
 
 
   
   private void addExtendedImplemented() {
     this.progressIndicator.setText("Searching for extended classes...");
     this.extendedClasses = new HashSet();
     
     PsiReferenceList psiExtendsList = this.psiClass.getExtendsList();
     if (psiExtendsList != null) {
       
       PsiClassType[] psiClassTypes = psiExtendsList.getReferencedTypes();
       for (int i = 0; i < psiClassTypes.length; i++) {
         
         PsiClassType psiClassType = psiClassTypes[i];
         PsiClass resolvedClass = psiClassType.resolve();
         if (resolvedClass != null)
         {
           addClassIfValid(this.extendedClasses, resolvedClass.getQualifiedName()); } 
       } 
     } 
     this.progressIndicator.setFraction(0.05D);
     
     this.progressIndicator.setText("Searching for implemented classes...");
     this.implementedClasses = new HashSet();
     PsiReferenceList psiImplementsList = this.psiClass.getImplementsList();
     if (psiImplementsList != null) {
       
       PsiClassType[] psiClassTypes = psiImplementsList.getReferencedTypes();
       for (int i = 0; i < psiClassTypes.length; i++) {
         
         PsiClassType psiClassType = psiClassTypes[i];
         PsiClass resolvedClass = psiClassType.resolve();
         if (resolvedClass != null)
         {
           addClassIfValid(this.implementedClasses, resolvedClass.getQualifiedName()); } 
       } 
     } 
     this.progressIndicator.setFraction(0.1D);
   }
 }


