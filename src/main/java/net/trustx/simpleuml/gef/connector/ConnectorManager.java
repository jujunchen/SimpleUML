 package net.trustx.simpleuml.gef.connector;

 import java.awt.Graphics2D;
 import java.awt.Point;
 import java.awt.Rectangle;
 import java.util.Collection;
 import java.util.HashMap;
 import java.util.HashSet;
 import java.util.Iterator;
 import java.util.LinkedList;
 import java.util.Set;
 import javax.swing.JComponent;
 import javax.swing.RepaintManager;
 import net.trustx.simpleuml.gef.Figure;
 import net.trustx.simpleuml.gef.components.FigureComponent;



 public class ConnectorManager
 {
   private JComponent parentComponent;
   public static final int TYPE_INCOMING = 1;
   public static final int TYPE_OUTGOING = 2;
   public static final int TYPE_ALL = 4;
   private HashMap connectorLayouterMap;
   private HashSet connectorSet;
   private HashMap connectorComponentMap;
   private HashMap layouterComponentMap;
   private HashSet parent;

   public ConnectorManager(JComponent parentComponent) {
     this.parentComponent = parentComponent;
     this.connectorLayouterMap = new HashMap<Object, Object>();
     this.connectorSet = new HashSet();
     this.connectorComponentMap = new HashMap<Object, Object>();
     this.layouterComponentMap = new HashMap<Object, Object>();

     this.parent = new HashSet(0);
   }



   public void addConnectorManager(ConnectorManager connectorManager) {
     this.parent.add(connectorManager);
   }



   public void removeConnectormanager(ConnectorManager connectorManager) {
     this.parent.remove(connectorManager);
   }



   public void addConnector(Connector connector) {
     if (this.connectorSet.contains(connector)) {
       return;
     }


     int hc = 13 * connector.getStartFigureAnchor().getFigure().hashCode() + connector.getEndFigureAnchor().getFigure().hashCode();
     int hc2 = 13 * connector.getEndFigureAnchor().getFigure().hashCode() + connector.getStartFigureAnchor().getFigure().hashCode();
     if (this.connectorLayouterMap.containsKey("" + hc)) {

       ConnectorLayouter layouter = (ConnectorLayouter)this.connectorLayouterMap.get("" + hc);
       layouter.addConnector(connector);
     }
     else if (this.connectorLayouterMap.containsKey("" + hc2)) {

       ConnectorLayouter layouter = (ConnectorLayouter)this.connectorLayouterMap.get("" + hc2);
       layouter.addConnector(connector);
     } else {
       ConnectorLayouter layouter;


       if (connector.getStartFigureAnchor().getFigure() == connector.getEndFigureAnchor().getFigure()) {

         layouter = new SelfReferenceLayouter(connector.getStartFigureAnchor().getFigure());
       }
       else {

         layouter = new AutomaticConnectorLayouter(connector.getStartFigureAnchor().getFigure(), connector.getEndFigureAnchor().getFigure());
       }
       this.connectorLayouterMap.put("" + hc, layouter);
       layouter.addConnector(connector);
       addLayouterToComponent(layouter, (FigureComponent)connector.getStartFigureAnchor().getFigure());
       addLayouterToComponent(layouter, (FigureComponent)connector.getEndFigureAnchor().getFigure());
     }

     addConnectorToComponent(connector);

     this.connectorSet.add(connector);
   }



   private void addConnectorToComponent(Connector connector) {
     HashSet<Connector> hashSet = (HashSet)this.connectorComponentMap.get(connector.getStartFigureAnchor().getFigure());
     if (hashSet != null) {

       hashSet.add(connector);
     }
     else {

       HashSet<Connector> set = new HashSet();
       set.add(connector);
       this.connectorComponentMap.put(connector.getStartFigureAnchor().getFigure(), set);
     }

     hashSet = (HashSet<Connector>)this.connectorComponentMap.get(connector.getEndFigureAnchor().getFigure());
     if (hashSet != null) {

       hashSet.add(connector);
     }
     else {

       HashSet<Connector> set = new HashSet();
       set.add(connector);
       this.connectorComponentMap.put(connector.getEndFigureAnchor().getFigure(), set);
     }
   }



   private void addLayouterToComponent(ConnectorLayouter layouter, FigureComponent comp) {
     HashSet<ConnectorLayouter> hashSet = (HashSet)this.layouterComponentMap.get(comp);
     if (hashSet != null) {

       hashSet.add(layouter);
     }
     else {

       HashSet<ConnectorLayouter> set = new HashSet();
       set.add(layouter);
       this.layouterComponentMap.put(comp, set);
     }
   }



   public void removeConnector(Connector connector) {
     int hc = 13 * connector.getStartFigureAnchor().getFigure().hashCode() + connector.getEndFigureAnchor().getFigure().hashCode();
     int hc2 = 13 * connector.getEndFigureAnchor().getFigure().hashCode() + connector.getStartFigureAnchor().getFigure().hashCode();
     if (this.connectorLayouterMap.containsKey("" + hc)) {

       ConnectorLayouter layouter = (ConnectorLayouter)this.connectorLayouterMap.get("" + hc);
       layouter.removeConnector(connector);
       if (layouter.isEmpty())
       {
         this.connectorLayouterMap.remove("" + hc);
       }
     }
     else if (this.connectorLayouterMap.containsKey("" + hc2)) {

       ConnectorLayouter layouter = (ConnectorLayouter)this.connectorLayouterMap.get("" + hc2);
       layouter.removeConnector(connector);
       if (layouter.isEmpty())
       {
         this.connectorLayouterMap.remove("" + hc);
       }
     }
     this.connectorSet.remove(connector);
     removeConnectorFromComponent(connector);
   }



   private void removeConnectorFromComponent(Connector connector) {
     HashSet hashSet = (HashSet)this.connectorComponentMap.get(connector.getStartFigureAnchor().getFigure());
     if (hashSet != null) {

       hashSet.remove(connector);
       if (hashSet.isEmpty())
       {
         this.connectorComponentMap.remove(connector.getStartFigureAnchor().getFigure());
       }
     }


     hashSet = (HashSet)this.connectorComponentMap.get(connector.getEndFigureAnchor().getFigure());
     if (hashSet != null) {

       hashSet.remove(connector);
       if (hashSet.isEmpty())
       {
         this.connectorComponentMap.remove(connector.getEndFigureAnchor().getFigure());
       }
     }
   }



   public void validateConnectors() {
     LinkedList parent = new LinkedList(this.parent);
     for (Iterator<ConnectorManager> iterator = parent.iterator(); iterator.hasNext(); ) {

       ConnectorManager manager = iterator.next();
       manager.validateConnectors();
     }
     Collection col = this.connectorLayouterMap.values();
     for (Iterator<ConnectorLayouter> iterator1 = col.iterator(); iterator1.hasNext(); ) {

       ConnectorLayouter connectorLayouter = iterator1.next();
       connectorLayouter.layoutConnectors(this.parentComponent);
     }
   }



   public void validateConnectors(FigureComponent figureComponent) {
     for (Iterator<ConnectorManager> iterator = this.parent.iterator(); iterator.hasNext(); ) {

       ConnectorManager manager = iterator.next();
       manager.validateConnectors(figureComponent);
     }

     FigureComponent[] children = figureComponent.getChildren();
     for (int i = 0; i < children.length; i++) {

       FigureComponent child = children[i];
       validateConnectors(child);
     }

     HashSet set = (HashSet)this.layouterComponentMap.get(figureComponent);
     if (set == null) {
       return;
     }

     for (Iterator<ConnectorLayouter> iterator1 = set.iterator(); iterator1.hasNext(); ) {

       ConnectorLayouter connectorLayouter = iterator1.next();
       connectorLayouter.layoutConnectors(this.parentComponent);
     }
   }















   public void markConnectorDirty(Connector connector) {
     Rectangle rectangle = connector.getBounds();

     if (rectangle != null)
     {
       RepaintManager.currentManager(this.parentComponent).addDirtyRegion(this.parentComponent, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
     }
   }





   public void paintConnectors(Graphics2D graphics2D) {
     LinkedList linkedList = new LinkedList(this.connectorSet);
     for (Iterator<Connector> iterator = linkedList.iterator(); iterator.hasNext(); ) {

       Connector connector = iterator.next();








       connector.paint(graphics2D);
     }
   }



   public void clearAll() {
     this.connectorLayouterMap.clear();
     this.connectorSet.clear();
     this.connectorComponentMap.clear();
     this.layouterComponentMap.clear();
   }



   public Collection getConnectedComponents(FigureComponent figureComponent) {
     HashSet<Figure> set = new HashSet();
     HashSet hashSet = (HashSet)this.connectorComponentMap.get(figureComponent);
     if (hashSet == null)
     {
       return set;
     }


     for (Iterator<Connector> iterator = hashSet.iterator(); iterator.hasNext(); ) {

       Connector connector = iterator.next();
       set.add(connector.getStartFigureAnchor().getFigure());
       set.add(connector.getEndFigureAnchor().getFigure());
     }
     return set;
   }




   public void removeConnectors(FigureComponent figureComponent) {
     HashSet<?> hashSet = (HashSet)this.connectorComponentMap.get(figureComponent);
     if (hashSet != null) {




       HashSet tempSet = new HashSet(hashSet);
       for (Iterator<Connector> iterator = tempSet.iterator(); iterator.hasNext(); ) {

         Connector connector = iterator.next();
         removeConnector(connector);
       }
     }
   }



   public void removeConnectors(FigureComponent figureComponent, int type, int decoratorType) {
     HashSet<?> hashSet = (HashSet)this.connectorComponentMap.get(figureComponent);
     if (hashSet != null) {




       HashSet tempSet = new HashSet(hashSet);
       for (Iterator<Connector> iterator = tempSet.iterator(); iterator.hasNext(); ) {

         Connector connector = iterator.next();
         if (connector.getConnectorDecorator().getType() == decoratorType) {

           if (type == 1) {

             if (connector.getEndFigureAnchor().getFigure() == figureComponent)
             {
               removeConnector(connector); }
             continue;
           }
           if (type == 2) {

             if (connector.getStartFigureAnchor().getFigure() == figureComponent)
             {
               removeConnector(connector);
             }

             continue;
           }
           removeConnector(connector);
         }
       }
     }
   }




   public Set getConnectors() {
     return new HashSet(this.connectorSet);
   }



   public Connector getConnectorForLocation(Point point) {
     return getConnectorForLocation((int)point.getX(), (int)point.getY());
   }



   public Connector getConnectorForLocation(int x, int y) {
     for (Iterator<Connector> iterator = this.connectorSet.iterator(); iterator.hasNext(); ) {

       Connector connector = iterator.next();
       if (connector.contains(x, y))
       {
         return connector;
       }
     }
     return null;
   }



   public Set getConnectorsForComponent(FigureComponent figureComponent) {
     HashSet<?> hashSet = (HashSet)this.connectorComponentMap.get(figureComponent);
     if (hashSet == null)
     {
       return new HashSet();
     }


     return new HashSet(hashSet);
   }




   public boolean executeCommandOnConnectors(ConnectorCommand command) {
     command.preExecution();

     for (Iterator<Connector> iterator = this.connectorSet.iterator(); iterator.hasNext(); ) {

       Connector connector = iterator.next();
       boolean success = command.executeCommand(connector);
       if (!success) {

         command.postExecution();
         return false;
       }
     }

     command.postExecution();
     return true;
   }



   public static boolean executeCommandOnConnector(ConnectorCommand command, Connector connector) {
     command.preExecution();
     boolean success = command.executeCommand(connector);
     command.postExecution();
     return success;
   }
 }


