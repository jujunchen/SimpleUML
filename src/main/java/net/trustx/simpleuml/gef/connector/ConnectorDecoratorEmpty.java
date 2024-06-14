 package net.trustx.simpleuml.gef.connector;

 import java.awt.BasicStroke;
 import java.awt.Color;
 import java.awt.Font;
 import java.awt.Graphics2D;
 import java.awt.Point;
 import java.awt.Rectangle;
 import java.awt.Shape;
 import java.awt.Stroke;




 public abstract class ConnectorDecoratorEmpty
   implements ConnectorDecorator
 {
   private Shape startShape;
   private Shape endShape;
   private Stroke lineStyle;
   private Shape[] startDecorations;
   private Shape[] centerDecorations;
   private Shape[] endDecorations;
   private String description;
   private Color fillColor;
   private boolean antialiased;
   private Font decorationFont;

   public ConnectorDecoratorEmpty() {
     this.startShape = new Rectangle(0, 0);
     this.endShape = new Rectangle(0, 0);
     this.lineStyle = new BasicStroke();
     this.startDecorations = new Shape[0];
     this.centerDecorations = new Shape[0];
     this.endDecorations = new Shape[0];
     this.description = "";
     this.fillColor = Color.BLACK;
   }



   public ConnectorDecoratorEmpty(ConnectorDecoratorSettings decoratorSettings) {
     this();
     this.fillColor = decoratorSettings.getFillColor();
     this.decorationFont = decoratorSettings.getDecorationFont();
     this.antialiased = decoratorSettings.isAntialiased();
   }



   public ConnectorDecoratorEmpty(Shape startShape, Shape endShape, Stroke lineStyle, Shape[] startDecorations, Shape[] centerDecorations, Shape[] endDecorations, Color fillColor) {
     this.centerDecorations = centerDecorations;
     this.endDecorations = endDecorations;
     this.endShape = endShape;
     this.lineStyle = lineStyle;
     this.startDecorations = startDecorations;
     this.startShape = startShape;
     this.fillColor = fillColor;
   }



   public Shape getStartShape() {
     return this.startShape;
   }



   public Shape getEndShape() {
     return this.endShape;
   }



   public Stroke getLineStyle() {
     return this.lineStyle;
   }



   public Shape[] getStartDecorations() {
     return this.startDecorations;
   }



   public Shape[] getEndDecorations() {
     return this.endDecorations;
   }



   public Shape[] getCenterDecorations(Graphics2D g2d) {
     return this.centerDecorations;
   }



   public boolean isAntialiased() {
     return this.antialiased;
   }



   public void setAntialiased(boolean antialiased) {
     this.antialiased = antialiased;
   }



   public void setCenterDecorations(Shape[] centerDecorations) {
     this.centerDecorations = centerDecorations;
   }



   public void setEndDecorations(Shape[] endDecorations) {
     this.endDecorations = endDecorations;
   }



   public void setEndShape(Shape endShape) {
     this.endShape = endShape;
   }



   public void setLineStyle(Stroke lineStyle) {
     this.lineStyle = lineStyle;
   }



   public void setStartDecorations(Shape[] startDecorations) {
     this.startDecorations = startDecorations;
   }



   public void setStartShape(Shape startShape) {
     this.startShape = startShape;
   }



   public void setDescription(String description) {
     this.description = description;
   }



   public Font getDecorationFont() {
     return this.decorationFont;
   }



   public void setDecorationFont(Font decorationFont) {
     this.decorationFont = decorationFont;
   }




   public void paintStartDecorations(Graphics2D g2d, double angle, Point location) {}




   public void paintEndDecorations(Graphics2D g2d, double angle, Point location) {}




   public void paintCenterDecorations(Graphics2D g2d, double angle, Point location) {}



   public Rectangle getLastStartDecorationBounds() {
     return null;
   }



   public Rectangle getLastEndDecorationBounds() {
     return null;
   }



   public Rectangle getLastCenterDecorationBounds() {
     return null;
   }



   public String getDescription() {
     return this.description;
   }



   public Color getFillColor() {
     return this.fillColor;
   }



   public void setFillColor(Color color) {
     this.fillColor = color;
   }



   public boolean equals(Object o) {
     if (this == o)
       return true;
     if (!(o instanceof ConnectorDecoratorEmpty)) {
       return false;
     }
     ConnectorDecoratorEmpty connectorDecoratorEmpty = (ConnectorDecoratorEmpty)o;

     return this.description.equals(connectorDecoratorEmpty.description);
   }




   public int hashCode() {
     return this.description.hashCode();
   }
 }


