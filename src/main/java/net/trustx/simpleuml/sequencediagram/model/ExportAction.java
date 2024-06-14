 package net.trustx.simpleuml.sequencediagram.model;

 import com.intellij.openapi.actionSystem.AnAction;
 import com.intellij.openapi.actionSystem.AnActionEvent;
 import java.awt.Component;
 import java.awt.Dimension;
 import java.awt.image.BufferedImage;
 import java.io.File;
 import java.io.IOException;
 import javax.imageio.ImageIO;
 import javax.swing.JFileChooser;
 import javax.swing.filechooser.FileFilter;
 import net.trustx.simpleuml.sequencediagram.components.SequenceDiagramComponent;
 import net.trustx.simpleuml.sequencediagram.display.Display;

























 public class ExportAction
   extends AnAction
 {
   private Display display;
   private SequenceDiagramComponent diagramComponent;

   public ExportAction(SequenceDiagramComponent component, Display display) {
     super("Export Image...");
     this.display = display;
     this.diagramComponent = component;
   }



   public void actionPerformed(AnActionEvent e) {
     JFileChooser chooser = new JFileChooser();
     String dir = this.diagramComponent.getFolderURL();
     if (dir.startsWith("file://"))
       dir = this.diagramComponent.getFolderURL().substring("file://".length()); 
     chooser.setCurrentDirectory(new File(dir));
     chooser.setDialogType(1);
     chooser.setDialogTitle("Save Diagram Graphic");
     FileFilter filter = new pngFilter();
     chooser.setFileFilter(filter);
     int returnVal = chooser.showSaveDialog((Component)this.display);
     if (returnVal == 0) {

       File f = chooser.getSelectedFile();
       String filename = chooser.getSelectedFile().getName();
       if (!filename.endsWith(".png"))
         f = new File(f.getPath() + ".png"); 
       export(f);
     }
   }



   private void export(File file) {
     Dimension size = this.display.getPreferredSize();
     BufferedImage bi = new BufferedImage(size.width, size.height, 2);


     this.display.paintComponent(bi.createGraphics());

     try {
       ImageIO.write(bi, "png", file);
     }
     catch (IOException ioe) {

       ioe.printStackTrace();
     }
   }

   private class pngFilter
     extends FileFilter {
     private pngFilter() {}

     public String getDescription() {
       return ".png files";
     }



     public boolean accept(File pathname) {
       return (pathname.getName().endsWith(".png") || pathname.isDirectory());
     }
   }
 }


