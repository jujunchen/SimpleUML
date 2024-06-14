 package net.trustx.simpleuml.packagediagram.actions;

 import java.awt.Dimension;
 import java.awt.Point;
 import java.util.HashMap;
 import net.trustx.simpleuml.components.ActionContributorCommand;
 import net.trustx.simpleuml.gef.components.DiagramPane;
 import net.trustx.simpleuml.gef.components.FigureComponent;
 import net.trustx.simpleuml.gef.components.FigureComponentDragHandler;
 import net.trustx.simpleuml.gef.components.FigureComponentResizeHandler;
 import net.trustx.simpleuml.gef.components.FigureComponentSelectionHandler;
 import net.trustx.simpleuml.gef.components.HTMLFigureComponent;
 import net.trustx.simpleuml.packagediagram.components.PackageDiagramComponent;




 public class CreateTextComponentCommand
   extends ActionContributorCommand
 {
   private PackageDiagramComponent packageDiagramComponent;

   public CreateTextComponentCommand(String text, PackageDiagramComponent packageDiagramComponent) {
     super(text);
     this.packageDiagramComponent = packageDiagramComponent;
   }



   public void executeCommand(HashMap commandProperties, boolean first, boolean last) {
     HTMLFigureComponent htmlFigureComponent = new HTMLFigureComponent((DiagramPane)this.packageDiagramComponent);

     htmlFigureComponent.getTitleLabel().setFont(this.packageDiagramComponent.getDiagramSettings().getDiagramTitleFont());
     htmlFigureComponent.getTextArea().setFont(this.packageDiagramComponent.getDiagramSettings().getDiagramFont());
     htmlFigureComponent.getTitleTextField().setFont(this.packageDiagramComponent.getDiagramSettings().getDiagramTitleFont());

     FigureComponentDragHandler dragHandler = new FigureComponentDragHandler((DiagramPane)this.packageDiagramComponent, this.packageDiagramComponent.getPackageDiagramComponentPanel().getScrollPane());
     dragHandler.install((FigureComponent)htmlFigureComponent);

     FigureComponentResizeHandler resizeHandler = new FigureComponentResizeHandler((DiagramPane)this.packageDiagramComponent);
     resizeHandler.install((FigureComponent)htmlFigureComponent);

     FigureComponentSelectionHandler selectionHandler = new FigureComponentSelectionHandler((DiagramPane)this.packageDiagramComponent);
     selectionHandler.install((FigureComponent)htmlFigureComponent);


     this.packageDiagramComponent.addFigureComponent((FigureComponent)htmlFigureComponent, true);
     if (commandProperties.get("ActionContributorCommandPopupHandler.Location") != null) {

       Point location = (Point)commandProperties.get("ActionContributorCommandPopupHandler.Location");
       htmlFigureComponent.setPosX(location.x);
       htmlFigureComponent.setPosY(location.y);
     }
     htmlFigureComponent.setPreferredSize(new Dimension(200, 100));

     htmlFigureComponent.setSelectionManager(this.packageDiagramComponent.getSelectionManager());
     htmlFigureComponent.setSelected(false);

     this.packageDiagramComponent.layoutContainer();
     this.packageDiagramComponent.revalidate();
   }



   public String getGroupName() {
     return "Diagram";
   }
 }


