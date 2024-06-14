 package net.trustx.simpleuml.gef.components;

 import java.awt.BorderLayout;
 import java.awt.Color;
 import java.awt.Point;
 import java.awt.Rectangle;
 import java.awt.event.ComponentAdapter;
 import java.awt.event.ComponentEvent;
 import java.awt.event.ContainerAdapter;
 import java.awt.event.ContainerEvent;
 import java.awt.event.KeyAdapter;
 import java.awt.event.KeyEvent;
 import java.awt.event.MouseListener;
 import java.awt.event.MouseMotionListener;
 import java.util.HashSet;
 import java.util.Iterator;
 import javax.swing.BorderFactory;
 import javax.swing.JComponent;
 import javax.swing.JPanel;
 import javax.swing.SwingUtilities;
 import javax.swing.border.Border;
 import net.trustx.simpleuml.components.ActionContributor;
 import net.trustx.simpleuml.components.ActionContributorCommand;
 import net.trustx.simpleuml.components.ActionContributorCommandInfo;
 import net.trustx.simpleuml.components.border.BevelBorder2D;
 import net.trustx.simpleuml.components.border.SelectionBorder;
 import net.trustx.simpleuml.gef.Figure;
 import net.trustx.simpleuml.gef.Selectable;
 import net.trustx.simpleuml.gef.SelectionManager;
 import net.trustx.simpleuml.gef.connector.ConnectorManager;





 public class FigureComponent
   extends JPanel
   implements Selectable, Figure, ActionContributor
 {
   public static final int MARQUEE_LAYER = 1000;
   public static final int DEFAULT_LAYER = 0;
   public static final int VISUAL_GROUP_LAYER = -100;
   private int posX;
   private int posY;
   private boolean adjusting;
   private DiagramPane diagramPane;
   private SelectionManager selectionManager;
   private ConnectorManager connectorManager;
   private HashSet childrenSet;
   private int preferredLayer;
   private JPanel contentPane;
   private boolean resizingSupported;
   private JPanel resizeHandler;
   private ResizeComponentKnob resizeKnob;
   private boolean printWYSIWYG;
   private boolean pinned;
   private Color color;

   public FigureComponent(DiagramPane diagramPane) {
     setLayout(new BorderLayout());
     setOpaque(false);
     this.diagramPane = diagramPane;
     this.connectorManager = diagramPane.getConnectorManager();
     this.contentPane = new JPanel(new BorderLayout());
     this.contentPane.setOpaque(false);
     add(this.contentPane, "Center");

     this.resizingSupported = false;

     this.printWYSIWYG = true;

     setSelectionManager(new SelectionManager());


     this.childrenSet = new HashSet(0);

     initKeyboardSupport();

     addComponentListener(new ComponentAdapter()
         {
           public void componentResized(ComponentEvent e)
           {
             FigureComponent.this.updateConnectors();
           }
         });

     addContainerListener(new ContainerAdapter()
         {
           public void componentAdded(ContainerEvent e)
           {
             if (e.getChild() instanceof FigureComponent)
             {
               FigureComponent.this.childrenSet.add(e.getChild());
             }
           }
         });
   }



   public DiagramPane getDiagramPane() {
     return this.diagramPane;
   }



   public JPanel getContentPane() {
     return this.contentPane;
   }



   public JPanel getResizeHandler() {
     return this.resizeHandler;
   }



   public boolean isResizingSupported() {
     return this.resizingSupported;
   }



   public boolean isPrintWYSIWYG() {
     return this.printWYSIWYG;
   }



   public void setPrintWYSIWYG(boolean printWYSIWYG) {
     this.printWYSIWYG = printWYSIWYG;
   }



   public void setResizingSupported(boolean resizingSupported) {
     if (this.resizingSupported == resizingSupported) {
       return;
     }

     if (resizingSupported) {

       this.resizeHandler = new JPanel(new BorderLayout());
       this.resizeHandler.setOpaque(false);
       this.resizeKnob = new ResizeComponentKnob(this, 8);
       this.resizeHandler.add(this.resizeKnob, "East");
       add(this.resizeHandler, "South");
     }
     else {

       remove(this.resizeHandler);
       this.resizeHandler = null;
       this.resizeKnob = null;
     }

     this.resizingSupported = resizingSupported;
     resizingSupportChanged();
   }




   protected void resizingSupportChanged() {}



   public ResizeComponentKnob getResizeKnob() {
     return this.resizeKnob;
   }



   private void updateConnectors() {
     this.connectorManager.validateConnectors(this);
   }



   protected void initKeyboardSupport() {
     addKeyListener(new KeyAdapter()
         {
           public void keyPressed(KeyEvent e)
           {
             FigureComponent.this.getParent().dispatchEvent(e);
           }



           public void keyReleased(KeyEvent e) {
             FigureComponent.this.getParent().dispatchEvent(e);
           }



           public void keyTyped(KeyEvent e) {
             FigureComponent.this.getParent().dispatchEvent(e);
           }
         });
   }



   public void setPosX(int posX) {
     this.posX = posX;
   }




   public void prepareMoveFigureComponent() {}




   public void moveFigureComponent(int dx, int dy) {
     setPosX(getPosX() + dx);
     setPosY(getPosY() + dy);
   }




   public void finishedMoveFigureComponent() {}




   public int getPosX() {
     return this.posX;
   }



   public void setPosY(int posY) {
     this.posY = posY;
   }



   public int getComponentWidth() {
     return (getPreferredSize()).width;
   }



   public int getComponentHeight() {
     return (getPreferredSize()).height;
   }



   public String getKey() {
     return Integer.toString(hashCode());
   }



   public boolean isPinned() {
     return this.pinned;
   }



   public void setPinned(boolean pinned) {
     this.pinned = pinned;
   }



   public boolean isOnFigureBounds(Point p) {
     Rectangle r = getBounds();
     return (isOnHorizontalLine(r.x, r.x + r.width, r.y, p) || isOnHorizontalLine(r.x, r.x + r.width, r.y + r.height, p) || isOnVerticalLine(r.y, r.y + r.height, r.x, p) || isOnVerticalLine(r.y, r.y + r.height, r.x + r.width, p));
   }



   private boolean isOnHorizontalLine(int x1, int x2, int y, Point p) {
     return (p.y == y && p.x >= x1 && p.x <= x2);
   }



   private boolean isOnVerticalLine(int y1, int y2, int x, Point p) {
     return (p.x == x && p.y >= y1 && p.y <= y2);
   }



   public boolean isCenteredOnSide(Point location) {
     return false;
   }



   public void setColor(Color color) {
     this.color = color;
   }



   public Color getColor() {
     return this.color;
   }



   public Rectangle getConnectorBounds(JComponent parent) {
     return SwingUtilities.convertRectangle(getParent(), getBounds(), parent);
   }



   public void updateBeforeRelease(FigureComponent figureComponent) {
     setBorder(BorderFactory.createLineBorder(Color.RED, 2));
   }



   public FigureComponent[] getChildren() {
     return (FigureComponent[])this.childrenSet.toArray((Object[])new FigureComponent[this.childrenSet.size()]);
   }



   public void addChildren(FigureComponent[] figureComponents) {
     for (int i = 0; i < figureComponents.length; i++) {

       FigureComponent component = figureComponents[i];
       this.childrenSet.add(component);
     }
   }



   public int getPosY() {
     return this.posY;
   }



   public boolean isAdjusting() {
     return this.adjusting;
   }



   public void setAdjusting(boolean adjusting) {
     this.adjusting = adjusting;
   }




   public boolean isSelected() {
     return this.selectionManager.contains(this);
   }



   public void setSelected(boolean selected) {
     if (selected) {

       this.selectionManager.add(this);
     }
     else {

       this.selectionManager.remove(this);
     }
   }



   public void selectionChanged() {
     updateBorder();
   }



   public SelectionManager getSelectionManager() {
     return this.selectionManager;
   }




   public void setSelectionManager(SelectionManager selectionManager) {
     if (this.selectionManager != null)
     {
       this.selectionManager.remove(this);
     }
     if (selectionManager == null)
     {
       throw new RuntimeException("SelectionManager can not be null");
     }

     this.selectionManager = selectionManager;
     updateBorder();
   }
























   public void updateBorder() {
     if (isSelected()) {

       setBorder((Border)new SelectionBorder((Border)new BevelBorder2D(0, 2)));
     }
     else {

       setBorder((Border)new BevelBorder2D(0, 2));
     }
     revalidate();
   }



   public boolean isFocusable() {
     return true;
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

     for (Iterator<FigureComponent> iterator = this.childrenSet.iterator(); iterator.hasNext(); ) {

       FigureComponent figureComponent = iterator.next();
       figureComponent.disposeUIResources();
     }

     removeAll();
   }



   public int getPreferredLayer() {
     return this.preferredLayer;
   }



   public void setPreferredLayer(int preferredLayer) {
     this.preferredLayer = preferredLayer;
   }



   public ActionContributorCommand getActionContributorCommand(ActionContributorCommandInfo info) {
     return null;
   }



   public ActionContributorCommandInfo[] getActionContributorCommandInfos() {
     return new ActionContributorCommandInfo[0];
   }
 }


