 package net.trustx.simpleuml.gef.connector;

 import net.trustx.simpleuml.gef.components.DiagramPane;






 public class ConnectorAntialiasingCommand
   implements ConnectorCommand
 {
   private DiagramPane diagramPane;
   private boolean antialiasing;

   public ConnectorAntialiasingCommand(DiagramPane diagramPane, boolean antialiasing) {
     this.diagramPane = diagramPane;
     this.antialiasing = antialiasing;
   }




   public void preExecution() {}



   public boolean executeCommand(Connector connector) {
     connector.getConnectorDecorator().setAntialiased(this.antialiasing);
     return true;
   }



   public void postExecution() {
     this.diagramPane.repaint();
   }
 }


