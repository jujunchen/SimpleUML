 package net.trustx.simpleuml.plugin.actions;

 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.actionSystem.Presentation;
 import com.intellij.openapi.actionSystem.ToggleAction;
 import java.awt.Component;
 import java.awt.Dialog;
 import java.awt.Frame;
 import java.awt.Window;
 import javax.swing.ImageIcon;
 import javax.swing.JDialog;
 import javax.swing.JFrame;
 import javax.swing.JOptionPane;
 import javax.swing.SwingUtilities;
 import net.trustx.simpleuml.components.BirdViewFrame;
 import net.trustx.simpleuml.components.Birdviewable;
 import net.trustx.simpleuml.components.DiagramComponent;
 import net.trustx.simpleuml.plugin.UMLToolWindowPlugin;



















 public class ShowBirdViewAction
   extends ToggleAction
 {
   private UMLToolWindowPlugin umlToolWindowPlugin;

   public ShowBirdViewAction(UMLToolWindowPlugin umlToolWindowPlugin) {
     super("Show Birdview", "Shows the Birdview", new ImageIcon(SaveAsImageAction.class.getResource("/net/trustx/simpleuml/icons/showBirdView.png")));
     this.umlToolWindowPlugin = umlToolWindowPlugin;
   }



   public boolean isSelected(AnActionEvent e) {
     boolean selected = (this.umlToolWindowPlugin.getBirdViewFrame() != null && this.umlToolWindowPlugin.getBirdViewFrame().isShowing());
     return selected;
   }



   public void setSelected(AnActionEvent e, boolean selected) {
     if (selected) {

       showNewBirdView();
     }
     else {

       hideBirdView();
     }
   }



   public void showNewBirdView() {
     DiagramComponent diagramComponent = this.umlToolWindowPlugin.getSelectedDiagramComponent();

     createNewBirdView(this.umlToolWindowPlugin.getContentPane());

     Birdviewable birdviewable = null;
     if (diagramComponent instanceof Birdviewable)
     {
       birdviewable = (Birdviewable)diagramComponent;
     }
     this.umlToolWindowPlugin.getBirdViewFrame().setCurrentBirdviewable(birdviewable);
     this.umlToolWindowPlugin.getBirdViewFrame().setVisible(true);
     this.umlToolWindowPlugin.setBirdViewShouldBeVisible(true);
   }



   private void hideBirdView() {
     if (this.umlToolWindowPlugin.getBirdViewFrame() != null)
     {
       this.umlToolWindowPlugin.getBirdViewFrame().disposeBirdViewFrame(true);
     }
   }



   public void update(AnActionEvent event) {
     Presentation presentation = event.getPresentation();
     if (this.umlToolWindowPlugin.getSelectedDiagramComponent() == null) {

       presentation.setEnabled(false);
     }
     else {

       presentation.setEnabled(true);
     }

     presentation.putClientProperty("selected", Boolean.valueOf(isSelected(event)));
   }



   private void createNewBirdView(Component component) {
     Window win = (Window)SwingUtilities.getAncestorOfClass(JDialog.class, component);
     if (win == null)
     {
       win = (Window)SwingUtilities.getAncestorOfClass(JFrame.class, component);
     }
     if (win == null)
     {
       win = JOptionPane.getRootFrame();
     }

     if (win instanceof Frame) {

       this.umlToolWindowPlugin.setBirdViewFrame(new BirdViewFrame((Frame)win));
     }
     else if (win instanceof Dialog) {

       this.umlToolWindowPlugin.setBirdViewFrame(new BirdViewFrame((Dialog)win));
     }
   }
 }


