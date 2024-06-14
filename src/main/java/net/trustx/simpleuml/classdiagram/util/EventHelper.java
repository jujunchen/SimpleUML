 package net.trustx.simpleuml.classdiagram.util;

 import com.intellij.psi.JavaPsiFacade;
 import com.intellij.psi.PsiClass;
 import com.intellij.psi.PsiElement;
 import com.intellij.psi.search.GlobalSearchScope;
 import java.awt.Color;
 import java.awt.Cursor;
 import java.awt.event.MouseEvent;
 import java.awt.event.MouseListener;
 import java.awt.event.MouseMotionListener;
 import javax.swing.JComponent;
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponent;
 import net.trustx.simpleuml.components.UMLLabel;



 public class EventHelper
 {
   public static void initQuickDiagramSupport(final ClassDiagramComponent classDiagramComponent, JComponent parentComponent, final UMLLabel umlLabel, final boolean underscored, final String qualifiedName) {
     umlLabel.addMouseListener(new MouseListener()
         {
           public void mouseClicked(MouseEvent e)
           {
             if (e.getButton() == 1 && !e.isControlDown()) {

               PsiClass psiClass = JavaPsiFacade.getInstance(classDiagramComponent.getProject()).findClass(qualifiedName, GlobalSearchScope.allScope(classDiagramComponent.getProject()));
               if (psiClass == null) {
                 return;
               }

               if (!classDiagramComponent.getClassDiagramComponentPanel().containsHighlight((PsiElement)psiClass))
               {
                 classDiagramComponent.getClassDiagramComponentPanel().addPsiElement((PsiElement)psiClass, true);
               }
             }
           }




           public void mousePressed(MouseEvent e) {}




           public void mouseReleased(MouseEvent e) {}



           public void mouseEntered(MouseEvent e) {
             if (!e.isControlDown()) {

               umlLabel.setForeground(classDiagramComponent.getDiagramSettings().getQuickDiagramLinkColor());
               umlLabel.setUnderscored(true);
               umlLabel.setCursor(Cursor.getPredefinedCursor(12));
             }
           }



           public void mouseExited(MouseEvent e) {
             umlLabel.setForeground(Color.BLACK);
             umlLabel.setUnderscored(underscored);
             umlLabel.setCursor(Cursor.getDefaultCursor());
           }
         });

     umlLabel.addMouseMotionListener(new MouseMotionListener()
         {
           public void mouseDragged(MouseEvent e) {}





           public void mouseMoved(MouseEvent e) {
             if (!e.isControlDown()) {

               umlLabel.setForeground(classDiagramComponent.getDiagramSettings().getQuickDiagramLinkColor());
               umlLabel.setUnderscored(true);
               umlLabel.setCursor(Cursor.getPredefinedCursor(12));
             }
           }
         });
   }
 }


