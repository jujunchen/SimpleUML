 package net.trustx.simpleuml.sequencediagram.model;

 import com.intellij.openapi.actionSystem.AnAction;
 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.actionSystem.DataKeys;
 import com.intellij.openapi.project.Project;
 import java.awt.Component;
 import java.io.File;
 import java.io.FileNotFoundException;
 import java.io.FileOutputStream;
 import java.io.InputStream;
 import java.net.MalformedURLException;
 import java.net.URL;
 import javax.swing.JFileChooser;
 import javax.swing.filechooser.FileFilter;
 import javax.xml.transform.Result;
 import javax.xml.transform.Source;
 import javax.xml.transform.Transformer;
 import javax.xml.transform.TransformerConfigurationException;
 import javax.xml.transform.TransformerException;
 import javax.xml.transform.TransformerFactory;
 import javax.xml.transform.stream.StreamResult;
 import javax.xml.transform.stream.StreamSource;
 import net.trustx.simpleuml.plugin.UMLToolWindowPlugin;
 import net.trustx.simpleuml.sequencediagram.components.SequenceDiagramComponent;
 import net.trustx.simpleuml.sequencediagram.display.Display;
 import net.trustx.simpleuml.sequencediagram.util.Callstack;
 import net.trustx.simpleuml.util.UMLUtils;






















 public class PrintDocumentationAction
   extends AnAction
 {
   SequenceDiagramComponent diagramComponent;
   Display display;

   public PrintDocumentationAction(String name, SequenceDiagramComponent component, Display d) {
     super("Generate Documentation...");
     this.diagramComponent = component;
     this.display = d;
   }


   public void actionPerformed(AnActionEvent e) {
     Project project = (Project)DataKeys.PROJECT.getData(e.getDataContext());
     FileFilter filter = new htmlFilter();
     JFileChooser chooser = new JFileChooser();
     String dir = this.diagramComponent.getFolderURL();
     UMLToolWindowPlugin.getUMLToolWindowPlugin(project).saveDiagramComponents();

     if (dir.startsWith("file://"))
       dir = this.diagramComponent.getFolderURL().substring("file://".length()); 
     chooser.setCurrentDirectory(new File(dir));
     chooser.setDialogType(1);
     chooser.setDialogTitle("Generate Diagram Documentation");
     chooser.setFileFilter(filter);

     int returnVal = chooser.showSaveDialog((Component)this.display);
     if (returnVal == 0) {

       File outFile = chooser.getSelectedFile();
       String filename = chooser.getSelectedFile().getName();
       if (!filename.endsWith(".html")) {
         outFile = new File(outFile.getPath() + ".html");
       }
       try {
         URL savedFile = new URL(UMLUtils.getWholePath(this.diagramComponent.getFolderURL(), this.diagramComponent.getDiagramName()));
         Source xmlSource = new StreamSource(new File(savedFile.getFile()));
         InputStream xslLoc = Callstack.class.getResourceAsStream("/net/trustx/simpleuml/sequencediagram/util/Documentation.xsl");
         Source xsltSource = new StreamSource(xslLoc);
         FileOutputStream fos = new FileOutputStream(outFile);
         Result result = new StreamResult(fos);

         TransformerFactory factory = TransformerFactory.newInstance();
         Transformer trans = null;
         trans = factory.newTransformer(xsltSource);
         trans.transform(xmlSource, result);
       } catch (TransformerConfigurationException e1) {
         e1.printStackTrace();
       } catch (TransformerException e1) {
         e1.printStackTrace();
       } catch (FileNotFoundException e1) {
         e1.printStackTrace();
       } catch (MalformedURLException ex) {
         ex.printStackTrace();
       }
     }
   }

   private class htmlFilter
     extends FileFilter {
     private htmlFilter() {}

     public String getDescription() {
       return ".html files";
     }


     public boolean accept(File pathname) {
       return (pathname.getName().endsWith(".html") || pathname.isDirectory());
     }
   }
 }


