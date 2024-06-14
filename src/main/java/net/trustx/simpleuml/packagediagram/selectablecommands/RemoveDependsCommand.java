 package net.trustx.simpleuml.packagediagram.selectablecommands;

 import net.trustx.simpleuml.gef.Selectable;
 import net.trustx.simpleuml.gef.SelectableCommand;
 import net.trustx.simpleuml.gef.components.FigureComponent;
 import net.trustx.simpleuml.packagediagram.components.PackageDiagramComponent;
 import net.trustx.simpleuml.packagediagram.components.PsiPackageComponent;



 public class RemoveDependsCommand
   implements SelectableCommand
 {
   private PackageDiagramComponent packageDiagramComponent;

   public RemoveDependsCommand(PackageDiagramComponent packageDiagramComponent) {
     this.packageDiagramComponent = packageDiagramComponent;
   }




   public void preExecution() {}



   public boolean executeCommand(Selectable selectable) {
     if (selectable instanceof PsiPackageComponent) {

       PsiPackageComponent psiPackageComponent = (PsiPackageComponent)selectable;

       this.packageDiagramComponent.getConnectorManager().removeConnectors((FigureComponent)psiPackageComponent, 2, 16);
       this.packageDiagramComponent.getConnectorManager().validateConnectors();
     }

     return true;
   }



   public void postExecution() {
     this.packageDiagramComponent.getConnectorManager().validateConnectors();
     this.packageDiagramComponent.layoutContainer();
     this.packageDiagramComponent.repaint();
   }
 }


