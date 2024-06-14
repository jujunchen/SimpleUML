 package net.trustx.simpleuml.gef.components;

 import java.awt.event.MouseEvent;
 import java.awt.event.MouseListener;




 public class FigureComponentSelectionHandler
   implements MouseListener
 {
   private DiagramPane diagramPane;

   public FigureComponentSelectionHandler(DiagramPane diagramPane) {
     this.diagramPane = diagramPane;
   }



   public void install(FigureComponent figureComponent) {
     figureComponent.addMouseListener(this);
   }



   public void mouseClicked(MouseEvent e) {
     FigureComponent figureComponent = (FigureComponent)e.getComponent();

     figureComponent.requestFocusInWindow();
     if (e.isControlDown() && e.getButton() == 1 && !figureComponent.isSelected()) {

       figureComponent.setSelected(true);
     }
     else if (e.isControlDown() && e.getButton() == 1 && figureComponent.isSelected()) {

       figureComponent.setSelected(false);
     }
     else if (e.getButton() == 1) {

       this.diagramPane.getSelectionManager().clear();
       figureComponent.setSelected(true);
     }
   }



   public void mousePressed(MouseEvent e) {
     FigureComponent figureComponent = (FigureComponent)e.getComponent();
     figureComponent.requestFocusInWindow();
   }

   public void mouseReleased(MouseEvent e) {}

   public void mouseEntered(MouseEvent e) {}

   public void mouseExited(MouseEvent e) {}
 }


