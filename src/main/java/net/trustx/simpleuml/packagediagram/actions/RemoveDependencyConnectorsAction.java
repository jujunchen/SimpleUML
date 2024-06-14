 package net.trustx.simpleuml.packagediagram.actions;
 
 import java.util.HashMap;
 import net.trustx.simpleuml.components.ActionContributorCommand;
 import net.trustx.simpleuml.packagediagram.components.PackageDiagramComponent;
 import net.trustx.simpleuml.packagediagram.components.PsiPackageComponent;
 
 

 
 public class RemoveDependencyConnectorsAction
   extends ActionContributorCommand
 {
   private final PackageDiagramComponent packageDiagramComponent;
   private PsiPackageComponent psiPackageComponent;
   
   public RemoveDependencyConnectorsAction(PsiPackageComponent psiPackageComponent) {
     super("Remove Connectors");
     this.packageDiagramComponent = (PackageDiagramComponent)psiPackageComponent.getDiagramPane();
     this.psiPackageComponent = psiPackageComponent;
   }
 
 
   
   public void executeCommand(HashMap commandProperties, boolean first, boolean last) {
     this.packageDiagramComponent.removeDependsOfSelectedPackages();
     this.packageDiagramComponent.removeDepends(this.psiPackageComponent);
   }
 }


