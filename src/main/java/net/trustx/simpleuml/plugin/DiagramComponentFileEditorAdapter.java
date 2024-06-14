 package net.trustx.simpleuml.plugin;

 import com.intellij.codeHighlighting.BackgroundEditorHighlighter;
 import com.intellij.ide.structureView.StructureViewBuilder;
 import com.intellij.openapi.fileEditor.FileEditor;
 import com.intellij.openapi.fileEditor.FileEditorLocation;
 import com.intellij.openapi.fileEditor.FileEditorState;
 import com.intellij.openapi.fileEditor.FileEditorStateLevel;
 import com.intellij.openapi.project.Project;
 import com.intellij.openapi.util.Key;
 import java.awt.Color;
 import java.awt.Component;
 import java.awt.event.ComponentAdapter;
 import java.awt.event.ComponentEvent;
 import java.beans.PropertyChangeListener;
 import javax.swing.BorderFactory;
 import javax.swing.JComponent;
 import javax.swing.JSplitPane;
 import javax.swing.border.EmptyBorder;
 import javax.swing.event.ChangeEvent;
 import javax.swing.event.ChangeListener;
 import net.trustx.simpleuml.components.Birdviewable;
 import net.trustx.simpleuml.components.DiagramComponent;
 import net.trustx.simpleuml.components.DiagramPreviewPanel;
 import net.trustx.simpleuml.components.Previewable;























 public class DiagramComponentFileEditorAdapter
   implements FileEditor
 {
   private Project project;
   private DiagramComponent diagramComponent;
   private JSplitPane splitPane;
   private DiagramPreviewPanel previewPanel;

   public DiagramComponentFileEditorAdapter(Project project, DiagramComponent diagramComponent) {
     this.project = project;
     this.diagramComponent = diagramComponent;

     Previewable previewable = diagramComponent.getPreviewable();
     this.previewPanel = new DiagramPreviewPanel(previewable);
     this.previewPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
     this.previewPanel.getScrollPane().setBorder(BorderFactory.createEmptyBorder());
     this.previewPanel.setCropImage(false);
     this.previewPanel.setTextAntialiasing(true);
     this.previewPanel.setAntialiasing(true);

     this.previewPanel.addComponentListener(new ComponentAdapter() {
           public void componentShown(ComponentEvent e) { DiagramComponentFileEditorAdapter.this.previewPanel.setScaleFactorToFit(); }
           public void componentHidden(ComponentEvent e) { DiagramComponentFileEditorAdapter.this.previewPanel.setPreviewable(null); } public void componentResized(ComponentEvent e) {
             DiagramComponentFileEditorAdapter.this.previewPanel.setScaleFactorToFit();
           }
         });
     if (previewable instanceof Birdviewable) {

       Birdviewable birdviewable = (Birdviewable)previewable;
       birdviewable.addChangeListener(new ChangeListener() {
             public void stateChanged(ChangeEvent e) { DiagramComponentFileEditorAdapter.this.previewPanel.setScaleFactorToFit(); }
           });
       this.previewPanel.setDrawViewport(true);
     }

     this.previewPanel.setPreviewable(previewable);

     this.splitPane = new JSplitPane(1, (Component)this.previewPanel, (Component)diagramComponent);
     this.splitPane.setDividerLocation(0);
     this.splitPane.setOneTouchExpandable(true);
     this.splitPane.setBorder(new EmptyBorder(0, 0, 0, 0));
   }



   public DiagramComponent getDiagramComponent() {
     return this.diagramComponent;
   }



   public Project getProject() {
     return this.project;
   }



   public JComponent getComponent() {
     return this.splitPane;
   }



   public JComponent getPreferredFocusedComponent() {
     return this.diagramComponent.getPreferredFocusedComponent();
   }



   public String getName() {
     return "simpleUML";
   }



   public FileEditorState getState(FileEditorStateLevel fileEditorStateLevel) {
     return new FileEditorState()
       {
         public boolean canBeMergedWith(FileEditorState otherState, FileEditorStateLevel level)
         {
           return false;
         }
       };
   }




   public void setState(FileEditorState fileEditorState) {}



   public boolean isModified() {
     return false;
   }



   public boolean isValid() {
     return true;
   }




   public void selectNotify() {}




   public void deselectNotify() {}




   public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {}



   public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {}



   public BackgroundEditorHighlighter getBackgroundHighlighter() {
     return null;
   }


   public FileEditorLocation getCurrentLocation() {
     return null;
   }


   public StructureViewBuilder getStructureViewBuilder() {
     return null;
   }


   public <T> T getUserData(Key<T> key) {
     return null;
   }

   public <T> void putUserData(Key<T> key, T value) {}

   public void dispose() {}
 }


