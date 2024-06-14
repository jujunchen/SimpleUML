 package net.trustx.simpleuml.packagediagram.actions;

 import java.util.HashMap;
 import net.trustx.simpleuml.components.ActionContributorCommand;
 import net.trustx.simpleuml.gef.components.DiagramPane;
 import net.trustx.simpleuml.packagediagram.components.PsiPackageComponent;




 public class ToggleClassesAction
   extends ActionContributorCommand
 {
   private PsiPackageComponent psiPackageComponent;
   private boolean showClasses;

   public ToggleClassesAction(PsiPackageComponent psiPackageComponent, boolean showClasses) {
     this.psiPackageComponent = psiPackageComponent;
     this.showClasses = showClasses;
   }



   public void executeCommand(HashMap commandProperties, boolean first, boolean last) {
     DiagramPane diagramPane = this.psiPackageComponent.getDiagramPane();

     this.psiPackageComponent.setShowClasses(this.showClasses);

     diagramPane.layoutContainer();
     diagramPane.getConnectorManager().validateConnectors();
     diagramPane.repaint();
   }
 }


