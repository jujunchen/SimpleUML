 package net.trustx.simpleuml.gef.components;
 
 import java.awt.Color;
 import javax.swing.BorderFactory;
 
 

 public class MarqueeFigureComponent
   extends FigureComponent
 {
   public MarqueeFigureComponent(DiagramPane diagramPane) {
     super(diagramPane);
     setPreferredLayer(1000);
     setBorder(BorderFactory.createLineBorder(Color.BLACK));
     setOpaque(false);
   }
 
 
 
   
   public void updateBorder() {}
 
 
   
   public void setSelected(boolean selected) {
     getSelectionManager().remove(this);
   }
 
 
   
   public boolean isAdjusting() {
     return true;
   }
 }


