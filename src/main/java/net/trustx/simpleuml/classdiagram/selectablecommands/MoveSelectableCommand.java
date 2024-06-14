 package net.trustx.simpleuml.classdiagram.selectablecommands;
 
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponent;
 import net.trustx.simpleuml.classdiagram.components.PsiClassComponent;
 import net.trustx.simpleuml.gef.Selectable;
 import net.trustx.simpleuml.gef.SelectableCommand;
 import net.trustx.simpleuml.gef.components.FigureComponent;
 

 public class MoveSelectableCommand
   implements SelectableCommand
 {
   private int x;
   private int y;
   private ClassDiagramComponent classDiagramComponent;
   
   public MoveSelectableCommand(int x, int y, ClassDiagramComponent classDiagramComponent) {
     this.x = x;
     this.y = y;
     this.classDiagramComponent = classDiagramComponent;
   }
 
 
 
   
   public void preExecution() {}
 
 
   
   public boolean executeCommand(Selectable selectable) {
     if (selectable instanceof PsiClassComponent) {
       
       PsiClassComponent psiClassComponent = (PsiClassComponent)selectable;
       
       psiClassComponent.setPosX(psiClassComponent.getPosX() + this.x);
       psiClassComponent.setPosY(psiClassComponent.getPosY() + this.y);
       this.classDiagramComponent.layoutContainer();
       this.classDiagramComponent.getConnectorManager().validateConnectors((FigureComponent)psiClassComponent);
     } 
     
     return true;
   }
 
 
   
   public void postExecution() {
     this.classDiagramComponent.layoutContainer();
     
     this.classDiagramComponent.getClassDiagramComponentPanel().getScrollPane().revalidate();
     this.classDiagramComponent.getClassDiagramComponentPanel().getScrollPane().doLayout();
     
     this.classDiagramComponent.changesMade(false);
   }
 }


