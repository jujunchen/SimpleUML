 package net.trustx.simpleuml.gef.components;

 import java.awt.Color;
 import java.awt.Cursor;
 import java.awt.Dimension;
 import java.awt.Graphics;
 import javax.swing.JComponent;



 public class ResizeComponentKnob
   extends JComponent
 {
   private JComponent owner;
   private int size;

   public ResizeComponentKnob(JComponent owner, int size) {
     this.owner = owner;
     this.size = size;
     setCursor(Cursor.getPredefinedCursor(6));
   }



   public JComponent getOwner() {
     return this.owner;
   }



   public Dimension getMinimumSize() {
     return new Dimension(this.size, this.size);
   }



   public Dimension getPreferredSize() {
     return new Dimension(this.size, this.size);
   }



   public Dimension getMaximumSize() {
     return new Dimension(this.size, this.size);
   }



   protected void paintComponent(Graphics g) {
     super.paintComponent(g);
     Color oldColor = g.getColor();

     for (int i = 0; i < this.size; i += 3) {

       g.setColor(Color.GRAY);
       g.drawLine(i, this.size, this.size, i);

       g.setColor(Color.DARK_GRAY);
       g.drawLine(i + 1, this.size, this.size, i + 1);
     }

     g.setColor(oldColor);
   }



   protected void printComponent(Graphics g) {
     super.paintComponent(g);
   }
 }


