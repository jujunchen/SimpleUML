 package net.trustx.simpleuml.components;

 import com.intellij.openapi.actionSystem.ActionGroup;
 import com.intellij.openapi.actionSystem.AnAction;
 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.vfs.VirtualFile;
 import com.intellij.openapi.vfs.VirtualFileManager;
 import com.intellij.psi.PsiElement;
 import java.awt.LayoutManager;
 import javax.swing.JComponent;
 import javax.swing.JPanel;
 import net.trustx.simpleuml.util.UMLUtils;





 public abstract class DiagramComponent
   extends JPanel
 {
   public DiagramComponent(LayoutManager layout, boolean isDoubleBuffered) {
     super(layout, isDoubleBuffered);
   }



   public DiagramComponent(LayoutManager layout) {
     super(layout);
   }



   public DiagramComponent(boolean isDoubleBuffered) {
     super(isDoubleBuffered);
   }








   public DiagramComponent() {}








   public abstract ActionGroup getTabActionGroup();







   protected abstract void add(PsiElement paramPsiElement, boolean paramBoolean);







   public abstract void addPsiElements(AnActionEvent paramAnActionEvent);







   public final boolean addPsiElement(PsiElement psiElement, boolean update) {
     if (canHandle(psiElement) && !contains(psiElement)) {

       add(psiElement, update);
       return true;
     }

     return false;
   }









   public abstract boolean canHandle(PsiElement paramPsiElement);









   protected abstract boolean contains(PsiElement paramPsiElement);








   public final boolean containsPsiElement(PsiElement psiElement) {
     return (canHandle(psiElement) && contains(psiElement));
   }









   protected abstract void highlight(PsiElement paramPsiElement);









   public final boolean containsHighlight(PsiElement psiElement) {
     if (canHandle(psiElement) && contains(psiElement)) {

       highlight(psiElement);
       return true;
     }

     return false;
   }








   public abstract String getDiagramName();








   public abstract void setDiagramName(String paramString);








   public abstract String getFolderURL();







   public abstract void setFolderURL(String paramString);







   public abstract AnAction getAddToDiagramComponentAction();







   public abstract AnAction getGoToDiagramComponentAction();







   public abstract DiagramSaver getDiagramSaver();







   public void disposeUIResources() {}







   public abstract String getDiagramType();







   public boolean canSave() {
     String wholePath = UMLUtils.getWholePath(getFolderURL(), getDiagramName());
     VirtualFile vf = VirtualFileManager.getInstance().findFileByUrl(wholePath);

     if (vf == null)
     {
       return true;
     }
     return vf.isWritable();
   }

   public abstract JComponent getPreferredFocusedComponent();

   public abstract Previewable getPreviewable();
 }


