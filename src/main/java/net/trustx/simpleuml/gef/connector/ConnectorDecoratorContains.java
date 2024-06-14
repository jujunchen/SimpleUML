 package net.trustx.simpleuml.gef.connector;

 import java.awt.Color;
 import java.awt.Font;
 import java.awt.Graphics2D;
 import java.awt.Point;
 import java.awt.Polygon;
 import java.awt.Rectangle;





 public class ConnectorDecoratorContains
   extends ConnectorDecoratorEmpty
 {
   private Rectangle bounds;

   public ConnectorDecoratorContains(ConnectorDecoratorSettings decoratorSettings) {
     super(decoratorSettings);

     Polygon pointer = new Polygon();
     pointer.addPoint(0, 0);
     pointer.addPoint(6, 10);
     pointer.addPoint(0, 20);
     pointer.addPoint(-6, 10);
     setStartShape(pointer);
     setFillColor(Color.BLACK);
   }



   public int hashCode() {
     return getClass().getName().hashCode();
   }



   public int getType() {
     return 32;
   }



   public boolean equals(Object obj) {
     return (super.equals(obj) && obj instanceof ConnectorDecoratorUses);
   }



   public void paintCenterDecorations(Graphics2D g2d, double angle, Point location) {
     Font origFont = g2d.getFont();
     g2d.setFont(getDecorationFont());
     Rectangle rectangle = g2d.getFontMetrics().getStringBounds(getDescription(), g2d).getBounds();
     g2d.drawString(getDescription(), location.x - rectangle.width / 2, location.y - rectangle.height / 2);
     this.bounds = new Rectangle(location.x - rectangle.width / 2, location.y - rectangle.height / 2, rectangle.width, rectangle.height);
     g2d.setFont(origFont);
   }



   public Rectangle getLastCenterDecorationBounds() {
     return this.bounds;
   }
 }


