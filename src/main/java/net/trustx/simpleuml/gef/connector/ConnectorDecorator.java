package net.trustx.simpleuml.gef.connector;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;

public interface ConnectorDecorator {
  public static final int TYPE_IMPLEMENTS = 1;
  
  public static final int TYPE_EXTENDS = 2;
  
  public static final int TYPE_USES = 4;
  
  public static final int TYPE_INNER = 8;
  
  public static final int TYPE_DEPENDS = 16;
  
  public static final int TYPE_CONTAINS = 32;
  
  Shape getStartShape();
  
  Shape getEndShape();
  
  Stroke getLineStyle();
  
  void paintStartDecorations(Graphics2D paramGraphics2D, double paramDouble, Point paramPoint);
  
  Rectangle getLastStartDecorationBounds();
  
  void paintEndDecorations(Graphics2D paramGraphics2D, double paramDouble, Point paramPoint);
  
  Rectangle getLastEndDecorationBounds();
  
  void paintCenterDecorations(Graphics2D paramGraphics2D, double paramDouble, Point paramPoint);
  
  Rectangle getLastCenterDecorationBounds();
  
  boolean isAntialiased();
  
  void setAntialiased(boolean paramBoolean);
  
  String getDescription();
  
  Color getFillColor();
  
  void setFillColor(Color paramColor);
  
  boolean equals(Object paramObject);
  
  int hashCode();
  
  int getType();
}


