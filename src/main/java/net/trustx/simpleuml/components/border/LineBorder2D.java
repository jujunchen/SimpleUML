 package net.trustx.simpleuml.components.border;

 import java.awt.Color;
 import java.awt.Component;
 import java.awt.Graphics;
 import java.awt.Graphics2D;
 import java.awt.Insets;
 import java.awt.Paint;
 import java.awt.Rectangle;
 import javax.swing.border.AbstractBorder;



 public class LineBorder2D
   extends AbstractBorder
 {
   private Color lineColor;
   private int top;
   private int left;
   private int right;
   private int bottom;
   private Rectangle rect1;
   private Rectangle rect2;
   private Rectangle rect3;
   private Rectangle rect4;

   public LineBorder2D() {
     this(Color.BLUE, 1);
   }



   public LineBorder2D(Color lineColor, int top, int left, int bottom, int right) {
     this.lineColor = lineColor;
     this.top = top;
     this.left = left;
     this.right = right;
     this.bottom = bottom;

     this.rect1 = new Rectangle();
     this.rect2 = new Rectangle();
     this.rect3 = new Rectangle();
     this.rect4 = new Rectangle();
   }



   public LineBorder2D(Color lineColor, int thickness) {
     this(lineColor, thickness, thickness, thickness, thickness);
   }



   public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
     Graphics2D g2d = (Graphics2D)g;

     Color oldColor = g.getColor();
     Paint oldPaint = g2d.getPaint();

     g.setColor(this.lineColor);
     g2d.setPaint(this.lineColor);

     this.rect1.setBounds(x, y, width, this.top);
     this.rect2.setBounds(x + width - this.right, y, this.right, height);
     this.rect3.setBounds(x, y + height - this.bottom, width, this.bottom);
     this.rect4.setBounds(x, y, this.left, height);

     g2d.fill(this.rect1);
     g2d.fill(this.rect2);
     g2d.fill(this.rect3);
     g2d.fill(this.rect4);


     g2d.setPaint(oldPaint);
     g.setColor(oldColor);
   }



   public Insets getBorderInsets(Component c) {
     return new Insets(this.top, this.left, this.bottom, this.right);
   }
 }


