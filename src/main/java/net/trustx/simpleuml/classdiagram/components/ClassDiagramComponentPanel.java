 package net.trustx.simpleuml.classdiagram.components;

 import com.intellij.openapi.actionSystem.ActionGroup;
 import com.intellij.openapi.actionSystem.ActionManager;
 import com.intellij.openapi.actionSystem.ActionToolbar;
 import com.intellij.openapi.actionSystem.AnAction;
 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.actionSystem.DataKeys;
 import com.intellij.openapi.actionSystem.DefaultActionGroup;
 import com.intellij.openapi.progress.ProgressIndicator;
 import com.intellij.openapi.progress.ProgressManager;
 import com.intellij.openapi.project.Project;
 import com.intellij.openapi.ui.Messages;
 import com.intellij.openapi.wm.ToolWindow;
 import com.intellij.openapi.wm.ToolWindowManager;
 import com.intellij.psi.PsiClass;
 import com.intellij.psi.PsiDirectory;
 import com.intellij.psi.PsiElement;
 import com.intellij.psi.PsiFile;
 import com.intellij.psi.PsiJavaFile;
 import com.intellij.psi.PsiManager;
 import com.intellij.psi.PsiTreeChangeListener;
 import com.intellij.refactoring.listeners.RefactoringElementListenerProvider;
 import com.intellij.refactoring.listeners.RefactoringListenerManager;
 import java.awt.BorderLayout;
 import java.awt.Component;
 import java.awt.EventQueue;
 import java.awt.Graphics2D;
 import java.awt.Point;
 import java.awt.event.MouseWheelEvent;
 import java.awt.event.MouseWheelListener;
 import java.lang.reflect.InvocationTargetException;
 import java.util.Iterator;
 import java.util.LinkedList;
 import javax.swing.BoundedRangeModel;
 import javax.swing.JComponent;
 import javax.swing.JPanel;
 import javax.swing.JScrollPane;
 import javax.swing.JViewport;
 import javax.swing.event.ChangeListener;
 import net.trustx.simpleuml.classdiagram.actions.AddFullyQualifiedClassNameToDiagramAction;
 import net.trustx.simpleuml.classdiagram.actions.AddToClassDiagramAction;
 import net.trustx.simpleuml.classdiagram.actions.GoToClassDiagramAction;
 import net.trustx.simpleuml.classdiagram.actions.ToggleAutomaticLayoutAction;
 import net.trustx.simpleuml.classdiagram.actions.ToggleContainsAction;
 import net.trustx.simpleuml.classdiagram.actions.ToggleDependsAction;
 import net.trustx.simpleuml.classdiagram.actions.ToggleExtendsAction;
 import net.trustx.simpleuml.classdiagram.actions.ToggleImplementsAction;
 import net.trustx.simpleuml.classdiagram.actions.ToggleInnerAction;
 import net.trustx.simpleuml.classdiagram.actions.ToggleUsesAction;
 import net.trustx.simpleuml.components.Birdviewable;
 import net.trustx.simpleuml.components.DiagramComponent;
 import net.trustx.simpleuml.components.DiagramSaver;
 import net.trustx.simpleuml.components.Previewable;
 import net.trustx.simpleuml.gef.components.ActionContributorCommandPopupHandler;
 import net.trustx.simpleuml.gef.components.DiagramPaneDragHandler;
 import net.trustx.simpleuml.gef.components.DiagramPaneMarqueeHandler;
 import net.trustx.simpleuml.gef.components.FigureComponentDragHandler;
 import net.trustx.simpleuml.plugin.actions.CloseActiveDiagramAction;
 import net.trustx.simpleuml.plugin.actions.DeleteActiveDiagramAction;
 import net.trustx.simpleuml.plugin.actions.RenameActiveDiagramAction;


 public class ClassDiagramComponentPanel
   extends DiagramComponent
   implements Birdviewable
 {
   private ClassDiagramComponent classDiagramComponent;
   private JScrollPane scrollPane;
   private String folderURL;
   private String diagramName;
   private ClassDiagramPsiTreeChangeListener psiTreeChangeListener;

   public ClassDiagramComponentPanel(ClassDiagramComponent classDiagramComponent, String folderURL, String name) {
     super(new BorderLayout());

     this.classDiagramComponent = classDiagramComponent;
     this.folderURL = folderURL;
     this.diagramName = name;
     classDiagramComponent.setClassDiagramComponentPanel(this);

     this.scrollPane = new JScrollPane((Component)classDiagramComponent);
     this.scrollPane.setWheelScrollingEnabled(false);
     this.scrollPane.getVerticalScrollBar().setUnitIncrement(20);
     this.scrollPane.getHorizontalScrollBar().setUnitIncrement(20);
     this.scrollPane.getViewport().setScrollMode(1);

     addMouseWheelSupport(this.scrollPane, classDiagramComponent);

     DiagramPaneDragHandler dragHandler = new DiagramPaneDragHandler();
     dragHandler.install(classDiagramComponent, getScrollPane());

     new ActionContributorCommandPopupHandler(classDiagramComponent);

     new DiagramPaneMarqueeHandler(classDiagramComponent);

     DefaultActionGroup actionGroup = initDiagramComponentActionGroup(classDiagramComponent);

     ActionToolbar toolBar = ActionManager.getInstance().createActionToolbar("simpleUML.Toolbar", (ActionGroup)actionGroup, false);
     JPanel toolBarPanel = new JPanel(new BorderLayout());
     toolBarPanel.add(toolBar.getComponent(), "West");
     add(toolBarPanel, "West");

     add(this.scrollPane, "Center");

     this.psiTreeChangeListener = new ClassDiagramPsiTreeChangeListener(this);
     PsiManager.getInstance(getProject()).addPsiTreeChangeListener((PsiTreeChangeListener)this.psiTreeChangeListener);

     RefactoringElementListenerProvider provider = new ClassDiagramRefactoringElementListenerProvider(classDiagramComponent);

//     RefactoringListenerManager.getInstance(getProject()).addListenerProvider(provider);
   }



   private DefaultActionGroup initDiagramComponentActionGroup(ClassDiagramComponent classDiagramComponent) {
     DefaultActionGroup actionGroup = new DefaultActionGroup();

     ToggleUsesAction toggleUsesAction = new ToggleUsesAction(classDiagramComponent);
     ToggleImplementsAction toggleImplementsAction = new ToggleImplementsAction(classDiagramComponent);
     ToggleExtendsAction toggleExtendsAction = new ToggleExtendsAction(classDiagramComponent);
     ToggleInnerAction toggleInnerAction = new ToggleInnerAction(classDiagramComponent);
     ToggleDependsAction toggleDependsAction = new ToggleDependsAction(classDiagramComponent);
     ToggleContainsAction toggleContainsAction = new ToggleContainsAction(classDiagramComponent);

     ToggleAutomaticLayoutAction toggleAutomaticLayoutAction = new ToggleAutomaticLayoutAction(classDiagramComponent);

     AddFullyQualifiedClassNameToDiagramAction addFullyQualifiedClassNameToDiagramAction = new AddFullyQualifiedClassNameToDiagramAction(this);

     actionGroup.add((AnAction)toggleExtendsAction);
     actionGroup.add((AnAction)toggleImplementsAction);
     actionGroup.add((AnAction)toggleUsesAction);
     actionGroup.add((AnAction)toggleInnerAction);
     actionGroup.add((AnAction)toggleDependsAction);
     actionGroup.add((AnAction)toggleContainsAction);
     actionGroup.addSeparator();
     actionGroup.add((AnAction)toggleAutomaticLayoutAction);
     actionGroup.addSeparator();
     actionGroup.add((AnAction)addFullyQualifiedClassNameToDiagramAction);

     return actionGroup;
   }






   private void addMouseWheelSupport(final JScrollPane comp, final ClassDiagramComponent classDiagramComponent) {
     comp.addMouseWheelListener(new MouseWheelListener()
         {
           public void mouseWheelMoved(MouseWheelEvent e)
           {
             if (classDiagramComponent.hasAdjustingComponents())
             {
               comp.getViewport().setScrollMode(0);
             }
             if (e.getWheelRotation() < 0) {

               if (comp.getVerticalScrollBar().isVisible())
               {
                 int maxScrollAmount = comp.getVerticalScrollBar().getValue();
                 int requestedScrollAmount = e.getScrollAmount() * comp.getVerticalScrollBar().getUnitIncrement();
                 int amount = -1 * Math.min(maxScrollAmount, requestedScrollAmount);
                 classDiagramComponent.moveAdjustingComponents(0, amount);
                 Point viewPosition = comp.getViewport().getViewPosition();
                 viewPosition.y += amount;
                 comp.getViewport().setViewPosition(viewPosition);
               }
               else
               {
                 int maxScrollAmount = comp.getHorizontalScrollBar().getValue();
                 int requestedScrollAmount = e.getScrollAmount() * comp.getHorizontalScrollBar().getUnitIncrement();
                 int amount = -1 * Math.min(maxScrollAmount, requestedScrollAmount);
                 classDiagramComponent.moveAdjustingComponents(amount, 0);
                 Point viewPosition = comp.getViewport().getViewPosition();
                 viewPosition.x += amount;
                 comp.getViewport().setViewPosition(viewPosition);

               }

             }
             else if (comp.getVerticalScrollBar().isVisible()) {

               BoundedRangeModel model = comp.getVerticalScrollBar().getModel();
               int maxScrollAmount = model.getMaximum() - model.getExtent() - model.getValue();
               int requestedScrollAmount = e.getScrollAmount() * comp.getVerticalScrollBar().getUnitIncrement();
               int amount = Math.min(maxScrollAmount, requestedScrollAmount);
               classDiagramComponent.moveAdjustingComponents(0, amount);
               Point viewPosition = comp.getViewport().getViewPosition();
               viewPosition.y += amount;
               comp.getViewport().setViewPosition(viewPosition);
             }
             else {

               BoundedRangeModel model = comp.getHorizontalScrollBar().getModel();
               int maxScrollAmount = model.getMaximum() - model.getExtent() - model.getValue();
               int requestedScrollAmount = e.getScrollAmount() * comp.getHorizontalScrollBar().getUnitIncrement();
               int amount = Math.min(maxScrollAmount, requestedScrollAmount);
               classDiagramComponent.moveAdjustingComponents(amount, 0);
               Point viewPosition = comp.getViewport().getViewPosition();
               viewPosition.x += amount;
               comp.getViewport().setViewPosition(viewPosition);
             }

             if (classDiagramComponent.hasAdjustingComponents())
             {
               comp.getViewport().setScrollMode(1);
             }
           }
         });
   }



   public void disposeUIResources() {
     this.classDiagramComponent.disposeUIResources();
     PsiManager.getInstance(getProject()).removePsiTreeChangeListener((PsiTreeChangeListener)this.psiTreeChangeListener);
   }



   public String getDiagramType() {
     return "Classdiagram";
   }



   public ClassDiagramComponent getClassDiagramComponent() {
     return this.classDiagramComponent;
   }



   public void setClassDiagramComponent(ClassDiagramComponent classDiagramComponent) {
     this.classDiagramComponent = classDiagramComponent;
   }



   public boolean isShowingOnScreen() {
     return this.classDiagramComponent.isShowing();
   }



   public int getMinimumComponentX() {
     return this.classDiagramComponent.getMinimumComponentX();
   }



   public int getMinimumComponentY() {
     return this.classDiagramComponent.getMinimumComponentY();
   }



   public int getMaximumComponentX() {
     return this.classDiagramComponent.getMaximumComponentX();
   }



   public int getMaximumComponentY() {
     return this.classDiagramComponent.getMaximumComponentY();
   }



   public int getRealWidth() {
     return this.classDiagramComponent.getWidth();
   }



   public int getRealHeight() {
     return this.classDiagramComponent.getHeight();
   }



   public void printAllContents(Graphics2D g2d) {
     this.classDiagramComponent.printAll(g2d);
   }



   public boolean isReady() {
     return this.classDiagramComponent.isVisible();
   }



   public JViewport getJViewport() {
     return this.scrollPane.getViewport();
   }



   public JScrollPane getScrollPane() {
     return this.scrollPane;
   }



   public void addChangeListener(ChangeListener changeListener) {
     this.classDiagramComponent.addChangeListener(changeListener);
   }



   public void removeChangeListener(ChangeListener changeListener) {
     this.classDiagramComponent.removeChangeListener(changeListener);
   }



   public void requestUpdate() {
     this.classDiagramComponent.changesMade(false);
   }



   public Project getProject() {
     return this.classDiagramComponent.getProject();
   }



   public String getFolderURL() {
     return this.folderURL;
   }



   public void setFolderURL(String folderURL) {
     this.folderURL = folderURL;
   }



   public AnAction getAddToDiagramComponentAction() {
     return (AnAction)new AddToClassDiagramAction(this);
   }



   public AnAction getGoToDiagramComponentAction() {
     return (AnAction)new GoToClassDiagramAction(this);
   }



   public DiagramSaver getDiagramSaver() {
     return new ClassDiagramSaver(this);
   }



   public String getDiagramName() {
     return this.diagramName;
   }



   public void setDiagramName(String name) {
     this.diagramName = name;
   }



   public ClassDiagramComponentSettings getComponentSettings() {
     return this.classDiagramComponent.getComponentSettings();
   }



   public ActionGroup getTabActionGroup() {
     DefaultActionGroup actionGroup = new DefaultActionGroup();

     actionGroup.add((AnAction)new RenameActiveDiagramAction());
     actionGroup.add((AnAction)new CloseActiveDiagramAction());
     actionGroup.add((AnAction)new DeleteActiveDiagramAction());

     return (ActionGroup)actionGroup;
   }



   protected void add(PsiElement psiElement, boolean update) {
     if (psiElement instanceof PsiClass) {

       PsiClass psiClass = (PsiClass)psiElement;
       PsiClassComponent psiClassComponent = new PsiClassComponent(this.classDiagramComponent, psiClass);

       FigureComponentDragHandler dragHandler = new FigureComponentDragHandler(this.classDiagramComponent, getScrollPane());
       dragHandler.install(psiClassComponent);

       this.classDiagramComponent.addFigureComponent(psiClassComponent, update);
     }
   }



   public void addPsiElements(AnActionEvent event) {
     addSelectedFilesToDiagram(event);
   }



   public boolean canHandle(PsiElement psiElement) {
     return psiElement instanceof PsiClass;
   }



   public boolean contains(PsiElement psiElement) {
     if (psiElement instanceof PsiClass) {

       PsiClass psiClass = (PsiClass)psiElement;
       return this.classDiagramComponent.containsClass(psiClass.getQualifiedName());
     }


     return false;
   }




   public void highlight(PsiElement psiElement) {
     if (psiElement instanceof PsiClass) {

       PsiClass psiClass = (PsiClass)psiElement;
       this.classDiagramComponent.containsHighlightClass(psiClass.getQualifiedName());
     }
   }



   private void addSelectedFilesToDiagram(AnActionEvent event) {
     Project project = (Project)DataKeys.PROJECT.getData(event.getDataContext());
     ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("simpleUML");

     PsiFile psiFile = (PsiFile)DataKeys.PSI_FILE.getData(event.getDataContext());

     PsiElement[] selectedElements = (PsiElement[])DataKeys.PSI_ELEMENT_ARRAY.getData(event.getDataContext());
     PsiElement selectedElement = (PsiElement)DataKeys.PSI_ELEMENT.getData(event.getDataContext());

     if (selectedElements != null) {

       addSelectedClasses(selectedElements, project, 3);
       toolWindow.activate(null);
     }
     else if (selectedElement != null && selectedElement instanceof PsiClass) {

       addSelectedClasses(new PsiElement[] { selectedElement }, project, 3);
       toolWindow.activate(null);
     }
     else if (psiFile != null && psiFile instanceof PsiJavaFile) {

       PsiJavaFile psiJavaFile = (PsiJavaFile)psiFile;
       PsiClass[] psiClasses = psiJavaFile.getClasses();
       addSelectedClasses((PsiElement[])psiClasses, project, 3);
     }
   }



   public void addSelectedClasses(PsiElement[] pes, Project project, int recursively) {
     final LinkedList psiClassList = new LinkedList();
     addSelectedClassesToList(psiClassList, pes, project, recursively);

     ProgressManager.getInstance().runProcessWithProgressSynchronously(new Runnable()
         {
           public void run() {
             ProgressIndicator progressWindow = ProgressManager.getInstance().getProgressIndicator();
             int i = 0;
             for (Iterator<PsiClass> iterator = psiClassList.iterator(); iterator.hasNext(); ) {

               i++;
               final PsiClass psiClass = iterator.next();

               progressWindow.setFraction(i / psiClassList.size());
               progressWindow.setText("Loading " + psiClass.getQualifiedName());


               try {
                 EventQueue.invokeAndWait(new Runnable()
                     {
                       public void run()
                       {
                         PsiClassComponent psiClassComponent = new PsiClassComponent(ClassDiagramComponentPanel.this.classDiagramComponent, psiClass);
                         FigureComponentDragHandler dragHandler = new FigureComponentDragHandler(ClassDiagramComponentPanel.this.classDiagramComponent, ClassDiagramComponentPanel.this.getScrollPane());
                         dragHandler.install(psiClassComponent);
                         ClassDiagramComponentPanel.this.classDiagramComponent.addFigureComponent(psiClassComponent, true);
                       }
                     });
               }
               catch (InterruptedException e) {

                 e.printStackTrace();
               }
               catch (InvocationTargetException e) {

                 e.printStackTrace();
               }
             }
             progressWindow.stop();
           }
         }, "Adding Classes...", false, project);
   }



   public void addSelectedClassesToList(LinkedList<PsiClass> classList, PsiElement[] pes, Project project, int recursively) {
     for (int i = 0; i < pes.length; i++) {

       PsiElement pe = pes[i];
       if (pe instanceof PsiDirectory) {

         if (recursively == 3) {

           int response = Messages.showYesNoDialog(project, "Add folders recursively?", "Question", Messages.getQuestionIcon());
           recursively = (response == 0) ? 1 : 2;
         }
         if (recursively == 1)
         {
           PsiDirectory pd = (PsiDirectory)pe;
           PsiElement[] newPes = pd.getChildren();
           addSelectedClassesToList(classList, newPes, project, 1);
         }

       }
       else if (pe instanceof PsiClass) {

         PsiClass psiClass = (PsiClass)pe;
         if (!containsHighlight((PsiElement)psiClass))
         {

           classList.add(psiClass);
         }
       }
       else if (pe instanceof PsiJavaFile) {

         PsiClass[] pcs = ((PsiJavaFile)pe).getClasses();
         addSelectedClassesToList(classList, (PsiElement[])pcs, project, recursively);
       }
     }
   }



   public JComponent getPreferredFocusedComponent() {
     return (JComponent)this.classDiagramComponent;
   }



   public Previewable getPreviewable() {
     return (Previewable)this;
   }
 }


