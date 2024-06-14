package net.trustx.simpleuml.gef;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JComponent;

public interface Figure {
  Rectangle getConnectorBounds(JComponent paramJComponent);
  
  int getWidth();
  
  int getHeight();
  
  void setPosX(int paramInt);
  
  void setPosY(int paramInt);
  
  String getKey();
  
  boolean isPinned();
  
  void setPinned(boolean paramBoolean);
  
  boolean isOnFigureBounds(Point paramPoint);
  
  boolean isCenteredOnSide(Point paramPoint);
  
  void setColor(Color paramColor);
  
  Color getColor();
}


