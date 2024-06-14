 package net.trustx.simpleuml.sequencediagram.display;
 
 import java.awt.FontMetrics;
 import java.awt.Graphics2D;
 import java.awt.geom.Rectangle2D;
 import java.util.Vector;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 class TextBox
 {
   private int pad = 5;
   
   private int boxWidth = -1;
   private int boxHeight = -1;
   private int textOffset = -1;
   private int maxWidth = -1;
   private static final String HYPHEN = "-";
   private Vector lines;
   private String text;
   private String prefix = "";
 
   
   TextBox(String text) {
     this.text = text;
   }
 
   
   int getPad() {
     return this.pad;
   }
 
   
   int getWidth() {
     return this.boxWidth;
   }
 
   
   int getHeight() {
     return this.boxHeight;
   }
 
   
   int getTextOffset() {
     return this.textOffset;
   }
 
   
   private double sizeOf(FontMetrics fm, Graphics2D g2, String s) {
     return fm.getStringBounds(s, g2).getWidth();
   }
 
   
   private double sizeOf(FontMetrics fm, Graphics2D g2, char c) {
     return sizeOf(fm, g2, "" + c);
   }
 
   
   void init(Graphics2D g2, int maxWidth, String prefix) {
     this.maxWidth = maxWidth;
     this.prefix = prefix;
     init(g2);
   }
 
   
   void init(Graphics2D g2, int maxWidth) {
     this.maxWidth = maxWidth;
     init(g2);
   }
 
   
   void init(Graphics2D g2, String prefix) {
     this.prefix = prefix;
     init(g2);
   }
 
   
   void init(Graphics2D g2) {
     if (this.text != null) {
       
       FontMetrics fm = g2.getFontMetrics();
       int height = fm.getMaxAscent() + fm.getMaxDescent();
       Rectangle2D rect = fm.getStringBounds(this.prefix + this.text, g2);
       this.boxWidth = (int)rect.getWidth() + this.pad * 2;
       this.textOffset = fm.getMaxAscent() + this.pad;
       if (this.maxWidth > 0 && this.boxWidth > this.maxWidth) {
         
         this.lines = wrapText(fm, g2, this.prefix + this.text, this.maxWidth);
         this.boxWidth = this.maxWidth;
       } else {
         
         this.lines = null;
       } 
       this.boxHeight = height * getLineCount() + this.pad * 2;
     } 
   }
 
   
   private Vector wrapText(FontMetrics fm, Graphics2D g2, String text, double lineSize) {
     double currentSize = 0.0D;
     int i = 0;
     int line = 0;
     int lastBlank = 0;
     int breakWidth = (int)sizeOf(fm, g2, "-");
     Vector<String> lines = new Vector(10);
     String fullString = text;
     while (i < fullString.length()) {
       
       int startIndex = i;
       currentSize = 0.0D;
       lastBlank = i;
       while (i < fullString.length() && currentSize + sizeOf(fm, g2, fullString.charAt(i)) + breakWidth < lineSize) {
         
         if (fullString.charAt(i) == ' ')
           lastBlank = i; 
         currentSize += sizeOf(fm, g2, fullString.charAt(i++));
       } 
       int j = i;
 
       
       while (j < fullString.length() && currentSize + sizeOf(fm, g2, fullString.charAt(j)) < lineSize && fullString.charAt(j) != ' ')
       {
         currentSize += sizeOf(fm, g2, fullString.charAt(j++));
       }
       String delim = "";
       if (j < fullString.length() && fullString.charAt(j) == ' ') {
         
         i = j;
         i++;
       }
       else if (i < fullString.length() && lastBlank != startIndex) {
         
         i = lastBlank;
         i++;
       }
       else if (i < fullString.length()) {
         delim = "-";
       }  lines.add(line++, fullString.substring(startIndex, i) + delim);
     } 
     return lines;
   }
 
   
   public String getLine(int lineNumber) {
     String ret = null;
     if (this.lines != null && this.lines.size() > lineNumber) {
       ret = (String) this.lines.get(lineNumber);
     } else {
       ret = this.prefix + this.text;
     } 
     return ret;
   }
 
   
   public String getText() {
     return this.text;
   }
 
   
   public int getLineCount() {
     int ret = 1;
     if (this.lines != null)
       ret = this.lines.size(); 
     return ret;
   }
 }


