 package net.trustx.simpleuml.classdiagram.actions;

 import java.util.HashMap;
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponent;
 import net.trustx.simpleuml.classdiagram.components.PsiClassComponent;
 import net.trustx.simpleuml.components.ActionContributorCommand;


 public class RemoveDependencyConnectorsAction
   extends ActionContributorCommand
 {
   private final ClassDiagramComponent classDiagramComponent;
   private PsiClassComponent psiClassComponent;

   public RemoveDependencyConnectorsAction(PsiClassComponent psiClassComponent) {
     super("Remove Connectors");
     this.classDiagramComponent = (ClassDiagramComponent)psiClassComponent.getDiagramPane();
     this.psiClassComponent = psiClassComponent;
   }




   public void executeCommand(HashMap commandProperties, boolean first, boolean last) {
     this.classDiagramComponent.removeDependsOfSelectedClasses();
     this.classDiagramComponent.removeDepends(this.psiClassComponent);
   }
 }


