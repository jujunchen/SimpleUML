 package net.trustx.simpleuml.gef.connector;
 
 import java.awt.Point;
 import java.awt.Rectangle;
 import java.util.Iterator;
 import java.util.LinkedList;
 import javax.swing.JComponent;
 import net.trustx.simpleuml.gef.Figure;
 import net.trustx.simpleuml.gef.anchor.AnchorFree;
 
 
 

 public class SelfReferenceLayouter
   extends AbstractConnectorLayouter
 {
   private static final int INITIAL_H_SEP = 15;
   private static final int H_SEP = 10;
   private static final int V_SEP = 10;
   
   public SelfReferenceLayouter(Figure figure) {
     super(figure, figure);
   }
 
 
   
   public void layoutConnectors(JComponent parent) {
     LinkedList conList = getConnectorList();
     Rectangle bounds = getFigure1().getConnectorBounds(parent);
     
     int availableLength = (bounds.width < bounds.height) ? bounds.width : bounds.height;
     double increment = availableLength / (conList.size() + 1.0D);
     
     int i = 1;
     for (Iterator<Connector> iterator = conList.iterator(); iterator.hasNext(); ) {
       
       Connector connector = iterator.next();
       connector.setPaintable(true);
       
       double x = (bounds.x + bounds.width) - i * increment;
       Point startPoint = new Point((int)x, bounds.y + bounds.height);
       connector.getStartFigureAnchor().setLocation(startPoint);
       
       double y = (bounds.y + bounds.height) - i * increment;
       Point endPoint = new Point(bounds.x + bounds.width, (int)y);
       connector.getEndFigureAnchor().setLocation(endPoint);
       
       AnchorFree anchorFree1 = new AnchorFree(new Point((int)x, bounds.y + bounds.height + i * 10 + 15));
       AnchorFree anchorFree2 = new AnchorFree(new Point(bounds.x + bounds.width + i * 10, bounds.y + bounds.height + i * 10 + 15));
       AnchorFree anchorFree3 = new AnchorFree(new Point(bounds.x + bounds.width + i * 10, (int)y));
       
       connector.resetAnchorList();
       
       connector.getAnchorList().add(1, anchorFree3);
       connector.getAnchorList().add(1, anchorFree2);
       connector.getAnchorList().add(1, anchorFree1);
       
       i++;
     } 
   }
 }


