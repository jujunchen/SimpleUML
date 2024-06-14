 package net.trustx.simpleuml.gef.connector;

 import java.awt.Rectangle;
 import java.util.Iterator;
 import java.util.LinkedList;
 import net.trustx.simpleuml.gef.Figure;





 public abstract class AbstractConnectorLayouter
   implements ConnectorLayouter
 {
   private LinkedList connectorList;
   private Figure figure1;
   private Figure figure2;

   public AbstractConnectorLayouter(Figure figure1, Figure figure2) {
     this.connectorList = new LinkedList();
     this.figure1 = figure1;
     this.figure2 = figure2;
   }



   public void addConnector(Connector connector) {
     this.connectorList.add(connector);
   }



   public void removeConnector(Connector connector) {
     this.connectorList.remove(connector);
   }



   public boolean isEmpty() {
     return this.connectorList.isEmpty();
   }



   public LinkedList getConnectorList() {
     return this.connectorList;
   }



   public Figure getFigure1() {
     return this.figure1;
   }



   public Figure getFigure2() {
     return this.figure2;
   }



   public Rectangle getConnectorBounds() {
     Rectangle rectangle = null;
     for (Iterator<Connector> iterator = getConnectorList().iterator(); iterator.hasNext(); ) {

       Connector connector = iterator.next();
       Rectangle tR = connector.getBounds();
       if (tR == null) {
         continue;
       }

       if (rectangle == null) {

         rectangle = connector.getBounds();

         continue;
       }
       rectangle = rectangle.union(connector.getBounds());
     }


     return rectangle;
   }
 }


