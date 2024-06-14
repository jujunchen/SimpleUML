 package net.trustx.simpleuml.packagediagram.selectablecommands;

 import net.trustx.simpleuml.gef.Selectable;
 import net.trustx.simpleuml.gef.SelectableCommand;
 import net.trustx.simpleuml.gef.components.FigureComponent;
 import net.trustx.simpleuml.packagediagram.components.PackageDiagramComponent;
 import net.trustx.simpleuml.packagediagram.components.PsiPackageComponent;




 public class MoveSelectableCommand
   implements SelectableCommand
 {
   private int x;
   private int y;
   private PackageDiagramComponent packageDiagramComponent;

   public MoveSelectableCommand(int x, int y, PackageDiagramComponent packageDiagramComponent) {
     this.x = x;
     this.y = y;
     this.packageDiagramComponent = packageDiagramComponent;
   }




   public void preExecution() {}



   public boolean executeCommand(Selectable selectable) {
     if (selectable instanceof PsiPackageComponent) {

       PsiPackageComponent psiPackageComponent = (PsiPackageComponent)selectable;

       psiPackageComponent.setPosX(psiPackageComponent.getPosX() + this.x);
       psiPackageComponent.setPosY(psiPackageComponent.getPosY() + this.y);
       this.packageDiagramComponent.layoutContainer();
       this.packageDiagramComponent.getConnectorManager().validateConnectors((FigureComponent)psiPackageComponent);
     }

     return true;
   }



   public void postExecution() {
     this.packageDiagramComponent.layoutContainer();

     this.packageDiagramComponent.getPackageDiagramComponentPanel().getScrollPane().revalidate();
     this.packageDiagramComponent.getPackageDiagramComponentPanel().getScrollPane().doLayout();

     this.packageDiagramComponent.changesMade();
   }
 }


