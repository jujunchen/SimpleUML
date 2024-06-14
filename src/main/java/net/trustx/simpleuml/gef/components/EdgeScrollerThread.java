 package net.trustx.simpleuml.gef.components;

 import java.awt.EventQueue;
 import java.awt.Point;
 import java.lang.reflect.InvocationTargetException;
 import javax.swing.JScrollPane;
 import javax.swing.JViewport;
 import net.trustx.simpleuml.gef.SelectionManager;




 public class EdgeScrollerThread
   extends Thread
 {
   private boolean active;
   private DiagramPane diagramPane;
   private JScrollPane scrollPane;
   private FigureComponent figureComponent;
   private int delay;
   private int amount;
   private int direction;
   public static final int DIRECTION_UP = 1;
   public static final int DIRECTION_DOWN = 2;
   public static final int DIRECTION_LEFT = 3;
   public static final int DIRECTION_RIGHT = 4;

   public EdgeScrollerThread() {
     this.active = false;
   }



   public EdgeScrollerThread(DiagramPane diagramPane, JScrollPane scrollPane, FigureComponent figureComponent, int delay, int amount, int direction) {
     this.diagramPane = diagramPane;
     this.scrollPane = scrollPane;
     this.figureComponent = figureComponent;
     this.delay = delay;
     this.amount = amount;
     this.direction = direction;
     this.active = true;
   }



   public void run() {
     final int oldWidth = this.diagramPane.getWidth();
     final int compWidth = this.figureComponent.getWidth();
     final int oldHeight = this.diagramPane.getHeight();
     final int compHeight = this.figureComponent.getWidth();

     while (this.active) {


       try {
         if (this.direction == 4) {

           EventQueue.invokeAndWait(new Runnable()
               {
                 public void run()
                 {
                   if (EdgeScrollerThread.this.figureComponent.getPosX() + compWidth < oldWidth)
                   {
                     EdgeScrollerThread.this.moveComponents(EdgeScrollerThread.this.amount, 0);
                     JViewport viewport = EdgeScrollerThread.this.scrollPane.getViewport();
                     Point p = viewport.getViewPosition();
                     viewport.setViewPosition(new Point(p.x + EdgeScrollerThread.this.amount, p.y));

                   }
                   else
                   {
                     EdgeScrollerThread.this.setActive(false);
                   }

                 }
               });
         } else if (this.direction == 3) {

           EventQueue.invokeAndWait(new Runnable()
               {
                 public void run()
                 {
                   if (EdgeScrollerThread.this.figureComponent.getPosX() > 0)
                   {
                     int scrollAmount = Math.min(EdgeScrollerThread.this.figureComponent.getPosX(), EdgeScrollerThread.this.amount);
                     EdgeScrollerThread.this.moveComponents(-scrollAmount, 0);
                     JViewport viewport = EdgeScrollerThread.this.scrollPane.getViewport();
                     Point p = viewport.getViewPosition();
                     viewport.setViewPosition(new Point(p.x - scrollAmount, p.y));
                   }
                   else
                   {
                     EdgeScrollerThread.this.setActive(false);
                   }

                 }
               });
         } else if (this.direction == 1) {

           EventQueue.invokeAndWait(new Runnable()
               {
                 public void run()
                 {
                   if (EdgeScrollerThread.this.figureComponent.getPosY() > 0)
                   {
                     int scrollAmount = Math.min(EdgeScrollerThread.this.figureComponent.getPosY(), EdgeScrollerThread.this.amount);
                     EdgeScrollerThread.this.moveComponents(0, -scrollAmount);
                     JViewport viewport = EdgeScrollerThread.this.scrollPane.getViewport();
                     Point p = viewport.getViewPosition();
                     viewport.setViewPosition(new Point(p.x, p.y - scrollAmount));
                   }
                   else
                   {
                     EdgeScrollerThread.this.setActive(false);
                   }

                 }
               });
         } else if (this.direction == 2) {

           EventQueue.invokeAndWait(new Runnable()
               {
                 public void run()
                 {
                   if (EdgeScrollerThread.this.figureComponent.getPosY() + compHeight < oldHeight)
                   {
                     EdgeScrollerThread.this.moveComponents(0, EdgeScrollerThread.this.amount);
                     JViewport viewport = EdgeScrollerThread.this.scrollPane.getViewport();
                     Point p = viewport.getViewPosition();
                     viewport.setViewPosition(new Point(p.x, p.y + EdgeScrollerThread.this.amount));
                   }
                   else
                   {
                     EdgeScrollerThread.this.setActive(false);
                   }

                 }
               });
         }
       } catch (InterruptedException e) {

         e.printStackTrace();
       }
       catch (InvocationTargetException e) {

         e.printStackTrace();
       }



       try {
         Thread.sleep(this.delay);
       }
       catch (InterruptedException e) {}
     }
   }





   private void moveComponents(int x, int y) {
     if (this.figureComponent.isSelected()) {

       this.diagramPane.getSelectionManager().executeCommandOnSelection(new MoveSelectableCommand(x, y, this.diagramPane));
     }
     else {

       SelectionManager.executeCommandOnSelectable(new MoveSelectableCommand(x, y, this.diagramPane), this.figureComponent);
     }
   }



   public boolean isActive() {
     return this.active;
   }



   public void setActive(boolean active) {
     if (!active)
     {
       this.scrollPane.getViewport().setScrollMode(1);
     }
     this.active = active;
   }
 }


