 package net.trustx.simpleuml.classdiagram.actions;
 
 import java.awt.Dimension;
 import java.awt.Point;
 import java.awt.Rectangle;
 import java.util.HashMap;
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponent;
 import net.trustx.simpleuml.components.ActionContributorCommand;
 import net.trustx.simpleuml.gef.Selectable;
 import net.trustx.simpleuml.gef.SelectableCommand;
 import net.trustx.simpleuml.gef.components.DiagramPane;
 import net.trustx.simpleuml.gef.components.FigureComponent;
 import net.trustx.simpleuml.gef.components.FigureComponentDragHandler;
 import net.trustx.simpleuml.gef.components.FigureComponentResizeHandler;
 import net.trustx.simpleuml.gef.components.FigureComponentSelectionHandler;
 import net.trustx.simpleuml.gef.components.StickyFigureComponent;

 
 
 public class CreateStickyComponentCommand
   extends ActionContributorCommand
 {
   private ClassDiagramComponent classDiagramComponent;
   
   public CreateStickyComponentCommand(String text, ClassDiagramComponent classDiagramComponent) {
     super(text);
     this.classDiagramComponent = classDiagramComponent;
   }
 
 
   
   public void executeCommand(HashMap commandProperties, boolean first, boolean last) {
     CollectBoundsSelectableCommand collectBoundsCommand = new CollectBoundsSelectableCommand();
     this.classDiagramComponent.getSelectionManager().executeCommandOnSelection(collectBoundsCommand);
     
     StickyFigureComponent stickyComponent = new StickyFigureComponent((DiagramPane)this.classDiagramComponent);
     
     FigureComponentDragHandler dragHandler = new FigureComponentDragHandler((DiagramPane)this.classDiagramComponent, this.classDiagramComponent.getClassDiagramComponentPanel().getScrollPane());
     dragHandler.install((FigureComponent)stickyComponent);
     
     FigureComponentResizeHandler resizeHandler = new FigureComponentResizeHandler((DiagramPane)this.classDiagramComponent);
     resizeHandler.install((FigureComponent)stickyComponent);
     
     FigureComponentSelectionHandler selectionHandler = new FigureComponentSelectionHandler((DiagramPane)this.classDiagramComponent);
     selectionHandler.install((FigureComponent)stickyComponent);
     
     Rectangle r = collectBoundsCommand.getBounds();
     if (r != null) {
 
       
       r.grow(10, 10);
       this.classDiagramComponent.addFigureComponent((FigureComponent)stickyComponent, true);
       stickyComponent.setPosX(r.x);
       stickyComponent.setPosY(r.y);
       stickyComponent.setPreferredSize(new Dimension(r.width, r.height));
     }
     else {
       
       this.classDiagramComponent.addFigureComponent((FigureComponent)stickyComponent, true);
       if (commandProperties.get("ActionContributorCommandPopupHandler.Location") != null) {
         
         Point location = (Point)commandProperties.get("ActionContributorCommandPopupHandler.Location");
         stickyComponent.setPosX(location.x);
         stickyComponent.setPosY(location.y);
       } 
       stickyComponent.setPreferredSize(new Dimension(50, 50));
     } 
     
     stickyComponent.setSelectionManager(this.classDiagramComponent.getSelectionManager());
     stickyComponent.setSelected(false);
     
     this.classDiagramComponent.layoutContainer();
     this.classDiagramComponent.revalidate();
   }
 
 
   
   public String getGroupName() {
     return "Diagram";
   }
 
   
   private static class CollectBoundsSelectableCommand
     implements SelectableCommand
   {
     private Rectangle bounds;
 
     
     private CollectBoundsSelectableCommand() {}
 
     
     public void preExecution() {}
 
     
     public boolean executeCommand(Selectable selectable) {
       if (selectable instanceof FigureComponent) {
         
         FigureComponent figureComponent = (FigureComponent)selectable;
         if (this.bounds == null) {
           
           this.bounds = figureComponent.getBounds();
         }
         else {
           
           this.bounds = figureComponent.getBounds().union(this.bounds);
         } 
       } 
       return true;
     }
 
 
 
     
     public void postExecution() {}
 
 
     
     public Rectangle getBounds() {
       return this.bounds;
     }
   }
 }


