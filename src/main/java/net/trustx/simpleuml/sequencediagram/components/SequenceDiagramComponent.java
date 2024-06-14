 package net.trustx.simpleuml.sequencediagram.components;
 
 import com.intellij.openapi.actionSystem.ActionGroup;
 import com.intellij.openapi.actionSystem.AnAction;
 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.actionSystem.DefaultActionGroup;
 import com.intellij.openapi.project.Project;
 import com.intellij.psi.PsiElement;
 import java.awt.BorderLayout;
 import java.awt.Component;
 import java.awt.Dimension;
 import java.awt.EventQueue;
 import java.awt.Graphics2D;
 import java.awt.Point;
 import java.awt.Toolkit;
 import java.awt.event.MouseListener;
 import java.awt.event.MouseMotionListener;
 import java.beans.PropertyChangeEvent;
 import java.beans.PropertyChangeListener;
 import java.io.File;
 import java.util.Iterator;
 import java.util.LinkedList;
 import javax.swing.JComponent;
 import javax.swing.JFrame;
 import javax.swing.JScrollPane;
 import javax.swing.JSplitPane;
 import javax.swing.JViewport;
 import javax.swing.event.ChangeEvent;
 import javax.swing.event.ChangeListener;
 import net.trustx.simpleuml.components.Birdviewable;
 import net.trustx.simpleuml.components.DiagramComponent;
 import net.trustx.simpleuml.components.DiagramSaver;
 import net.trustx.simpleuml.components.Previewable;
 import net.trustx.simpleuml.plugin.UMLToolWindowPlugin;
 import net.trustx.simpleuml.plugin.actions.CloseActiveDiagramAction;
 import net.trustx.simpleuml.plugin.actions.DeleteActiveDiagramAction;
 import net.trustx.simpleuml.plugin.actions.RenameActiveDiagramAction;
 import net.trustx.simpleuml.sequencediagram.actions.AddClassToSequenceAction;
 import net.trustx.simpleuml.sequencediagram.display.AggregateAction;
 import net.trustx.simpleuml.sequencediagram.display.AggregateActionListener;
 import net.trustx.simpleuml.sequencediagram.display.DeleteLinkAction;
 import net.trustx.simpleuml.sequencediagram.display.Display;
 import net.trustx.simpleuml.sequencediagram.display.EditCommentAction;
 import net.trustx.simpleuml.sequencediagram.display.Editor;
 import net.trustx.simpleuml.sequencediagram.display.GotoClassAction;
 import net.trustx.simpleuml.sequencediagram.model.Model;
 import net.trustx.simpleuml.sequencediagram.model.ModelTextEvent;
 import net.trustx.simpleuml.sequencediagram.model.ModelTextListener;
 import net.trustx.simpleuml.sequencediagram.model.PrintDocumentationAction;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class SequenceDiagramComponent
   extends DiagramComponent
   implements PropertyChangeListener, ModelTextListener, Birdviewable
 {
   private Model model;
   private Editor editor;
   private Display disp;
   private String folderURL;
   private String myName;
   private JSplitPane split;
   private Project project;
   private LinkedList changeListenerList;
   public static final String URL_FILE_PREFIX = "file://";
   private JScrollPane topScrollPane;
   private CoalesceThread coalesceThread;
   
   public SequenceDiagramComponent(String folderURL, String name, Project project) {
     super(new BorderLayout());
     this.myName = name;
     this.folderURL = folderURL;
     this.project = project;
     this.changeListenerList = new LinkedList();
     this.split = new JSplitPane(0);
     
     this.model = new Model(project);
     this.model.addModelTextListener(this);
     
     this.disp = new Display(this.model);
     this.topScrollPane = new JScrollPane((Component)this.disp, 20, 30);
 
     
     this.topScrollPane.getVerticalScrollBar().setUnitIncrement(10);
     addDiagramSelectionListeners(this.topScrollPane, project);
     
     this.split.setTopComponent(this.topScrollPane);
     
     this.editor = new Editor(this.model);
     JScrollPane bottom = new JScrollPane((Component)this.editor, 20, 30);
 
     
     bottom.getVerticalScrollBar().setUnitIncrement(10);
     this.split.setBottomComponent(bottom);
     
     this.model.addModelTextListener((ModelTextListener)this.disp);
     this.model.addModelTextListener((ModelTextListener)this.editor);
     this.split.setOneTouchExpandable(true);
     this.split.setDividerLocation(9999);
     add(this.split);
     this.split.setVisible(true);
     
     this.disp.init(this.model.getText());
     
     this.model.addPropertyChangeListener("file", this);
   }
 
 
 
 
   
   private void addDiagramSelectionListeners(JScrollPane top, Project project) {
     AggregateAction agg;
     top.addMouseListener((MouseListener)(agg = new AggregateAction(this.disp, top.getViewport())));
     top.addMouseMotionListener((MouseMotionListener)agg);
     GotoClassAction gotoClassAction;
     top.addMouseListener((MouseListener)(gotoClassAction = new GotoClassAction(this.disp, top.getViewport(), this.model, project)));
     top.addMouseMotionListener((MouseMotionListener)gotoClassAction);
     agg.addAction((AggregateActionListener)gotoClassAction);
     ((AggregateActionListener)gotoClassAction).setPointer((DiagramSelectionListener)agg);
     EditCommentAction editCommentAction;
     top.addMouseListener((MouseListener)(editCommentAction = new EditCommentAction(this.disp, top.getViewport(), this.model)));
     top.addMouseMotionListener((MouseMotionListener)editCommentAction);
     agg.addAction((AggregateActionListener)editCommentAction);
     ((AggregateActionListener)editCommentAction).setPointer((DiagramSelectionListener)agg);
     DeleteLinkAction deleteLinkAction;
     top.addMouseListener((MouseListener)(deleteLinkAction = new DeleteLinkAction(this.disp, this.model)));
     top.addMouseMotionListener((MouseMotionListener)deleteLinkAction);
     agg.addAction((AggregateActionListener)deleteLinkAction);
     ((AggregateActionListener)deleteLinkAction).setPointer((DiagramSelectionListener)agg);
   }
 
 
   
   public Model getModel() {
     return this.model;
   }
 
 
   
   public void propertyChange(PropertyChangeEvent evt) {
     if (!evt.getPropertyName().equals("file"))
       return; 
     setTitle((File)evt.getNewValue());
   }
 
 
 
   
   public void setTitle(File f) {
     String name = (f == null) ? "" : f.getName();
   }
 
 
 
 
 
 
 
 
   
   public void exception(Exception e) {}
 
 
 
 
 
 
 
   
   public ActionGroup getTabActionGroup() {
     DefaultActionGroup actionGroup = new DefaultActionGroup();
     
     actionGroup.add((AnAction)new RenameActiveDiagramAction());
     actionGroup.add((AnAction)new CloseActiveDiagramAction());
     actionGroup.add((AnAction)new DeleteActiveDiagramAction());
     actionGroup.add((AnAction)new PrintDocumentationAction(getDiagramName(), this, this.disp));
     
     return (ActionGroup)actionGroup;
   }
 
 
   
   public void modelTextChanged(ModelTextEvent mte) {
     JScrollPane sp = (JScrollPane)this.split.getTopComponent();
     
     if (mte.getSource() == Model.class) {
       
       int height = sp.getVerticalScrollBar().getMaximum();
       sp.getViewport().setViewPosition(new Point(0, height));
     } 
     changesMade();
   }
 
 
 
   
   protected void add(PsiElement element, boolean update) {}
 
 
 
   
   public void addPsiElements(AnActionEvent event) {}
 
 
   
   public boolean canHandle(PsiElement psiElement) {
     return true;
   }
 
 
   
   protected boolean contains(PsiElement psiElement) {
     return false;
   }
 
 
 
   
   protected void highlight(PsiElement psiElement) {}
 
 
   
   public String getDiagramName() {
     return this.myName;
   }
 
 
   
   public void setDiagramName(String diagramName) {
     this.myName = diagramName;
   }
 
 
   
   public String getFolderURL() {
     return this.folderURL;
   }
 
 
   
   public void setFolderURL(String folderURL) {
     this.folderURL = folderURL;
   }
 
 
   
   public AnAction getAddToDiagramComponentAction() {
     return (AnAction)new AddClassToSequenceAction(this.myName, this);
   }
 
 
   
   public AnAction getGoToDiagramComponentAction() {
     return (AnAction)new AddClassToSequenceAction(this.myName, this);
   }
 
 
   
   public DiagramSaver getDiagramSaver() {
     return this.model.getSaveDocAction();
   }
 
 
   
   public String getDiagramType() {
     return "Sequencediagram";
   }
 
 
   
   public static void main(String[] s) {
     SequenceDiagramComponent seq = new SequenceDiagramComponent("SequenceDiagramComponent", "1", null);
     JFrame t = new JFrame("SEQEUENCE");
     t.getContentPane().add((Component)seq, "Center");
     Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
     int width = size.width / 2;
     int height = size.height / 2;
     t.pack();
     t.setSize(new Dimension(width, height));
     t.setLocation(width / 2, height / 2);
     t.setVisible(true);
   }
 
 
   
   public void addChangeListener(ChangeListener changeListener) {
     this.changeListenerList.add(changeListener);
   }
 
 
   
   public void removeChangeListener(ChangeListener changeListener) {
     this.changeListenerList.remove(changeListener);
   }
 
 
   
   public void requestUpdate() {
     changesMade();
   }
 
 
   
   public boolean isShowingOnScreen() {
     return this.disp.isShowing();
   }
 
 
   
   public int getMinimumComponentX() {
     return 0;
   }
 
 
   
   public int getMinimumComponentY() {
     return 0;
   }
 
 
   
   public int getMaximumComponentX() {
     return this.disp.getWidth();
   }
 
 
   
   public int getMaximumComponentY() {
     return this.disp.getHeight();
   }
 
 
   
   public int getRealWidth() {
     return this.disp.getWidth();
   }
 
 
   
   public int getRealHeight() {
     return this.disp.getHeight();
   }
 
 
   
   public void printAllContents(Graphics2D g2d) {
     this.disp.printAll(g2d);
   }
 
 
   
   public boolean isReady() {
     return this.disp.isVisible();
   }
 
 
   
   public JViewport getJViewport() {
     return this.topScrollPane.getViewport();
   }
 
 
   
   public Project getProject() {
     return this.project;
   }
 
 
 
 
 
   
   public synchronized void changesMade() {
     if (this.coalesceThread == null || !this.coalesceThread.isAlive()) {
       
       this.coalesceThread = new CoalesceThread();
       this.coalesceThread.start();
     }
     else {
       
       this.coalesceThread.requestChange();
     } 
   }
 
 
   
   private void notifyChangeListeners() {
     for (Iterator<ChangeListener> iterator = this.changeListenerList.iterator(); iterator.hasNext(); ) {
       
       ChangeListener changeListener = iterator.next();
       changeListener.stateChanged(new ChangeEvent(this));
     } 
   }
 
 
 
 
 
 
   
   private class CoalesceThread
     extends Thread
   {
     private transient int sleepCount = 1;
 
 
 
     
     public synchronized void requestChange() {
       this.sleepCount++;
     }
 
 
     
     public synchronized void process() {
       this.sleepCount = 0;
     }
 
 
     
     public void run() {
       while (this.sleepCount > 0) {
         
         process();
         
         try {
           UMLToolWindowPlugin umlToolWindowPlugin = UMLToolWindowPlugin.getUMLToolWindowPlugin(SequenceDiagramComponent.this.project);
           if (umlToolWindowPlugin == null) {
             return;
           }
           
           long birdViewUpdateDelay = umlToolWindowPlugin.getConfigurationFactory().getGeneralDiagramSettings().getBirdViewUpdateDelay();
           Thread.sleep(birdViewUpdateDelay);
         }
         catch (InterruptedException e) {
           
           e.printStackTrace();
         } 
       } 
       EventQueue.invokeLater(new Runnable()
           {
             public void run()
             {
               if (SequenceDiagramComponent.this.isShowing())
               {
                 SequenceDiagramComponent.this.notifyChangeListeners();
               }
             }
           });
     }
   }
 
 
 
 
   
   public JComponent getPreferredFocusedComponent() {
     return (JComponent)this.disp;
   }
 
 
   
   public Previewable getPreviewable() {
     return (Previewable)this;
   }
 }


