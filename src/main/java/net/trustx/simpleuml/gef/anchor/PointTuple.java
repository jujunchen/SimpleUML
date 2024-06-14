 package net.trustx.simpleuml.gef.anchor;
 
 

 
 
 public class PointTuple
 {
   private AnchorInfo p1;
   private AnchorInfo p2;
   
   public PointTuple(AnchorInfo p1, AnchorInfo p2) {
     this.p1 = p1;
     this.p2 = p2;
   }
 
 
   
   public AnchorInfo getP1() {
     return this.p1;
   }
 
 
   
   public void setP1(AnchorInfo p1) {
     this.p1 = p1;
   }
 
 
   
   public AnchorInfo getP2() {
     return this.p2;
   }
 
 
   
   public void setP2(AnchorInfo p2) {
     this.p2 = p2;
   }
 }


