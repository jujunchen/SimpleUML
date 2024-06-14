 package net.trustx.simpleuml.gef.connector;

 import net.trustx.simpleuml.gef.components.DiagramPane;




 public class ConnectorVisibilityCommand
   implements ConnectorCommand
 {
   private DiagramPane diagramPane;
   private boolean visibility;

   public ConnectorVisibilityCommand(DiagramPane diagramPane, boolean visibility) {
     this.diagramPane = diagramPane;
     this.visibility = visibility;
   }




   public void preExecution() {}



   public boolean executeCommand(Connector connector) {
     connector.setVisible(this.visibility);
     return true;
   }



   public void postExecution() {
     this.diagramPane.revalidate();
     this.diagramPane.repaint();
   }
 }


