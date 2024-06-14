 package net.trustx.simpleuml.classdiagram.components;

 import java.util.LinkedList;
 import net.trustx.simpleuml.components.ActionContributor;
 import net.trustx.simpleuml.components.ActionContributorCommand;
 import net.trustx.simpleuml.components.ActionContributorCommandInfo;
 import net.trustx.simpleuml.gef.connector.Connector;
 import net.trustx.simpleuml.gef.connector.ConnectorDecoratorContainsCommand;
 import net.trustx.simpleuml.gef.connector.ConnectorDecoratorSettings;
 import net.trustx.simpleuml.gef.connector.ConnectorDecoratorUsesCommand;
 import net.trustx.simpleuml.gef.connector.ConnectorManhattanCommand;
 import net.trustx.simpleuml.gef.connector.ConnectorStraightCommand;


 public class ConnectorActionContributor
   implements ActionContributor
 {
   private Connector connector;
   private ClassDiagramComponent classDiagramComponent;

   public ConnectorActionContributor(ClassDiagramComponent classDiagramComponent, Connector connector) {
     this.classDiagramComponent = classDiagramComponent;
     this.connector = connector;
   }



   public ActionContributorCommandInfo[] getActionContributorCommandInfos() {
     LinkedList<ActionContributorCommandInfo> infoList = new LinkedList();

     if (this.connector.getType() == 1) {

       infoList.add(new ActionContributorCommandInfo("Manhattan", new String[] { "Connection" }));
     }
     else if (this.connector.getType() == 2) {

       infoList.add(new ActionContributorCommandInfo("Straight", new String[] { "Connection" }));
     }

     if (this.connector.getConnectorDecorator().getType() == 4) {

       infoList.add(new ActionContributorCommandInfo("Composition", new String[] { "Connection" }));
     }
     else if (this.connector.getConnectorDecorator().getType() == 32) {

       infoList.add(new ActionContributorCommandInfo("Aggregation", new String[] { "Connection" }));
     }

     return infoList.<ActionContributorCommandInfo>toArray(new ActionContributorCommandInfo[infoList.size()]);
   }



   public ActionContributorCommand getActionContributorCommand(ActionContributorCommandInfo info) {
     if ("Manhattan".equals(info.getActionName()))
     {
       return (ActionContributorCommand)new ConnectorManhattanCommand("Manhattan", this.classDiagramComponent, this.connector);
     }
     if ("Straight".equals(info.getActionName()))
     {
       return (ActionContributorCommand)new ConnectorStraightCommand("Straight", this.classDiagramComponent, this.connector);
     }
     if ("Composition".equals(info.getActionName())) {

       ConnectorDecoratorSettings decoratorSettings = new ConnectorDecoratorSettings(this.connector.getConnectorDecorator().isAntialiased(), this.classDiagramComponent.getDiagramSettings().getDiagramFont(), this.classDiagramComponent.getDiagramSettings().getDiagramBackgroundColor());




       return (ActionContributorCommand)new ConnectorDecoratorContainsCommand("Composition", this.classDiagramComponent, decoratorSettings, this.connector);
     }
     if ("Aggregation".equals(info.getActionName())) {

       ConnectorDecoratorSettings decoratorSettings = new ConnectorDecoratorSettings(this.connector.getConnectorDecorator().isAntialiased(), this.classDiagramComponent.getDiagramSettings().getDiagramFont(), this.classDiagramComponent.getDiagramSettings().getDiagramBackgroundColor());




       return (ActionContributorCommand)new ConnectorDecoratorUsesCommand("Aggregation", this.classDiagramComponent, decoratorSettings, this.connector);
     }


     return null;
   }
 }


