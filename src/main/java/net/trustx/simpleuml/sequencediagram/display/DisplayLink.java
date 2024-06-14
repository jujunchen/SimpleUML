 package net.trustx.simpleuml.sequencediagram.display;

 import java.awt.Color;
 import java.awt.FontMetrics;
 import java.awt.Graphics2D;
 import java.awt.Paint;
 import java.awt.Point;

















 public class DisplayLink
 {
   private Paint fillColor = new Color(255, 252, 200);
   private Paint textColor = Color.black;
   private Paint linkColor = Color.black;

   DisplayObjectInfo from;
   DisplayObjectInfo to;
   TextBox textBox;
   private TextBox commentBox;
   int textXOffset = -1;

   int y = -1;
   int seq = -1;
   int callNumber = -1;

   int lineStartX = -1;
   int lineEndX = -1;

   private boolean highlight;

   boolean showComment;

   static final int BUFFER = 3;

   public static final int FUDGE_AREA = 8;

   public static final int CLOSED_COMMENT_SIZE = 5;

   private int methodHeight;


   DisplayLink(int callNumber, String name, DisplayObjectInfo from, DisplayObjectInfo to, int seq, String comment, boolean showComment) {
     this.from = from;
     this.to = to;
     this.seq = seq;
     this.textBox = new TextBox(name);
     this.commentBox = new TextBox(comment);
     this.showComment = showComment;
     this.callNumber = callNumber;
     this.methodHeight = getHeight();
   }



   void initText(Graphics2D g2) {
     this.textBox.init(g2, this.callNumber + ". ");
   }



   void initComment(Graphics2D g2) {
     this.commentBox.init(g2, getCommentWidth());
   }




   void initLine() {
     if (this.from.getSeq() < this.to.getSeq()) {

       this.lineStartX = this.from.getRightX(this.seq);
       this.lineEndX = this.to.getLeftX(this.seq);
     }
     else {

       this.lineStartX = this.from.getLeftX(this.seq);
       this.lineEndX = this.to.getRightX(this.seq);
     }

     int gap = this.lineEndX - this.lineStartX;
     this.textXOffset = gap - this.textBox.getWidth();
   }



   public void setHighlight() {
     this.highlight = true;
   }



   public void clearHighlight() {
     this.highlight = false;
   }



   public DisplayObjectInfo getFrom() {
     return this.from;
   }



   public DisplayObjectInfo getTo() {
     return this.to;
   }



   public int getSeq() {
     return this.seq;
   }



   void setY(int y) {
     this.y = y;
   }



   int getY() {
     return this.y;
   }



   int getWidth() {
     return this.textBox.getWidth();
   }



   int getHeight() {
     int ret = this.textBox.getHeight();
     return ret;
   }



   int getMinHeight() {
     int ret = getHeight();
     if (this.showComment && this.methodHeight < this.commentBox.getHeight())
       ret = this.commentBox.getHeight() + 12;
     return ret;
   }



   boolean isSelfCall() {
     return (this.from.getSeq() == this.to.getSeq());
   }



   int paint(Graphics2D g2, int currentHeight) {
     int diagramHeight = currentHeight;
     drawText(g2);

     drawLine(g2);

     drawArrow(g2);

     if (this.highlight) {
       drawHighlight(g2);
     }
     if (this.showComment) {
       diagramHeight = drawComment(g2, diagramHeight);
     } else if (this.commentBox != null && this.commentBox.getText() != null && this.commentBox.getText().length() > 0) {

       g2.setPaint(this.fillColor);
       g2.fillRect(getCommentXStart(), getCommentYStart(), 5, 5);
       g2.setPaint(this.textColor);
       g2.drawRect(getCommentXStart(), getCommentYStart(), 5, 5);
     }





     return diagramHeight;
   }




   void drawText(Graphics2D g2) {
     g2.setPaint(this.textColor);
     g2.drawString(this.textBox.getLine(0), this.lineStartX + this.textXOffset + this.textBox.getPad(), getY() + this.textBox.getTextOffset());
   }





   void drawLine(Graphics2D g2) {
     int lineY = getY() + getHeight();
     g2.setPaint(this.linkColor);
     g2.drawLine(this.lineStartX, lineY, this.lineEndX, lineY);
   }




   void drawHighlight(Graphics2D g2) {
     int lineY = getY() + getHeight();
     g2.setPaint(this.linkColor);
     g2.drawLine(this.lineStartX, lineY + 1, this.lineEndX, lineY);

     g2.drawLine(this.lineStartX, lineY - 1, this.lineEndX, lineY);
   }




   void drawArrow(Graphics2D g2) {
     int arrowTailX = this.lineEndX;

     if (this.lineStartX < this.lineEndX) {
       arrowTailX -= 4;
     } else {
       arrowTailX += 4;
     }
     int lineY = getY() + getHeight();
     g2.drawLine(arrowTailX, lineY - 3, this.lineEndX, lineY);

     g2.drawLine(arrowTailX, lineY + 3, this.lineEndX, lineY);
   }




   public int getStartX() {
     return this.lineStartX;
   }



   public int getEndX() {
     return this.lineEndX;
   }



   private int drawComment(Graphics2D g2, int diagramHeight) {
     FontMetrics fm = g2.getFontMetrics();
     int boxXStart = getCommentXStart();
     int boxYStart = getCommentYStart();
     int height = diagramHeight;
     int fontHeight = fm.getMaxAscent() + fm.getMaxDescent();
     if (this.commentBox.getLineCount() > 0) {

       g2.setPaint(this.fillColor);
       g2.fillRect(boxXStart, boxYStart, getCommentWidth() + 1, this.commentBox.getHeight());

       g2.setPaint(this.textColor);
       g2.drawRect(boxXStart, boxYStart, getCommentWidth(), this.commentBox.getHeight());

       for (int i = 0; i < this.commentBox.getLineCount(); i++)
       {

         g2.drawString(this.commentBox.getLine(i), boxXStart + 3, boxYStart + fontHeight * (i + 1));
       }


       height += this.commentBox.getHeight();
     }
     return height;
   }



   private int getCommentXStart() {
     int x = 0;
     if (getStartX() < getEndX()) {
       x = getStartX() + 3;
     } else {
       x = getEndX() + 3;
     }  return x;
   }



   private int getCommentYStart() {
     return getY() + getHeight() + 3;
   }



   private int getCommentHeight() {
     return this.commentBox.getHeight();
   }



   private int getCommentWidth() {
     int width = -1;
     if (getEndX() > getStartX()) {
       width = getEndX() - getStartX() - 3 - 4;
     } else {
       width = getStartX() - getEndX() + 3 - 4;
     }  return width;
   }



   public boolean showingCommentsAt(Point p) {
     boolean ret = false;
     if (this.showComment && getCommentYStart() <= p.getY() && (getCommentYStart() + getCommentHeight()) >= p.getY() && getCommentXStart() < p.getX() && (getCommentXStart() + getCommentWidth()) >= p.getX()) {



       ret = true;
     } else if (!this.showComment && getCommentYStart() <= p.getY() && (getCommentYStart() + 5) >= p.getY() && getCommentXStart() < p.getX() && (getCommentXStart() + 5) >= p.getX()) {



       ret = true;
     }  return ret;
   }





   private boolean closeEnough(int y, Point selected) {
     if (y == selected.getY())
       return true;
     if (y > selected.getY() && y - selected.getY() < 8.0D)
       return true;
     if (selected.getY() > y && selected.getY() - y < 8.0D)
       return true;
     return false;
   }




   public boolean showingAt(Point p) {
     boolean ret = false;
     if (p != null && closeEnough(getY() + getHeight(), p) && ((getStartX() < getEndX() && getStartX() < p.getX() && getEndX() > p.getX()) || (getStartX() > getEndX() && getStartX() > p.getX() && getEndX() < p.getX())))
     {

       ret = true; }
     return ret;
   }



   public void setMethodHeight(int height) {
     this.methodHeight = height;
   }
 }


