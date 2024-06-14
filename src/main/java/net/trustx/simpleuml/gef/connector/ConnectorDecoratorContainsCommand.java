 package net.trustx.simpleuml.gef.connector;
 
 import java.util.HashMap;
 import net.trustx.simpleuml.components.ActionContributorCommand;
 import net.trustx.simpleuml.gef.components.DiagramPane;
 
 

 
 public class ConnectorDecoratorContainsCommand
   extends ActionContributorCommand
 {
   private DiagramPane diagramPane;
   private ConnectorDecoratorSettings decoratorSettings;
   private Connector connector;
   
   public ConnectorDecoratorContainsCommand(String text, DiagramPane diagramPane, ConnectorDecoratorSettings decoratorSettings, Connector connector) {
     super(text);
     this.diagramPane = diagramPane;
     this.decoratorSettings = decoratorSettings;
     this.connector = connector;
   }
 
 
   
   public void executeCommand(HashMap commandProperties, boolean first, boolean last) {
     if (this.connector.getConnectorDecorator().getType() == 4)
     {
       this.connector.setConnectorDecorator(new ConnectorDecoratorContains(this.decoratorSettings));
     }
     
     this.diagramPane.getConnectorManager().validateConnectors();
     this.diagramPane.revalidate();
   }
 
 
   
   public String getGroupName() {
     return "Connection";
   }
 }


