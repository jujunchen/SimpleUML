 package net.trustx.simpleuml.gef.components;
 
 import java.awt.Dimension;
 import java.awt.Point;
 import java.awt.Rectangle;
 import java.awt.event.MouseEvent;
 import java.awt.event.MouseListener;
 import java.awt.event.MouseMotionListener;
 
 

 
 public class DiagramPaneMarqueeHandler
   implements MouseListener, MouseMotionListener
 {
   private MarqueeFigureComponent marqueeFigureComponent;
   private DiagramPane diagramPane;
   private Point marqueeStartPoint;
   private boolean active;
   
   public DiagramPaneMarqueeHandler(DiagramPane diagramPane) {
     this.marqueeFigureComponent = new MarqueeFigureComponent(diagramPane);
     this.diagramPane = diagramPane;
     diagramPane.addMouseListener(this);
     diagramPane.addMouseMotionListener(this);
   }
 
 
 
   
   public void mouseClicked(MouseEvent e) {}
 
 
   
   public void mousePressed(MouseEvent e) {
     if (e.isControlDown()) {
       
       this.diagramPane.addFigureComponent(this.marqueeFigureComponent, false);
       this.marqueeFigureComponent.setBounds(new Rectangle(e.getPoint(), new Dimension(0, 0)));
       this.marqueeFigureComponent.setPosX((e.getPoint()).x);
       this.marqueeFigureComponent.setPosY((e.getPoint()).y);
       this.marqueeStartPoint = e.getPoint();
       this.active = true;
     } 
   }
 
 
   
   public void mouseReleased(MouseEvent e) {
     if (e.isControlDown() && this.active) {
       
       this.diagramPane.executeCommandOnFigureComponents(new FigureComponentCommand()
           {
             public void preExecution() {}
 
 
 
 
             
             public boolean executeCommand(FigureComponent figureComponent) {
               if (DiagramPaneMarqueeHandler.this.marqueeFigureComponent.getBounds().intersects(figureComponent.getBounds()))
               {
                 figureComponent.setSelected(true);
               }
               return true;
             }
 
 
 
             
             public void postExecution() {}
           });
       this.diagramPane.removeFigureComponent(this.marqueeFigureComponent);
       this.diagramPane.repaint();
       this.active = false;
     }
     else {
       
       cleanRemove();
     } 
   }
 
 
   
   private void cleanRemove() {
     if (this.active) {
       
       this.diagramPane.removeFigureComponent(this.marqueeFigureComponent);
       this.diagramPane.repaint();
       this.active = false;
     } 
   }
 
 
 
   
   public void mouseEntered(MouseEvent e) {}
 
 
 
   
   public void mouseExited(MouseEvent e) {}
 
 
   
   public void mouseDragged(MouseEvent e) {
     if (e.isControlDown() && this.active) {
       int x, y, width, height;
 
 
 
 
       
       if (e.getX() < this.marqueeStartPoint.x) {
         
         x = e.getX();
         width = this.marqueeStartPoint.x - e.getX();
       }
       else {
         
         x = this.marqueeStartPoint.x;
         width = e.getX() - this.marqueeStartPoint.x;
       } 
       
       if (e.getY() < this.marqueeStartPoint.y) {
         
         y = e.getY();
         height = this.marqueeStartPoint.y - e.getY();
       }
       else {
         
         y = this.marqueeStartPoint.y;
         height = e.getY() - this.marqueeStartPoint.y;
       } 
       Rectangle marqueeRect = new Rectangle(x, y, width, height);
       this.marqueeFigureComponent.setBounds(marqueeRect);
       this.marqueeFigureComponent.setPosX(x);
       this.marqueeFigureComponent.setPosY(y);
     }
     else if (e.isControlDown() && !this.active) {
       
       this.diagramPane.addFigureComponent(this.marqueeFigureComponent, false);
       this.marqueeFigureComponent.setBounds(new Rectangle(e.getPoint(), new Dimension(10, 10)));
       this.marqueeFigureComponent.setPosX((e.getPoint()).x);
       this.marqueeFigureComponent.setPosY((e.getPoint()).y);
       this.marqueeStartPoint = e.getPoint();
       this.active = true;
     }
     else {
       
       cleanRemove();
     } 
   }
   
   public void mouseMoved(MouseEvent e) {}
 }


