 package net.trustx.simpleuml.components;
 
 import java.awt.BasicStroke;
 import java.awt.Color;
 import java.awt.Dimension;
 import java.awt.Graphics;
 import java.awt.Graphics2D;
 import javax.swing.JFrame;
 import javax.swing.JPanel;
 

 
 
 public class ExpandLabel
   extends JPanel
 {
   private static int SIZE = 9;
   
   private boolean expanded;
   
   private boolean expandable;
   
   private Dimension dimension;
   private boolean dashed;
   private BasicStroke dashStroke;
   
   public static void main(String[] args) {
     JFrame frame = new JFrame();
     frame.setDefaultCloseOperation(3);
     frame.getContentPane().add(new ExpandComponent("ewerw", true, true, true), "Center");
     frame.pack();
     frame.setVisible(true);
   }
 
 
   
   public ExpandLabel(boolean dashed) {
     this.dashed = dashed;
     this.dashStroke = new BasicStroke(1.0F, 0, 0, 1.0F, new float[] { 1.0F, 1.0F }, 0.0F);
     setOpaque(false);
     this.dimension = new Dimension(SIZE + 3, SIZE + 1);
   }
 
 
   
   protected void paintComponent(Graphics g) {
     super.paintComponent(g);
     int offset = 1;
     if (this.expandable) {
       
       g.setColor(Color.DARK_GRAY);
       g.drawRect(offset, 1, SIZE - 1, SIZE - 1);
       g.drawLine(0, SIZE / 2 + 1, offset, SIZE / 2 + 1);
       g.drawLine(SIZE, SIZE / 2 + 1, SIZE + offset, SIZE / 2 + 1);
       
       if (this.expanded)
       {
         g.setColor(Color.BLACK);
         g.drawLine(offset + 2, SIZE / 2 + 1, offset + SIZE - 3, SIZE / 2 + 1);
       }
       else
       {
         g.setColor(Color.BLACK);
         g.drawLine(offset + 2, SIZE / 2 + 1, offset + SIZE - 3, SIZE / 2 + 1);
         g.drawLine(offset + SIZE / 2, 3, offset + SIZE / 2, SIZE - 3 + 1);
       }
     
     } else {
       
       Graphics2D g2d = (Graphics2D)g;
       g.setColor(Color.DARK_GRAY);
       
       if (this.dashed)
       {
         g2d.setStroke(this.dashStroke);
       }
       g.drawLine(0, SIZE / 2, SIZE, SIZE / 2);
     } 
   }
 
 
   
   public boolean isExpandable() {
     return this.expandable;
   }
 
 
   
   public void setExpandable(boolean expandable) {
     this.expandable = expandable;
     revalidate();
     repaint();
   }
 
 
   
   public boolean isExpanded() {
     return this.expanded;
   }
 
 
   
   public void setExpanded(boolean expanded) {
     this.expanded = expanded;
     revalidate();
     repaint();
   }
 
 
   
   public Dimension getMaximumSize() {
     return this.dimension;
   }
 
 
   
   public Dimension getMinimumSize() {
     return this.dimension;
   }
 
 
   
   public Dimension getPreferredSize() {
     return this.dimension;
   }
 }


