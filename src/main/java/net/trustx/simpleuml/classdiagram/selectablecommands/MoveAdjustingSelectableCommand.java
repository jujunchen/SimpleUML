 package net.trustx.simpleuml.classdiagram.selectablecommands;
 
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponent;
 import net.trustx.simpleuml.classdiagram.components.PsiClassComponent;
 import net.trustx.simpleuml.gef.Selectable;
 import net.trustx.simpleuml.gef.SelectableCommand;
 import net.trustx.simpleuml.gef.components.FigureComponent;
 

 public class MoveAdjustingSelectableCommand
   implements SelectableCommand
 {
   private int x;
   private int y;
   private ClassDiagramComponent classDiagramComponent;
   private boolean changed;
   
   public MoveAdjustingSelectableCommand(int x, int y, ClassDiagramComponent classDiagramComponent) {
     this.x = x;
     this.y = y;
     this.classDiagramComponent = classDiagramComponent;
     this.changed = false;
   }
 
 
 
   
   public void preExecution() {}
 
 
   
   public boolean executeCommand(Selectable selectable) {
     if (selectable instanceof PsiClassComponent) {
       
       PsiClassComponent psiClassComponent = (PsiClassComponent)selectable;
       if (psiClassComponent.isAdjusting()) {
 
         
         psiClassComponent.setPosX(psiClassComponent.getPosX() + this.x);
         psiClassComponent.setPosY(psiClassComponent.getPosY() + this.y);
         this.classDiagramComponent.layoutContainer();
         this.classDiagramComponent.getConnectorManager().validateConnectors((FigureComponent)psiClassComponent);
         
         this.changed = true;
       } 
     } 
     return true;
   }
 
 
   
   public void postExecution() {
     if (this.changed) {
       
       this.classDiagramComponent.layoutContainer();
       this.classDiagramComponent.revalidate();
       
       this.classDiagramComponent.changesMade(false);
     } 
   }
 }


