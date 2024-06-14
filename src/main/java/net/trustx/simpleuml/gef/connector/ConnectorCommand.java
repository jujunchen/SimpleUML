package net.trustx.simpleuml.gef.connector;

public interface ConnectorCommand {
  void preExecution();
  
  boolean executeCommand(Connector paramConnector);
  
  void postExecution();
}


