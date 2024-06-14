package net.trustx.simpleuml.gef;

public interface SelectableCommand {
  void preExecution();
  
  boolean executeCommand(Selectable paramSelectable);
  
  void postExecution();
}


