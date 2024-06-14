 package net.trustx.simpleuml.gef.components;

 import java.awt.Color;
 import java.awt.Font;
 import java.awt.Graphics;
 import java.awt.Graphics2D;
 import java.awt.Insets;
 import java.awt.Rectangle;
 import java.awt.event.FocusAdapter;
 import java.awt.event.FocusEvent;
 import java.awt.event.MouseListener;
 import java.awt.event.MouseMotionListener;
 import java.util.Collection;
 import java.util.HashMap;
 import java.util.HashSet;
 import java.util.Iterator;
 import javax.swing.JLayeredPane;
 import net.trustx.simpleuml.components.ActionContributor;
 import net.trustx.simpleuml.components.ActionContributorCommand;
 import net.trustx.simpleuml.components.ActionContributorCommandInfo;
 import net.trustx.simpleuml.gef.SelectionManager;
 import net.trustx.simpleuml.gef.connector.Connector;
 import net.trustx.simpleuml.gef.connector.ConnectorDecoratorSettings;
 import net.trustx.simpleuml.gef.connector.ConnectorManager;



 public class DiagramPane
   extends JLayeredPane
   implements ActionContributor
 {
   private Rectangle allowedContentBounds;
   private ConnectorManager connectorManager;
   private SelectionManager selectionManager;
   private HashMap figureMap;
   private HashSet diagramPaneListeners;

   public DiagramPane() {
     setLayout(new DefaultDiagramPaneLayout());
     this.diagramPaneListeners = new HashSet();
     this.connectorManager = new ConnectorManager(this);
     this.selectionManager = new SelectionManager();
     this.figureMap = new HashMap<Object, Object>();

     addFocusListener(new FocusAdapter()
         {
           public void focusGained(FocusEvent e) {}






           public void focusLost(FocusEvent e) {}
         });
   }





   public void addFigureComponent(FigureComponent figureComponent, boolean update) {
     this.figureMap.put(figureComponent.getKey(), figureComponent);

     setLayer(figureComponent, figureComponent.getPreferredLayer());
     add(figureComponent);
     layoutContainer();
     fireFigureComponentAdded(figureComponent);
   }



   public void removeFigureComponent(FigureComponent figureComponent) {
     this.connectorManager.removeConnectors(figureComponent);
     this.selectionManager.remove(figureComponent);
     remove(figureComponent);
     this.figureMap.remove(figureComponent.getKey());
     layoutContainer();
     fireFigureComponentRemoved(figureComponent);
   }



   public SelectionManager getSelectionManager() {
     return this.selectionManager;
   }



   public ConnectorManager getConnectorManager() {
     return this.connectorManager;
   }



   public void addConnector(Connector connector) {
     this.connectorManager.addConnector(connector);
     fireConnectorAdded(connector);
   }



   public void removeConnector(Connector connector) {
     this.connectorManager.removeConnector(connector);
     fireConnectorRemoved(connector);
   }




   public void addDiagramPaneListener(DiagramPaneListener listener) {
     this.diagramPaneListeners.add(listener);
   }



   public void removeDiagramPaneListener(DiagramPaneListener listener) {
     this.diagramPaneListeners.remove(listener);
   }



   private void fireFigureComponentAdded(FigureComponent figureComponent) {
     for (Iterator<DiagramPaneListener> iterator = this.diagramPaneListeners.iterator(); iterator.hasNext(); ) {

       DiagramPaneListener listener = iterator.next();
       listener.figureAdded(figureComponent);
     }
   }



   private void fireFigureComponentRemoved(FigureComponent figureComponent) {
     for (Iterator<DiagramPaneListener> iterator = this.diagramPaneListeners.iterator(); iterator.hasNext(); ) {

       DiagramPaneListener listener = iterator.next();
       listener.figureRemoved(figureComponent);
     }
   }



   private void fireConnectorAdded(Connector connector) {
     for (Iterator<DiagramPaneListener> iterator = this.diagramPaneListeners.iterator(); iterator.hasNext(); ) {

       DiagramPaneListener listener = iterator.next();
       listener.connectorAdded(connector);
     }
   }



   private void fireConnectorRemoved(Connector connector) {
     for (Iterator<DiagramPaneListener> iterator = this.diagramPaneListeners.iterator(); iterator.hasNext(); ) {

       DiagramPaneListener listener = iterator.next();
       listener.connectorRemoved(connector);
     }
   }



   protected void paintComponent(Graphics g) {
     super.paintComponent(g);
     this.connectorManager.paintConnectors((Graphics2D)g);
   }



   public void layoutContainer() {
     doLayout();
     revalidate();
   }



   public HashMap getFigureMap() {
     return this.figureMap;
   }



   public Collection getFigureComponents() {
     return this.figureMap.values();
   }



   public Rectangle getAllowedContentBounds() {
     if (this.allowedContentBounds != null)
     {
       return this.allowedContentBounds;
     }


     Insets bI = getInsets();
     return new Rectangle(bI.left, bI.top, (getSize()).width - bI.right, (getSize()).height - bI.bottom);
   }




   public void setAllowedContentBounds(Rectangle allowedContentBounds) {
     if (allowedContentBounds != null)
     {
       this.allowedContentBounds = allowedContentBounds;
     }
   }



   public void disposeUIResources() {
     MouseListener[] mouseListeners = getMouseListeners();
     for (int i = 0; i < mouseListeners.length; i++) {

       MouseListener listener = mouseListeners[i];
       removeMouseListener(listener);
     }



     MouseMotionListener[] mouseMotionListeners = getMouseMotionListeners();
     for (int j = 0; j < mouseMotionListeners.length; j++) {

       MouseMotionListener listener = mouseMotionListeners[j];
       removeMouseMotionListener(listener);
     }


     for (Iterator<FigureComponent> iterator = this.figureMap.values().iterator(); iterator.hasNext(); ) {

       FigureComponent figureComponent = iterator.next();
       figureComponent.disposeUIResources();
     }

     this.figureMap.clear();
     this.connectorManager.clearAll();
     this.selectionManager.clear();
     this.diagramPaneListeners.clear();
     removeAll();
   }



   public boolean executeCommandOnFigureComponents(FigureComponentCommand command) {
     command.preExecution();

     for (Iterator<FigureComponent> iterator = this.figureMap.values().iterator(); iterator.hasNext(); ) {

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



   public static boolean executeCommandOnFigureComponent(FigureComponentCommand componentCommand, FigureComponent figureComponent) {
     componentCommand.preExecution();
     boolean success = componentCommand.executeCommand(figureComponent);
     componentCommand.postExecution();
     return success;
   }



   public ConnectorDecoratorSettings getDefaultDecoratorSettings() {
     return new ConnectorDecoratorSettings(false, new Font("monospaced", 0, 12), Color.WHITE);
   }



   public ActionContributorCommand getActionContributorCommand(ActionContributorCommandInfo info) {
     return null;
   }



   public ActionContributorCommandInfo[] getActionContributorCommandInfos() {
     return new ActionContributorCommandInfo[0];
   }
 }


