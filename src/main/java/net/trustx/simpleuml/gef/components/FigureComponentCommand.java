package net.trustx.simpleuml.gef.components;

public interface FigureComponentCommand {
  void preExecution();
  
  boolean executeCommand(FigureComponent paramFigureComponent);
  
  void postExecution();
}


