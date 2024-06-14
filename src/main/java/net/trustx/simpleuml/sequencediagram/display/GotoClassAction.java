 package net.trustx.simpleuml.sequencediagram.display;

 import com.intellij.openapi.fileEditor.FileEditorManager;
 import com.intellij.openapi.fileEditor.OpenFileDescriptor;
 import com.intellij.openapi.project.Project;
 import com.intellij.psi.PsiElement;
 import java.awt.Point;
 import java.awt.event.ActionEvent;
 import java.awt.event.MouseEvent;
 import javax.swing.JScrollPane;
 import javax.swing.JViewport;
 import net.trustx.simpleuml.sequencediagram.components.DiagramSelectionListener;
 import net.trustx.simpleuml.sequencediagram.model.Model;












 public class GotoClassAction
   extends DiagramSelectionListener
   implements AggregateActionListener
 {
   Model model;
   Project project;
   private static final String GOTO_ACTION_NAME = "Goto Class";
   private DiagramSelectionListener aggregateListener;

   public GotoClassAction(Display d, JViewport view, Model m, Project p) {
     super(d, view);
     this.model = m;
     this.project = p;
   }



   public GotoClassAction(Model m, Project p) {
     this.model = m;
     this.project = p;
   }



   public void setPointer(DiagramSelectionListener l) {
     this.aggregateListener = l;
   }



   public void mouseClicked(MouseEvent e) {
     if (e.getModifiers() == 16) {

       Point p = e.getPoint();
       JViewport view = ((JScrollPane)e.getSource()).getViewport();
       p.setLocation(e.getX() + view.getViewPosition().getX(), e.getY() + view.getViewPosition().getY());
       DisplayLink dl = this.disp.getLinkAt(p);
       if (dl != null)
       {
         fire(dl.getSeq());
       }
     }
   }



   public void mouseExited(MouseEvent e) {
     super.mouseExited(e);
     this.disp.clearHighlight();
   }



   public void actionPerformed(ActionEvent e) {
     if (this.aggregateListener != null) {

       DisplayLink dl = this.disp.getLinkAt(this.aggregateListener.getCurrentRelativePosition());
       if (dl != null)
       {
         fire(dl.getSeq());
       }
     }
   }



   public String getName() {
     return "Goto Class";
   }



   public void fire(int sequence) {
     PsiElement psId = this.model.getPsiElement(sequence);
     if (psId != null) {

       OpenFileDescriptor openFileDescriptor = new OpenFileDescriptor(this.project, psId.getContainingFile().getVirtualFile(), psId.getTextOffset());
       FileEditorManager.getInstance(this.project).openTextEditor(openFileDescriptor, true);
     }
   }
 }


