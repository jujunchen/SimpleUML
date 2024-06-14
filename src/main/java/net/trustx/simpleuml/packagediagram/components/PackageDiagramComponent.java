 package net.trustx.simpleuml.packagediagram.components;
 
 import com.intellij.openapi.project.Project;
 import com.intellij.openapi.ui.Messages;
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
 import java.util.Iterator;
 import java.util.LinkedHashMap;
 import java.util.LinkedList;
 import javax.swing.AbstractAction;
 import javax.swing.Action;
 import javax.swing.JComponent;
 import javax.swing.JViewport;
 import javax.swing.KeyStroke;
 import javax.swing.ToolTipManager;
 import javax.swing.event.ChangeEvent;
 import javax.swing.event.ChangeListener;
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
 import net.trustx.simpleuml.gef.selectablecommands.RemoveSelectedComponentsCommand;
 import net.trustx.simpleuml.packagediagram.actions.CreateStickyComponentCommand;
 import net.trustx.simpleuml.packagediagram.actions.CreateTextComponentCommand;
 import net.trustx.simpleuml.packagediagram.configuration.PackageDiagramSettings;
 import net.trustx.simpleuml.packagediagram.configuration.PackageDiagramSettingsListener;
 import net.trustx.simpleuml.packagediagram.selectablecommands.AddUpdateDependsCommand;
 import net.trustx.simpleuml.packagediagram.selectablecommands.HasAdjustingComponentsCommand;
 import net.trustx.simpleuml.packagediagram.selectablecommands.MoveAdjustingSelectableCommand;
 import net.trustx.simpleuml.packagediagram.selectablecommands.MoveSelectableCommand;
 import net.trustx.simpleuml.packagediagram.selectablecommands.RemoveDependsCommand;
 import net.trustx.simpleuml.plugin.DebugConsolePlugin;
 import net.trustx.simpleuml.plugin.UMLToolWindowPlugin;
 
 public class PackageDiagramComponent extends DiagramPane implements PackageDiagramSettingsListener, ComponentSettingsListener {
   private static final int MOVE_AMOUNT = 50;
   private PackageDiagramComponentPanel packageDiagramComponentPanel;
   private Project project;
   private LinkedList changeListenerList;
   private String tooltipText;
   private PackageDiagramSettings diagramSettings;
   private PackageDiagramComponentSettings componentSettings;
   private HashMap actionContributorCommandMap;
   private CoalesceThread coalesceThread;
   
   public PackageDiagramComponentSettings getComponentSettings() {
     return this.componentSettings;
   }
 
 
 
 
 
 
   
   public PackageDiagramComponent(PackageDiagramSettings ds, PackageDiagramComponentSettings componentSettings, Project project) {
     this.diagramSettings = ds;
     this.project = project;
     
     this.changeListenerList = new LinkedList();
     
     this.componentSettings = componentSettings;
     
     initActionContributorCommandMap();
 
 
     
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
             PackageDiagramComponent.this.requestFocusInWindow();
           }
         });
   }
 
 
 
   
   public void addFigureComponent(FigureComponent figureComponent, boolean update) {
     super.addFigureComponent(figureComponent, update);
     
     new ActionContributorCommandPopupHandler(this, figureComponent);
 
 
     
     if (figureComponent instanceof PsiPackageComponent) {
       
       PsiPackageComponent psiPackageComponent = (PsiPackageComponent)figureComponent;
       
       this.diagramSettings.addSettingsListener(psiPackageComponent);
 
       
       psiPackageComponent.setPackageDiagramComponent(this);
     } 
     Point point = this.packageDiagramComponentPanel.getJViewport().getViewPosition();
     figureComponent.setPosX(point.x);
     figureComponent.setPosY(point.y);
     
     if (update) {
       
       settingsChanged(this.diagramSettings);
       layoutContainer();
       getConnectorManager().validateConnectors();
       repaint();
       
       moveToFront((Component)figureComponent);
     } 
     
     changesMade();
   }
 
 
 
   
   private void initHoverSupport() {
     ToolTipManager.sharedInstance().registerComponent((JComponent)this);
     
     addMouseMotionListener(new MouseMotionAdapter()
         {
           public void mouseMoved(MouseEvent e)
           {
             Connector connector = PackageDiagramComponent.this.getConnectorManager().getConnectorForLocation(e.getX(), e.getY());
             if (connector != null) {
               
               String descrition = connector.getConnectorDecorator().getDescription();
               if (!"".equals(descrition) && connector.isPaintable() && connector.isVisible()) {
                 
                 PackageDiagramComponent.this.tooltipText = descrition;
                 return;
               } 
             } 
             PackageDiagramComponent.this.tooltipText = null;
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
                 PackageDiagramComponent.this.deselectAllComponents();
               }
             }
           }
         });
   }
 
 
   
   private void deselectAllComponents() {
     getSelectionManager().clear();
   }
 
 
   
   private void initKeyboardSupport() {
     Action showDebugConsoleAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           DebugConsolePlugin debugConsolePlugin = new DebugConsolePlugin(PackageDiagramComponent.this.project);
           debugConsolePlugin.projectOpened();
         }
       };
     getInputMap().put(KeyStroke.getKeyStroke(68, 704), "showDebugConsoleAction");
     getActionMap().put("showDebugConsoleAction", showDebugConsoleAction);
 
     
     Action printComponentsAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           PackageDiagramComponent.this.executeCommandOnFigureComponents(new FigureComponentCommand()
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
           PackageDiagramComponent.this.removeSelectedComponents();
         }
       };
     getInputMap().put(KeyStroke.getKeyStroke(127, 0), "removeSelectedComponentsAction");
     getActionMap().put("removeSelectedComponentsAction", removeSelectedComponentsAction);
 
     
     Action moveSelectedComponentsRightAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           PackageDiagramComponent.this.moveSelectedComponents(50, 0);
         }
       };
     getInputMap().put(KeyStroke.getKeyStroke(39, 64), "moveSelectedComponentsRightAction");
     getActionMap().put("moveSelectedComponentsRightAction", moveSelectedComponentsRightAction);
 
     
     Action moveViewRightAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           JViewport viewport = PackageDiagramComponent.this.packageDiagramComponentPanel.getJViewport();
           int x = (viewport.getViewPosition()).x;
           int y = (viewport.getViewPosition()).y;
           int maxAmount = PackageDiagramComponent.this.getWidth() - (viewport.getViewRect()).width;
           int amount = Math.min(maxAmount - x, 50);
           viewport.setViewPosition(new Point(x + amount, y));
         }
       };
     getInputMap().put(KeyStroke.getKeyStroke(39, 0), "moveViewRightAction");
     getActionMap().put("moveViewRightAction", moveViewRightAction);
 
     
     Action moveSelectedComponentsLeftAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           PackageDiagramComponent.this.moveSelectedComponents(-50, 0);
         }
       };
     getInputMap().put(KeyStroke.getKeyStroke(37, 64), "moveSelectedComponentsLeftAction");
     getActionMap().put("moveSelectedComponentsLeftAction", moveSelectedComponentsLeftAction);
 
     
     Action moveViewLeftAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           JViewport viewport = PackageDiagramComponent.this.packageDiagramComponentPanel.getJViewport();
           int x = (viewport.getViewPosition()).x;
           int y = (viewport.getViewPosition()).y;
           int amount = Math.min(x, 50);
           viewport.setViewPosition(new Point(x - amount, y));
         }
       };
     getInputMap().put(KeyStroke.getKeyStroke(37, 0), "moveViewLeftAction");
     getActionMap().put("moveViewLeftAction", moveViewLeftAction);
 
     
     Action moveSelectedComponentsUpAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           PackageDiagramComponent.this.moveSelectedComponents(0, -50);
         }
       };
     getInputMap().put(KeyStroke.getKeyStroke(38, 64), "moveSelectedComponentsUpAction");
     getActionMap().put("moveSelectedComponentsUpAction", moveSelectedComponentsUpAction);
 
     
     Action moveViewUpAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           JViewport viewport = PackageDiagramComponent.this.packageDiagramComponentPanel.getJViewport();
           int x = (viewport.getViewPosition()).x;
           int y = (viewport.getViewPosition()).y;
           int amount = Math.min(y, 50);
           viewport.setViewPosition(new Point(x, y - amount));
         }
       };
     getInputMap().put(KeyStroke.getKeyStroke(38, 0), "moveViewUpAction");
     getActionMap().put("moveViewUpAction", moveViewUpAction);
 
     
     Action moveSelectedComponentsDownAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           PackageDiagramComponent.this.moveSelectedComponents(0, 50);
         }
       };
     getInputMap().put(KeyStroke.getKeyStroke(40, 64), "moveSelectedComponentsDownAction");
     getActionMap().put("moveSelectedComponentsDownAction", moveSelectedComponentsDownAction);
 
     
     Action moveViewDownAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           JViewport viewport = PackageDiagramComponent.this.packageDiagramComponentPanel.getJViewport();
           int x = (viewport.getViewPosition()).x;
           int y = (viewport.getViewPosition()).y;
           int maxAmount = PackageDiagramComponent.this.getHeight() - (viewport.getViewRect()).height;
           int amount = Math.min(maxAmount - y, 50);
           viewport.setViewPosition(new Point(x, y + amount));
         }
       };
     getInputMap().put(KeyStroke.getKeyStroke(40, 0), "moveViewDownAction");
     getActionMap().put("moveViewDownAction", moveViewDownAction);
 
     
     Action selectAllComponentsAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           PackageDiagramComponent.this.selectAllComponents();
         }
       };
     getInputMap().put(KeyStroke.getKeyStroke(65, 128), "selectAllComponentsAction");
     getActionMap().put("selectAllComponentsAction", selectAllComponentsAction);
   }
 
 
   
   public void layoutContainer() {
     super.layoutContainer();
     changesMade();
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
 
 
 
   
   public void removeSelectedComponents() {
     getSelectionManager().executeCommandOnSelection((SelectableCommand)new RemoveSelectedComponentsCommand(this));
   }
 
 
   
   public void moveSelectedComponents(int x, int y) {
     getSelectionManager().executeCommandOnSelection((SelectableCommand)new MoveSelectableCommand(x, y, this));
   }
 
 
   
   protected void processKeyEvent(KeyEvent e) {
     super.processKeyEvent(e);
   }
 
 
   
   public boolean isFocusable() {
     return true;
   }
 
 
   
   public boolean isFocusCycleRoot() {
     return true;
   }
 
 
   
   public void setPackageDiagramComponentPanel(PackageDiagramComponentPanel packageDiagramComponentPanel) {
     this.packageDiagramComponentPanel = packageDiagramComponentPanel;
   }
 
 
 
 
 
   
   private void initActionContributorCommandMap() {
     this.actionContributorCommandMap = new LinkedHashMap<Object, Object>();
     
     this.actionContributorCommandMap.put(new ActionContributorCommandInfo("Create Sticky Component", new String[] { "Diagram" }), new CreateStickyComponentCommand("Create Sticky Component", this));
     this.actionContributorCommandMap.put(new ActionContributorCommandInfo("Create Text Component", new String[] { "Diagram" }), new CreateTextComponentCommand("Create Text Component", this));
     
     this.actionContributorCommandMap.put(new ActionContributorCommandInfo("Remove All", new String[] { "Diagram" }), new ActionContributorCommand("Remove All")
         {
           public void executeCommand(HashMap commandProperties, boolean first, boolean last)
           {
             int response = Messages.showYesNoDialog((Component)PackageDiagramComponent.this, "Remove All Figures in this Diagram?", "Warning", Messages.getWarningIcon());
             if (response == 0) {
               
               ArrayList al = new ArrayList(PackageDiagramComponent.this.getFigureComponents());
               for (Iterator<FigureComponent> iterator = al.iterator(); iterator.hasNext(); ) {
                 
                 FigureComponent figureComponent = iterator.next();
                 PackageDiagramComponent.this.removeFigureComponent(figureComponent);
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
 
 
   
   public void addUpdateDependsOfSelectedPackages() {
     getSelectionManager().executeCommandOnSelection((SelectableCommand)new AddUpdateDependsCommand(this));
   }
 
 
   
   public void addUpdateDepends(PsiPackageComponent psiPackageComponent) {
     AddUpdateDependsCommand command = new AddUpdateDependsCommand(this);
     SelectionManager.executeCommandOnSelectable((SelectableCommand)command, (Selectable)psiPackageComponent);
   }
 
 
 
 
 
   
   public synchronized void changesMade() {
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
 
 
 
   
   public void settingsChanged(PackageDiagramComponentSettings componentSettings) {}
 
 
   
   public void settingsChanged(final PackageDiagramSettings packageDiagramSettings) {
     setBackground(packageDiagramSettings.getDiagramBackgroundColor());
     
     getConnectorManager().executeCommandOnConnectors(new ConnectorCommand()
         {
           public void preExecution() {}
 
 
 
 
           
           public boolean executeCommand(Connector connector) {
             ConnectorDecorator connectorDecorator = connector.getConnectorDecorator();
             connectorDecorator.setFillColor(packageDiagramSettings.getDiagramBackgroundColor());
             connectorDecorator.setAntialiased(packageDiagramSettings.isUseAntialiasedConnectors());
             return true;
           }
 
 
 
 
 
           
           public void postExecution() {}
         });
     repaint();
     changesMade();
   }
 
 
   
   public PackageDiagramComponentPanel getPackageDiagramComponentPanel() {
     return this.packageDiagramComponentPanel;
   }
 
 
   
   public Project getProject() {
     return this.project;
   }
 
 
   
   public void setChangingComponent(PsiPackageComponent psiPackageComponent) {
     moveToFront((Component)psiPackageComponent);
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
 
 
   
   public int getMinimumComponentX() {
     int x = Integer.MAX_VALUE;
     Collection col = getFigureMap().values();
     for (Iterator<FigureComponent> iterator = col.iterator(); iterator.hasNext(); ) {
       
       FigureComponent figureComponent = iterator.next();
       x = (figureComponent.getX() < x) ? figureComponent.getX() : x;
     } 
     return x;
   }
 
 
   
   public int getMinimumComponentY() {
     int y = Integer.MAX_VALUE;
     Collection col = getFigureMap().values();
     for (Iterator<FigureComponent> iterator = col.iterator(); iterator.hasNext(); ) {
       
       FigureComponent figureComponent = iterator.next();
       y = (figureComponent.getY() < y) ? figureComponent.getY() : y;
     } 
     return y;
   }
 
 
   
   public int getMaximumComponentX() {
     int x = 1;
     Collection col = getFigureMap().values();
     for (Iterator<FigureComponent> iterator = col.iterator(); iterator.hasNext(); ) {
       
       FigureComponent figureComponent = iterator.next();
       x = (figureComponent.getX() + figureComponent.getWidth() > x) ? (figureComponent.getX() + figureComponent.getWidth()) : x;
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
       
       FigureComponent figureComponent = iterator.next();
       y = (figureComponent.getY() + figureComponent.getHeight() > y) ? (figureComponent.getY() + figureComponent.getHeight()) : y;
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
 
 
   
   public boolean containsPackage(String qualifiedName) {
     Figure containedPsiPackageComponent = (Figure)getFigureMap().get(qualifiedName);
     return (containedPsiPackageComponent != null);
   }
 
 
   
   public boolean containsHighlightPackage(String qualifiedName) {
     PsiPackageComponent containedPsiPackageComponent = (PsiPackageComponent)getFigureMap().get(qualifiedName);
     if (containedPsiPackageComponent != null) {
       
       displayHighlightBorder(containedPsiPackageComponent);
       return true;
     } 
     return false;
   }
 
 
   
   public PsiPackageComponent[] getPsiPackageComponents() {
     final LinkedList packageList = new LinkedList();
     
     executeCommandOnFigureComponents(new FigureComponentCommand()
         {
           public void preExecution() {}
 
 
 
 
           
           public boolean executeCommand(FigureComponent figureComponent) {
             if (figureComponent instanceof PsiPackageComponent)
             {
               packageList.add(figureComponent);
             }
             return true;
           }
 
 
 
 
           
           public void postExecution() {}
         });
     return (PsiPackageComponent[])packageList.toArray((Object[])new PsiPackageComponent[packageList.size()]);
   }
 
 
   
   private void displayHighlightBorder(final PsiPackageComponent psiPackageComponent) {
     (new Thread()
       {
         public void run()
         {
           EventQueue.invokeLater(new Runnable()
               {
                 public void run()
                 {
                   psiPackageComponent.setHighlighted(true);
                   PackageDiagramComponent.this.moveToFront((Component)psiPackageComponent);
                   PackageDiagramComponent.this.scrollRectToVisible(psiPackageComponent.getBounds());
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
                   psiPackageComponent.setHighlighted(false);
                 }
               });
         }
       }).start();
   }
 
 
   
   public PackageDiagramSettings getDiagramSettings() {
     return this.diagramSettings;
   }
 
 
   
   public PsiPackageComponent getPsiPackageComponent(String qualifiedName) {
     return (PsiPackageComponent)getFigureMap().get(qualifiedName);
   }
 
 
   
   public void removeDependsOfSelectedPackages() {
     getSelectionManager().executeCommandOnSelection((SelectableCommand)new RemoveDependsCommand(this));
   }
 
 
   
   public void removeDepends(PsiPackageComponent psiPackageComponent) {
     RemoveDependsCommand removeDependsCommand = new RemoveDependsCommand(this);
     SelectionManager.executeCommandOnSelectable((SelectableCommand)removeDependsCommand, (Selectable)psiPackageComponent);
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
           UMLToolWindowPlugin umlToolWindowPlugin = UMLToolWindowPlugin.getUMLToolWindowPlugin(PackageDiagramComponent.this.project);
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
               if (PackageDiagramComponent.this.isShowing())
               {
                 PackageDiagramComponent.this.notifyChangeListeners();
               }
             }
           });
     }
   }
 }


