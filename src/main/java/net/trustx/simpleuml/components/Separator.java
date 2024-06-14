 package net.trustx.simpleuml.components;

 import java.awt.BasicStroke;
 import java.awt.Color;
 import java.awt.Dimension;
 import java.awt.Graphics;
 import java.awt.Graphics2D;
 import javax.swing.JPanel;




 public class Separator
   extends JPanel
 {
   private boolean dashed;
   private BasicStroke dashStroke;

   public Separator(boolean dashed) {
     setOpaque(false);
     this.dashed = dashed;
     this.dashStroke = new BasicStroke(1.0F, 0, 0, 1.0F, new float[] { 1.0F, 1.0F }, 0.0F);
   }



   protected void paintComponent(Graphics g) {
     super.paintComponent(g);
     Graphics2D g2d = (Graphics2D)g;
     g.setColor(Color.DARK_GRAY);
     if (this.dashed)
     {
       g2d.setStroke(this.dashStroke);
     }

     g.drawLine(1, getHeight() / 2, getWidth() - 2, getHeight() / 2);
   }



   public Dimension getMinimumSize() {
     return new Dimension(0, 9);
   }



   public Dimension getMaximumSize() {
     return new Dimension(32767, 9);
   }



   public Dimension getPreferredSize() {
     return new Dimension(0, 9);
   }
 }


