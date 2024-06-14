 package net.trustx.simpleuml.packagediagram.selectablecommands;
 
 import net.trustx.simpleuml.gef.Selectable;
 import net.trustx.simpleuml.gef.SelectableCommand;
 import net.trustx.simpleuml.packagediagram.components.PsiPackageComponent;
 

 
 public class HasAdjustingComponentsCommand
   implements SelectableCommand
 {
   private boolean adjusting = false;
   
   public boolean hasAdjustingComponents() {
     return this.adjusting;
   }
 
 
 
   
   public void preExecution() {}
 
 
   
   public boolean executeCommand(Selectable selectable) {
     if (selectable instanceof PsiPackageComponent)
     {
       return executeCommand((PsiPackageComponent)selectable);
     }
     return true;
   }
 
 
 
   
   public boolean executeCommand(PsiPackageComponent psiPackageComponent) {
     if (psiPackageComponent.isAdjusting()) {
       
       this.adjusting = true;
       return false;
     } 
     
     return true;
   }
   
   public void postExecution() {}
 }


