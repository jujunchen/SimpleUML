 package net.trustx.simpleuml.sequencediagram.display;

 import java.awt.Color;
 import java.awt.Graphics2D;
 import java.awt.Paint;





















 public class MethodBox
 {
   private Paint lineColor = Color.black;

   private Paint boxColor = Color.white;

   private DisplayLink call;

   private DisplayLink callReturn;
   private int horizontalSeq;
   public static final int BOX_WIDTH = 8;

   MethodBox(DisplayLink call, DisplayLink callReturn) {
     this.call = call;
     this.callReturn = callReturn;
   }


   void setHorizontalSeq(int horizontalSeq) {
     this.horizontalSeq = horizontalSeq;
   }


   int getStartSeq() {
     return this.call.getSeq();
   }


   int getEndSeq() {
     return this.callReturn.getSeq();
   }


   void paint(Graphics2D g2) {
     int left = this.call.getTo().getCenterX() - 4 + 3 * this.horizontalSeq;
     int top = this.call.getY() + this.call.getHeight();
     int bottom = this.callReturn.getY() + this.callReturn.getHeight();
     if (bottom < top + this.call.getMinHeight())
       bottom = top + this.call.getMinHeight() + this.call.getHeight();
     g2.setPaint(this.boxColor);
     g2.fillRect(left, top, 9, bottom - top);

     g2.setPaint(this.lineColor);
     g2.drawRect(left, top, 8, bottom - top);
   }


   public int getHeight() {
     int top = this.call.getY() + this.call.getHeight();
     int bottom = this.callReturn.getY() + this.callReturn.getHeight();

     return bottom - top;
   }


   public String toString() {
     return "MethodBox call <" + this.call + "> return <" + this.callReturn + ">";
   }
 }


