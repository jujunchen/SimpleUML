 package net.trustx.simpleuml.gef.connector;

 import java.awt.Point;
 import java.awt.Rectangle;
 import java.awt.geom.Line2D;
 import java.awt.geom.Point2D;
 import java.util.Iterator;
 import javax.swing.JComponent;
 import javax.swing.RepaintManager;
 import net.trustx.simpleuml.gef.Figure;
 import net.trustx.simpleuml.gef.anchor.AnchorFree;
 import net.trustx.simpleuml.gef.anchor.AnchorInfo;
 import net.trustx.simpleuml.gef.anchor.PointTuple;



 public class AutomaticConnectorLayouter
   extends AbstractConnectorLayouter
 {
   private PointTuple[] getLines(Rectangle diagramComponent1, Rectangle diagramComponent2, int lineCount) {
     Rectangle squareDG1, squareDG2;
     if (diagramComponent1.height > diagramComponent1.width) {

       int side = (int)(diagramComponent1.width * 0.7D);
       int xc1 = diagramComponent1.x + diagramComponent1.width / 2;
       int yc1 = diagramComponent1.y + diagramComponent1.height / 2;
       squareDG1 = new Rectangle(xc1 - side / 2, yc1 - side / 2, side, side);
     }
     else {

       int side = (int)(diagramComponent1.height * 0.7D);
       int xc1 = diagramComponent1.x + diagramComponent1.width / 2;
       int yc1 = diagramComponent1.y + diagramComponent1.height / 2;
       squareDG1 = new Rectangle(xc1 - side / 2, yc1 - side / 2, side, side);
     }

     if (diagramComponent2.height > diagramComponent2.width) {

       int side = (int)(diagramComponent2.width * 0.7D);
       int xc1 = diagramComponent2.x + diagramComponent2.width / 2;
       int yc1 = diagramComponent2.y + diagramComponent2.height / 2;
       squareDG2 = new Rectangle(xc1 - side / 2, yc1 - side / 2, side, side);
     }
     else {

       int side = (int)(diagramComponent2.height * 0.7D);
       int xc1 = diagramComponent2.x + diagramComponent2.width / 2;
       int yc1 = diagramComponent2.y + diagramComponent2.height / 2;
       squareDG2 = new Rectangle(xc1 - side / 2, yc1 - side / 2, side, side);
     }


     int x1 = (squareDG1.getBounds()).x + (squareDG1.getBounds()).width / 2;
     int y1 = (squareDG1.getBounds()).y + (squareDG1.getBounds()).height / 2;

     Point centerC1 = new Point(x1, y1);

     int x2 = (squareDG2.getBounds()).x + (squareDG2.getBounds()).width / 2;
     int y2 = (squareDG2.getBounds()).y + (squareDG2.getBounds()).height / 2;

     Point centerC2 = new Point(x2, y2);




     Point2D.Double orthVec = getOrthogonalVector(centerC2, centerC1);



     Point2D.Double[] endPoints = getEndPoints(squareDG1, orthVec);
     Point2D.Double[] endPoints2 = getEndPoints(squareDG2, orthVec);


     double l1 = abs(new Point2D.Double((endPoints[0]).x - (endPoints[1]).x, (endPoints[0]).y - (endPoints[1]).y));
     double l2 = abs(new Point2D.Double((endPoints2[0]).x - (endPoints2[1]).x, (endPoints2[0]).y - (endPoints2[1]).y));





     if (l1 < l2) {

       endPoints2[0] = new Point2D.Double((endPoints[0]).x, (endPoints[0]).y);
       endPoints2[1] = new Point2D.Double((endPoints[1]).x, (endPoints[1]).y);
     }
     else {

       endPoints[0] = new Point2D.Double((endPoints2[0]).x, (endPoints2[0]).y);
       endPoints[1] = new Point2D.Double((endPoints2[1]).x, (endPoints2[1]).y);
     }



     (endPoints[0]).x += centerC1.x;
     (endPoints[0]).y += centerC1.y;

     (endPoints[1]).x += centerC1.x;
     (endPoints[1]).y += centerC1.y;

     (endPoints2[0]).x += centerC2.x;
     (endPoints2[0]).y += centerC2.y;

     (endPoints2[1]).x += centerC2.x;
     (endPoints2[1]).y += centerC2.y;



     Point2D.Double[] points = divideIntoPoints(endPoints[0], endPoints[1], lineCount, 99999.0D);
     Point2D.Double[] points2 = divideIntoPoints(endPoints2[0], endPoints2[1], lineCount, 99999.0D);

     PointTuple[] lineEnds = new PointTuple[points.length];

     for (int i = 0; i < points.length; i++) {

       Point2D.Double startPoint = points[i];
       Point2D.Double endPoint = points2[i];

       AnchorInfo sP = getIntersection(diagramComponent1, new Line2D.Double(startPoint, endPoint));
       AnchorInfo eP = getIntersection(diagramComponent2, new Line2D.Double(endPoint, startPoint));

       lineEnds[i] = new PointTuple(sP, eP);
     }

     return lineEnds;
   }



   private Point2D.Double[] divideIntoPoints(Point2D.Double startPoint, Point2D.Double endPoint, int pointCount, double distance) {
     Point2D.Double vec = new Point2D.Double(endPoint.x - startPoint.x, endPoint.y - startPoint.y);
     double length = abs(vec);
     Point2D.Double[] res = new Point2D.Double[pointCount];
     if (pointCount * distance > length) {

       double incX = vec.x / (pointCount + 1.0D);
       double incY = vec.y / (pointCount + 1.0D);
       double currentX = 0.0D;
       double currentY = 0.0D;
       for (int i = 0; i < pointCount; i++) {

         currentX += incX;
         currentY += incY;
         res[i] = new Point2D.Double(currentX + startPoint.x, currentY + startPoint.y);
       }
     }







     return res;
   }



   private Point2D.Double[] getEndPoints(Rectangle rect, Point2D.Double orthVec) {
     Point2D.Double[] points = new Point2D.Double[4];

     int x1 = rect.x + rect.width / 2;
     int y1 = rect.y + rect.height / 2;

     int e1x = rect.x - x1;
     int e1y = rect.y - y1;

     points[0] = project(new Point(e1x, e1y), orthVec);

     int e2x = rect.x + rect.width - x1;
     int e2y = rect.y - y1;

     points[1] = project(new Point(e2x, e2y), orthVec);

     int e3x = rect.x - x1;
     int e3y = rect.y + rect.height - y1;

     points[2] = project(new Point(e3x, e3y), orthVec);

     int e4x = rect.x + rect.width - x1;
     int e4y = rect.y + rect.height - y1;

     points[3] = project(new Point(e4x, e4y), orthVec);


     Point2D.Double biggestPoint = points[0];
     for (int i = 1; i < points.length; i++) {

       Point2D.Double point = points[i];
       if (abs(point) > abs(biggestPoint))
       {
         biggestPoint = point;
       }
     }


     Point2D.Double biggestDistancePoint = points[0];
     for (int j = 1; j < points.length; j++) {

       Point2D.Double point = points[j];

       Point2D.Double d1 = new Point2D.Double(biggestDistancePoint.x - biggestPoint.x, biggestDistancePoint.y - biggestPoint.y);
       Point2D.Double d2 = new Point2D.Double(point.x - biggestPoint.x, point.y - biggestPoint.y);
       if (abs(d2) > abs(d1))
       {
         biggestDistancePoint = point;
       }
     }


     return new Point2D.Double[] { biggestPoint, biggestDistancePoint };
   }



   private double abs(Point2D.Double p) {
     return Math.sqrt(p.x * p.x + p.y * p.y);
   }


   private Point2D.Double getOrthogonalVector(Point centerC2, Point centerC1) {
     double oX, oY;
     int xx1 = centerC2.x - centerC1.x;
     int yy1 = centerC2.y - centerC1.y;



     if (xx1 == 0 || yy1 == 0) {

       oX = yy1;
       oY = xx1;
     }
     else {

       oX = -yy1 / xx1;
       oY = 1.0D;
     }

     double f = Math.sqrt(oX * oX + oY * oY);
     oX /= f;
     oY /= f;

     oX *= 100000.0D;
     oY *= 100000.0D;

     return new Point2D.Double(oX, oY);
   }



   private Point2D.Double project(Point a, Point2D.Double b) {
     double nom = a.x * b.x + a.y * b.y;
     double denom = b.x * b.x + b.y * b.y;
     double rx = nom * b.x / denom;
     double ry = nom * b.y / denom;

     return new Point2D.Double(rx, ry);
   }



   private AnchorInfo getIntersection(Rectangle component, Line2D.Double line) {
     int x3 = component.x;
     int x4 = component.x + component.width;
     int y3 = component.y;
     int y4 = component.y + component.height;

     Line2D.Double lineR1 = new Line2D.Double(x3, y3, x4, y3);
     Line2D.Double lineR2 = new Line2D.Double(x4, y3, x4, y4);
     Line2D.Double lineR3 = new Line2D.Double(x4, y4, x3, y4);
     Line2D.Double lineR4 = new Line2D.Double(x3, y4, x3, y3);






     double x = 0.0D;
     double y = 0.0D;

     double distance = Double.MAX_VALUE;

     int side = 2;

     if (lineR1.intersectsLine(line)) {

       double _x3 = x3;
       double _y3 = y3;
       double _x4 = x4;
       double _y4 = y3;

       double ua = ((_x4 - _x3) * (line.y1 - _y3) - (_y4 - _y3) * (line.x1 - _x3)) / ((_y4 - _y3) * (line.x2 - line.x1) - (_x4 - _x3) * (line.y2 - line.y1));
       double ub = ((line.x2 - line.x1) * (line.y1 - _y3) - (line.y2 - line.y1) * (line.x1 - _x3)) / ((_y4 - _y3) * (line.x2 - line.x1) - (_x4 - _x3) * (line.y2 - line.y1));

       if (ua >= 0.0D && ua <= 1.0D && ub >= 0.0D && ub <= 1.0D) {

         double xt = line.x1 + ua * (line.x2 - line.x1);
         double yt = line.y1 + ua * (line.y2 - line.y1);

         double dt = abs(new Point2D.Double(line.x2 - xt, line.y2 - yt));
         if (dt < distance) {

           x = xt;
           y = yt;
           distance = dt;
           side = 2;
         }
       }
     }

     if (lineR2.intersectsLine(line)) {

       double _x3 = x4;
       double _y3 = y3;
       double _x4 = x4;
       double _y4 = y4;

       double ua = ((_x4 - _x3) * (line.y1 - _y3) - (_y4 - _y3) * (line.x1 - _x3)) / ((_y4 - _y3) * (line.x2 - line.x1) - (_x4 - _x3) * (line.y2 - line.y1));
       double ub = ((line.x2 - line.x1) * (line.y1 - _y3) - (line.y2 - line.y1) * (line.x1 - _x3)) / ((_y4 - _y3) * (line.x2 - line.x1) - (_x4 - _x3) * (line.y2 - line.y1));

       if (ua >= 0.0D && ua <= 1.0D && ub >= 0.0D && ub <= 1.0D) {

         double xt = line.x1 + ua * (line.x2 - line.x1);
         double yt = line.y1 + ua * (line.y2 - line.y1);

         double dt = abs(new Point2D.Double(line.x2 - xt, line.y2 - yt));
         if (dt < distance) {

           x = xt;
           y = yt;
           distance = dt;
           side = 4;
         }
       }
     }


     if (lineR3.intersectsLine(line)) {

       double _x3 = x4;
       double _y3 = y4;
       double _x4 = x3;
       double _y4 = y4;

       double ua = ((_x4 - _x3) * (line.y1 - _y3) - (_y4 - _y3) * (line.x1 - _x3)) / ((_y4 - _y3) * (line.x2 - line.x1) - (_x4 - _x3) * (line.y2 - line.y1));
       double ub = ((line.x2 - line.x1) * (line.y1 - _y3) - (line.y2 - line.y1) * (line.x1 - _x3)) / ((_y4 - _y3) * (line.x2 - line.x1) - (_x4 - _x3) * (line.y2 - line.y1));

       if (ua >= 0.0D && ua <= 1.0D && ub >= 0.0D && ub <= 1.0D) {

         double xt = line.x1 + ua * (line.x2 - line.x1);
         double yt = line.y1 + ua * (line.y2 - line.y1);

         double dt = abs(new Point2D.Double(line.x2 - xt, line.y2 - yt));
         if (dt < distance) {

           x = xt;
           y = yt;
           distance = dt;
           side = 16;
         }
       }
     }


     if (lineR4.intersectsLine(line)) {

       double _x3 = x3;
       double _y3 = y4;
       double _x4 = x3;
       double _y4 = y3;

       double ua = ((_x4 - _x3) * (line.y1 - _y3) - (_y4 - _y3) * (line.x1 - _x3)) / ((_y4 - _y3) * (line.x2 - line.x1) - (_x4 - _x3) * (line.y2 - line.y1));
       double ub = ((line.x2 - line.x1) * (line.y1 - _y3) - (line.y2 - line.y1) * (line.x1 - _x3)) / ((_y4 - _y3) * (line.x2 - line.x1) - (_x4 - _x3) * (line.y2 - line.y1));

       if (ua >= 0.0D && ua <= 1.0D && ub >= 0.0D && ub <= 1.0D) {

         double xt = line.x1 + ua * (line.x2 - line.x1);
         double yt = line.y1 + ua * (line.y2 - line.y1);

         double dt = abs(new Point2D.Double(line.x2 - xt, line.y2 - yt));
         if (dt < distance) {

           x = xt;
           y = yt;
           distance = dt;
           side = 8;
         }
       }
     }


     return new AnchorInfo((int)x, (int)y, side);
   }



   public AutomaticConnectorLayouter(Figure figure1, Figure figure2) {
     super(figure1, figure2);
   }



   public void layoutConnectors(JComponent parent) {
     Rectangle rect = getConnectorBounds();
     if (rect != null)
     {
       RepaintManager.currentManager(parent).addDirtyRegion(parent, rect.x, rect.y, rect.width, rect.height);
     }


     if (getFigure1() == getFigure2()) {

       for (Iterator<Connector> iterator1 = getConnectorList().iterator(); iterator1.hasNext(); ) {

         Connector connector = iterator1.next();
         connector.getStartFigureAnchor().setLocation(new Point(0, 0));
         connector.getEndFigureAnchor().setLocation(new Point(0, 0));
         connector.setPaintable(false);
       }

       return;
     }
     Rectangle rect1 = getFigure1().getConnectorBounds(parent);
     Rectangle rect2 = getFigure2().getConnectorBounds(parent);

     rect1.grow(1, 1);
     rect2.grow(1, 1);


     if (rect1.intersects(rect2)) {

       for (Iterator<Connector> iterator1 = getConnectorList().iterator(); iterator1.hasNext(); ) {

         Connector connector = iterator1.next();
         connector.getStartFigureAnchor().setLocation(new Point(0, 0));
         connector.getEndFigureAnchor().setLocation(new Point(0, 0));
         connector.setPaintable(false);
       }

       return;
     }

     Rectangle figure1Bounds = getFigure1().getConnectorBounds(parent);
     Rectangle figure2Bounds = getFigure2().getConnectorBounds(parent);

     PointTuple[] points = getLines(figure1Bounds, figure2Bounds, getConnectorList().size());

     int i = 0;
     int incr = 1;

     for (Iterator<Connector> iterator = getConnectorList().iterator(); iterator.hasNext(); ) {

       Connector connector = iterator.next();

       if (connector.getType() == 1) {

         if (connector.getStartFigureAnchor().getFigure() == getFigure1()) {

           connector.getStartFigureAnchor().setLocation((Point)points[i].getP1());
           connector.getEndFigureAnchor().setLocation((Point)points[i].getP2());
         }
         else {

           connector.getStartFigureAnchor().setLocation((Point)points[i].getP2());
           connector.getEndFigureAnchor().setLocation((Point)points[i].getP1());
         }

         connector.setPaintable(true);
       }
       else if (connector.getType() == 2) {

         if (connector.getStartFigureAnchor().getFigure() == getFigure1()) {

           layoutManhattan(connector, points[i].getP1(), points[i].getP2());
         }
         else {

           layoutManhattan(connector, points[i].getP2(), points[i].getP1());
         }

         connector.setPaintable(true);
       }

       i += incr;
     }

     rect = getConnectorBounds();
     if (rect != null)
     {
       RepaintManager.currentManager(parent).addDirtyRegion(parent, rect.x, rect.y, rect.width, rect.height);
     }
   }



   private void layoutManhattan(Connector connector, AnchorInfo p1, AnchorInfo p2) {
     connector.resetAnchorList();

     connector.getStartFigureAnchor().setLocation((Point)p1);
     connector.getEndFigureAnchor().setLocation((Point)p2);


     if (p1.getConstraint() == p2.getConstraint()) {


       connector.setPaintable(false);

     }
     else {

       if (p1.getConstraint() == 2 && p2.getConstraint() == 8) {

         AnchorFree bendAnchor = new AnchorFree(new Point(p1.x, p2.y));
         connector.getAnchorList().add(1, bendAnchor);
       }
       else if (p1.getConstraint() == 2 && p2.getConstraint() == 4) {

         AnchorFree bendAnchor = new AnchorFree(new Point(p1.x, p2.y));
         connector.getAnchorList().add(1, bendAnchor);
       }
       else if (p1.getConstraint() == 16 && p2.getConstraint() == 8) {

         AnchorFree bendAnchor = new AnchorFree(new Point(p1.x, p2.y));
         connector.getAnchorList().add(1, bendAnchor);
       }
       else if (p1.getConstraint() == 16 && p2.getConstraint() == 4) {

         AnchorFree bendAnchor = new AnchorFree(new Point(p1.x, p2.y));
         connector.getAnchorList().add(1, bendAnchor);

       }
       else if (p2.getConstraint() == 2 && p1.getConstraint() == 8) {

         AnchorFree bendAnchor = new AnchorFree(new Point(p2.x, p1.y));
         connector.getAnchorList().add(1, bendAnchor);
       }
       else if (p2.getConstraint() == 2 && p1.getConstraint() == 4) {

         AnchorFree bendAnchor = new AnchorFree(new Point(p2.x, p1.y));
         connector.getAnchorList().add(1, bendAnchor);
       }
       else if (p2.getConstraint() == 16 && p1.getConstraint() == 8) {

         AnchorFree bendAnchor = new AnchorFree(new Point(p2.x, p1.y));
         connector.getAnchorList().add(1, bendAnchor);
       }
       else if (p2.getConstraint() == 16 && p1.getConstraint() == 4) {

         AnchorFree bendAnchor = new AnchorFree(new Point(p2.x, p1.y));
         connector.getAnchorList().add(1, bendAnchor);
       }


       if (p1.getConstraint() == 2 && p2.getConstraint() == 16) {

         AnchorFree bendAnchor1 = new AnchorFree(new Point(p1.x, (p1.y + p2.y) / 2));
         AnchorFree bendAnchor2 = new AnchorFree(new Point(p2.x, (p1.y + p2.y) / 2));
         connector.getAnchorList().add(1, bendAnchor2);
         connector.getAnchorList().add(1, bendAnchor1);
       }
       else if (p1.getConstraint() == 8 && p2.getConstraint() == 4) {

         AnchorFree bendAnchor1 = new AnchorFree(new Point((p1.x + p2.x) / 2, p1.y));
         AnchorFree bendAnchor2 = new AnchorFree(new Point((p1.x + p2.x) / 2, p2.y));
         connector.getAnchorList().add(1, bendAnchor2);
         connector.getAnchorList().add(1, bendAnchor1);
       }
       else if (p2.getConstraint() == 2 && p1.getConstraint() == 16) {

         AnchorFree bendAnchor1 = new AnchorFree(new Point(p2.x, (p2.y + p1.y) / 2));
         AnchorFree bendAnchor2 = new AnchorFree(new Point(p1.x, (p2.y + p1.y) / 2));
         connector.getAnchorList().add(1, bendAnchor1);
         connector.getAnchorList().add(1, bendAnchor2);
       }
       else if (p2.getConstraint() == 8 && p1.getConstraint() == 4) {

         AnchorFree bendAnchor1 = new AnchorFree(new Point((p2.x + p1.x) / 2, p2.y));
         AnchorFree bendAnchor2 = new AnchorFree(new Point((p2.x + p1.x) / 2, p1.y));
         connector.getAnchorList().add(1, bendAnchor1);
         connector.getAnchorList().add(1, bendAnchor2);
       }
     }
   }
 }


