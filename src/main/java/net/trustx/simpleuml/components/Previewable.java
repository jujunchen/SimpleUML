package net.trustx.simpleuml.components;

import com.intellij.openapi.project.Project;
import java.awt.Graphics2D;
import javax.swing.JViewport;

public interface Previewable {
  boolean isShowingOnScreen();
  
  int getMinimumComponentX();
  
  int getMinimumComponentY();
  
  int getMaximumComponentX();
  
  int getMaximumComponentY();
  
  int getRealWidth();
  
  int getRealHeight();
  
  void printAllContents(Graphics2D paramGraphics2D);
  
  boolean isReady();
  
  JViewport getJViewport();
  
  Project getProject();
}


