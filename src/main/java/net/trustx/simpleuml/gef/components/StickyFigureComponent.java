 package net.trustx.simpleuml.gef.components;
 
 import java.awt.Color;
 import java.awt.Component;
 import java.awt.Dimension;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.LinkedHashMap;
 import java.util.LinkedList;
 import javax.swing.border.Border;
 import net.trustx.simpleuml.components.ActionContributorCommand;
 import net.trustx.simpleuml.components.ActionContributorCommandInfo;
 import net.trustx.simpleuml.components.border.LineBorder2D;
 import net.trustx.simpleuml.components.border.SelectionBorder;
 
 
 

 
 
 public class StickyFigureComponent
   extends FigureComponent
 {
   private LinkedList figureList;
   private boolean sticky;
   private HashMap actionContributorCommandMap;
   
   public StickyFigureComponent(DiagramPane diagramPane) {
     super(diagramPane);
     setMinimumSize(new Dimension(10, 10));
     setPreferredLayer(-100);
     this.sticky = true;
     
     initActionContributorCommandMap();
   }
 
 
   
   public void setColor(Color color) {
     if (color == null) {
       
       super.setColor(Color.BLACK);
     }
     else {
       
       super.setColor(color);
     } 
     updateBorder();
   }
 
 
   
   public LinkedList getFigureList() {
     return this.figureList;
   }
 
 
   
   public void setFigureList(LinkedList figureList) {
     this.figureList = figureList;
   }
 
 
   
   public boolean isSticky() {
     return this.sticky;
   }
 
 
   
   public void setSticky(boolean sticky) {
     this.sticky = sticky;
   }
 
 
   
   public void prepareMoveFigureComponent() {
     this.figureList = new LinkedList();
     int size = getDiagramPane().getComponentCount();
     for (int i = 0; i < size; i++) {
       
       Component comp = getDiagramPane().getComponent(i);
       if (comp == this) {
         break;
       }
       
       if (comp instanceof FigureComponent) {
         
         FigureComponent figureComponent = (FigureComponent)comp;
         if (figureComponent.getBounds().intersects(getBounds()))
         {
           this.figureList.add(figureComponent);
         }
       } 
     } 
   }
 
 
   
   public void moveFigureComponent(final int dx, final int dy) {
     executeCommandOnFigureComponents(new FigureComponentCommand()
         {
           public void preExecution() {}
 
 
 
 
           
           public boolean executeCommand(FigureComponent figureComponent) {
             if (StickyFigureComponent.this.sticky && !figureComponent.isSelected()) {
               
               figureComponent.moveFigureComponent(dx, dy);
               StickyFigureComponent.this.getDiagramPane().layoutContainer();
               StickyFigureComponent.this.getDiagramPane().getConnectorManager().validateConnectors(figureComponent);
             } 
             
             return true;
           }
 
 
 
           
           public void postExecution() {}
         });
     super.moveFigureComponent(dx, dy);
   }
 
 
   
   public void finishedMoveFigureComponent() {
     if (this.figureList != null)
     {
       this.figureList.clear();
     }
   }
 
 
   
   public boolean executeCommandOnFigureComponents(FigureComponentCommand command) {
     command.preExecution();
     
     for (Iterator<FigureComponent> iterator = this.figureList.iterator(); iterator.hasNext(); ) {
       
       FigureComponent figureComponent = iterator.next();
       boolean success = command.executeCommand(figureComponent);
       if (!success) {
         
         command.postExecution();
         return false;
       } 
     } 
     
     command.postExecution();
     return true;
   }
 
 
   
   private void initActionContributorCommandMap() {
     this.actionContributorCommandMap = new LinkedHashMap<Object, Object>();
     this.actionContributorCommandMap.put(new ActionContributorCommandInfo("Remove", new String[] { "Figure" }), new RemoveFigureComponentCommand("Remove", this));
     this.actionContributorCommandMap.put(new ActionContributorCommandInfo("Unsticky", new String[] { "Figure" }), new StickyFigureCommandUnsticky("Unsticky", this));
     this.actionContributorCommandMap.put(new ActionContributorCommandInfo("Sticky", new String[] { "Figure" }), new StickyFigureCommandSticky("Sticky", this));
     this.actionContributorCommandMap.put(new ActionContributorCommandInfo("Change Color", new String[] { "Figure" }), new ChangeColorCommand("Change Color", this));
   }
 
 
   
   public ActionContributorCommand getActionContributorCommand(ActionContributorCommandInfo info) {
     return (ActionContributorCommand)this.actionContributorCommandMap.get(info);
   }
 
 
   
   public ActionContributorCommandInfo[] getActionContributorCommandInfos() {
     if (isSticky())
     {
       return new ActionContributorCommandInfo[] { new ActionContributorCommandInfo("Remove", new String[] { "Figure" }), new ActionContributorCommandInfo("Unsticky", new String[] { "Figure" }), new ActionContributorCommandInfo("Change Color", new String[] { "Figure" }) };
     }
 
 
 
 
     
     return new ActionContributorCommandInfo[] { new ActionContributorCommandInfo("Remove", new String[] { "Figure" }), new ActionContributorCommandInfo("Sticky", new String[] { "Figure" }), new ActionContributorCommandInfo("Change Color", new String[] { "Figure" }) };
   }
 
 
 
 
 
 
   
   public void updateBorder() {
     if (isSelected()) {
       
       setBorder((Border)new SelectionBorder((Border)new LineBorder2D(getColor(), 1)));
     }
     else {
       
       setBorder((Border)new LineBorder2D(getColor(), 1));
     } 
     revalidate();
   }
 }


