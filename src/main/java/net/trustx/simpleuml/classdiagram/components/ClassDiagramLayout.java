 package net.trustx.simpleuml.classdiagram.components;

 import java.awt.Component;
 import java.awt.Container;
 import java.awt.Dimension;
 import java.awt.LayoutManager;



 public class ClassDiagramLayout
   implements LayoutManager
 {
   private int maxX;
   private int maxY;

   public void addLayoutComponent(String name, Component comp) {}

   public void removeLayoutComponent(Component comp) {}

   public Dimension preferredLayoutSize(Container parent) {
     return new Dimension(this.maxX, this.maxY);
   }



   public Dimension minimumLayoutSize(Container parent) {
     return new Dimension(this.maxX, this.maxY);
   }



   public void layoutContainer(Container parent) {
     synchronized (parent.getTreeLock()) {

       boolean adjusting = false;
       int maxX = 0;
       int maxY = 0;
       for (int i = 0; i < parent.getComponentCount(); i++) {

         PsiClassComponent classComponent = (PsiClassComponent)parent.getComponent(i);

         if (classComponent.getPosX() + classComponent.getWidth() > maxX)
         {
           maxX = classComponent.getPosX() + classComponent.getWidth();
         }
         if (classComponent.getPosY() + classComponent.getHeight() > maxY)
         {
           maxY = classComponent.getPosY() + classComponent.getHeight();
         }
         if (!classComponent.isAdjusting()) {

           if (classComponent.getPosX() < 0)
           {
             classComponent.setPosX(0);
           }
           if (classComponent.getPosY() < 0)
           {
             classComponent.setPosY(0);
           }
         }


         if (classComponent.isAdjusting())
         {
           adjusting = true;
         }
       }




       if (!adjusting) {

         this.maxX = maxX + 500;
         this.maxY = maxY + 500;
       }
     }
   }
 }


