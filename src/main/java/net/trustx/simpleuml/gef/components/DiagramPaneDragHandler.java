 package net.trustx.simpleuml.gef.components;
 
 import java.awt.Point;
 import java.awt.event.MouseEvent;
 import java.awt.event.MouseListener;
 import java.awt.event.MouseMotionListener;
 import java.util.Collection;
 import java.util.Iterator;
 import javax.swing.JScrollPane;
 import javax.swing.JViewport;
 import javax.swing.SwingUtilities;
 

 
 public class DiagramPaneDragHandler
   implements MouseListener, MouseMotionListener
 {
   private DiagramPane diagramPane;
   private JScrollPane scrollPane;
   private Point mousePos;
   private Point viewportPos;
   
   public void install(DiagramPane diagramPane, JScrollPane scrollPane) {
     this.diagramPane = diagramPane;
     this.scrollPane = scrollPane;
     if (diagramPane == null)
     {
       throw new IllegalArgumentException("diagramPane can not be null");
     }
     if (scrollPane == null)
     {
       throw new IllegalArgumentException("scrollPane can not be null");
     }
     
     diagramPane.addMouseListener(this);
     diagramPane.addMouseMotionListener(this);
   }
 
 
 
   
   public void mouseClicked(MouseEvent e) {}
 
 
   
   public void mousePressed(MouseEvent e) {
     if (!e.isControlDown()) {
       
       this.diagramPane.setAutoscrolls(false);
       this.mousePos = e.getPoint();
       SwingUtilities.convertPointToScreen(this.mousePos, this.diagramPane);
       this.viewportPos = this.scrollPane.getViewport().getViewPosition();
     } 
   }
 
 
 
   
   public void mouseReleased(MouseEvent e) {
     Collection figureComponents = this.diagramPane.getFigureComponents();
     for (Iterator<FigureComponent> iterator = figureComponents.iterator(); iterator.hasNext(); ) {
       
       FigureComponent figureComponent = iterator.next();
       figureComponent.setAdjusting(false);
     } 
   }
 
 
 
   
   public void mouseEntered(MouseEvent e) {}
 
 
 
   
   public void mouseExited(MouseEvent e) {}
 
 
   
   public void mouseDragged(MouseEvent e) {
     if (!e.isControlDown() && this.mousePos != null && this.viewportPos != null) {
       
       Point p = e.getPoint();
       SwingUtilities.convertPointToScreen(p, this.diagramPane);
       JViewport viewport = this.scrollPane.getViewport();
       
       Point newViewPoint = new Point(this.viewportPos.x + this.mousePos.x - p.x, this.viewportPos.y + this.mousePos.y - p.y);
       if (newViewPoint.x < 0)
       {
         newViewPoint.x = 0;
       }
       if (newViewPoint.y < 0)
       {
         newViewPoint.y = 0;
       }
       if ((viewport.getViewRect()).width + newViewPoint.x > this.diagramPane.getWidth())
       {
         newViewPoint.x = this.diagramPane.getWidth() - (viewport.getViewRect()).width;
       }
       if ((viewport.getViewRect()).height + newViewPoint.y > this.diagramPane.getHeight())
       {
         newViewPoint.y = this.diagramPane.getHeight() - (viewport.getViewRect()).height;
       }
       
       viewport.setViewPosition(newViewPoint);
     } 
   }
   
   public void mouseMoved(MouseEvent e) {}
 }


