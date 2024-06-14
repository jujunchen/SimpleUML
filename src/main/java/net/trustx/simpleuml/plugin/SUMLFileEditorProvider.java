 package net.trustx.simpleuml.plugin;
 
 import com.intellij.openapi.application.ApplicationManager;
 import com.intellij.openapi.components.ApplicationComponent;
 import com.intellij.openapi.fileEditor.FileEditor;
 import com.intellij.openapi.fileEditor.FileEditorPolicy;
 import com.intellij.openapi.fileEditor.FileEditorProvider;
 import com.intellij.openapi.fileEditor.FileEditorState;
 import com.intellij.openapi.fileEditor.FileEditorStateLevel;
 import com.intellij.openapi.project.Project;
 import com.intellij.openapi.vfs.VirtualFile;
 import com.intellij.openapi.vfs.VirtualFileManager;
 import java.io.ByteArrayOutputStream;
 import java.io.IOException;
 import java.io.OutputStreamWriter;
 import net.trustx.simpleuml.components.DiagramComponent;
 import net.trustx.simpleuml.util.UMLUtils;
 import org.jdom.Document;
 import org.jdom.Element;
 import org.jdom.output.XMLOutputter;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class SUMLFileEditorProvider
   implements ApplicationComponent, FileEditorProvider
 {
   public FileEditor createEditor(Project project, VirtualFile virtualFile) {
     try {
       VirtualFile parent = virtualFile.getParent();
       if (parent == null)
       {
         return null;
       }
       
       DiagramComponent diagramComponent = DiagramFactory.createDiagramComponent(project, parent.getUrl(), virtualFile.getName());
       if (diagramComponent == null)
       {
         return null;
       }
       return new DiagramComponentFileEditorAdapter(project, diagramComponent);
     }
     catch (UnknownDiagramTypeException e) {
       
       e.printStackTrace();
       
       return null;
     } 
   }
 
   
   public FileEditorPolicy getPolicy() {
     return FileEditorPolicy.HIDE_DEFAULT_EDITOR;
   }
 
 
   
   public FileEditorState readState(Element element, Project project, VirtualFile virtualFile) {
     return new FileEditorState()
       {
         public boolean canBeMergedWith(FileEditorState otherState, FileEditorStateLevel level)
         {
           return false;
         }
       };
   }
 
 
   
   public void disposeEditor(FileEditor fileEditor) {
     if (fileEditor instanceof DiagramComponentFileEditorAdapter) {
       
       final DiagramComponentFileEditorAdapter diagramComponentFileEditorAdapter = (DiagramComponentFileEditorAdapter)fileEditor;
       
       final DiagramComponent diagramComponent = diagramComponentFileEditorAdapter.getDiagramComponent();
       String wholePath = UMLUtils.getWholePath(diagramComponent.getFolderURL(), diagramComponent.getDiagramName());
       
       final UMLToolWindowPlugin umlToolWindowPlugin = UMLToolWindowPlugin.getUMLToolWindowPlugin(diagramComponentFileEditorAdapter.getProject());
       umlToolWindowPlugin.getDiagramComponentMap().remove(wholePath);
       
       final Document document = diagramComponent.getDiagramSaver().saveToDocument();
       
       ApplicationManager.getApplication().runWriteAction(new Runnable()
           {
             
             public void run()
             {
               try {
                 VirtualFile vf = VirtualFileManager.getInstance().findFileByUrl(UMLUtils.getWholePath(diagramComponent.getFolderURL(), diagramComponent.getDiagramName()));
                 if (vf == null) {
                   
                   VirtualFile fileByUrl = VirtualFileManager.getInstance().findFileByUrl(diagramComponent.getFolderURL());
                   if (fileByUrl == null)
                   {
                     throw new IOException("Could not create file in " + diagramComponent.getFolderURL());
                   }
                   vf = fileByUrl.createChildData(this, diagramComponent.getDiagramName());
                 } 
                 
                 umlToolWindowPlugin.getUMLFileManager().addURL(vf.getUrl());
                 
                 if (diagramComponent.canSave()) {
                   
                   XMLOutputter outputter = new XMLOutputter();
                   ByteArrayOutputStream baos = new ByteArrayOutputStream(8192);
                   OutputStreamWriter osw = new OutputStreamWriter(baos);
                   outputter.output(document, osw);
                   osw.flush();
                   osw.close();
                   vf.setBinaryContent(baos.toByteArray());
                 } 
                 
                 diagramComponentFileEditorAdapter.getDiagramComponent().disposeUIResources();
               }
               catch (IOException e) {
                 
                 e.printStackTrace();
               } 
             }
           });
     } 
   }
 
 
 
 
 
   
   public String getEditorTypeId() {
     return "suml";
   }
 
 
 
   
   public void writeState(FileEditorState fileEditorState, Project project, Element element) {}
 
 
   
   public boolean accept(Project project, VirtualFile virtualFile) {
     if (virtualFile == null || !"suml".equals(virtualFile.getExtension()))
     {
       return false;
     }
     
     VirtualFile parent = virtualFile.getParent();
     if (parent == null) return false;
     
     return !UMLToolWindowPlugin.getUMLToolWindowPlugin(project).isDiagramComponentInToolWindow(parent.getUrl(), virtualFile.getName());
   }
 
 
   
   public String getComponentName() {
     return "simpleUML.SUMLFileEditorProvider";
   }
 
 
   
   public void initComponent() {
     ApplicationManager.getApplication().runWriteAction(new Runnable() {
           public void run() {}
         });
   }
   
   public void disposeComponent() {}
 }


