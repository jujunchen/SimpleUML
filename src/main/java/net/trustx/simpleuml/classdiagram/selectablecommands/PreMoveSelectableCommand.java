 package net.trustx.simpleuml.classdiagram.selectablecommands;

 import net.trustx.simpleuml.classdiagram.components.PsiClassComponent;
 import net.trustx.simpleuml.gef.Selectable;
 import net.trustx.simpleuml.gef.SelectableCommand;



 public class PreMoveSelectableCommand
   implements SelectableCommand
 {
   public void preExecution() {}

   public boolean executeCommand(Selectable selectable) {
     if (selectable instanceof PsiClassComponent)
     {
       ((PsiClassComponent)selectable).setAdjusting(true);
     }
     return true;
   }

   public void postExecution() {}
 }


