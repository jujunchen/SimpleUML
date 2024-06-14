 package net.trustx.simpleuml.gef.connector;

 import java.awt.Color;
 import java.awt.Graphics2D;
 import java.awt.Point;
 import java.awt.Rectangle;
 import java.awt.RenderingHints;
 import java.awt.Shape;
 import java.awt.Stroke;
 import java.awt.geom.AffineTransform;
 import java.awt.geom.GeneralPath;
 import java.awt.geom.Line2D;
 import java.util.ArrayList;
 import java.util.Iterator;
 import net.trustx.simpleuml.components.ActionContributor;
 import net.trustx.simpleuml.components.ActionContributorCommand;
 import net.trustx.simpleuml.components.ActionContributorCommandInfo;
 import net.trustx.simpleuml.gef.anchor.Anchor;
 import net.trustx.simpleuml.gef.anchor.FigureAnchor;
 import net.trustx.simpleuml.gef.components.DiagramPane;





 public class ConnectorDirect
   implements Connector
 {
   private ArrayList<Anchor> anchorList;
   private FigureAnchor startFigureAnchor;
   private FigureAnchor endFigureAnchor;
   private ConnectorDecorator connectorDecorator;
   private boolean visible;
   private boolean paintable;
   private GeneralPath generalPath;
   private int type;
   private DiagramPane container;
   private boolean drawDecorations;
   private ActionContributor actionContributor;

   public ConnectorDirect(DiagramPane container, ConnectorDecorator connectorDecorator, FigureAnchor startFigureAnchor, FigureAnchor endFigureAnchor) {
     this.container = container;

     this.connectorDecorator = connectorDecorator;
     this.endFigureAnchor = endFigureAnchor;
     this.startFigureAnchor = startFigureAnchor;

     this.anchorList = new ArrayList();
     this.anchorList.add(startFigureAnchor);
     this.anchorList.add(endFigureAnchor);

     setType(1);
   }



   public void setActionContributor(ActionContributor actionContributor) {
     this.actionContributor = actionContributor;
   }



   public int getType() {
     return this.type;
   }



   public void setType(int type) {
     if (type == 1 || type == 2) {

       this.type = type;
       resetAnchorList();
     }
     else {

       throw new IllegalArgumentException("Type " + type + " is not valid");
     }
   }




   public ArrayList getAnchorList() {
     return this.anchorList;
   }



   public void resetAnchorList() {
     this.anchorList.clear();
     this.anchorList.add(this.startFigureAnchor);
     this.anchorList.add(this.endFigureAnchor);
   }



   public FigureAnchor getStartFigureAnchor() {
     return this.startFigureAnchor;
   }



   public FigureAnchor getEndFigureAnchor() {
     return this.endFigureAnchor;
   }



   public ConnectorDecorator getConnectorDecorator() {
     return this.connectorDecorator;
   }



   public void setVisible(boolean visible) {
     this.visible = visible;
   }



   public boolean isVisible() {
     return this.visible;
   }



   public void setConnectorDecorator(ConnectorDecorator connectorDecorator) {
     this.connectorDecorator = connectorDecorator;
   }



   public Rectangle getBounds() {
     if (!this.visible || !this.paintable)
     {
       return null;
     }
     Line2D.Double line = new Line2D.Double(this.startFigureAnchor.getLocation(), this.endFigureAnchor.getLocation());
     Rectangle bounds = line.getBounds();
     for (Iterator<Anchor> iterator = this.anchorList.iterator(); iterator.hasNext(); ) {

       Anchor anchor = iterator.next();
       bounds.add(anchor.getLocation());
     }

     if (isDrawDecorations()) {

       Rectangle rect = this.connectorDecorator.getLastStartDecorationBounds();
       if (rect != null)
       {
         bounds.add(rect);
       }
       rect = this.connectorDecorator.getLastCenterDecorationBounds();
       if (rect != null)
       {
         bounds.add(rect);
       }
       rect = this.connectorDecorator.getLastEndDecorationBounds();
       if (rect != null)
       {
         bounds.add(rect);
       }
     }

     bounds.grow(10, 10);

     return bounds;
   }



   public void paint(Graphics2D g2d) {
     if (!this.visible || !this.paintable) {
       return;
     }


     Stroke origStroke = g2d.getStroke();

     Object originalHint = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
     if (this.connectorDecorator.isAntialiased())
     {
       g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
     }

     g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_DEFAULT);

     drawConnectorLine(g2d);

     g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);


     Anchor lastAnchor = this.anchorList.get(this.anchorList.size() - 1);
     Anchor sLastAnchor = this.anchorList.get(this.anchorList.size() - 2);
     Point p2 = this.endFigureAnchor.getLocation();

     Shape rotPointer = getTransformedShape(calcAngle(sLastAnchor, lastAnchor), this.connectorDecorator.getEndShape(), p2);

     drawFilledShape(g2d, rotPointer);



     Anchor firstAnchor = this.anchorList.get(0);
     Anchor sFirstAnchor = this.anchorList.get(1);
     Point p1 = this.startFigureAnchor.getLocation();

     Shape rotStartShape = getTransformedShape(calcAngle(sFirstAnchor, firstAnchor), this.connectorDecorator.getStartShape(), p1);
     drawFilledShape(g2d, rotStartShape);

     Point sp = getStartFigureAnchor().getLocation();
     Point ep = getEndFigureAnchor().getLocation();
     Point rp = new Point((sp.x + ep.x) / 2, (sp.y + ep.y) / 2);


     if (isDrawDecorations()) {

       this.connectorDecorator.paintStartDecorations(g2d, 0.0D, rp);
       this.connectorDecorator.paintCenterDecorations(g2d, 0.0D, rp);
       this.connectorDecorator.paintEndDecorations(g2d, 0.0D, rp);
     }


     if (this.connectorDecorator.isAntialiased())
     {
       g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, originalHint);
     }
     g2d.setStroke(origStroke);
   }



   public void setDrawDecorations(boolean drawDecorations) {
     this.drawDecorations = drawDecorations;
   }



   public boolean isDrawDecorations() {
     return this.drawDecorations;
   }



   private void drawFilledShape(Graphics2D g2d, Shape rotPointer) {
     g2d.setPaint(this.connectorDecorator.getFillColor());
     g2d.fill(rotPointer);
     g2d.setPaint(Color.black);
     g2d.draw(rotPointer);
   }



   private Shape getTransformedShape(double angle, Shape shape, Point point) {
     AffineTransform rT = AffineTransform.getRotateInstance(angle);
     Shape rotPointer = rT.createTransformedShape(shape);
     AffineTransform tT = AffineTransform.getTranslateInstance(point.x, point.y);
     rotPointer = tT.createTransformedShape(rotPointer);
     return rotPointer;
   }



   private void drawConnectorLine(Graphics2D g2d) {
     Stroke origStroke = g2d.getStroke();
     g2d.setStroke(this.connectorDecorator.getLineStyle());
     this.generalPath = new GeneralPath();
     Anchor last = this.anchorList.get(this.anchorList.size() - 1);
     this.generalPath.moveTo((last.getLocation()).x, (last.getLocation()).y);
     for (int i = this.anchorList.size() - 1; i >= 0; i--) {

       Anchor anchor = this.anchorList.get(i);
       this.generalPath.lineTo((anchor.getLocation()).x, (anchor.getLocation()).y);
     }
     g2d.draw(this.generalPath);
     g2d.setStroke(origStroke);
   }



   private double calcAngle(Anchor anchor1, Anchor anchor2) {
     double xx1 = (anchor1.getLocation()).x;
     double yy1 = (anchor1.getLocation()).y;
     double xx2 = (anchor2.getLocation()).x;
     double yy2 = (anchor2.getLocation()).y;

     double m = (yy2 - yy1 < 0.0D) ? -1.0D : 1.0D;
     double angle = 1.5707963267948966D;

     if (Math.abs(yy1 - yy2) > 0.1D) {

       angle = 1.5707963267948966D + m * Math.PI / 2.0D - Math.atan((xx1 - xx2) / (yy1 - yy2));
     }
     else {

       m = (xx2 < xx1) ? -1.0D : 1.0D;
       angle *= m;
     }
     return angle;
   }



   public boolean equals(Object o) {
     if (!(o instanceof Connector)) {
       return false;
     }
     Connector diagramConnector = (Connector)o;

     if (!this.startFigureAnchor.getFigure().equals(diagramConnector.getStartFigureAnchor().getFigure()))
       return false;
     if (!this.endFigureAnchor.getFigure().equals(diagramConnector.getEndFigureAnchor().getFigure()))
       return false;
     return this.connectorDecorator.equals(diagramConnector.getConnectorDecorator());
   }





   public int hashCode() {
     int result = this.startFigureAnchor.getFigure().hashCode();
     result = 29 * result + this.endFigureAnchor.getFigure().hashCode();
     return result;
   }




   public void disposeConnector() {}



   public boolean isPaintable() {
     return this.paintable;
   }



   public boolean contains(int x, int y) {
     if (this.generalPath != null) {

       Anchor last = this.anchorList.get(this.anchorList.size() - 1);
       for (int i = this.anchorList.size() - 1; i >= 0; i--) {

         Anchor anchor = this.anchorList.get(i);
         if ((new Line2D.Double(last.getLocation(), anchor.getLocation())).intersects((x - 2), (y - 2), 4.0D, 4.0D))
         {
           return true;
         }
         last = anchor;
       }
     }

     return false;
   }



   public void setPaintable(boolean paintable) {
     this.paintable = paintable;
   }



   public ActionContributorCommand getActionContributorCommand(ActionContributorCommandInfo info) {
     if (this.actionContributor != null)
     {
       return this.actionContributor.getActionContributorCommand(info);
     }
     return null;
   }



   public ActionContributorCommandInfo[] getActionContributorCommandInfos() {
     if (this.actionContributor != null)
     {
       return this.actionContributor.getActionContributorCommandInfos();
     }
     return new ActionContributorCommandInfo[0];
   }
 }


