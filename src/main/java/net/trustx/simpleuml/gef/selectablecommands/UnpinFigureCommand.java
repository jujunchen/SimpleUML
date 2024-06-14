 package net.trustx.simpleuml.gef.selectablecommands;

 import net.trustx.simpleuml.gef.Figure;
 import net.trustx.simpleuml.gef.Selectable;
 import net.trustx.simpleuml.gef.SelectableCommand;







 public class UnpinFigureCommand
   implements SelectableCommand
 {
   public void preExecution() {}

   public boolean executeCommand(Selectable selectable) {
     if (selectable instanceof Figure)
     {
       ((Figure)selectable).setPinned(false);
     }
     return true;
   }

   public void postExecution() {}
 }


