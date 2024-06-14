 package net.trustx.simpleuml.components;

 import java.awt.Component;
 import java.awt.Dimension;
 import java.awt.Image;
 import java.awt.Rectangle;
 import javax.swing.JComponent;
 import javax.swing.RepaintManager;


 public class DebugRepaintManager
   extends RepaintManager
 {
   public synchronized void addDirtyRegion(JComponent c, int x, int y, int w, int h) {
     if (c.getClass().getName().indexOf("com.intellij") == -1)
     {
       System.out.println("DebugRepaintManager.addDirtyRegion: " + x + " " + y + " " + w + " " + h + " c = " + c);
     }
     super.addDirtyRegion(c, x, y, w, h);
   }



   public synchronized void addInvalidComponent(JComponent invalidComponent) {
     System.out.println("DebugRepaintManager.addInvalidComponent");
     super.addInvalidComponent(invalidComponent);
   }



   protected Object clone() throws CloneNotSupportedException {
     System.out.println("DebugRepaintManager.clone");
     return super.clone();
   }



   public boolean equals(Object obj) {
     System.out.println("DebugRepaintManager.equals");
     return super.equals(obj);
   }



   protected void finalize() throws Throwable {
     System.out.println("DebugRepaintManager.finalize");
     super.finalize();
   }



   public Rectangle getDirtyRegion(JComponent aComponent) {
     System.out.println("DebugRepaintManager.getDirtyRegion");
     return super.getDirtyRegion(aComponent);
   }




   public Dimension getDoubleBufferMaximumSize() {
     return super.getDoubleBufferMaximumSize();
   }




   public Image getOffscreenBuffer(Component c, int proposedWidth, int proposedHeight) {
     return super.getOffscreenBuffer(c, proposedWidth, proposedHeight);
   }




   public Image getVolatileOffscreenBuffer(Component c, int proposedWidth, int proposedHeight) {
     return super.getVolatileOffscreenBuffer(c, proposedWidth, proposedHeight);
   }



   public int hashCode() {
     System.out.println("DebugRepaintManager.hashCode");
     return super.hashCode();
   }



   public boolean isCompletelyDirty(JComponent aComponent) {
     System.out.println("DebugRepaintManager.isCompletelyDirty");
     return super.isCompletelyDirty(aComponent);
   }




   public boolean isDoubleBufferingEnabled() {
     return super.isDoubleBufferingEnabled();
   }



   public void markCompletelyClean(JComponent aComponent) {
     System.out.println("DebugRepaintManager.markCompletelyClean");
     super.markCompletelyClean(aComponent);
   }



   public void markCompletelyDirty(JComponent aComponent) {
     System.out.println("DebugRepaintManager.markCompletelyDirty");
     super.markCompletelyDirty(aComponent);
   }



   public void paintDirtyRegions() {
     System.out.println("DebugRepaintManager.paintDirtyRegions");
     super.paintDirtyRegions();
   }



   public synchronized void removeInvalidComponent(JComponent component) {
     System.out.println("DebugRepaintManager.removeInvalidComponent");
     super.removeInvalidComponent(component);
   }



   public DebugRepaintManager() {
     System.out.println("DebugRepaintManager.DebugRepaintManager");
   }



   public void setDoubleBufferingEnabled(boolean aFlag) {
     System.out.println("DebugRepaintManager.setDoubleBufferingEnabled");
     super.setDoubleBufferingEnabled(aFlag);
   }



   public void setDoubleBufferMaximumSize(Dimension d) {
     System.out.println("DebugRepaintManager.setDoubleBufferMaximumSize");
     super.setDoubleBufferMaximumSize(d);
   }



   public synchronized String toString() {
     System.out.println("DebugRepaintManager.toString");
     return super.toString();
   }



   public void validateInvalidComponents() {
     System.out.println("DebugRepaintManager.validateInvalidComponents");
     super.validateInvalidComponents();
   }
 }


