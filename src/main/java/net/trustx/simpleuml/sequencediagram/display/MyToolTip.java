 package net.trustx.simpleuml.sequencediagram.display;
 
 import java.awt.Color;
 import java.awt.Dimension;
 import java.awt.Font;
 import java.awt.FontMetrics;
 import java.awt.Graphics;
 import java.awt.Rectangle;
 import java.util.Vector;
 import javax.swing.JPanel;
 import javax.swing.JWindow;
 import javax.swing.border.LineBorder;
 import javax.swing.text.View;
 
 
 
 
 
 class MyToolTip
   extends JWindow
 {
   private JPanel panel = new JPanel();
   private static final char HYPHEN = '-';
   private Display display;
   private FontMetrics fontMetrics;
   private static final int MAX_TT_WIDTH = 200;
   private Vector wText = new Vector(10);
 
 
   
   public void paint(Graphics g) {
     super.paint(g);
     Font font = this.panel.getFont();
     Dimension size = this.panel.getSize();
     if (this.panel.isOpaque()) {
       
       g.setColor(this.panel.getBackground());
       g.fillRect(1, 1, size.width - 2, size.height - 2);
     } 
     g.setColor(this.panel.getForeground());
     g.setFont(font);
     
     Rectangle paintTextR = new Rectangle(1, 1, size.width - 2, size.height - 2);
 
 
 
     
     View v = (View)this.panel.getClientProperty("html");
     if (v != null) {
       
       v.paint(g, paintTextR);
     }
     else {
       
       int yline = 0;
       for (int i = 0; i < this.wText.size(); i++) {
         
         yline = (paintTextR.y + this.fontMetrics.getAscent()) * (i + 1);
         g.drawString(this.wText.get(i), paintTextR.x + 3, yline);
       } 
     } 
   }
 
 
 
 
   
   MyToolTip(Display display) {
     this.display = display;
     getContentPane().add(this.panel);
     this.panel.setBackground(new Color(255, 252, 200));
     this.panel.setBorder(new LineBorder(Color.black));
     this.fontMetrics = this.panel.getFontMetrics(this.panel.getFont());
   }
 
 
   
   private Vector wrapText(String text, double lineSize) {
     double currentSize = 0.0D;
     int i = 0;
     int line = 0;
     int lastBlank = 0;
     int breakWidth = this.fontMetrics.charWidth('-');
     Vector<String> lines = new Vector(10);
     String fullString = text;
     while (i < fullString.length()) {
       
       int startIndex = i;
       currentSize = 0.0D;
       lastBlank = i;
       while (i < fullString.length() && currentSize + this.fontMetrics.charWidth(fullString.charAt(i)) + breakWidth < lineSize) {
         
         if (fullString.charAt(i) == ' ')
           lastBlank = i; 
         currentSize += this.fontMetrics.charWidth(fullString.charAt(i++));
       } 
       int j = i;
 
       
       while (j < fullString.length() && currentSize + this.fontMetrics.charWidth(fullString.charAt(j)) < lineSize && fullString.charAt(j) != ' ')
       {
         currentSize += this.fontMetrics.charWidth(fullString.charAt(j++));
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
 
 
   
   public void show(String text) {
     if (text.length() > 0) {
       
       int width = this.fontMetrics.stringWidth(text) + 8;
       int height = this.fontMetrics.getMaxAscent() + this.fontMetrics.getMaxDescent() + 2;
       if (width > 200) {
         
         this.wText = wrapText(text, 200.0D);
         height *= this.wText.size();
         width = 200;
       } else {
         
         this.wText.add(0, text);
       }  setSize(width, height);
       this.panel.setSize(width, height);
       this.panel.setPreferredSize(new Dimension(width, height));
       this.display.revalidate();
       setVisible(true);
     } 
   }
 }


