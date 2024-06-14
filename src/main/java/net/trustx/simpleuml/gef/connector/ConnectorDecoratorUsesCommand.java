 package net.trustx.simpleuml.gef.connector;

 import java.util.HashMap;
 import net.trustx.simpleuml.components.ActionContributorCommand;
 import net.trustx.simpleuml.gef.components.DiagramPane;





 public class ConnectorDecoratorUsesCommand
   extends ActionContributorCommand
 {
   private DiagramPane diagramPane;
   private ConnectorDecoratorSettings decoratorSettings;
   private Connector connector;

   public ConnectorDecoratorUsesCommand(String text, DiagramPane diagramPane, ConnectorDecoratorSettings decoratorSettings, Connector connector) {
     super(text);
     this.diagramPane = diagramPane;
     this.decoratorSettings = decoratorSettings;
     this.connector = connector;
   }



   public void executeCommand(HashMap commandProperties, boolean first, boolean last) {
     if (this.connector.getConnectorDecorator().getType() == 32)
     {
       this.connector.setConnectorDecorator(new ConnectorDecoratorUses(this.decoratorSettings));
     }


     this.diagramPane.getConnectorManager().validateConnectors();
     this.diagramPane.revalidate();
   }



   public String getGroupName() {
     return "Connection";
   }
 }


