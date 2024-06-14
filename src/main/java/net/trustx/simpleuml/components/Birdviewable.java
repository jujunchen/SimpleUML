package net.trustx.simpleuml.components;

import javax.swing.event.ChangeListener;

public interface Birdviewable extends Previewable {
  void addChangeListener(ChangeListener paramChangeListener);
  
  void removeChangeListener(ChangeListener paramChangeListener);
  
  void requestUpdate();
}


