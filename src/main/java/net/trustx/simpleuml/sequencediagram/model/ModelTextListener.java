package net.trustx.simpleuml.sequencediagram.model;

import java.util.EventListener;

public interface ModelTextListener extends EventListener {
  void modelTextChanged(ModelTextEvent paramModelTextEvent);
}


