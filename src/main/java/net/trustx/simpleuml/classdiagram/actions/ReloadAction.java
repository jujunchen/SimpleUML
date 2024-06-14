 package net.trustx.simpleuml.classdiagram.actions;
 
 import java.util.HashMap;
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponent;
 import net.trustx.simpleuml.classdiagram.components.PsiClassComponent;
 import net.trustx.simpleuml.components.ActionContributorCommand;
 

 public class ReloadAction
   extends ActionContributorCommand
 {
   private final ClassDiagramComponent classDiagramComponent;
   private PsiClassComponent psiClassComponent;
   
   public ReloadAction(PsiClassComponent psiClassComponent) {
     super("Reload");
     this.classDiagramComponent = (ClassDiagramComponent)psiClassComponent.getDiagramPane();
     this.psiClassComponent = psiClassComponent;
   }
 
 
   
   public void executeCommand(HashMap commandProperties, boolean first, boolean last) {
     this.classDiagramComponent.reloadSelectedComponents();
     this.psiClassComponent.reload();
   }
 }


