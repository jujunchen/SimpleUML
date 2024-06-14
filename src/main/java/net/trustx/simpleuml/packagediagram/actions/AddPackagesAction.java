 package net.trustx.simpleuml.packagediagram.actions;

 import com.intellij.openapi.project.Project;
 import com.intellij.openapi.ui.Messages;
 import com.intellij.psi.PsiElement;
 import com.intellij.psi.PsiPackage;
 import java.util.Collection;
 import java.util.Collections;
 import java.util.Comparator;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.LinkedList;
 import javax.swing.Icon;
 import net.trustx.simpleuml.components.ActionContributorCommand;
 import net.trustx.simpleuml.components.packagechooser.PsiPackageChooser;
 import net.trustx.simpleuml.packagediagram.components.PackageDiagramComponentPanel;





 public abstract class AddPackagesAction
   extends ActionContributorCommand
 {
   public static final String PACKAGES_PROPERTY_NAME = "AddPackagesAction.Packages";

   public AddPackagesAction() {}

   public AddPackagesAction(String text) {
     super(text);
   }



   public AddPackagesAction(String text, String description, Icon icon) {
     super(text, description, icon);
   }



   public abstract PackageDiagramComponentPanel getPackageDiagramComponentPanel();


   protected void addPackagesToDiagram(HashMap psiPackagesToAdd) {
     Project project = getPackageDiagramComponentPanel().getProject();

     if (psiPackagesToAdd.size() == 0) {

       Messages.showMessageDialog(project, "No additional packages found", "Info", Messages.getInformationIcon());

       return;
     }
     LinkedList<?> ll = new LinkedList(psiPackagesToAdd.values());

     Collections.sort(ll, new Comparator()
         {
           public int compare(Object o1, Object o2)
           {
             PsiPackage psiPackage1 = (PsiPackage)o1;
             PsiPackage psiPackage2 = (PsiPackage)o2;

             return psiPackage1.getQualifiedName().compareTo(psiPackage2.getQualifiedName());
           }
         });

     PsiPackageChooser psiPackageChooser = new PsiPackageChooser(project, ll, true);
     psiPackageChooser.setModal(true);
     psiPackageChooser.show();

     if (psiPackageChooser.getExitCode() == 0) {

       Collection col = psiPackageChooser.getSelectedPsiPackages();
       for (Iterator<PsiPackage> iterator = col.iterator(); iterator.hasNext(); ) {

         PsiPackage psiPackage = iterator.next();
         getPackageDiagramComponentPanel().addPsiElement((PsiElement)psiPackage, true);
       }
     }
   }
 }


