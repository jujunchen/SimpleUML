package net.trustx.simpleuml.sequencediagram.display;

import java.awt.event.ActionListener;
import net.trustx.simpleuml.sequencediagram.components.DiagramSelectionListener;

public interface AggregateActionListener extends ActionListener {
  String getName();
  
  void setPointer(DiagramSelectionListener paramDiagramSelectionListener);
}


