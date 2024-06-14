 package net.trustx.simpleuml.packagediagram.actions;

 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.psi.JavaPsiFacade;
 import com.intellij.psi.PsiClass;
 import com.intellij.psi.PsiDirectory;
 import com.intellij.psi.PsiElementVisitor;
 import com.intellij.psi.PsiJavaFile;
 import com.intellij.psi.PsiPackage;
 import java.util.HashMap;
 import java.util.HashSet;
 import java.util.Iterator;
 import net.trustx.simpleuml.packagediagram.components.PackageDiagramComponent;
 import net.trustx.simpleuml.packagediagram.components.PackageDiagramComponentPanel;
 import net.trustx.simpleuml.packagediagram.components.PsiPackageComponent;
 import net.trustx.simpleuml.util.FindReferenceVisitor;




 public class AddDependenciesAction
   extends AddPackagesAction
 {
   private final PackageDiagramComponent packageDiagramComponent;
   private PsiPackageComponent psiPackageComponent;

   public AddDependenciesAction(PsiPackageComponent psiPackageComponent) {
     super("Dependencies");
     this.packageDiagramComponent = (PackageDiagramComponent)psiPackageComponent.getDiagramPane();
     this.psiPackageComponent = psiPackageComponent;
   }



   public void executeCommand(HashMap commandProperties, boolean first, boolean last) {
     PsiDirectory[] psiDirectories = this.psiPackageComponent.getPsiPackage().getDirectories();
     HashMap<Object, Object> psiPackagesToAdd = (HashMap)commandProperties.get("AddPackagesAction.Packages");

     for (int i = 0; i < psiDirectories.length; i++) {

       PsiDirectory psiDirectory = psiDirectories[i];
       FindReferenceVisitor frv = new FindReferenceVisitor();
       psiDirectory.accept((PsiElementVisitor)frv);

       HashSet hashSet = frv.getList();

       if (psiPackagesToAdd == null) {

         psiPackagesToAdd = new HashMap<Object, Object>();
         commandProperties.put("AddPackagesAction.Packages", psiPackagesToAdd);
       }

       for (Iterator<PsiClass> iterator = hashSet.iterator(); iterator.hasNext(); ) {

         PsiClass depClass = iterator.next();
         PsiJavaFile psiJavaFile = (PsiJavaFile)depClass.getContainingFile();
         String packageName = psiJavaFile.getPackageName();
         PsiPackage depPackage = JavaPsiFacade.getInstance(this.packageDiagramComponent.getProject()).findPackage(packageName);
         if (depPackage != null && !this.packageDiagramComponent.containsPackage(depPackage.getQualifiedName()) && !psiPackagesToAdd.containsKey(depPackage.getQualifiedName()))
         {


           psiPackagesToAdd.put(depPackage.getQualifiedName(), depPackage);
         }
       }
     }



     if (last)
     {
       addPackagesToDiagram(psiPackagesToAdd);
     }
   }



   public void update(AnActionEvent e) {
     e.getPresentation().setEnabled((this.psiPackageComponent.getPsiPackage() != null));
   }



   public PackageDiagramComponentPanel getPackageDiagramComponentPanel() {
     return this.packageDiagramComponent.getPackageDiagramComponentPanel();
   }
 }


