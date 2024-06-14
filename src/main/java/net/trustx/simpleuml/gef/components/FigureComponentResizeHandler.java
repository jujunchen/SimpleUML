 package net.trustx.simpleuml.gef.components;
 
 import java.awt.Dimension;
 import java.awt.Point;
 import java.awt.event.MouseEvent;
 import java.awt.event.MouseListener;
 import java.awt.event.MouseMotionListener;
 import javax.swing.SwingUtilities;
 
 public class FigureComponentResizeHandler
   implements MouseListener, MouseMotionListener
 {
   private int startDragX;
   private int startDragY;
   private DiagramPane diagramPane;
   
   public FigureComponentResizeHandler(DiagramPane diagramPane) {
     this.diagramPane = diagramPane;
   }
 
 
   
   public void install(FigureComponent figureComponent) {
     figureComponent.setResizingSupported(true);
     figureComponent.getResizeKnob().addMouseListener(this);
     figureComponent.getResizeKnob().addMouseMotionListener(this);
   }
 
 
 
   
   public void mouseClicked(MouseEvent e) {}
 
 
   
   public void mousePressed(MouseEvent e) {
     ResizeComponentKnob knob = (ResizeComponentKnob)e.getComponent();
     FigureComponent figureComponent = (FigureComponent)knob.getOwner();
     
     figureComponent.requestFocusInWindow();
     Point sp = e.getPoint();
     SwingUtilities.convertPointToScreen(sp, knob);
     this.startDragX = sp.x;
     this.startDragY = sp.y;
     
     this.diagramPane.moveToFront(figureComponent);
     
     figureComponent.setAdjusting(true);
   }
 
 
 
   
   public void mouseReleased(MouseEvent e) {}
 
 
 
   
   public void mouseEntered(MouseEvent e) {}
 
 
 
   
   public void mouseExited(MouseEvent e) {}
 
 
   
   public void mouseDragged(MouseEvent e) {
     ResizeComponentKnob knob = (ResizeComponentKnob)e.getComponent();
     FigureComponent figureComponent = (FigureComponent)knob.getOwner();
     
     Point sp = e.getPoint();
     SwingUtilities.convertPointToScreen(sp, knob);
 
 
     
     Dimension dim = figureComponent.getPreferredSize();
     Dimension newSize = new Dimension(dim.width + sp.x - this.startDragX, dim.height + sp.y - this.startDragY);
     if (newSize.width < (figureComponent.getMinimumSize()).width) {
       
       this.startDragX = (knob.getLocationOnScreen()).x + knob.getWidth() / 2;
       newSize.width = (figureComponent.getMinimumSize()).width;
     }
     else {
       
       this.startDragX = sp.x;
     } 
     
     if (newSize.height < (figureComponent.getMinimumSize()).height) {
       
       this.startDragY = (knob.getLocationOnScreen()).y + knob.getHeight() / 2;
       newSize.height = (figureComponent.getMinimumSize()).height;
     }
     else {
       
       this.startDragY = sp.y;
     } 
 
 
     
     figureComponent.setPreferredSize(newSize);
     
     figureComponent.setAdjusting(true);
 
     
     this.diagramPane.layoutContainer();
     this.diagramPane.revalidate();
   }
   
   public void mouseMoved(MouseEvent e) {}
 }


