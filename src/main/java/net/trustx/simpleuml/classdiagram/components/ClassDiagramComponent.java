 package net.trustx.simpleuml.classdiagram.components;

 import com.intellij.openapi.project.Project;
 import com.intellij.openapi.ui.Messages;
 import com.intellij.psi.PsiClass;
 import java.awt.Component;
 import java.awt.EventQueue;
 import java.awt.Point;
 import java.awt.Rectangle;
 import java.awt.event.ActionEvent;
 import java.awt.event.FocusAdapter;
 import java.awt.event.FocusEvent;
 import java.awt.event.KeyEvent;
 import java.awt.event.MouseAdapter;
 import java.awt.event.MouseEvent;
 import java.awt.event.MouseMotionAdapter;
 import java.util.ArrayList;
 import java.util.Collection;
 import java.util.HashMap;
 import java.util.HashSet;
 import java.util.Iterator;
 import java.util.LinkedHashMap;
 import java.util.LinkedList;
 import java.util.Set;
 import javax.swing.AbstractAction;
 import javax.swing.Action;
 import javax.swing.JComponent;
 import javax.swing.JViewport;
 import javax.swing.KeyStroke;
 import javax.swing.ToolTipManager;
 import javax.swing.event.ChangeEvent;
 import javax.swing.event.ChangeListener;
 import net.trustx.simpleuml.classdiagram.actions.CreateStickyComponentCommand;
 import net.trustx.simpleuml.classdiagram.actions.CreateTextComponentCommand;
 import net.trustx.simpleuml.classdiagram.configuration.ClassDiagramSettings;
 import net.trustx.simpleuml.classdiagram.configuration.ClassDiagramSettingsListener;
 import net.trustx.simpleuml.classdiagram.layout.DiagramLayouter;
 import net.trustx.simpleuml.classdiagram.selectablecommands.AddUpdateDependsCommand;
 import net.trustx.simpleuml.classdiagram.selectablecommands.HasAdjustingComponentsCommand;
 import net.trustx.simpleuml.classdiagram.selectablecommands.MoveAdjustingSelectableCommand;
 import net.trustx.simpleuml.classdiagram.selectablecommands.MoveSelectableCommand;
 import net.trustx.simpleuml.classdiagram.selectablecommands.PostMoveSelectableCommand;
 import net.trustx.simpleuml.classdiagram.selectablecommands.PreMoveSelectableCommand;
 import net.trustx.simpleuml.classdiagram.selectablecommands.ReloadSelectedComponentsCommand;
 import net.trustx.simpleuml.classdiagram.selectablecommands.RemoveDependsCommand;
 import net.trustx.simpleuml.components.ActionContributorCommand;
 import net.trustx.simpleuml.components.ActionContributorCommandInfo;
 import net.trustx.simpleuml.gef.Figure;
 import net.trustx.simpleuml.gef.Selectable;
 import net.trustx.simpleuml.gef.SelectableCommand;
 import net.trustx.simpleuml.gef.SelectionManager;
 import net.trustx.simpleuml.gef.components.ActionContributorCommandPopupHandler;
 import net.trustx.simpleuml.gef.components.DiagramPane;
 import net.trustx.simpleuml.gef.components.FigureComponent;
 import net.trustx.simpleuml.gef.components.FigureComponentCommand;
 import net.trustx.simpleuml.gef.connector.Connector;
 import net.trustx.simpleuml.gef.connector.ConnectorCommand;
 import net.trustx.simpleuml.gef.connector.ConnectorDecorator;
 import net.trustx.simpleuml.gef.connector.ConnectorDecoratorSettings;
 import net.trustx.simpleuml.gef.graph.Vertex;
 import net.trustx.simpleuml.gef.selectablecommands.PinFigureCommand;
 import net.trustx.simpleuml.gef.selectablecommands.RemoveSelectedComponentsCommand;
 import net.trustx.simpleuml.gef.selectablecommands.UnpinFigureCommand;
 import net.trustx.simpleuml.plugin.DebugConsolePlugin;
 import net.trustx.simpleuml.plugin.UMLToolWindowPlugin;

 public class ClassDiagramComponent
   extends DiagramPane
   implements ClassDiagramSettingsListener, ComponentSettingsListener {
   private static final int MOVE_AMOUNT_BIG = 100;
   private static final int MOVE_AMOUNT_MEDIUM = 10;
   private static final int MOVE_AMOUNT_SMALL = 1;
   private ClassDiagramComponentPanel classDiagramComponentPanel;
   private ClassDiagramSettings diagramSettings;
   private Project project;
   private ClassDiagramComponentSettings componentSettings;
   private LinkedList changeListenerList;
   private Set connectorsToRecycle;
   private String tooltipText;
   private ConnectorUpdater connectorUpdater;
   private HashMap actionContributorCommandMap;
   private CoalesceThread coalesceThread;

   public ClassDiagramComponent(ClassDiagramSettings ds, ClassDiagramComponentSettings componentSettings, Project project) {
     this.diagramSettings = ds;
     this.project = project;

     this.changeListenerList = new LinkedList();
     this.connectorsToRecycle = new HashSet();
     this.componentSettings = componentSettings;

     initActionContributorCommandMap();

     this.connectorUpdater = new ConnectorUpdater(this);
     addDiagramPaneListener(this.connectorUpdater);

     componentSettings.addSettingsListener(this);
     this.diagramSettings.addSettingsListener(this);

     setOpaque(true);
     setBackground(ds.getDiagramBackgroundColor());



     initSelectionSupport();
     initKeyboardSupport();
     initHoverSupport();

     layoutContainer();

     enableEvents(2056L);

     addFocusListener(new FocusAdapter()
         {
           public void focusGained(FocusEvent e) {}

           public void focusLost(FocusEvent e) {}
         });
     addMouseListener(new MouseAdapter()
         {
           public void mouseClicked(MouseEvent e)
           {
             ClassDiagramComponent.this.requestFocusInWindow();
           }
         });

     componentSettings.setPaintUses(false);
   }



   public ConnectorUpdater getConnectorUpdater() {
     return this.connectorUpdater;
   }



   private void initHoverSupport() {
     ToolTipManager.sharedInstance().registerComponent((JComponent)this);

     addMouseMotionListener(new MouseMotionAdapter()
         {
           public void mouseMoved(MouseEvent e)
           {
             Connector connector = ClassDiagramComponent.this.getConnectorManager().getConnectorForLocation(e.getX(), e.getY());
             if (connector != null) {

               String descrition = connector.getConnectorDecorator().getDescription();
               if (!"".equals(descrition) && connector.isPaintable() && connector.isVisible()) {

                 ClassDiagramComponent.this.tooltipText = descrition;
                 return;
               }
             }
             ClassDiagramComponent.this.tooltipText = null;
           }
         });
   }



   public String getToolTipText(MouseEvent event) {
     return this.tooltipText;
   }



   private void initSelectionSupport() {
     addMouseListener(new MouseAdapter()
         {
           public void mouseClicked(MouseEvent e)
           {
             if (!e.isControlDown() && !e.isShiftDown())
             {
               if (e.getButton() == 1)
               {
                 ClassDiagramComponent.this.deselectAllComponents();
               }
             }
           }
         });
   }



   private void initKeyboardSupport() {
     Action showDebugConsoleAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           DebugConsolePlugin debugConsolePlugin = new DebugConsolePlugin(ClassDiagramComponent.this.project);
           debugConsolePlugin.projectOpened();
         }
       };
     getInputMap().put(KeyStroke.getKeyStroke(68, 704), "showDebugConsoleAction");
     getActionMap().put("showDebugConsoleAction", showDebugConsoleAction);


     Action printComponentsAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           ClassDiagramComponent.this.executeCommandOnFigureComponents(new FigureComponentCommand()
               {
                 public void preExecution() {}





                 public boolean executeCommand(FigureComponent figureComponent) {
                   System.out.println("component = " + figureComponent);
                   System.out.println("component.isAdjusting() = " + figureComponent.isAdjusting());
                   System.out.println("component.selected() = " + figureComponent.isSelected());
                   System.out.println("");
                   return true;
                 }





                 public void postExecution() {}
               });
         }
       };
     getInputMap().put(KeyStroke.getKeyStroke(79, 704), "printComponentsAction");
     getActionMap().put("printComponentsAction", printComponentsAction);


     Action removeSelectedComponentsAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           ClassDiagramComponent.this.removeSelectedComponents();
         }
       };
     getInputMap().put(KeyStroke.getKeyStroke(127, 0), "removeSelectedComponentsAction");
     getActionMap().put("removeSelectedComponentsAction", removeSelectedComponentsAction);


     Action reloadSelectedComponentsAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           ClassDiagramComponent.this.reloadSelectedComponents();
         }
       };
     getInputMap().put(KeyStroke.getKeyStroke(116, 0), "reloadSelectedComponentsAction");
     getActionMap().put("reloadSelectedComponentsAction", reloadSelectedComponentsAction);


     Action moveSelectedComponentsRightBigAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           ClassDiagramComponent.this.moveSelectedComponents(100, 0);
         }
       };
     getInputMap().put(KeyStroke.getKeyStroke(39, 64), "moveSelectedComponentsRightBigAction");
     getActionMap().put("moveSelectedComponentsRightBigAction", moveSelectedComponentsRightBigAction);

     Action moveSelectedComponentsRightMediumAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           ClassDiagramComponent.this.moveSelectedComponents(10, 0);
         }
       };
     getInputMap().put(KeyStroke.getKeyStroke(39, 128), "moveSelectedComponentsRightMediumAction");
     getActionMap().put("moveSelectedComponentsRightMediumAction", moveSelectedComponentsRightMediumAction);

     Action moveSelectedComponentsRightSmallAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           ClassDiagramComponent.this.moveSelectedComponents(1, 0);
         }
       };
     getInputMap().put(KeyStroke.getKeyStroke(39, 512), "moveSelectedComponentsRightSmallAction");
     getActionMap().put("moveSelectedComponentsRightSmallAction", moveSelectedComponentsRightSmallAction);

     Action moveViewRightAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           JViewport viewport = ClassDiagramComponent.this.classDiagramComponentPanel.getJViewport();
           int x = (viewport.getViewPosition()).x;
           int y = (viewport.getViewPosition()).y;
           int maxAmount = ClassDiagramComponent.this.getWidth() - (viewport.getViewRect()).width;
           int amount = Math.min(maxAmount - x, 100);
           viewport.setViewPosition(new Point(x + amount, y));
         }
       };
     getInputMap().put(KeyStroke.getKeyStroke(39, 0), "moveViewRightAction");
     getActionMap().put("moveViewRightAction", moveViewRightAction);


     Action moveSelectedComponentsLeftBigAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           ClassDiagramComponent.this.moveSelectedComponents(-100, 0);
         }
       };
     getInputMap().put(KeyStroke.getKeyStroke(37, 64), "moveSelectedComponentsLeftBigAction");
     getActionMap().put("moveSelectedComponentsLeftBigAction", moveSelectedComponentsLeftBigAction);

     Action moveSelectedComponentsLeftMediumAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           ClassDiagramComponent.this.moveSelectedComponents(-10, 0);
         }
       };
     getInputMap().put(KeyStroke.getKeyStroke(37, 128), "moveSelectedComponentsLeftMediumAction");
     getActionMap().put("moveSelectedComponentsLeftMediumAction", moveSelectedComponentsLeftMediumAction);

     Action moveSelectedComponentsLeftSmallAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           ClassDiagramComponent.this.moveSelectedComponents(-1, 0);
         }
       };
     getInputMap().put(KeyStroke.getKeyStroke(37, 512), "moveSelectedComponentsLeftSmallAction");
     getActionMap().put("moveSelectedComponentsLeftSmallAction", moveSelectedComponentsLeftSmallAction);


     Action moveViewLeftAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           JViewport viewport = ClassDiagramComponent.this.classDiagramComponentPanel.getJViewport();
           int x = (viewport.getViewPosition()).x;
           int y = (viewport.getViewPosition()).y;
           int amount = Math.min(x, 100);
           viewport.setViewPosition(new Point(x - amount, y));
         }
       };
     getInputMap().put(KeyStroke.getKeyStroke(37, 0), "moveViewLeftAction");
     getActionMap().put("moveViewLeftAction", moveViewLeftAction);


     Action moveSelectedComponentsUpBigAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           ClassDiagramComponent.this.moveSelectedComponents(0, -100);
         }
       };
     getInputMap().put(KeyStroke.getKeyStroke(38, 64), "moveSelectedComponentsUpBigAction");
     getActionMap().put("moveSelectedComponentsUpBigAction", moveSelectedComponentsUpBigAction);

     Action moveSelectedComponentsUpMediumAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           ClassDiagramComponent.this.moveSelectedComponents(0, -10);
         }
       };
     getInputMap().put(KeyStroke.getKeyStroke(38, 128), "moveSelectedComponentsUpMediumAction");
     getActionMap().put("moveSelectedComponentsUpMediumAction", moveSelectedComponentsUpMediumAction);

     Action moveSelectedComponentsUpSmallAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           ClassDiagramComponent.this.moveSelectedComponents(0, -1);
         }
       };
     getInputMap().put(KeyStroke.getKeyStroke(38, 512), "moveSelectedComponentsUpSmallAction");
     getActionMap().put("moveSelectedComponentsUpSmallAction", moveSelectedComponentsUpSmallAction);


     Action moveViewUpAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           JViewport viewport = ClassDiagramComponent.this.classDiagramComponentPanel.getJViewport();
           int x = (viewport.getViewPosition()).x;
           int y = (viewport.getViewPosition()).y;
           int amount = Math.min(y, 100);
           viewport.setViewPosition(new Point(x, y - amount));
         }
       };
     getInputMap().put(KeyStroke.getKeyStroke(38, 0), "moveViewUpAction");
     getActionMap().put("moveViewUpAction", moveViewUpAction);


     Action moveSelectedComponentsDownBigAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           ClassDiagramComponent.this.moveSelectedComponents(0, 100);
         }
       };
     getInputMap().put(KeyStroke.getKeyStroke(40, 64), "moveSelectedComponentsDownBigAction");
     getActionMap().put("moveSelectedComponentsDownBigAction", moveSelectedComponentsDownBigAction);

     Action moveSelectedComponentsDownMediumAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           ClassDiagramComponent.this.moveSelectedComponents(0, 10);
         }
       };
     getInputMap().put(KeyStroke.getKeyStroke(40, 128), "moveSelectedComponentsDownMediumAction");
     getActionMap().put("moveSelectedComponentsDownMediumAction", moveSelectedComponentsDownMediumAction);

     Action moveSelectedComponentsDownSmallAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           ClassDiagramComponent.this.moveSelectedComponents(0, 1);
         }
       };
     getInputMap().put(KeyStroke.getKeyStroke(40, 512), "moveSelectedComponentsDownSmallAction");
     getActionMap().put("moveSelectedComponentsDownSmallAction", moveSelectedComponentsDownSmallAction);


     Action moveViewDownAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           JViewport viewport = ClassDiagramComponent.this.classDiagramComponentPanel.getJViewport();
           int x = (viewport.getViewPosition()).x;
           int y = (viewport.getViewPosition()).y;
           int maxAmount = ClassDiagramComponent.this.getHeight() - (viewport.getViewRect()).height;
           int amount = Math.min(maxAmount - y, 100);
           viewport.setViewPosition(new Point(x, y + amount));
         }
       };
     getInputMap().put(KeyStroke.getKeyStroke(40, 0), "moveViewDownAction");
     getActionMap().put("moveViewDownAction", moveViewDownAction);


     Action selectAllComponentsAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           ClassDiagramComponent.this.selectAllComponents();
         }
       };
     getInputMap().put(KeyStroke.getKeyStroke(65, 128), "selectAllComponentsAction");
     getActionMap().put("selectAllComponentsAction", selectAllComponentsAction);


     Action layoutComponentsAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           ClassDiagramComponent.this.layoutComponents(true);
         }
       };
     getInputMap().put(KeyStroke.getKeyStroke(76, 128), "layoutComponentsAction");
     getActionMap().put("layoutComponentsAction", layoutComponentsAction);


     Action pinSelectedComponentsAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           ClassDiagramComponent.this.pinSlectedClasses();
         }
       };
     getInputMap().put(KeyStroke.getKeyStroke(80, 128), "pinSelectedComponentsAction");
     getActionMap().put("pinSelectedComponentsAction", pinSelectedComponentsAction);

     Action unpinSelectedComponentsAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           ClassDiagramComponent.this.unpinSlectedClasses();
         }
       };
     getInputMap().put(KeyStroke.getKeyStroke(85, 128), "unpinSelectedComponentsAction");
     getActionMap().put("unpinSelectedComponentsAction", unpinSelectedComponentsAction);
   }



   public void layoutContainer() {
     super.layoutContainer();
     changesMade(false);
   }



   public void layoutComponents(boolean interactive) {
     if (interactive) {

       int response = Messages.showYesNoDialog((Component)this, "This will destroy your current layout.\nDo you want to continue?", "Warning", Messages.getWarningIcon());
       if (response == 0)
       {
         DiagramLayouter layouter = new DiagramLayouter(this);
         LinkedList cyclePath = layouter.getCyclePath();
         if (cyclePath.size() == 0) {

           layouter.execute();
         }
         else {

           StringBuffer sb = new StringBuffer();
           sb.append("There is a cycle in your graph:\n");
           for (Iterator<Vertex> iterator = cyclePath.iterator(); iterator.hasNext(); ) {

             Vertex vertex = iterator.next();
             sb.append("    ");
             sb.append(vertex.toString());
             sb.append('\n');
           }
           Messages.showMessageDialog(sb.toString(), "Error", Messages.getErrorIcon());
         }
         changesMade(false);
       }

     } else {

       layoutContainer();
       DiagramLayouter layouter = new DiagramLayouter(this);
       LinkedList cyclePath = layouter.getCyclePath();
       if (cyclePath.size() == 0)
       {
         layouter.execute();
       }
     }
   }



   public void reloadSelectedComponents() {
     getSelectionManager().executeCommandOnSelection((SelectableCommand)new ReloadSelectedComponentsCommand());
   }



   public void removeSelectedComponents() {
     getSelectionManager().executeCommandOnSelection((SelectableCommand)new RemoveSelectedComponentsCommand(this));
   }



   private void selectAllComponents() {
     executeCommandOnFigureComponents(new FigureComponentCommand()
         {
           public void preExecution() {}





           public boolean executeCommand(FigureComponent figureComponent) {
             figureComponent.setSelected(true);
             return true;
           }




           public void postExecution() {}
         });
   }




   private void deselectAllComponents() {
     getSelectionManager().clear();
   }



   public void preMoveSelectedComponents() {
     getSelectionManager().executeCommandOnSelection((SelectableCommand)new PreMoveSelectableCommand());
   }



   public void moveSelectedComponents(int x, int y) {
     getSelectionManager().executeCommandOnSelection((SelectableCommand)new MoveSelectableCommand(x, y, this));
   }



   public boolean hasAdjustingComponents() {
     HasAdjustingComponentsCommand command = new HasAdjustingComponentsCommand();
     getSelectionManager().executeCommandOnSelection((SelectableCommand)command);
     return command.hasAdjustingComponents();
   }



   public void moveAdjustingComponents(int x, int y) {
     MoveAdjustingSelectableCommand command = new MoveAdjustingSelectableCommand(x, y, this);
     getSelectionManager().executeCommandOnSelection((SelectableCommand)command);
   }



   public void postMoveSelectedComponents() {
     getSelectionManager().executeCommandOnSelection((SelectableCommand)new PostMoveSelectableCommand());
   }



   public void addSelection(PsiClassComponent psiClassComponent) {
     getSelectionManager().add((Selectable)psiClassComponent);
   }



   public void reloadClasses(PsiClass[] classes) {
     for (int i = 0; i < classes.length; i++) {

       PsiClass aClass = classes[i];
       String qn = aClass.getQualifiedName();
       if (getFigureMap().containsKey(qn)) {

         PsiClassComponent psiClassComponent = (PsiClassComponent)getFigureMap().get(qn);
         psiClassComponent.reload();
       }
       PsiClass[] innerClasses = aClass.getInnerClasses();
       reloadClasses(innerClasses);
     }
     changesMade(false);
   }



   public Project getProject() {
     return this.project;
   }



   public void settingsChanged(final ClassDiagramSettings settings) {
     setBackground(settings.getDiagramBackgroundColor());

     getConnectorManager().executeCommandOnConnectors(new ConnectorCommand()
         {
           public void preExecution() {}





           public boolean executeCommand(Connector connector) {
             ConnectorDecorator connectorDecorator = connector.getConnectorDecorator();
             connectorDecorator.setFillColor(settings.getDiagramBackgroundColor());
             connectorDecorator.setAntialiased(settings.isUseAntialiasedConnectors());
             return true;
           }






           public void postExecution() {}
         });
     repaint();
     changesMade(true);
   }




   public void settingsChanged(final ClassDiagramComponentSettings componentSettings) {
     getConnectorManager().executeCommandOnConnectors(new ConnectorCommand()
         {
           public void preExecution() {}





           public boolean executeCommand(Connector connector) {
             if (connector.getConnectorDecorator().getType() == 2) {

               connector.setVisible(componentSettings.isPaintExtends());
             }
             else if (connector.getConnectorDecorator().getType() == 1) {

               connector.setVisible(componentSettings.isPaintImplements());
             }
             else if (connector.getConnectorDecorator().getType() == 4) {

               connector.setVisible(componentSettings.isPaintUses());
             }
             else if (connector.getConnectorDecorator().getType() == 8) {

               connector.setVisible(componentSettings.isPaintInner());
             }
             else if (connector.getConnectorDecorator().getType() == 16) {

               connector.setVisible(componentSettings.isPaintDepends());
             }
             else if (connector.getConnectorDecorator().getType() == 32) {

               connector.setVisible(componentSettings.isPaintContains());
             }
             return true;
           }





           public void postExecution() {}
         });
     repaint();
     changesMade(true);
   }



   public ClassDiagramSettings getDiagramSettings() {
     return this.diagramSettings;
   }



   public void disposeConnectors(FigureComponent figure, boolean clearConnectorsToRecycle) {
     Collection components = getConnectorManager().getConnectedComponents(figure);
     if (clearConnectorsToRecycle)
     {
       this.connectorsToRecycle.clear();
     }
     for (Iterator<PsiClassComponent> iterator = components.iterator(); iterator.hasNext(); ) {

       PsiClassComponent psiClassComponent = iterator.next();
       rebuildPsiClassComponentsWhenNeeded(psiClassComponent);
     }

     if (clearConnectorsToRecycle)
     {
       this.connectorsToRecycle.clear();
     }

     getConnectorManager().removeConnectors(figure);
   }



   public void temporarlyDisposeConnectors(FigureComponent figure) {
     this.connectorsToRecycle = getConnectorManager().getConnectorsForComponent(figure);
     disposeConnectors(figure, false);
   }



   private void rebuildPsiClassComponentsWhenNeeded(PsiClassComponent classComponent) {
     if (this.diagramSettings.getExtendsBehaviour() == 4 || this.diagramSettings.getImplementsBehaviour() == 4) {

       classComponent.rebuildComponent(false);
       changesMade(false);
     }
   }



   public void addFigureComponent(FigureComponent figureComponent, boolean update) {
     super.addFigureComponent(figureComponent, update);

     new ActionContributorCommandPopupHandler(this, figureComponent);

     this.connectorsToRecycle.clear();

     if (figureComponent instanceof PsiClassComponent) {

       PsiClassComponent psiClassComponent = (PsiClassComponent)figureComponent;

       this.diagramSettings.addSettingsListener(psiClassComponent);


       psiClassComponent.setClassDiagramComponent(this);
     }
     Point point = this.classDiagramComponentPanel.getJViewport().getViewPosition();
     figureComponent.setPosX(point.x);
     figureComponent.setPosY(point.y);

     if (update) {

       settingsChanged(this.diagramSettings);
       layoutContainer();
       getConnectorManager().validateConnectors();
       repaint();

       moveToFront((Component)figureComponent);
     }

     changesMade(update);
   }




   public void removeFigureComponent(FigureComponent figureComponent) {
     Collection relatedFigures = getConnectorManager().getConnectedComponents(figureComponent);
     super.removeFigureComponent(figureComponent);

     for (Iterator<FigureComponent> iterator = relatedFigures.iterator(); iterator.hasNext(); ) {

       FigureComponent figureComponent2 = iterator.next();

       if (figureComponent2 instanceof PsiClassComponent) {

         PsiClassComponent psiClassComponent = (PsiClassComponent)figureComponent2;
         rebuildPsiClassComponentsWhenNeeded(psiClassComponent);
       }
     }


     changesMade(true);
   }



   public PsiClassComponent getPsiClassComponent(String qualifiedName) {
     return (PsiClassComponent)getFigureMap().get(qualifiedName);
   }



   private void displayHighlightBorder(final PsiClassComponent strutureClassComponent) {
     (new Thread()
       {
         public void run()
         {
           EventQueue.invokeLater(new Runnable()
               {
                 public void run()
                 {
                   strutureClassComponent.setHighlighted(true);
                   ClassDiagramComponent.this.moveToFront((Component)strutureClassComponent);
                   ClassDiagramComponent.this.scrollRectToVisible(strutureClassComponent.getBounds());
                 }
               });

           try {
             Thread.sleep(2000L);
           }
           catch (InterruptedException e) {}


           EventQueue.invokeLater(new Runnable()
               {
                 public void run()
                 {
                   strutureClassComponent.setHighlighted(false);
                 }
               });
         }
       }).start();
   }



   public void setChangingComponent(PsiClassComponent draggingComponent) {
     moveToFront((Component)draggingComponent);
   }



   public PsiClassComponent[] getPsiClassComponents() {
     final LinkedList classesList = new LinkedList();

     executeCommandOnFigureComponents(new FigureComponentCommand()
         {
           public void preExecution() {}





           public boolean executeCommand(FigureComponent figureComponent) {
             if (figureComponent instanceof PsiClassComponent)
             {
               classesList.add(figureComponent);
             }
             return true;
           }





           public void postExecution() {}
         });
     return (PsiClassComponent[])classesList.toArray((Object[])new PsiClassComponent[classesList.size()]);
   }



   public boolean isFocusable() {
     return true;
   }



   public boolean isFocusCycleRoot() {
     return true;
   }



   public boolean containsHighlightClass(String qualifiedName) {
     PsiClassComponent containedPsiClassComponent = (PsiClassComponent)getFigureMap().get(qualifiedName);
     if (containedPsiClassComponent != null) {

       displayHighlightBorder(containedPsiClassComponent);
       return true;
     }
     return false;
   }



   public boolean containsClass(String qualifiedName) {
     Figure containedPsiClassComponent = (Figure)getFigureMap().get(qualifiedName);
     return (containedPsiClassComponent != null);
   }



   protected void processKeyEvent(KeyEvent e) {
     super.processKeyEvent(e);
   }



   public HashMap getPsiClassComponentMap() {
     HashMap<Object, Object> psiClassComponentMap = new HashMap<Object, Object>();
     Collection values = getFigureMap().values();
     for (Iterator<Figure> iterator = values.iterator(); iterator.hasNext(); ) {

       Figure figure = iterator.next();
       if (figure instanceof PsiClassComponent) {

         PsiClassComponent psiClassComponent = (PsiClassComponent)figure;
         psiClassComponentMap.put(psiClassComponent.getKey(), psiClassComponent);
       }
     }

     return psiClassComponentMap;
   }



   public void removeSelection(PsiClassComponent psiClassComponent) {
     getSelectionManager().remove((Selectable)psiClassComponent);
   }



   public void removeAllSelections() {
     deselectAllComponents();
   }



   public int getMinimumComponentX() {
     int x = Integer.MAX_VALUE;
     Collection col = getFigureMap().values();
     for (Iterator<FigureComponent> iterator = col.iterator(); iterator.hasNext(); ) {

       FigureComponent psiClassComponent = iterator.next();
       x = (psiClassComponent.getX() < x) ? psiClassComponent.getX() : x;
     }
     return x;
   }



   public int getMinimumComponentY() {
     int y = Integer.MAX_VALUE;
     Collection col = getFigureMap().values();
     for (Iterator<FigureComponent> iterator = col.iterator(); iterator.hasNext(); ) {

       FigureComponent psiClassComponent = iterator.next();
       y = (psiClassComponent.getY() < y) ? psiClassComponent.getY() : y;
     }
     return y;
   }



   public int getMaximumComponentX() {
     int x = 1;
     Collection col = getFigureMap().values();
     for (Iterator<FigureComponent> iterator = col.iterator(); iterator.hasNext(); ) {

       FigureComponent psiClassComponent = iterator.next();
       x = (psiClassComponent.getX() + psiClassComponent.getWidth() > x) ? (psiClassComponent.getX() + psiClassComponent.getWidth()) : x;
     }
     Iterator<Connector> iter = getConnectorManager().getConnectors().iterator();
     while (iter.hasNext()) {

       Connector connector = iter.next();
       if (connector.isVisible() && connector.isPaintable()) {

         Rectangle bounds = connector.getBounds();
         x = (bounds.x + bounds.width > x) ? (bounds.x + bounds.width) : x;
       }
     }
     return x;
   }



   public int getMaximumComponentY() {
     int y = 1;
     Collection col = getFigureMap().values();
     for (Iterator<FigureComponent> iterator = col.iterator(); iterator.hasNext(); ) {

       FigureComponent psiClassComponent = iterator.next();
       y = (psiClassComponent.getY() + psiClassComponent.getHeight() > y) ? (psiClassComponent.getY() + psiClassComponent.getHeight()) : y;
     }
     Iterator<Connector> iter = getConnectorManager().getConnectors().iterator();
     while (iter.hasNext()) {

       Connector connector = iter.next();
       if (connector.isVisible() && connector.isPaintable()) {

         Rectangle bounds = connector.getBounds();
         y = (bounds.y + bounds.height > y) ? (bounds.y + bounds.height) : y;
       }
     }
     return y;
   }



   public ClassDiagramComponentSettings getComponentSettings() {
     return this.componentSettings;
   }



   public void setVisible(boolean aFlag) {
     super.setVisible(aFlag);
     changesMade(true);
   }











   private void initActionContributorCommandMap() {
     this.actionContributorCommandMap = new LinkedHashMap<Object, Object>();
     this.actionContributorCommandMap.put(new ActionContributorCommandInfo("Create Sticky Component", new String[] { "Diagram" }), new CreateStickyComponentCommand("Create Sticky Component", this));
     this.actionContributorCommandMap.put(new ActionContributorCommandInfo("Create Text Component", new String[] { "Diagram" }), new CreateTextComponentCommand("Create Text Component", this));

     this.actionContributorCommandMap.put(new ActionContributorCommandInfo("Layout", new String[] { "Diagram" }), new ActionContributorCommand("Layout")
         {
           public void executeCommand(HashMap commandProperties, boolean first, boolean last)
           {
             ClassDiagramComponent.this.layoutComponents(true);
           }
         });

     this.actionContributorCommandMap.put(new ActionContributorCommandInfo("Remove All", new String[] { "Diagram" }), new ActionContributorCommand("Remove All")
         {
           public void executeCommand(HashMap commandProperties, boolean first, boolean last)
           {
             int response = Messages.showYesNoDialog((Component)ClassDiagramComponent.this, "Remove All Figures in this Diagram?", "Warning", Messages.getWarningIcon());
             if (response == 0) {

               ArrayList al = new ArrayList(ClassDiagramComponent.this.getFigureComponents());
               for (Iterator<FigureComponent> iterator = al.iterator(); iterator.hasNext(); ) {

                 FigureComponent figureComponent = iterator.next();
                 ClassDiagramComponent.this.removeFigureComponent(figureComponent);
               }
             }
           }
         });
   }



   public ActionContributorCommand getActionContributorCommand(ActionContributorCommandInfo info) {
     return (ActionContributorCommand)this.actionContributorCommandMap.get(info);
   }



   public ActionContributorCommandInfo[] getActionContributorCommandInfos() {
     return (ActionContributorCommandInfo[])this.actionContributorCommandMap.keySet().toArray((Object[])new ActionContributorCommandInfo[this.actionContributorCommandMap.keySet().size()]);
   }






   public synchronized void changesMade(boolean layout) {
     if (layout && this.componentSettings.isLayoutOnChanges())
     {
       layoutComponents(false);
     }

     if (this.coalesceThread == null || !this.coalesceThread.isAlive()) {

       this.coalesceThread = new CoalesceThread();
       this.coalesceThread.start();
     }
     else {

       this.coalesceThread.requestChange();
     }
   }



   private void notifyChangeListeners() {
     for (Iterator<ChangeListener> iterator = this.changeListenerList.iterator(); iterator.hasNext(); ) {

       ChangeListener changeListener = iterator.next();
       changeListener.stateChanged(new ChangeEvent(this));
     }
   }



   public void addChangeListener(ChangeListener changeListener) {
     this.changeListenerList.add(changeListener);
   }



   public void removeChangeListener(ChangeListener changeListener) {
     this.changeListenerList.remove(changeListener);
   }



   public void pinSlectedClasses() {
     getSelectionManager().executeCommandOnSelection((SelectableCommand)new PinFigureCommand());
   }



   public void unpinSlectedClasses() {
     getSelectionManager().executeCommandOnSelection((SelectableCommand)new UnpinFigureCommand());
   }



   public ClassDiagramComponentPanel getClassDiagramComponentPanel() {
     return this.classDiagramComponentPanel;
   }



   public void setClassDiagramComponentPanel(ClassDiagramComponentPanel classDiagramComponentPanel) {
     this.classDiagramComponentPanel = classDiagramComponentPanel;
   }



   public void addUpdateDependsOfSelectedClasses() {
     getSelectionManager().executeCommandOnSelection((SelectableCommand)new AddUpdateDependsCommand(this));
   }



   public void addUpdateDepends(PsiClassComponent psiClassComponent) {
     AddUpdateDependsCommand command = new AddUpdateDependsCommand(this);
     SelectionManager.executeCommandOnSelectable((SelectableCommand)command, (Selectable)psiClassComponent);
   }



   public void removeDependsOfSelectedClasses() {
     getSelectionManager().executeCommandOnSelection((SelectableCommand)new RemoveDependsCommand(this));
   }



   public void removeDepends(PsiClassComponent psiClassComponent) {
     RemoveDependsCommand removeDependsCommand = new RemoveDependsCommand(this);
     SelectionManager.executeCommandOnSelectable((SelectableCommand)removeDependsCommand, (Selectable)psiClassComponent);
   }



   public void moveComponent(PsiClassComponent component, int x, int y) {
     MoveSelectableCommand command = new MoveSelectableCommand(x, y, this);
     SelectionManager.executeCommandOnSelectable((SelectableCommand)command, (Selectable)component);
   }



   public ConnectorDecoratorSettings getDefaultDecoratorSettings() {
     return new ConnectorDecoratorSettings(this.diagramSettings.isUseAntialiasedConnectors(), this.diagramSettings.getDiagramFont(), this.diagramSettings.getDiagramBackgroundColor());
   }



   public void reloadAllPsiComponents() {
     PsiClassComponent[] psiClassComponents = getPsiClassComponents();
     for (int i = 0; i < psiClassComponents.length; i++) {

       PsiClassComponent psiClassComponent = psiClassComponents[i];
       psiClassComponent.reload();
     }
   }







   private class CoalesceThread
     extends Thread
   {
     private transient int sleepCount = 1;




     public synchronized void requestChange() {
       this.sleepCount++;
     }



     public synchronized void process() {
       this.sleepCount = 0;
     }



     public void run() {
       while (this.sleepCount > 0) {

         process();

         try {
           UMLToolWindowPlugin umlToolWindowPlugin = UMLToolWindowPlugin.getUMLToolWindowPlugin(ClassDiagramComponent.this.project);
           if (umlToolWindowPlugin == null) {
             return;
           }

           long birdViewUpdateDelay = umlToolWindowPlugin.getConfigurationFactory().getGeneralDiagramSettings().getBirdViewUpdateDelay();
           Thread.sleep(birdViewUpdateDelay);
         }
         catch (InterruptedException e) {

           e.printStackTrace();
         }
       }
       EventQueue.invokeLater(new Runnable()
           {
             public void run()
             {
               if (ClassDiagramComponent.this.isShowing())
               {
                 ClassDiagramComponent.this.notifyChangeListeners();
               }
             }
           });
     }
   }
 }


