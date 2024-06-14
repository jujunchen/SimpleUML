 package net.trustx.simpleuml.gef.components;
 
 import java.awt.Point;
 import java.awt.Rectangle;
 import java.awt.event.MouseEvent;
 import java.awt.event.MouseListener;
 import java.awt.event.MouseMotionListener;
 import javax.swing.JScrollPane;
 import javax.swing.SwingUtilities;
 import net.trustx.simpleuml.gef.Selectable;
 import net.trustx.simpleuml.gef.SelectableCommand;
 import net.trustx.simpleuml.gef.SelectionManager;
 

 
 public class FigureComponentDragHandler
   implements MouseListener, MouseMotionListener
 {
   private static int ACTIVE_BORDER_SIZE = 20;
   private static int SCROLL_DELAY = 50;
   private int SCROLL_AMOUNT = 50;
   
   private DiagramPane diagramPane;
   
   private JScrollPane scrollPane;
   
   private int startDragX;
   private int startDragY;
   private EdgeScrollerThread edgeScrollerThread;
   
   public FigureComponentDragHandler(DiagramPane diagramPane, JScrollPane scrollPane) {
     this.diagramPane = diagramPane;
     this.scrollPane = scrollPane;
     this.edgeScrollerThread = new EdgeScrollerThread();
   }
 
 
   
   public void install(FigureComponent figureComponent) {
     figureComponent.addMouseListener(this);
     figureComponent.addMouseMotionListener(this);
   }
 
 
 
   
   public void mouseClicked(MouseEvent e) {}
 
 
   
   public void mousePressed(MouseEvent e) {
     FigureComponent figureComponent = (FigureComponent)e.getComponent();
     
     figureComponent.requestFocusInWindow();
     Point sp = e.getPoint();
     SwingUtilities.convertPointToScreen(sp, figureComponent);
     this.startDragX = sp.x;
     this.startDragY = sp.y;
     
     this.diagramPane.moveToFront(figureComponent);
     
     SelectionManager.executeCommandOnSelectable(new MoveSelectableCommand.PrepareMoveFigureCoomponentCommand(), figureComponent);
     this.diagramPane.getSelectionManager().executeCommandOnSelection(new MoveSelectableCommand.PrepareMoveFigureCoomponentCommand());
     
     this.diagramPane.executeCommandOnFigureComponents(new FigureComponentAdjustingCommand(true));
     
     figureComponent.setAdjusting(true);
   }
 
 
   
   public void mouseReleased(MouseEvent e) {
     FigureComponent figureComponent = (FigureComponent)e.getComponent();
     final Rectangle acb = this.diagramPane.getAllowedContentBounds();
     if (figureComponent.getPosX() < acb.x)
     {
       figureComponent.setPosX(acb.x);
     }
     if (figureComponent.getPosY() < acb.y)
     {
       figureComponent.setPosY(acb.y);
     }
     if (figureComponent.getPosX() + figureComponent.getWidth() > acb.width)
     {
       figureComponent.setPosX(acb.width - figureComponent.getWidth());
     }
     if (figureComponent.getPosY() + figureComponent.getHeight() > acb.height)
     {
       figureComponent.setPosY(acb.height - figureComponent.getHeight());
     }
     
     figureComponent.setAdjusting(false);
     
     if (this.edgeScrollerThread.isActive())
     {
       this.edgeScrollerThread.setActive(false);
     }
     
     this.diagramPane.executeCommandOnFigureComponents(new FigureComponentAdjustingCommand(false));
     SelectionManager.executeCommandOnSelectable(new MoveSelectableCommand.FinishMoveFigureCoomponentCommand(), figureComponent);
     this.diagramPane.getSelectionManager().executeCommandOnSelection(new MoveSelectableCommand.FinishMoveFigureCoomponentCommand());
     
     this.diagramPane.getSelectionManager().executeCommandOnSelection(new SelectableCommand()
         {
           public void preExecution() {}
 
 
 
 
           
           public boolean executeCommand(Selectable selectable) {
             if (selectable instanceof FigureComponent) {
 
               
               FigureComponent component = (FigureComponent)selectable;
               if (component.getPosX() < acb.x)
               {
                 component.setPosX(acb.x);
               }
               if (component.getPosY() < acb.y)
               {
                 component.setPosY(acb.y);
               }
               if (component.getPosX() + component.getWidth() > acb.width)
               {
                 component.setPosX(acb.width - component.getWidth());
               }
               if (component.getPosY() + component.getHeight() > acb.height)
               {
                 component.setPosY(acb.height - component.getHeight());
               }
               FigureComponentDragHandler.this.diagramPane.getConnectorManager().validateConnectors(component);
             } 
             return true;
           }
 
 
 
 
           
           public void postExecution() {}
         });
     this.diagramPane.revalidate();
     this.diagramPane.layoutContainer();
     this.diagramPane.getConnectorManager().validateConnectors();
     this.diagramPane.repaint();
   }
 
 
 
   
   public void mouseEntered(MouseEvent e) {}
 
 
 
   
   public void mouseExited(MouseEvent e) {}
 
 
   
   public void mouseDragged(MouseEvent e) {
     FigureComponent figureComponent = (FigureComponent)e.getComponent();
     Point sp = e.getPoint();
     SwingUtilities.convertPointToScreen(sp, figureComponent);
     
     if (figureComponent.isSelected()) {
       
       this.diagramPane.getSelectionManager().executeCommandOnSelection(new MoveSelectableCommand(sp.x - this.startDragX, sp.y - this.startDragY, this.diagramPane));
     }
     else {
       
       this.diagramPane.getSelectionManager().clear();
       SelectionManager.executeCommandOnSelectable(new MoveSelectableCommand(sp.x - this.startDragX, sp.y - this.startDragY, this.diagramPane), figureComponent);
     } 
     
     this.startDragX = sp.x;
     this.startDragY = sp.y;
     figureComponent.setAdjusting(true);
     
     if (this.scrollPane != null)
     {
       checkForEdgeScrolling(figureComponent, sp, (this.scrollPane.getViewport().getLocationOnScreen()).x + this.scrollPane.getViewport().getWidth(), (this.scrollPane.getViewport().getLocationOnScreen()).x, (this.scrollPane.getViewport().getLocationOnScreen()).y, (this.scrollPane.getViewport().getLocationOnScreen()).y + this.scrollPane.getViewport().getHeight());
     }
     
     this.diagramPane.layoutContainer();
     this.diagramPane.revalidate();
   }
 
 
 
   
   public void mouseMoved(MouseEvent e) {}
 
 
 
   
   private void checkForEdgeScrolling(FigureComponent figureComponent, Point mouseCoordOnScreen, int rightEdgeX, int leftEdgeX, int upperEdgeY, int bottomEdgeY) {
     if (mouseCoordOnScreen.x > rightEdgeX - ACTIVE_BORDER_SIZE) {
       
       if (!this.edgeScrollerThread.isActive())
       {
         this.edgeScrollerThread = new EdgeScrollerThread(this.diagramPane, this.scrollPane, figureComponent, SCROLL_DELAY, this.SCROLL_AMOUNT, 4);
         this.scrollPane.getViewport().setScrollMode(0);
         this.edgeScrollerThread.start();
       }
     
     } else if (mouseCoordOnScreen.x < leftEdgeX + ACTIVE_BORDER_SIZE) {
       
       if (!this.edgeScrollerThread.isActive() && (this.scrollPane.getViewport().getViewPosition()).x > 0)
       {
         this.edgeScrollerThread = new EdgeScrollerThread(this.diagramPane, this.scrollPane, figureComponent, SCROLL_DELAY, this.SCROLL_AMOUNT, 3);
         this.scrollPane.getViewport().setScrollMode(0);
         this.edgeScrollerThread.start();
       }
     
     } else if (mouseCoordOnScreen.y < upperEdgeY + ACTIVE_BORDER_SIZE) {
       
       if (!this.edgeScrollerThread.isActive() && (this.scrollPane.getViewport().getViewPosition()).y > 0)
       {
         this.edgeScrollerThread = new EdgeScrollerThread(this.diagramPane, this.scrollPane, figureComponent, SCROLL_DELAY, this.SCROLL_AMOUNT, 1);
         this.scrollPane.getViewport().setScrollMode(0);
         this.edgeScrollerThread.start();
       }
     
     } else if (mouseCoordOnScreen.y > bottomEdgeY - ACTIVE_BORDER_SIZE) {
       
       if (!this.edgeScrollerThread.isActive())
       {
         this.edgeScrollerThread = new EdgeScrollerThread(this.diagramPane, this.scrollPane, figureComponent, SCROLL_DELAY, this.SCROLL_AMOUNT, 2);
         this.scrollPane.getViewport().setScrollMode(0);
         this.edgeScrollerThread.start();
       
       }
     
     }
     else if (this.edgeScrollerThread.isActive()) {
       
       this.edgeScrollerThread.setActive(false);
     } 
   }
 }


