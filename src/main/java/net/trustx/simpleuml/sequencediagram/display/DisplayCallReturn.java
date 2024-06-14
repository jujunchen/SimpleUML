 package net.trustx.simpleuml.sequencediagram.display;

 import java.awt.BasicStroke;
 import java.awt.Graphics2D;
 import java.awt.Stroke;




















 public class DisplayCallReturn
   extends DisplayLink
 {
   Stroke dashStroke = new BasicStroke(1.0F, 2, 0, 10.0F, new float[] { 10.0F, 5.0F }, 0.0F);










   DisplayCallReturn(String name, DisplayObjectInfo from, DisplayObjectInfo to, int seq, String comment, boolean showComment) {
     super(0, name, from, to, seq, comment, showComment);
     from.addCallReturn(this);
   }



   void drawText(Graphics2D g2) {}


   void drawLine(Graphics2D g2) {
     Stroke oldStroke = g2.getStroke();
     g2.setStroke(this.dashStroke);
     super.drawLine(g2);
     g2.setStroke(oldStroke);
   }


   int getMinHeight() {
     return (int)(super.getMinHeight() * 1.5D);
   }
 }


