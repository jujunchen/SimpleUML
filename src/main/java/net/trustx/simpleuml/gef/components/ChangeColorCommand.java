 package net.trustx.simpleuml.gef.components;

 import java.awt.Color;
 import java.util.HashMap;
 import javax.swing.JColorChooser;
 import net.trustx.simpleuml.components.ActionContributorCommand;



 public class ChangeColorCommand
   extends ActionContributorCommand
 {
   private static final String COLOR_PROPERTY_NAME = "ChangeColorCommand.Color";
   private FigureComponent figureComponent;

   public ChangeColorCommand(String text, FigureComponent figureComponent) {
     super(text);
     this.figureComponent = figureComponent;
   }



   public void executeCommand(HashMap commandProperties, boolean first, boolean last) {
     DiagramPane diagramPane = this.figureComponent.getDiagramPane();

     if (!commandProperties.containsKey("ChangeColorCommand.Color"))
     {
       commandProperties.put("ChangeColorCommand.Color", JColorChooser.showDialog(diagramPane, "Choose a Color", this.figureComponent.getColor()));
     }

     this.figureComponent.setColor((Color) commandProperties.get("ChangeColorCommand.Color"));

     diagramPane.layoutContainer();
     diagramPane.getConnectorManager().validateConnectors();
     diagramPane.repaint();
   }
 }


