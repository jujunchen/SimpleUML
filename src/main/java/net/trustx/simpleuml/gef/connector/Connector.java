package net.trustx.simpleuml.gef.connector;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import net.trustx.simpleuml.components.ActionContributor;
import net.trustx.simpleuml.gef.anchor.FigureAnchor;

public interface Connector extends ActionContributor {
  public static final int TYPE_STRAIGHT = 1;
  
  public static final int TYPE_MANHATTAN = 2;
  
  int getType();
  
  void setType(int paramInt);
  
  ArrayList getAnchorList();
  
  void resetAnchorList();
  
  FigureAnchor getStartFigureAnchor();
  
  FigureAnchor getEndFigureAnchor();
  
  ConnectorDecorator getConnectorDecorator();
  
  void setConnectorDecorator(ConnectorDecorator paramConnectorDecorator);
  
  void setVisible(boolean paramBoolean);
  
  boolean isVisible();
  
  void paint(Graphics2D paramGraphics2D);
  
  void disposeConnector();
  
  Rectangle getBounds();
  
  void setPaintable(boolean paramBoolean);
  
  boolean isPaintable();
  
  boolean contains(int paramInt1, int paramInt2);
  
  void setActionContributor(ActionContributor paramActionContributor);
}


