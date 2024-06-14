 package net.trustx.simpleuml.gef.anchor;

 import java.awt.Point;






 public class AnchorFree
   implements Anchor
 {
   private Point location;
   private int constraint;

   public AnchorFree(Point location) {
     this.location = location;
     this.constraint = 1;
   }



   public Point getLocation() {
     return new Point(this.location);
   }



   public void setLocation(Point location) {
     this.location = location;
   }



   public int getType() {
     return 1;
   }



   public boolean hasValidPosition() {
     return true;
   }



   public Point getNearestValidPosition() {
     return new Point(this.location);
   }



   public int getConstraint() {
     return this.constraint;
   }



   public void setConstraint(int constraint) {
     this.constraint = constraint;
   }



   public String toString() {
     return "AnchorFree: " + this.location;
   }
 }


