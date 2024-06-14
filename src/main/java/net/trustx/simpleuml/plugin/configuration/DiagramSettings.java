package net.trustx.simpleuml.plugin.configuration;

import javax.swing.JComponent;
import org.jdom.Element;

public interface DiagramSettings {
  void saveToElement(Element paramElement);
  
  void readFromElement(Element paramElement);
  
  JComponent createComponent();
  
  boolean isModified();
  
  void apply();
  
  void reset();
  
  void disposeUIResources();
  
  String getKey();
}


