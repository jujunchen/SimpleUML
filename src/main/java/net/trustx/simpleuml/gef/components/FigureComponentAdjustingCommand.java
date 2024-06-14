 package net.trustx.simpleuml.gef.components;
 
 

 public class FigureComponentAdjustingCommand
   implements FigureComponentCommand
 {
   private boolean adjusting;
   
   public FigureComponentAdjustingCommand(boolean adjusting) {
     this.adjusting = adjusting;
   }
 
 
 
   
   public void preExecution() {}
 
 
   
   public boolean executeCommand(FigureComponent figureComponent) {
     figureComponent.setAdjusting(this.adjusting);
     return true;
   }
   
   public void postExecution() {}
 }


