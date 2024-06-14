 package net.trustx.simpleuml.gef.anchor;

 import java.awt.Point;






 public class AnchorInfo
   extends Point
 {
   private int constraint;

   public AnchorInfo(int x, int y, int constraint) {
     super(x, y);
     this.constraint = constraint;
   }



   public int getConstraint() {
     return this.constraint;
   }



   public boolean hasOppositeConstraint(AnchorInfo p2) {
     return ((getConstraint() == 2 && p2.getConstraint() == 16) || (getConstraint() == 16 && p2.getConstraint() == 2) || (getConstraint() == 4 && p2.getConstraint() == 8) || (getConstraint() == 8 && p2.getConstraint() == 4));
   }
 }


