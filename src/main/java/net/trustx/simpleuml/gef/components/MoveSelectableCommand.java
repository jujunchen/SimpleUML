 package net.trustx.simpleuml.gef.components;

 import net.trustx.simpleuml.gef.Selectable;
 import net.trustx.simpleuml.gef.SelectableCommand;




 public class MoveSelectableCommand
   implements SelectableCommand
 {
   private int x;
   private int y;
   private DiagramPane diagramPane;

   public MoveSelectableCommand(int x, int y, DiagramPane diagramPane) {
     this.x = x;
     this.y = y;
     this.diagramPane = diagramPane;
   }




   public void preExecution() {}



   public boolean executeCommand(Selectable selectable) {
     if (selectable instanceof FigureComponent) {

       FigureComponent figureComponent = (FigureComponent)selectable;
       figureComponent.moveFigureComponent(this.x, this.y);
       this.diagramPane.layoutContainer();
       this.diagramPane.getConnectorManager().validateConnectors(figureComponent);
     }
     return true;
   }



   public void postExecution() {
     this.diagramPane.layoutContainer();
   }




   public static class PrepareMoveFigureCoomponentCommand
     implements SelectableCommand
   {
     public void preExecution() {}



     public boolean executeCommand(Selectable selectable) {
       if (selectable instanceof FigureComponent)
       {
         ((FigureComponent)selectable).prepareMoveFigureComponent();
       }
       return true;
     }




     public void postExecution() {}
   }




   public static class FinishMoveFigureCoomponentCommand
     implements SelectableCommand
   {
     public void preExecution() {}




     public boolean executeCommand(Selectable selectable) {
       if (selectable instanceof FigureComponent)
       {
         ((FigureComponent)selectable).finishedMoveFigureComponent();
       }
       return true;
     }

     public void postExecution() {}
   }
 }


