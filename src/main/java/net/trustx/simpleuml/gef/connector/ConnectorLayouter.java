package net.trustx.simpleuml.gef.connector;

import java.awt.Rectangle;
import javax.swing.JComponent;

public interface ConnectorLayouter {
  void addConnector(Connector paramConnector);
  
  void removeConnector(Connector paramConnector);
  
  boolean isEmpty();
  
  void layoutConnectors(JComponent paramJComponent);
  
  Rectangle getConnectorBounds();
}


