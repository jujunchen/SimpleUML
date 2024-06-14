 package net.trustx.simpleuml.components.border;

 import java.awt.Color;
 import java.awt.Component;
 import java.awt.Graphics;
 import java.awt.Graphics2D;
 import java.awt.Insets;
 import java.awt.Paint;
 import java.awt.Rectangle;
 import javax.swing.border.AbstractBorder;
 import javax.swing.border.Border;



 public class SelectionBorder
   extends AbstractBorder
 {
   private Color lineColor;
   private Border containedBorder;
   private Rectangle rect1;
   private Rectangle rect2;
   private Rectangle rect3;
   private Rectangle rect4;

   public SelectionBorder(Border containedBorder) {
     this.containedBorder = containedBorder;
     this.lineColor = Color.BLUE;

     this.rect1 = new Rectangle();
     this.rect2 = new Rectangle();
     this.rect3 = new Rectangle();
     this.rect4 = new Rectangle();
   }



   public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
     this.containedBorder.paintBorder(c, g, x, y, width, height);

     Graphics2D g2d = (Graphics2D)g;

     Color oldColor = g.getColor();
     Paint oldPaint = g2d.getPaint();

     g.setColor(this.lineColor);
     g2d.setPaint(this.lineColor);


     this.rect1.setBounds(x, y, width, 1);
     this.rect2.setBounds(x + width - 1, y, 1, height);
     this.rect3.setBounds(x, y + height - 1, width, 1);
     this.rect4.setBounds(x, y, 1, height);

     g2d.fill(this.rect1);
     g2d.fill(this.rect2);
     g2d.fill(this.rect3);
     g2d.fill(this.rect4);


     g2d.setPaint(oldPaint);
     g.setColor(oldColor);
   }



   public Insets getBorderInsets(Component c) {
     return this.containedBorder.getBorderInsets(c);
   }
 }


