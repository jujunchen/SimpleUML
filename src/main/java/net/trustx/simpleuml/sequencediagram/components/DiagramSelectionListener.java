 package net.trustx.simpleuml.sequencediagram.components;
 
 import java.awt.Point;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.awt.event.MouseEvent;
 import java.awt.event.MouseListener;
 import java.awt.event.MouseMotionListener;
 import javax.swing.JScrollPane;
 import javax.swing.JViewport;
 import javax.swing.Timer;
 import net.trustx.simpleuml.sequencediagram.display.Display;
 import net.trustx.simpleuml.sequencediagram.display.DisplayLink;
 
 
 
 
 
 public abstract class DiagramSelectionListener
   implements MouseListener, MouseMotionListener
 {
   protected Display disp;
   private static final int HOVERTIME = 250;
   private Timer hoverTimer = new Timer(250, new HoverListener());
   
   private Point hoveringOver;
   
   private Point clickPoint;
   private boolean hasEntered;
   protected JViewport view;
   
   public DiagramSelectionListener(Display d, JViewport view) {
     this.disp = d;
     this.view = view;
   }
 
 
 
 
 
 
 
 
   
   public void mouseClicked(MouseEvent e) {
     this.clickPoint = e.getPoint();
   }
 
 
 
   
   public void mousePressed(MouseEvent e) {}
 
 
 
   
   public void mouseReleased(MouseEvent e) {}
 
 
 
   
   public void mouseDragged(MouseEvent e) {}
 
 
   
   public void mouseEntered(MouseEvent e) {
     if (!this.hasEntered) {
       this.hoverTimer.start();
     }
   }
 
   
   public void mouseMoved(MouseEvent e) {
     if (this.view != null) {
       
       Point p = e.getPoint();
       p.setLocation(e.getX() + this.view.getViewPosition().getX(), e.getY() + this.view.getViewPosition().getY());
       this.hoveringOver = p;
     } 
   }
 
 
 
   
   public void mouseExited(MouseEvent e) {
     this.hasEntered = false;
     this.hoverTimer.stop();
   }
 
 
 
   
   public Point getCurrentRelativePosition() {
     Point now = null;
     if (this.clickPoint != null) {
       now = new Point((int)(this.clickPoint.getX() + this.view.getViewPosition().getX()), (int)(this.clickPoint.getY() + this.view.getViewPosition().getY()));
     }
     return now;
   }
 
 
   
   protected Point getRelativePosition(MouseEvent e) {
     Point p = e.getPoint();
     JViewport view = ((JScrollPane)e.getSource()).getViewport();
     p.setLocation(e.getX() + view.getViewPosition().getX(), e.getY() + view.getViewPosition().getY());
     return p;
   }
 
 
   
   public Point getHoveringPoint() {
     return this.hoveringOver;
   }
   
   public DiagramSelectionListener() {}
   
   private class HoverListener
     implements ActionListener {
     public void actionPerformed(ActionEvent e) {
       if (!DiagramSelectionListener.this.hasEntered) {
         DiagramSelectionListener.this.hasEntered = true;
       } else {
         
         DisplayLink dl = null;
         if (DiagramSelectionListener.this.disp != null) {
           
           dl = DiagramSelectionListener.this.disp.getLinkAt(DiagramSelectionListener.this.hoveringOver);
           if (dl != null) {
             
             DiagramSelectionListener.this.disp.highLight(dl.getSeq());
           }
           else if ((dl = DiagramSelectionListener.this.disp.getCommentAt(DiagramSelectionListener.this.hoveringOver)) != null) {
             
             DiagramSelectionListener.this.disp.clearHighlight();
             DiagramSelectionListener.this.disp.ttComment(dl.getSeq(), DiagramSelectionListener.this.hoveringOver);
           }
           else {
             
             DiagramSelectionListener.this.disp.clearHighlight();
           } 
         } 
       } 
     }
     
     private HoverListener() {}
   }
 }


