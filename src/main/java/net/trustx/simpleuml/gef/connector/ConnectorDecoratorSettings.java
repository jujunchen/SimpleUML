 package net.trustx.simpleuml.gef.connector;
 
 import java.awt.Color;
 import java.awt.Font;
 
 

 
 public class ConnectorDecoratorSettings
 {
   private Color fillColor;
   private boolean antialiased;
   private Font decorationFont;
   
   public ConnectorDecoratorSettings(boolean antialiased, Font decorationFont, Color fillColor) {
     this.antialiased = antialiased;
     this.decorationFont = decorationFont;
     this.fillColor = fillColor;
   }
 
 
   
   public boolean isAntialiased() {
     return this.antialiased;
   }
 
 
   
   public Font getDecorationFont() {
     return this.decorationFont;
   }
 
 
   
   public Color getFillColor() {
     return this.fillColor;
   }
 }


