 package net.trustx.simpleuml.gef.connector;

 import java.util.HashMap;
 import net.trustx.simpleuml.components.ActionContributorCommand;
 import net.trustx.simpleuml.gef.components.DiagramPane;




 public class ConnectorStraightCommand
   extends ActionContributorCommand
 {
   private DiagramPane diagramPane;
   private Connector connector;

   public ConnectorStraightCommand(String text, DiagramPane diagramPane, Connector connector) {
     super(text);
     this.diagramPane = diagramPane;
     this.connector = connector;
   }



   public void executeCommand(HashMap commandProperties, boolean first, boolean last) {
     this.connector.setType(1);

     this.diagramPane.getConnectorManager().validateConnectors();
     this.diagramPane.revalidate();
   }



   public String getGroupName() {
     return "Connection";
   }
 }


