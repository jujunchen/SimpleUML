 package net.trustx.simpleuml.dependencydiagram.actions;

 import com.intellij.openapi.actionSystem.AnAction;
 import com.intellij.openapi.actionSystem.AnActionEvent;
 import javax.swing.ImageIcon;
 import net.trustx.simpleuml.dependencydiagram.components.DependencyDiagramComponentPanel;




 public class RefreshAction
   extends AnAction
 {
   private DependencyDiagramComponentPanel dependencyDiagramComponentPanel;

   public RefreshAction(DependencyDiagramComponentPanel dependencyDiagramComponentPanel) {
     super("Refresh", "Refresh the view", new ImageIcon(DependencyDiagramComponentPanel.class.getResource("/actions/sync.png")));
     this.dependencyDiagramComponentPanel = dependencyDiagramComponentPanel;
   }



   public void actionPerformed(AnActionEvent e) {
     this.dependencyDiagramComponentPanel.refresh();
   }
 }


