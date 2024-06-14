 package net.trustx.simpleuml.classdiagram.selectablecommands;

 import net.trustx.simpleuml.classdiagram.components.PsiClassComponent;
 import net.trustx.simpleuml.gef.Selectable;
 import net.trustx.simpleuml.gef.SelectableCommand;



 public class HasAdjustingComponentsCommand
   implements SelectableCommand
 {
   private boolean adjusting = false;

   public boolean hasAdjustingComponents() {
     return this.adjusting;
   }




   public void preExecution() {}



   public boolean executeCommand(Selectable selectable) {
     if (selectable instanceof PsiClassComponent)
     {
       return executeCommand((PsiClassComponent)selectable);
     }
     return true;
   }




   public boolean executeCommand(PsiClassComponent psiClassComponent) {
     if (psiClassComponent.isAdjusting()) {

       this.adjusting = true;
       return false;
     }

     return true;
   }

   public void postExecution() {}
 }


