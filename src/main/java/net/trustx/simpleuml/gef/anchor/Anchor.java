package net.trustx.simpleuml.gef.anchor;

import java.awt.Point;

public interface Anchor {
  public static final int TYPE_MANUAL = 1;
  
  public static final int TYPE_AUTOMATIC = 2;
  
  public static final int TYPE_AUTOMATIC_CENTER = 4;
  
  public static final int CONSTRAINT_NONE = 1;
  
  public static final int CONSTRAINT_NORTH = 2;
  
  public static final int CONSTRAINT_EAST = 4;
  
  public static final int CONSTRAINT_WEST = 8;
  
  public static final int CONSTRAINT_SOUTH = 16;
  
  Point getLocation();
  
  void setLocation(Point paramPoint);
  
  int getType();
  
  boolean hasValidPosition();
  
  Point getNearestValidPosition();
  
  int getConstraint();
  
  void setConstraint(int paramInt);
}


