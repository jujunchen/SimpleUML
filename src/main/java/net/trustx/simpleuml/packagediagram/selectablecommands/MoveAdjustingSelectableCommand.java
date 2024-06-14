 package net.trustx.simpleuml.packagediagram.selectablecommands;

 import net.trustx.simpleuml.gef.Selectable;
 import net.trustx.simpleuml.gef.SelectableCommand;
 import net.trustx.simpleuml.gef.components.FigureComponent;
 import net.trustx.simpleuml.packagediagram.components.PackageDiagramComponent;
 import net.trustx.simpleuml.packagediagram.components.PsiPackageComponent;




 public class MoveAdjustingSelectableCommand
   implements SelectableCommand
 {
   private int x;
   private int y;
   private PackageDiagramComponent packageDiagramComponent;
   private boolean changed;

   public MoveAdjustingSelectableCommand(int x, int y, PackageDiagramComponent packageDiagramComponent) {
     this.x = x;
     this.y = y;
     this.packageDiagramComponent = packageDiagramComponent;
     this.changed = false;
   }




   public void preExecution() {}



   public boolean executeCommand(Selectable selectable) {
     if (selectable instanceof PsiPackageComponent) {

       PsiPackageComponent psiPackageComponent = (PsiPackageComponent)selectable;
       if (psiPackageComponent.isAdjusting()) {


         psiPackageComponent.setPosX(psiPackageComponent.getPosX() + this.x);
         psiPackageComponent.setPosY(psiPackageComponent.getPosY() + this.y);
         this.packageDiagramComponent.layoutContainer();
         this.packageDiagramComponent.getConnectorManager().validateConnectors((FigureComponent)psiPackageComponent);

         this.changed = true;
       }
     }
     return true;
   }



   public void postExecution() {
     if (this.changed) {

       this.packageDiagramComponent.layoutContainer();
       this.packageDiagramComponent.revalidate();

       this.packageDiagramComponent.changesMade();
     }
   }
 }


