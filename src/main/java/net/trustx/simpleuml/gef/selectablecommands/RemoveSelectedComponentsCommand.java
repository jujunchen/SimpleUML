 package net.trustx.simpleuml.gef.selectablecommands;

 import net.trustx.simpleuml.gef.Selectable;
 import net.trustx.simpleuml.gef.SelectableCommand;
 import net.trustx.simpleuml.gef.components.DiagramPane;
 import net.trustx.simpleuml.gef.components.FigureComponent;







 public class RemoveSelectedComponentsCommand
   implements SelectableCommand
 {
   private DiagramPane diagramPane;

   public RemoveSelectedComponentsCommand(DiagramPane diagramPane) {
     this.diagramPane = diagramPane;
   }




   public void preExecution() {}



   public boolean executeCommand(Selectable selectable) {
     if (selectable instanceof FigureComponent)
     {
       return executeCommand((FigureComponent)selectable);
     }
     return true;
   }



   public boolean executeCommand(FigureComponent figureComponent) {
     this.diagramPane.removeFigureComponent(figureComponent);
     return true;
   }



   public void postExecution() {
     this.diagramPane.requestFocusInWindow();
     this.diagramPane.repaint();
   }
 }


