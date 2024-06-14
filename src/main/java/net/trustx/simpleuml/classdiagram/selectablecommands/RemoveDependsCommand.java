 package net.trustx.simpleuml.classdiagram.selectablecommands;

 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponent;
 import net.trustx.simpleuml.classdiagram.components.PsiClassComponent;
 import net.trustx.simpleuml.gef.Selectable;
 import net.trustx.simpleuml.gef.SelectableCommand;
 import net.trustx.simpleuml.gef.components.FigureComponent;




 public class RemoveDependsCommand
   implements SelectableCommand
 {
   private ClassDiagramComponent classDiagramComponent;

   public RemoveDependsCommand(ClassDiagramComponent classDiagramComponent) {
     this.classDiagramComponent = classDiagramComponent;
   }




   public void preExecution() {}



   public boolean executeCommand(Selectable selectable) {
     if (selectable instanceof PsiClassComponent) {

       PsiClassComponent psiClassComponent = (PsiClassComponent)selectable;
       this.classDiagramComponent.getConnectorManager().removeConnectors((FigureComponent)psiClassComponent, 2, 16);
       this.classDiagramComponent.getConnectorManager().validateConnectors();
     }

     return true;
   }



   public void postExecution() {
     this.classDiagramComponent.getConnectorManager().validateConnectors();
     this.classDiagramComponent.layoutContainer();
     this.classDiagramComponent.repaint();
   }
 }


