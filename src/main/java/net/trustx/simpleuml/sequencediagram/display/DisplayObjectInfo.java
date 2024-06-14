 package net.trustx.simpleuml.sequencediagram.display;

 import java.awt.Color;
 import java.awt.Graphics2D;
 import java.awt.Paint;
 import java.util.ArrayList;
 import java.util.Iterator;
 import java.util.List;























 public class DisplayObjectInfo
 {
   private Paint borderColor = Color.black;
   private static final Color TAN = new Color(244, 238, 196);
   private Paint fillColor = TAN;
   private Paint textColor = Color.black;
   private Paint lineColor = Color.lightGray;
   private Paint bottomTextColor = Color.black;

   private int x = -1;
   private int y = -1;
   private String text;
   private TextBox textBox;
   private int seq = -1;
   private int width = -1;

   private List calls = new ArrayList();
   private List returns = new ArrayList();
   private List methods = new ArrayList();



   DisplayObjectInfo(String text, int seq, boolean active) {
     if (active) {

       this.borderColor = Color.red;
       this.bottomTextColor = Color.blue;
     }
     else {

       this.borderColor = Color.black;
       this.bottomTextColor = this.textColor;
     }
     this.text = text;
     this.seq = seq;
     this.textBox = new TextBox(text);
   }



   public String getText() {
     return this.text;
   }



   void initOne(Graphics2D g2) {
     this.textBox.init(g2);
     for (Iterator<DisplayLink> iterator1 = this.calls.iterator(); iterator1.hasNext(); ) {

       DisplayLink l = iterator1.next();
       l.initText(g2);
     }
     for (Iterator<DisplayLink> it = this.returns.iterator(); it.hasNext();)
     {
       ((DisplayLink)it.next()).initText(g2);
     }
   }



   void translate(int increment) {
     this.x += increment;
   }



   void addCall(DisplayCall c) {
     this.calls.add(c);
   }



   void addCall(DisplaySelfCall c) {
     this.calls.add(c);
   }



   List getCalls() {
     return this.calls;
   }



   void addCallReturn(DisplayCallReturn cr) {
     this.returns.add(cr);
   }



   void addCallReturn(DisplaySelfCallReturn cr) {
     this.returns.add(cr);
   }



   void addMethod(MethodBox mb) {
     if (this.methods.isEmpty()) {

       mb.setHorizontalSeq(0);
     }
     else {

       int enclosingCount = 0;
       for (Iterator<MethodBox> it = this.methods.iterator(); it.hasNext(); ) {

         MethodBox otherMb = it.next();
         if (otherMb.getStartSeq() < mb.getStartSeq() && otherMb.getEndSeq() > mb.getEndSeq())
         {
           enclosingCount++; }
       }
       mb.setHorizontalSeq(enclosingCount);
     }
     this.methods.add(mb);
   }



   int getSeq() {
     return this.seq;
   }



   void setX(int x) {
     this.x = x;
   }



   int getX() {
     return this.x;
   }



   void setY(int y) {
     this.y = y;
   }



   int getY() {
     return this.y;
   }



   int getWidth() {
     if (this.width == -1) {
       return this.textBox.getWidth();
     }
     return this.width;
   }



   void setWidth(int width) {
     this.width = width;
   }



   int getTextWidth() {
     return this.textBox.getWidth();
   }



   int getHeight() {
     return this.textBox.getHeight();
   }



   int getCenterX() {
     return getX() + this.textBox.getWidth() / 2;
   }



   int getLeftX(int seq) {
     return getCenterX() - 4;
   }



   int getRightX(int seq) {
     return getCenterX() + getMethodDepth(seq) * 4;
   }



   int calcCurrentGap(DisplayObjectInfo inf, int verticalSeq) {
     if (getSeq() < inf.getSeq())
     {
       return inf.getLeftX(verticalSeq) - getRightX(verticalSeq);
     }


     return getLeftX(verticalSeq) - inf.getRightX(verticalSeq);
   }




   private int getMethodDepth(int seq) {
     int depth = 0;
     for (Iterator<MethodBox> it = this.methods.iterator(); it.hasNext(); ) {

       MethodBox mb = it.next();
       if (mb.getStartSeq() <= seq && mb.getEndSeq() >= seq)
         depth++;
     }
     return depth;
   }



   void paint(Graphics2D g2, int currentHeight) {
     g2.setPaint(this.lineColor);
     g2.drawLine(getCenterX(), this.y + getHeight(), getCenterX(), currentHeight - 10);
     g2.setPaint(this.fillColor);
     g2.fillRect(this.x, this.y, this.textBox.getWidth(), this.textBox.getHeight());


     g2.setPaint(this.borderColor);
     g2.drawRect(this.x, this.y, this.textBox.getWidth() - 1, this.textBox.getHeight() - 1);


     g2.setPaint(this.textColor);
     g2.drawString(this.text, this.x + this.textBox.getPad(), this.y + this.textBox.getTextOffset());



     g2.setPaint(this.bottomTextColor);
     g2.drawString(this.text, this.x + this.textBox.getPad(), currentHeight);


     for (Iterator<MethodBox> iterator = this.methods.iterator(); iterator.hasNext(); ) {

       MethodBox mb = iterator.next();
       mb.paint(g2);
     }
     for (Iterator<DisplayLink> iterator1 = this.calls.iterator(); iterator1.hasNext(); ) {

       DisplayLink l = iterator1.next();
       l.paint(g2, currentHeight);
     }
     for (Iterator<DisplayLink> it = this.returns.iterator(); it.hasNext();)
     {
       ((DisplayLink)it.next()).paint(g2, currentHeight);
     }
   }





   public String toString() {
     return "DisplayObjectInfo " + this.text + " seq " + this.seq;
   }
 }


