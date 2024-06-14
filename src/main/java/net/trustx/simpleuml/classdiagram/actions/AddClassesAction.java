 package net.trustx.simpleuml.classdiagram.actions;
 
 import com.intellij.openapi.application.ApplicationManager;
 import com.intellij.openapi.progress.ProgressIndicator;
 import com.intellij.openapi.progress.ProgressManager;
 import com.intellij.openapi.project.Project;
 import com.intellij.openapi.ui.Messages;
 import com.intellij.psi.PsiClass;
 import com.intellij.psi.PsiElement;
 import java.util.Collection;
 import java.util.Collections;
 import java.util.Comparator;
 import java.util.Iterator;
 import java.util.LinkedList;
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponentPanel;
 import net.trustx.simpleuml.components.ActionContributorCommand;
 import net.trustx.simpleuml.components.classchooser.PsiClassChooser;
 import net.trustx.simpleuml.util.UMLUtils;
 
 

 
 
 
 public abstract class AddClassesAction
   extends ActionContributorCommand
 {
   public static final String CLASSES_PROPERTY_NAME = "AddClassesAction.Classes";
   protected Project project;
   
   public AddClassesAction(Project project, String text) {
     super(text);
     this.project = project;
   }
 
 
   
   public abstract ClassDiagramComponentPanel getClassDiagramComponentPanel();
 
   
   protected void addClassesToDiagram(LinkedList<?> psiClassesToAdd) {
     Project project = getClassDiagramComponentPanel().getProject();
     
     if (psiClassesToAdd.size() == 0) {
       
       Messages.showMessageDialog(project, "No additional classes found", "Info", Messages.getInformationIcon());
       
       return;
     } 
     Collections.sort(psiClassesToAdd, new Comparator()
         {
           public int compare(Object o1, Object o2)
           {
             PsiClass psiClass1 = (PsiClass)o1;
             PsiClass psiClass2 = (PsiClass)o2;
             
             return UMLUtils.getNiceClassName(psiClass1).compareToIgnoreCase(UMLUtils.getNiceClassName(psiClass2));
           }
         });
     
     PsiClassChooser psiClassChooser = new PsiClassChooser(project, psiClassesToAdd, true);
     psiClassChooser.setModal(true);
     psiClassChooser.show();
     
     if (psiClassChooser.getExitCode() == 0)
     {
       addClasses(psiClassChooser);
     }
   }
 
 
   
   private void addClasses(final PsiClassChooser psiClassChooser) {
     ProgressManager.getInstance().runProcessWithProgressSynchronously(new Runnable()
         {
           public void run() {
             final ProgressIndicator progressWindow = ProgressManager.getInstance().getProgressIndicator();
             ApplicationManager.getApplication().runReadAction(new Runnable()
                 {
                   public void run()
                   {
                     Collection col = psiClassChooser.getSelectedPsiClasses();
                     int i = 0;
                     for (Iterator<PsiClass> iterator = col.iterator(); iterator.hasNext(); ) {
                       
                       i++;
                       PsiClass psiClass = iterator.next();
                       AddClassesAction.this.getClassDiagramComponentPanel().addPsiElement((PsiElement)psiClass, true);
                       
                       progressWindow.setFraction(i / col.size());
                       progressWindow.setText(psiClass.getQualifiedName());
                     } 
                     
                     progressWindow.stop();
                   }
                 });
           }
         },"Adding Classes...", false, this.project);
   }
 }


