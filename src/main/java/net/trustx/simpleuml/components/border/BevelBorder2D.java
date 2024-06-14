 package net.trustx.simpleuml.components.border;
 
 import java.awt.Color;
 import java.awt.Component;
 import java.awt.GradientPaint;
 import java.awt.Graphics;
 import java.awt.Graphics2D;
 import java.awt.Insets;
 import java.awt.Paint;
 import java.awt.Polygon;
 import javax.swing.JButton;
 import javax.swing.JFrame;
 import javax.swing.border.BevelBorder;
 

 
 public class BevelBorder2D
   extends BevelBorder
 {
   private static final int TOP = 1;
   private static final int LEFT = 2;
   private static final int RIGHT = 4;
   private static final int BOTTOM = 8;
   private int borderSize;
   private int partsToPaint;
   
   public static void main(String[] args) {
     JFrame frame = new JFrame("BevelBorder2DTest");
     JButton button = new JButton("testButton");
     button.setBorder(new BevelBorder2D(0, 2));
     frame.getContentPane().add(button, "North");
     
     JButton button2 = new JButton("testButton");
     button2.setBorder(new BevelBorder(0));
     frame.getContentPane().add(button2, "South");
     
     frame.setDefaultCloseOperation(3);
     frame.setSize(200, 200);
     frame.getContentPane().setBackground(Color.WHITE);
     frame.setVisible(true);
   }
 
 
 
 
 
 
   
   public BevelBorder2D(int bevelType, int borderSize) {
     super(bevelType);
     this.borderSize = borderSize;
     this.partsToPaint = 15;
   }
 
 
   
   public Insets getBorderInsets(Component c) {
     return new Insets(this.borderSize, this.borderSize, this.borderSize, this.borderSize);
   }
 
 
   
   public Insets getBorderInsets(Component c, Insets insets) {
     insets.left = insets.top = insets.right = insets.bottom = this.borderSize;
     return insets;
   }
 
 
   
   protected void paintRaisedBevel(Component c, Graphics g, int x, int y, int width, int height) {
     Graphics2D g2d = (Graphics2D)g;
     Color oldColor = g.getColor();
     Paint oldPaint = g2d.getPaint();
     int h = height;
     int w = width;
     
     g.translate(x, y);
     
     Polygon polygon1 = new Polygon();
     polygon1.addPoint(0, 0);
     polygon1.addPoint(0, h);
     polygon1.addPoint(this.borderSize, h - this.borderSize);
     polygon1.addPoint(this.borderSize, this.borderSize);
     
     GradientPaint paint1 = new GradientPaint(0.0F, 0.0F, getHighlightOuterColor(c), this.borderSize * 1.5F, 0.0F, c.getBackground());
     g2d.setPaint(paint1);
     g2d.fill(polygon1);
 
     
     Polygon polygon2 = new Polygon();
     polygon2.addPoint(0, 0);
     polygon2.addPoint(w, 0);
     polygon2.addPoint(w - this.borderSize, this.borderSize);
     polygon2.addPoint(this.borderSize, this.borderSize);
     
     GradientPaint paint2 = new GradientPaint(0.0F, 0.0F, getHighlightOuterColor(c), 0.0F, this.borderSize * 1.5F, c.getBackground());
     g2d.setPaint(paint2);
     g2d.fill(polygon2);
 
     
     Polygon polygon3 = new Polygon();
     polygon3.addPoint(0, h);
     polygon3.addPoint(w, h);
     polygon3.addPoint(w - this.borderSize, h - this.borderSize);
     polygon3.addPoint(this.borderSize, h - this.borderSize);
     
     GradientPaint paint3 = new GradientPaint(0.0F, h - 0.0F, getShadowOuterColor(c), 0.0F, h - this.borderSize * 1.5F, c.getBackground());
     g2d.setPaint(paint3);
     g2d.fill(polygon3);
 
     
     Polygon polygon4 = new Polygon();
     polygon4.addPoint(w, h);
     polygon4.addPoint(w, 0);
     polygon4.addPoint(w - this.borderSize, this.borderSize);
     polygon4.addPoint(w - this.borderSize, h - this.borderSize);
     
     GradientPaint paint4 = new GradientPaint(w - 0.0F, 0.0F, getShadowOuterColor(c), w - this.borderSize * 1.5F, 0.0F, c.getBackground());
     g2d.setPaint(paint4);
     g2d.fill(polygon4);
 
     
     g.translate(-x, -y);
     g.setColor(oldColor);
     g2d.setPaint(oldPaint);
   }
 
 
   
   protected void paintLoweredBevel(Component c, Graphics g, int x, int y, int width, int height) {
     Graphics2D g2d = (Graphics2D)g;
     Color oldColor = g.getColor();
     Paint oldPaint = g2d.getPaint();
     int h = height;
     int w = width;
     
     g.translate(x, y);
 
     
     Polygon polygon1 = new Polygon();
     polygon1.addPoint(0, 0);
     polygon1.addPoint(0, h);
     polygon1.addPoint(this.borderSize, h - this.borderSize);
     polygon1.addPoint(this.borderSize, this.borderSize);
     
     GradientPaint paint1 = new GradientPaint(0.0F, 0.0F, getShadowOuterColor(c), this.borderSize * 1.5F, 0.0F, c.getBackground());
     g2d.setPaint(paint1);
     g2d.fill(polygon1);
 
     
     Polygon polygon2 = new Polygon();
     polygon2.addPoint(0, 0);
     polygon2.addPoint(w, 0);
     polygon2.addPoint(w - this.borderSize, this.borderSize);
     polygon2.addPoint(this.borderSize, this.borderSize);
     
     GradientPaint paint2 = new GradientPaint(0.0F, 0.0F, getShadowOuterColor(c), 0.0F, this.borderSize * 1.5F, c.getBackground());
     g2d.setPaint(paint2);
     g2d.fill(polygon2);
 
     
     Polygon polygon3 = new Polygon();
     polygon3.addPoint(0, h);
     polygon3.addPoint(w, h);
     polygon3.addPoint(w - this.borderSize, h - this.borderSize);
     polygon3.addPoint(this.borderSize, h - this.borderSize);
     
     GradientPaint paint3 = new GradientPaint(0.0F, h - 0.0F, getHighlightOuterColor(c), 0.0F, h - this.borderSize * 1.5F, c.getBackground());
     g2d.setPaint(paint3);
     g2d.fill(polygon3);
 
     
     Polygon polygon4 = new Polygon();
     polygon4.addPoint(w, h);
     polygon4.addPoint(w, 0);
     polygon4.addPoint(w - this.borderSize, this.borderSize);
     polygon4.addPoint(w - this.borderSize, h - this.borderSize);
     
     GradientPaint paint4 = new GradientPaint(w - 0.0F, 0.0F, getHighlightOuterColor(c), w - this.borderSize * 1.5F, 0.0F, c.getBackground());
     g2d.setPaint(paint4);
     g2d.fill(polygon4);
 
     
     g.translate(-x, -y);
     g.setColor(oldColor);
     g2d.setPaint(oldPaint);
   }
 }


