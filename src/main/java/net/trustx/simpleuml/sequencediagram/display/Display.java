 package net.trustx.simpleuml.sequencediagram.display;
 
 import java.awt.Color;
 import java.awt.Dimension;
 import java.awt.Graphics;
 import java.awt.Graphics2D;
 import java.awt.Insets;
 import java.awt.Point;
 import java.awt.RenderingHints;
 import java.io.IOException;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;
 import javax.swing.JPanel;
 import net.trustx.simpleuml.sequencediagram.model.Link;
 import net.trustx.simpleuml.sequencediagram.model.MethodInfo;
 import net.trustx.simpleuml.sequencediagram.model.Model;
 import net.trustx.simpleuml.sequencediagram.model.ModelTextEvent;
 import net.trustx.simpleuml.sequencediagram.model.ModelTextListener;
 import net.trustx.simpleuml.sequencediagram.model.ObjectInfo;
 import net.trustx.simpleuml.sequencediagram.util.Callstack;
 
 
 
 
 
 
 
 public class Display
   extends JPanel
   implements ModelTextListener
 {
   private Map hintsMap = new HashMap<Object, Object>();
   
   private int inset = 5;
   
   private boolean initialized;
   private List objectLifelines = new ArrayList();
   private List links = new ArrayList();
   private DisplayLink highLight;
   private Model model;
   private static final int MIN_CALL_HEIGHT = 100;
   private static final int MAX_CALL_WIDTH = 200;
   private MyToolTip toolTip = new MyToolTip(this);
 
 
 
 
 
   
   public Display(Model model) {
     this.hintsMap.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
 
     
     setPreferredSize(new Dimension(200, 200));
     setBackground(Color.WHITE);
     this.model = model;
   }
 
 
   
   public synchronized DisplayLink getCommentAt(Point p) {
     Iterator<DisplayLink> i = this.links.iterator();
     DisplayLink selected = null;
     while (i.hasNext()) {
       
       selected = i.next();
       if (selected.showingCommentsAt(p)) {
         break;
       }
       selected = null;
     } 
     return selected;
   }
 
 
   
   public synchronized DisplayLink getLinkAt(Point p) {
     Iterator<DisplayLink> i = this.links.iterator();
     DisplayLink selected = null;
     while (i.hasNext()) {
       
       selected = i.next();
       if (selected.showingAt(p)) {
         break;
       }
 
 
       
       selected = null;
     } 
     
     return selected;
   }
 
   
   public void highLight(int sequence) {
     if (this.highLight != null && this.highLight.getSeq() != sequence)
       this.highLight.clearHighlight(); 
     this.highLight = this.links.get(sequence);
     this.highLight.setHighlight();
     
     repaint();
   }
 
   
   public void ttComment(int sequence, Point location) {
     Link link = this.model.getLink(sequence);
     Point ttpoint = new Point((int)location.getX(), (int)(location.getY() + getLocationOnScreen().getY() + 20.0D));
     this.toolTip.setLocation(ttpoint);
     if (link.getCallComment() != null && !link.getCommentOnDiagram()) {
       this.toolTip.show(link.getCallComment());
     }
     repaint();
   }
 
   
   public void clearHighlight() {
     if (this.highLight != null) {
       this.highLight.clearHighlight();
       this.toolTip.setVisible(false);
       repaint();
     } 
   }
 
 
   
   public void init(String s) {
     Callstack p = new Callstack();
     
     try {
       p.parse(s);
     } catch (IOException ioe) {
       return;
     } 
 
     
     List theObjects = p.getObjects();
     List<DisplayObjectInfo> objectLifelines = new ArrayList();
     for (Iterator<ObjectInfo> it = theObjects.iterator(); it.hasNext(); ) {
       
       ObjectInfo inf = it.next();
       objectLifelines.add(new DisplayObjectInfo(inf.getName(), inf.getSeq(), inf.isActive()));
     } 
     
     List theDisplayLinks = p.getLinks();
     List<DisplayLink> links = new ArrayList();
     int seq = 0;
     int linkCount = 1;
     for (Iterator<Link> iterator = theDisplayLinks.iterator(); iterator.hasNext(); ) {
       
       Link l = iterator.next();
       int fromSeq = l.getFrom().getSeq();
       int toSeq = l.getTo().getSeq();
       DisplayObjectInfo fromObj = objectLifelines.get(fromSeq);
       DisplayObjectInfo toObj = objectLifelines.get(toSeq);
       DisplayLink dl = null;
       Link link = this.model.getLink(seq);
       String linkComment = (link == null) ? "" : link.getCallComment();
       boolean linkCommentShow = (link != null && link.getCommentOnDiagram());
       if (l instanceof net.trustx.simpleuml.sequencediagram.model.Call) {
         
         if (fromSeq == toSeq) {
           dl = new DisplaySelfCall(linkCount, l.getName(), fromObj, toObj, seq, linkComment, linkCommentShow);
         }
         else {
           
           dl = new DisplayCall(linkCount, l.getName(), fromObj, toObj, seq, linkComment, linkCommentShow);
         } 
         
         linkCount++;
       }
       else if (l instanceof net.trustx.simpleuml.sequencediagram.model.CallReturn) {
         
         if (fromSeq == toSeq) {
           dl = new DisplaySelfCallReturn(0, l.getName(), fromObj, toObj, seq, linkComment, linkCommentShow);
         }
         else {
           
           dl = new DisplayCallReturn(l.getName(), fromObj, toObj, seq, linkComment, linkCommentShow);
         } 
       } 
 
 
 
       
       if (dl != null) {
         
         links.add(dl);
         seq++;
       } 
     } 
     
     for (Iterator<ObjectInfo> iterator1 = theObjects.iterator(); iterator1.hasNext(); ) {
       
       ObjectInfo inf = iterator1.next();
       DisplayObjectInfo displayInf = objectLifelines.get(inf.getSeq());
       
       for (Iterator<MethodInfo> methodsIt = inf.getMethods().iterator(); methodsIt.hasNext(); ) {
         
         MethodInfo methodInf = methodsIt.next();
         int startSeq = methodInf.getStartSeq();
         int endSeq = methodInf.getEndSeq();
         if (startSeq < links.size() && endSeq < links.size()) {
           
           MethodBox mb = new MethodBox(links.get(startSeq), links.get(endSeq));
 
           
           displayInf.addMethod(mb);
         } 
       } 
     } 
     
     setValues(objectLifelines, links);
   }
 
   
   public void modelTextChanged(ModelTextEvent mte) {
     init(mte.getText());
   }
 
   
   private synchronized void setValues(List objectLifelines, List links) {
     this.objectLifelines = objectLifelines;
     this.links = links;
     this.initialized = false;
     repaint();
   }
 
   
   public synchronized void paintComponent(Graphics g) {
     super.paintComponent(g);
     
     Graphics2D g2 = (Graphics2D)g;
     
     g2.addRenderingHints(this.hintsMap);
     
     Insets insets = getInsets();
     int currentHeight = getHeight() - insets.top - insets.bottom;
     
     g2.translate(insets.left, insets.top);
     
     if (!this.initialized) {
       layoutItems(g2);
     }
     int max = this.objectLifelines.size();
     
     for (int i = max - 1; i >= 0; i--)
     {
       ((DisplayObjectInfo)this.objectLifelines.get(i)).paint(g2, currentHeight);
     }
   }
 
 
 
 
   
   private void layoutItems(Graphics2D g2) {
     int x = this.inset;
     int y = this.inset;
     int numberOfClasses = this.objectLifelines.size();
     x = layOutVerticalLines(numberOfClasses, x, y, g2);
     
     int maxX = 200;
     for (int i = 0; i < numberOfClasses; i++) {
       
       DisplayObjectInfo obj = this.objectLifelines.get(i);
       
       layoutHorizontalCalls(obj, i, numberOfClasses);
       maxX = obj.getX() + obj.getWidth() + this.inset;
     } 
     int currentLinkIndex = 0;
     if (this.objectLifelines.isEmpty()) {
       y = 100;
     } else {
       y += ((DisplayObjectInfo)this.objectLifelines.get(currentLinkIndex)).getHeight();
     }  y = layoutVerticalCalls(y, g2);
 
     
     setPreferredSize(new Dimension(maxX, y + 20));
     revalidate();
   }
 
   
   private int layoutVerticalCalls(int y, Graphics2D g2) {
     for (Iterator<DisplayLink> it = this.links.iterator(); it.hasNext(); ) {
       
       DisplayLink link = it.next();
       link.setY(y);
       link.initLine();
       link.initComment(g2);
       
       if (link.getMinHeight() < link.getHeight()) {
         y += link.getHeight() + 3; continue;
       } 
       y += link.getMinHeight() + 3;
     } 
     return y;
   }
 
   
   private void layoutHorizontalCalls(DisplayObjectInfo obj, int i, int numberOfClasses) {
     for (Iterator<DisplayLink> it = obj.getCalls().iterator(); it.hasNext(); ) {
       
       DisplayLink call = it.next();
       int availableGap = -1;
       if (call.isSelfCall()) {
         
         if (i == numberOfClasses - 1) {
           
           int width = obj.getWidth();
           if (width < call.getWidth()) {
             obj.setWidth(obj.getTextWidth() + call.getWidth());
           }
           
           continue;
         } 
         availableGap = obj.calcCurrentGap(this.objectLifelines.get(i + 1), call.getSeq());
 
       
       }
       else {
 
         
         availableGap = obj.calcCurrentGap(call.getTo(), call.getSeq());
       } 
 
       
       if (availableGap < call.getWidth())
       {
         growVerticalGap(call, availableGap, i, numberOfClasses);
       }
     } 
   }
 
   
   private void growVerticalGap(DisplayLink call, int availableGap, int i, int numberOfClasses) {
     int offset = call.getWidth() - availableGap;
     int startJ = i + 1;
     if (call.getTo().getSeq() < startJ)
       startJ = call.getTo().getSeq() + 1; 
     for (int j = startJ; j < numberOfClasses; j++)
     {
       ((DisplayObjectInfo)this.objectLifelines.get(j)).translate(offset);
     }
   }
 
   
   private int layOutVerticalLines(int numberOfClasses, int x, int y, Graphics2D g2) {
     for (int i = 0; i < numberOfClasses; i++) {
       
       DisplayObjectInfo obj = this.objectLifelines.get(i);
       obj.setX(x);
       obj.setY(y);
       obj.initOne(g2);
       x += obj.getWidth() + this.inset;
     } 
     return x;
   }
 }


