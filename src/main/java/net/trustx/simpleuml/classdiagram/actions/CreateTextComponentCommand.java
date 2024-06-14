 package net.trustx.simpleuml.classdiagram.actions;

 import java.awt.Dimension;
 import java.awt.Point;
 import java.util.HashMap;
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponent;
 import net.trustx.simpleuml.components.ActionContributorCommand;
 import net.trustx.simpleuml.gef.components.DiagramPane;
 import net.trustx.simpleuml.gef.components.FigureComponent;
 import net.trustx.simpleuml.gef.components.FigureComponentDragHandler;
 import net.trustx.simpleuml.gef.components.FigureComponentResizeHandler;
 import net.trustx.simpleuml.gef.components.FigureComponentSelectionHandler;
 import net.trustx.simpleuml.gef.components.HTMLFigureComponent;




 public class CreateTextComponentCommand
   extends ActionContributorCommand
 {
   private ClassDiagramComponent classDiagramComponent;

   public CreateTextComponentCommand(String text, ClassDiagramComponent classDiagramComponent) {
     super(text);
     this.classDiagramComponent = classDiagramComponent;
   }



   public void executeCommand(HashMap commandProperties, boolean first, boolean last) {
     HTMLFigureComponent htmlFigureComponent = new HTMLFigureComponent((DiagramPane)this.classDiagramComponent);

     htmlFigureComponent.getTitleLabel().setFont(this.classDiagramComponent.getDiagramSettings().getDiagramTitleFont());
     htmlFigureComponent.getTextArea().setFont(this.classDiagramComponent.getDiagramSettings().getDiagramFont());
     htmlFigureComponent.getTitleTextField().setFont(this.classDiagramComponent.getDiagramSettings().getDiagramTitleFont());

     FigureComponentDragHandler dragHandler = new FigureComponentDragHandler((DiagramPane)this.classDiagramComponent, this.classDiagramComponent.getClassDiagramComponentPanel().getScrollPane());
     dragHandler.install((FigureComponent)htmlFigureComponent);

     FigureComponentResizeHandler resizeHandler = new FigureComponentResizeHandler((DiagramPane)this.classDiagramComponent);
     resizeHandler.install((FigureComponent)htmlFigureComponent);

     FigureComponentSelectionHandler selectionHandler = new FigureComponentSelectionHandler((DiagramPane)this.classDiagramComponent);
     selectionHandler.install((FigureComponent)htmlFigureComponent);


     this.classDiagramComponent.addFigureComponent((FigureComponent)htmlFigureComponent, true);
     if (commandProperties.get("ActionContributorCommandPopupHandler.Location") != null) {

       Point location = (Point)commandProperties.get("ActionContributorCommandPopupHandler.Location");
       htmlFigureComponent.setPosX(location.x);
       htmlFigureComponent.setPosY(location.y);
     }
     htmlFigureComponent.setPreferredSize(new Dimension(200, 100));

     htmlFigureComponent.setSelectionManager(this.classDiagramComponent.getSelectionManager());
     htmlFigureComponent.setSelected(false);

     this.classDiagramComponent.layoutContainer();
     this.classDiagramComponent.revalidate();
   }



   public String getGroupName() {
     return "Diagram";
   }
 }


