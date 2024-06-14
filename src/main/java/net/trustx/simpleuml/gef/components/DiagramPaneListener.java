package net.trustx.simpleuml.gef.components;

import net.trustx.simpleuml.gef.connector.Connector;

public interface DiagramPaneListener {
  void figureAdded(FigureComponent paramFigureComponent);
  
  void figureRemoved(FigureComponent paramFigureComponent);
  
  void connectorAdded(Connector paramConnector);
  
  void connectorRemoved(Connector paramConnector);
}


