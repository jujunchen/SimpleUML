 package net.trustx.simpleuml.gef.anchor;

 import java.awt.Point;
 import net.trustx.simpleuml.gef.Figure;





 public class FigureAnchor
   implements Anchor
 {
   private Figure figure;
   private Point location;
   private int type;
   private int constraint;

   public FigureAnchor(Figure figure) {
     this.figure = figure;
     this.location = new Point();
     this.type = 2;
     this.constraint = 1;
   }



   public Figure getFigure() {
     return this.figure;
   }



   public void setFigure(Figure figure) {
     this.figure = figure;
   }



   public Point getLocation() {
     return new Point(this.location);
   }



   public void setLocation(Point location) {
     this.location = location;
   }



   public int getType() {
     return this.type;
   }



   public void setType(int type) {
     this.type = type;
   }



   public boolean hasValidPosition() {
     if (this.type == 1)
     {
       return true;
     }
     if (this.type == 2)
     {
       return this.figure.isOnFigureBounds(this.location);
     }
     if (this.type == 4)
     {
       return this.figure.isCenteredOnSide(this.location);
     }
     return true;
   }



   public Point getNearestValidPosition() {
     return this.location;
   }



   public int getConstraint() {
     return this.constraint;
   }



   public void setConstraint(int constraint) {
     this.constraint = constraint;
   }
 }


