 package net.trustx.simpleuml.dependencydiagram.components;
 
 import com.intellij.openapi.project.Project;
 import java.awt.EventQueue;
 import java.awt.Rectangle;
 import java.awt.event.ActionEvent;
 import java.awt.event.FocusAdapter;
 import java.awt.event.FocusEvent;
 import java.awt.event.KeyEvent;
 import java.awt.event.MouseAdapter;
 import java.awt.event.MouseEvent;
 import java.awt.event.MouseMotionAdapter;
 import java.util.Collection;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.LinkedHashMap;
 import java.util.LinkedList;
 import javax.swing.AbstractAction;
 import javax.swing.Action;
 import javax.swing.JComponent;
 import javax.swing.KeyStroke;
 import javax.swing.ToolTipManager;
 import javax.swing.event.ChangeEvent;
 import javax.swing.event.ChangeListener;
 import net.trustx.simpleuml.components.ActionContributorCommand;
 import net.trustx.simpleuml.components.ActionContributorCommandInfo;
 import net.trustx.simpleuml.dependencydiagram.configuration.DependencyDiagramSettings;
 import net.trustx.simpleuml.dependencydiagram.configuration.DependencyDiagramSettingsListener;
 import net.trustx.simpleuml.gef.Figure;
 import net.trustx.simpleuml.gef.components.ActionContributorCommandPopupHandler;
 import net.trustx.simpleuml.gef.components.DiagramPane;
 import net.trustx.simpleuml.gef.components.FigureComponent;
 import net.trustx.simpleuml.gef.components.FigureComponentCommand;
 import net.trustx.simpleuml.gef.connector.Connector;
 import net.trustx.simpleuml.gef.connector.ConnectorCommand;
 import net.trustx.simpleuml.gef.connector.ConnectorDecorator;
 import net.trustx.simpleuml.plugin.DebugConsolePlugin;
 import net.trustx.simpleuml.plugin.UMLToolWindowPlugin;
 

 
 public class DependencyDiagramComponent
   extends DiagramPane
   implements DependencyDiagramSettingsListener, ComponentSettingsListener
 {
   private DependencyDiagramComponentPanel dependencyDiagramComponentPanel;
   private Project project;
   private LinkedList changeListenerList;
   private String tooltipText;
   private DependencyDiagramSettings diagramSettings;
   private DependencyDiagramComponentSettings componentSettings;
   private HashMap actionContributorCommandMap;
   private CoalesceThread coalesceThread;
   
   public DependencyDiagramComponentSettings getComponentSettings() {
     return this.componentSettings;
   }
 
 
 
 
 
 
   
   public DependencyDiagramComponent(DependencyDiagramSettings ds, DependencyDiagramComponentSettings componentSettings, Project project) {
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
             DependencyDiagramComponent.this.requestFocusInWindow();
           }
         });
   }
 
 
 
   
   public void addFigureComponent(FigureComponent figureComponent, boolean update) {
     super.addFigureComponent(figureComponent, update);
     
     new ActionContributorCommandPopupHandler(this, figureComponent);
     changesMade();
   }
 
 
   
   private void initHoverSupport() {
     ToolTipManager.sharedInstance().registerComponent((JComponent)this);
     
     addMouseMotionListener(new MouseMotionAdapter()
         {
           public void mouseMoved(MouseEvent e)
           {
             Connector connector = DependencyDiagramComponent.this.getConnectorManager().getConnectorForLocation(e.getX(), e.getY());
             if (connector != null) {
               
               String descrition = connector.getConnectorDecorator().getDescription();
               if (!"".equals(descrition) && connector.isPaintable() && connector.isVisible()) {
                 
                 DependencyDiagramComponent.this.tooltipText = descrition;
                 return;
               } 
             } 
             DependencyDiagramComponent.this.tooltipText = null;
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
                 DependencyDiagramComponent.this.deselectAllComponents();
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
           DebugConsolePlugin debugConsolePlugin = new DebugConsolePlugin(DependencyDiagramComponent.this.project);
           debugConsolePlugin.projectOpened();
         }
       };
     getInputMap().put(KeyStroke.getKeyStroke(68, 704), "showDebugConsoleAction");
     getActionMap().put("showDebugConsoleAction", showDebugConsoleAction);
 
     
     Action printComponentsAction = new AbstractAction()
       {
         public void actionPerformed(ActionEvent e)
         {
           DependencyDiagramComponent.this.executeCommandOnFigureComponents(new FigureComponentCommand()
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
   }
 
 
   
   public void layoutContainer() {
     super.layoutContainer();
     changesMade();
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
 
 
   
   public void setDependencyDiagramComponentPanel(DependencyDiagramComponentPanel dependencyDiagramComponentPanel) {
     this.dependencyDiagramComponentPanel = dependencyDiagramComponentPanel;
   }
 
 
 
 
 
   
   private void initActionContributorCommandMap() {
     this.actionContributorCommandMap = new LinkedHashMap<Object, Object>();
   }
 
 
   
   public ActionContributorCommand getActionContributorCommand(ActionContributorCommandInfo info) {
     return (ActionContributorCommand)this.actionContributorCommandMap.get(info);
   }
 
 
   
   public ActionContributorCommandInfo[] getActionContributorCommandInfos() {
     return (ActionContributorCommandInfo[])this.actionContributorCommandMap.keySet().toArray((Object[])new ActionContributorCommandInfo[this.actionContributorCommandMap.keySet().size()]);
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
 
 
 
   
   public void settingsChanged(DependencyDiagramComponentSettings componentSettings) {}
 
 
   
   public void settingsChanged(final DependencyDiagramSettings dependencyDiagramSettings) {
     setBackground(dependencyDiagramSettings.getDiagramBackgroundColor());
     
     getConnectorManager().executeCommandOnConnectors(new ConnectorCommand()
         {
           public void preExecution() {}
 
 
 
 
           
           public boolean executeCommand(Connector connector) {
             ConnectorDecorator connectorDecorator = connector.getConnectorDecorator();
             connectorDecorator.setFillColor(dependencyDiagramSettings.getDiagramBackgroundColor());
             connectorDecorator.setAntialiased(dependencyDiagramSettings.isUseAntialiasedConnectors());
             return true;
           }
 
 
 
 
 
           
           public void postExecution() {}
         });
     repaint();
     changesMade();
   }
 
 
   
   public DependencyDiagramComponentPanel getDependencyDiagramComponentPanel() {
     return this.dependencyDiagramComponentPanel;
   }
 
 
   
   public Project getProject() {
     return this.project;
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
 
 
   
   public boolean containsDependency(String qualifiedName) {
     Figure containedPsiDependencyComponent = (Figure)getFigureMap().get(qualifiedName);
     return (containedPsiDependencyComponent != null);
   }
 
 
   
   public boolean containsHighlightDependency(String qualifiedName) {
     PsiDependencyComponent containedPsiDependencyComponent = (PsiDependencyComponent)getFigureMap().get(qualifiedName);
     if (containedPsiDependencyComponent != null) {
       
       scrollRectToVisible(containedPsiDependencyComponent.getBounds());
       return true;
     } 
     return false;
   }
 
 
   
   public DependencyDiagramSettings getDiagramSettings() {
     return this.diagramSettings;
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
           UMLToolWindowPlugin umlToolWindowPlugin = UMLToolWindowPlugin.getUMLToolWindowPlugin(DependencyDiagramComponent.this.project);
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
               if (DependencyDiagramComponent.this.isShowing())
               {
                 DependencyDiagramComponent.this.notifyChangeListeners();
               }
             }
           });
     }
   }
 }


