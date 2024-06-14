 package net.trustx.simpleuml.gef.components;

 import java.awt.Component;
 import java.awt.Container;
 import java.awt.Dimension;
 import java.awt.LayoutManager;





 public class DefaultDiagramPaneLayout
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

         FigureComponent figureComponent = (FigureComponent)parent.getComponent(i);

         if (figureComponent.getPosX() + figureComponent.getWidth() > maxX)
         {
           maxX = figureComponent.getPosX() + figureComponent.getWidth();
         }
         if (figureComponent.getPosY() + figureComponent.getHeight() > maxY)
         {
           maxY = figureComponent.getPosY() + figureComponent.getHeight();
         }
         if (!figureComponent.isAdjusting()) {

           if (figureComponent.getPosX() < 0)
           {
             figureComponent.setPosX(0);
           }
           if (figureComponent.getPosY() < 0)
           {
             figureComponent.setPosY(0);
           }
         }


         if (figureComponent.isAdjusting())
         {
           adjusting = true;
         }
         figureComponent.setBounds(figureComponent.getPosX(), figureComponent.getPosY(), figureComponent.getComponentWidth(), figureComponent.getComponentHeight());
       }



       if (!adjusting) {

         this.maxX = maxX + 500;
         this.maxY = maxY + 500;
       }
     }
   }
 }


