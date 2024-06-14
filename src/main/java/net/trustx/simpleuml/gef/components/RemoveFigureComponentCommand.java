 package net.trustx.simpleuml.gef.components;

 import java.util.HashMap;
 import net.trustx.simpleuml.components.ActionContributorCommand;






 public class RemoveFigureComponentCommand
   extends ActionContributorCommand
 {
   private FigureComponent figureComponent;

   public RemoveFigureComponentCommand(String text, FigureComponent figureComponent) {
     super(text);
     this.figureComponent = figureComponent;
   }



   public void executeCommand(HashMap commandProperties, boolean first, boolean last) {
     DiagramPane diagramPane = this.figureComponent.getDiagramPane();
     diagramPane.getSelectionManager().remove(this.figureComponent);
     diagramPane.removeFigureComponent(this.figureComponent);
     diagramPane.layoutContainer();
     diagramPane.getConnectorManager().validateConnectors();
     diagramPane.repaint();
   }



   public String getGroupName() {
     return "Figure";
   }
 }


