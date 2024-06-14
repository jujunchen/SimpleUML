 package net.trustx.simpleuml.classdiagram.util;
 
 import java.awt.EventQueue;
 import java.awt.Point;
 import java.lang.reflect.InvocationTargetException;
 import javax.swing.JViewport;
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponent;
 import net.trustx.simpleuml.classdiagram.components.PsiClassComponent;
 

 public class EdgeScrollerThread
   extends Thread
 {
   private volatile boolean active;
   private ClassDiagramComponent classDiagramComponent;
   private PsiClassComponent psiClassComponent;
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
 
 
   
   public EdgeScrollerThread(ClassDiagramComponent classDiagramComponent, PsiClassComponent psiClassComponent, int delay, int amount, int direction) {
     this.classDiagramComponent = classDiagramComponent;
     this.psiClassComponent = psiClassComponent;
     this.delay = delay;
     this.amount = amount;
     this.direction = direction;
     this.active = true;
   }
 
 
   
   public void run() {
     final int oldWidth = this.classDiagramComponent.getWidth();
     final int compWidth = this.psiClassComponent.getWidth();
     final int oldHeight = this.classDiagramComponent.getHeight();
     final int compHeight = this.psiClassComponent.getWidth();
     
     while (this.active) {
 
       
       try {
         if (this.direction == 4) {
           
           EventQueue.invokeAndWait(new Runnable()
               {
                 public void run()
                 {
                   if (EdgeScrollerThread.this.psiClassComponent.getPosX() + compWidth < oldWidth)
                   {
                     EdgeScrollerThread.this.moveComponents(EdgeScrollerThread.this.amount, 0);
                     JViewport viewport = EdgeScrollerThread.this.classDiagramComponent.getClassDiagramComponentPanel().getJViewport();
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
                   if (EdgeScrollerThread.this.psiClassComponent.getPosX() > 0)
                   {
                     int scrollAmount = Math.min(EdgeScrollerThread.this.psiClassComponent.getPosX(), EdgeScrollerThread.this.amount);
                     EdgeScrollerThread.this.moveComponents(-scrollAmount, 0);
                     JViewport viewport = EdgeScrollerThread.this.classDiagramComponent.getClassDiagramComponentPanel().getJViewport();
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
                   if (EdgeScrollerThread.this.psiClassComponent.getPosY() > 0)
                   {
                     int scrollAmount = Math.min(EdgeScrollerThread.this.psiClassComponent.getPosY(), EdgeScrollerThread.this.amount);
                     EdgeScrollerThread.this.moveComponents(0, -scrollAmount);
                     JViewport viewport = EdgeScrollerThread.this.classDiagramComponent.getClassDiagramComponentPanel().getJViewport();
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
                   if (EdgeScrollerThread.this.psiClassComponent.getPosY() + compHeight < oldHeight)
                   {
                     EdgeScrollerThread.this.moveComponents(0, EdgeScrollerThread.this.amount);
                     JViewport viewport = EdgeScrollerThread.this.classDiagramComponent.getClassDiagramComponentPanel().getJViewport();
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
     if (this.psiClassComponent.isSelected()) {
       
       this.classDiagramComponent.moveSelectedComponents(x, y);
     }
     else {
       
       this.classDiagramComponent.moveComponent(this.psiClassComponent, x, y);
     } 
   }
 
 
   
   public boolean isActive() {
     return this.active;
   }
 
 
   
   public void setActive(boolean active) {
     if (!active)
     {
       this.classDiagramComponent.getClassDiagramComponentPanel().getJViewport().setScrollMode(1);
     }
     this.active = active;
   }
 }


