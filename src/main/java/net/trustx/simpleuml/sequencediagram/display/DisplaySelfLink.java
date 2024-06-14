 package net.trustx.simpleuml.sequencediagram.display;
 
 import java.awt.Graphics2D;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class DisplaySelfLink
   extends DisplayLink
 {
   DisplaySelfLink(int callNumber, String name, DisplayObjectInfo from, DisplayObjectInfo to, int seq, String comment, boolean showComment) {
     super(callNumber, name, from, to, seq, comment, showComment);
   }
 
 
   
   void initLine() {
     this.lineStartX = this.from.getRightX(this.seq);
     this.lineEndX = this.lineStartX + this.textBox.getWidth();
     
     int gap = this.lineEndX - this.lineStartX;
     this.textXOffset = gap - this.textBox.getWidth();
   }
 
   
   void drawLine(Graphics2D g2) {
     super.drawLine(g2);
     int lineY = getY() + getHeight();
     int height = 0;
     height = getVerticalLineLength();
     g2.drawLine(this.lineEndX, lineY, this.lineEndX, lineY + height);
     
     g2.drawLine(this.lineStartX, lineY + height, this.lineEndX, lineY + height);
   }
 
 
 
   
   private int getVerticalLineLength() {
     int height = getHeight() / 2;
     if (getHeight() < getMinHeight())
       height = getMinHeight() - height; 
     return height;
   }
 
   
   int getMinHeight() {
     int ret = 0;
     if (this.showComment) {
       ret = super.getMinHeight() + getHeight() / 3;
     } else {
       ret = getHeight();
     }  return ret;
   }
 
   
   void drawArrow(Graphics2D g2) {
     int lineY = getY() + getVerticalLineLength() + getHeight();
     int arrowTailX = this.lineStartX + 4;
     g2.drawLine(arrowTailX, lineY - 3, this.lineStartX, lineY);
     
     g2.drawLine(arrowTailX, lineY + 3, this.lineStartX, lineY);
   }
 }


