 package net.trustx.simpleuml.util;

 import com.intellij.openapi.fileEditor.FileEditorManager;
 import com.intellij.openapi.fileEditor.OpenFileDescriptor;
 import com.intellij.openapi.project.Project;
 import com.intellij.openapi.vfs.VirtualFile;
 import com.intellij.psi.PsiIdentifier;
 import java.awt.Color;
 import java.awt.Component;
 import java.awt.Cursor;
 import java.awt.event.MouseEvent;
 import java.awt.event.MouseListener;
 import java.awt.event.MouseMotionListener;
 import javax.swing.JComponent;
 import javax.swing.SwingUtilities;
 import net.trustx.simpleuml.components.UMLLabel;



































 public class EditorHelper
 {
   public static void initQuickSourceSupport(final Color quickSourceColor, final Project project, final JComponent parentComponent, final UMLLabel umlLabel, PsiIdentifier psiIdentifier, final boolean underscored, final boolean hasOtherListeners, final boolean dispatchMouseClickToParent) {
     if (psiIdentifier == null || psiIdentifier.getNavigationElement().getContainingFile() == null) {
       return;
     }


     VirtualFile virtualFile = psiIdentifier.getNavigationElement().getContainingFile().getVirtualFile();
     if (virtualFile == null) {
       return;
     }


     int textOffset = psiIdentifier.getNavigationElement().getTextRange().getStartOffset();
     final OpenFileDescriptor openFileDescriptor = new OpenFileDescriptor(project, virtualFile, textOffset);




     umlLabel.addMouseMotionListener(new MouseMotionListener()
         {
           public void mouseMoved(MouseEvent e)
           {
             if (e.isControlDown()) {

               umlLabel.setCursor(Cursor.getPredefinedCursor(12));
               umlLabel.setForeground(quickSourceColor);
               umlLabel.setUnderscored(true);
             }
             else if (!hasOtherListeners) {


               umlLabel.setCursor(Cursor.getPredefinedCursor(0));
               umlLabel.setForeground(Color.BLACK);
               umlLabel.setUnderscored(underscored);
             }
             parentComponent.dispatchEvent(SwingUtilities.convertMouseEvent((Component)umlLabel, e, parentComponent));
           }



           public void mouseDragged(MouseEvent e) {
             parentComponent.dispatchEvent(SwingUtilities.convertMouseEvent((Component)umlLabel, e, parentComponent));
           }
         });

     umlLabel.addMouseListener(new MouseListener()
         {
           public void mouseExited(MouseEvent e)
           {
             umlLabel.setCursor(Cursor.getPredefinedCursor(0));
             umlLabel.setForeground(Color.BLACK);
             umlLabel.setUnderscored(underscored);

             parentComponent.dispatchEvent(SwingUtilities.convertMouseEvent((Component)umlLabel, e, parentComponent));
           }



           public void mouseClicked(MouseEvent e) {
             if (e.isControlDown()) {

               FileEditorManager.getInstance(project).openTextEditor(openFileDescriptor, true);
             }
             else if (dispatchMouseClickToParent) {

               parentComponent.dispatchEvent(SwingUtilities.convertMouseEvent((Component)umlLabel, e, parentComponent));
             }
           }



           public void mouseEntered(MouseEvent e) {
             parentComponent.dispatchEvent(SwingUtilities.convertMouseEvent((Component)umlLabel, e, parentComponent));
           }



           public void mousePressed(MouseEvent e) {
             parentComponent.dispatchEvent(SwingUtilities.convertMouseEvent((Component)umlLabel, e, parentComponent));
           }



           public void mouseReleased(MouseEvent e) {
             parentComponent.dispatchEvent(SwingUtilities.convertMouseEvent((Component)umlLabel, e, parentComponent));
           }
         });
   }
 }


