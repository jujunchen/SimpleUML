 package net.trustx.simpleuml.classdiagram.actions;

 import java.util.HashMap;
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponent;
 import net.trustx.simpleuml.classdiagram.components.PsiClassComponent;
 import net.trustx.simpleuml.components.ActionContributorCommand;




 public class AddUpdateDependencyConnectorsAction
   extends ActionContributorCommand
 {
   private final ClassDiagramComponent classDiagramComponent;
   private PsiClassComponent psiClassComponent;

   public AddUpdateDependencyConnectorsAction(PsiClassComponent psiClassComponent) {
     super("Add/Update Connectors");
     this.classDiagramComponent = (ClassDiagramComponent)psiClassComponent.getDiagramPane();
     this.psiClassComponent = psiClassComponent;
   }



   public void executeCommand(HashMap commandProperties, boolean first, boolean last) {
     this.classDiagramComponent.addUpdateDependsOfSelectedClasses();
     this.classDiagramComponent.addUpdateDepends(this.psiClassComponent);
   }
 }


