 package net.trustx.simpleuml.components;
 
 import com.intellij.openapi.util.DimensionService;
 import java.awt.Color;
 import java.awt.Dialog;
 import java.awt.Dimension;
 import java.awt.Frame;
 import java.awt.Point;
 import java.awt.event.ComponentAdapter;
 import java.awt.event.ComponentEvent;
 import java.awt.event.WindowAdapter;
 import java.awt.event.WindowEvent;
 import javax.swing.BorderFactory;
 import javax.swing.JDialog;
 import javax.swing.event.ChangeEvent;
 import javax.swing.event.ChangeListener;
 import net.trustx.simpleuml.plugin.UMLToolWindowPlugin;
 

 
 public class BirdViewFrame
   extends JDialog
   implements ChangeListener
 {
   private DiagramPreviewPanel previewPanel;
   private Birdviewable birdviewable;
   
   public BirdViewFrame() {
     init();
   }
 
 
   
   public BirdViewFrame(Frame frame) {
     super(frame, false);
     init();
   }
 
 
   
   public BirdViewFrame(Dialog dialog) {
     super(dialog, false);
     init();
   }
 
 
   
   private void init() {
     Point location = DimensionService.getInstance().getLocation(getClass().getName());
     Dimension size = DimensionService.getInstance().getSize(getClass().getName());
     
     if (location != null)
     {
       setLocation(location);
     }
     
     if (size == null)
     {
       size = new Dimension(100, 100);
     }
     
     setSize(size);
 
     
     this.previewPanel = new DiagramPreviewPanel();
     this.previewPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
     this.previewPanel.getScrollPane().setBorder(BorderFactory.createEmptyBorder());
     this.previewPanel.setCropImage(false);
     this.previewPanel.setTextAntialiasing(true);
     this.previewPanel.setAntialiasing(true);
     
     getContentPane().add(this.previewPanel, "Center");
     
     this.previewPanel.addComponentListener(new ComponentAdapter()
         {
           public void componentShown(ComponentEvent e)
           {
             BirdViewFrame.this.previewPanel.setScaleFactorToFit();
           }
 
 
           
           public void componentHidden(ComponentEvent e) {
             BirdViewFrame.this.previewPanel.setPreviewable((Previewable)null);
           }
 
 
           
           public void componentResized(ComponentEvent e) {
             BirdViewFrame.this.previewPanel.setScaleFactorToFit();
           }
         });
 
     
     addWindowListener(new WindowAdapter()
         {
           public void windowClosing(WindowEvent e)
           {
             BirdViewFrame.this.disposeBirdViewFrame(true);
           }
         });
   }
 
 
 
 
   
   public void disposeBirdViewFrame(boolean requestedByUser) {
     if (this.birdviewable != null) {
       
       this.birdviewable.removeChangeListener(this);
       DimensionService.getInstance().setLocation(getClass().getName(), getLocation());
       DimensionService.getInstance().setSize(getClass().getName(), getSize());
       UMLToolWindowPlugin.getUMLToolWindowPlugin(this.previewPanel.getPreviewable().getProject()).setBirdViewShouldBeVisible(!requestedByUser);
     } 
     super.dispose();
   }
 
 
   
   public void setCurrentBirdviewable(Birdviewable birdviewable) {
     if (this.birdviewable != null)
     {
       this.birdviewable.removeChangeListener(this);
     }
     
     this.previewPanel.setPreviewable(birdviewable);
     this.birdviewable = birdviewable;
     
     if (birdviewable != null) {
       
       birdviewable.addChangeListener(this);
       this.previewPanel.setDrawViewport(true);
     } 
     if (isShowing())
     {
       this.previewPanel.setScaleFactorToFit();
     }
   }
 
 
 
   
   public void dispose() {}
 
 
   
   public void stateChanged(ChangeEvent e) {
     if (isShowing())
     {
       this.previewPanel.setScaleFactorToFit();
     }
   }
 }


