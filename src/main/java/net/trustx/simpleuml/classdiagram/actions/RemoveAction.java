 package net.trustx.simpleuml.classdiagram.actions;

 import com.intellij.openapi.actionSystem.AnAction;
 import com.intellij.openapi.actionSystem.AnActionEvent;
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponent;
 import net.trustx.simpleuml.classdiagram.components.PsiClassComponent;
 import net.trustx.simpleuml.gef.components.FigureComponent;


 public class RemoveAction
   extends AnAction
 {
   private final ClassDiagramComponent classDiagramComponent;
   private PsiClassComponent psiClassComponent;

   public RemoveAction(ClassDiagramComponent classDiagramComponent, PsiClassComponent psiClassComponent) {
     super("Remove");
     this.classDiagramComponent = classDiagramComponent;
     this.psiClassComponent = psiClassComponent;
   }



   public void actionPerformed(AnActionEvent event) {
     this.classDiagramComponent.removeFigureComponent((FigureComponent)this.psiClassComponent);
     this.classDiagramComponent.removeSelectedComponents();
   }
 }


