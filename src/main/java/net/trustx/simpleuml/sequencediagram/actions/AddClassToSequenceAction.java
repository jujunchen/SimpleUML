 package net.trustx.simpleuml.sequencediagram.actions;

 import com.intellij.openapi.actionSystem.AnAction;
 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.actionSystem.DataContext;
 import com.intellij.openapi.actionSystem.DataKeys;
 import com.intellij.openapi.editor.Editor;
 import com.intellij.openapi.project.Project;
 import com.intellij.openapi.vfs.VirtualFile;
 import com.intellij.psi.PsiClass;
 import com.intellij.psi.PsiElement;
 import com.intellij.psi.PsiFile;
 import com.intellij.psi.PsiMethod;
 import net.trustx.simpleuml.components.DiagramComponent;
 import net.trustx.simpleuml.plugin.UMLToolWindowPlugin;
 import net.trustx.simpleuml.sequencediagram.components.SequenceDiagramComponent;
 import net.trustx.simpleuml.sequencediagram.model.Model;
 import net.trustx.simpleuml.sequencediagram.util.PsiClassUtil;


















 public class AddClassToSequenceAction
   extends AnAction
 {
   private Model model;
   private PsiFile psiFile;
   private String selectedFile;
   private SequenceDiagramComponent sequenceDiagramComponent;

   public AddClassToSequenceAction() {
     super("AddClassToSequenceAction");
   }



   public AddClassToSequenceAction(String name, SequenceDiagramComponent sequenceDiagramComponent) {
     super(name);
     this.sequenceDiagramComponent = sequenceDiagramComponent;
   }










   public void actionPerformed(AnActionEvent event) {
     DataContext dataContext = event.getDataContext();
     Project project = (Project)DataKeys.PROJECT.getData(dataContext);

     UMLToolWindowPlugin umlToolWindowPlugin = UMLToolWindowPlugin.getUMLToolWindowPlugin(project);


     if (this.sequenceDiagramComponent == null) {

       DiagramComponent diagramComponent = umlToolWindowPlugin.getSelectedDiagramComponent();
       if (diagramComponent != null && diagramComponent instanceof SequenceDiagramComponent) {

         SequenceDiagramComponent component = (SequenceDiagramComponent)diagramComponent;
         this.sequenceDiagramComponent = component;
       } else {
         return;
       }
     }



     this.model = this.sequenceDiagramComponent.getModel();

     if (validEvent(event)) {

       this.psiFile = (PsiFile)DataKeys.PSI_FILE.getData(dataContext);
       VirtualFile virtualFile = (VirtualFile)DataKeys.VIRTUAL_FILE.getData(dataContext);
       Editor editor = (Editor)DataKeys.EDITOR.getData(dataContext);
       if (virtualFile == null || editor == null)
         return;
       this.selectedFile = virtualFile.getName();
       int selectedOffset = editor.getCaretModel().getOffset();
       PsiMethod selectedFromMethod = getSelectedFromMethod(this.psiFile, selectedOffset);

       PsiElement selectedElement = (PsiElement)DataKeys.PSI_ELEMENT.getData(dataContext);
       if ((selectedElement != null && selectedElement instanceof PsiClass) || selectedElement instanceof PsiMethod) {



         addSelectedClasses(selectedElement, selectedFromMethod);
         umlToolWindowPlugin.showDiagramComponent((DiagramComponent)this.sequenceDiagramComponent);
         this.sequenceDiagramComponent.changesMade();
       }

       if (this.model != null)
       {
         umlToolWindowPlugin.saveDiagramComponents();
       }
     }
   }




   private PsiMethod getSelectedFromMethod(PsiFile psiFile, int selectedOffset) {
     PsiElement[] children1 = psiFile.getChildren();
     int i = 0;
     PsiMethod ret = null;
     while (i < children1.length) {

       if (children1[i] instanceof PsiClass && children1[i].getStartOffsetInParent() < selectedOffset && children1[i].getTextRange().getEndOffset() > selectedOffset) {



         PsiElement[] children2 = children1[i].getChildren();
         for (int j = 0; j < children2.length; j++) {

           if (children2[j] instanceof PsiMethod && children2[j].getStartOffsetInParent() < selectedOffset && children2[j].getTextRange().getEndOffset() > selectedOffset) {



             ret = (PsiMethod)children2[j];

             break;
           }
         }
         break;
       }
       i++;
     }
     return ret;
   }











   private boolean validEvent(AnActionEvent event) {
     return (DataKeys.VIRTUAL_FILE.getData(event.getDataContext()) != null && DataKeys.EDITOR.getData(event.getDataContext()) != null);
   }




   private void addSelectedClasses(PsiElement selected, PsiMethod selectedFrom) {
     String className = null;
     PsiMethod psiMethod = null;
     PsiClass psiClass = null;
     if (selected instanceof PsiMethod) {

       psiMethod = (PsiMethod)selected;
       psiClass = (PsiClass)selected.getParent();
       className = PsiClassUtil.getClassName(psiClass);
     }

     if (className != null && this.model != null)
     {
       this.model.addCall(psiClass, psiMethod, selectedFrom, this.selectedFile);
     }
   }
 }


