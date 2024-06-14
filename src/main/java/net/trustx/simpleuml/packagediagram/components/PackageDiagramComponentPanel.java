 package net.trustx.simpleuml.packagediagram.components;

 import com.intellij.openapi.actionSystem.ActionGroup;
 import com.intellij.openapi.actionSystem.ActionManager;
 import com.intellij.openapi.actionSystem.ActionToolbar;
 import com.intellij.openapi.actionSystem.AnAction;
 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.actionSystem.DataKeys;
 import com.intellij.openapi.actionSystem.DefaultActionGroup;
 import com.intellij.openapi.project.Project;
 import com.intellij.openapi.ui.Messages;
 import com.intellij.openapi.wm.ToolWindow;
 import com.intellij.openapi.wm.ToolWindowManager;
 import com.intellij.psi.JavaDirectoryService;
 import com.intellij.psi.PsiDirectory;
 import com.intellij.psi.PsiElement;
 import com.intellij.psi.PsiPackage;
 import java.awt.BorderLayout;
 import java.awt.Component;
 import java.awt.Graphics2D;
 import java.awt.Point;
 import java.awt.event.MouseWheelEvent;
 import java.awt.event.MouseWheelListener;
 import javax.swing.BoundedRangeModel;
 import javax.swing.JComponent;
 import javax.swing.JPanel;
 import javax.swing.JScrollPane;
 import javax.swing.JViewport;
 import javax.swing.event.ChangeListener;
 import net.trustx.simpleuml.components.Birdviewable;
 import net.trustx.simpleuml.components.DiagramComponent;
 import net.trustx.simpleuml.components.DiagramSaver;
 import net.trustx.simpleuml.components.Previewable;
 import net.trustx.simpleuml.gef.components.ActionContributorCommandPopupHandler;
 import net.trustx.simpleuml.gef.components.DiagramPaneDragHandler;
 import net.trustx.simpleuml.gef.components.DiagramPaneMarqueeHandler;
 import net.trustx.simpleuml.gef.components.FigureComponentDragHandler;
 import net.trustx.simpleuml.gef.components.FigureComponentResizeHandler;
 import net.trustx.simpleuml.packagediagram.actions.AddToPackageDiagramAction;
 import net.trustx.simpleuml.packagediagram.actions.GoToPackageDiagramAction;
 import net.trustx.simpleuml.plugin.actions.CloseActiveDiagramAction;
 import net.trustx.simpleuml.plugin.actions.DeleteActiveDiagramAction;
 import net.trustx.simpleuml.plugin.actions.RenameActiveDiagramAction;




 public class PackageDiagramComponentPanel
   extends DiagramComponent
   implements Birdviewable
 {
   private PackageDiagramComponent packageDiagramComponent;
   private JScrollPane scrollPane;
   private String folderURL;
   private String diagramName;

   public PackageDiagramComponentPanel(PackageDiagramComponent packageDiagramComponent, String folderURL, String name) {
     super(new BorderLayout());

     this.packageDiagramComponent = packageDiagramComponent;
     this.folderURL = folderURL;
     this.diagramName = name;
     packageDiagramComponent.setPackageDiagramComponentPanel(this);

     this.scrollPane = new JScrollPane((Component)packageDiagramComponent);
     this.scrollPane.setWheelScrollingEnabled(false);
     this.scrollPane.getVerticalScrollBar().setUnitIncrement(20);
     this.scrollPane.getHorizontalScrollBar().setUnitIncrement(20);
     this.scrollPane.getViewport().setScrollMode(1);

     addMouseWheelSupport(this.scrollPane, packageDiagramComponent);

     DiagramPaneDragHandler dragHandler = new DiagramPaneDragHandler();
     dragHandler.install(packageDiagramComponent, getScrollPane());

     new ActionContributorCommandPopupHandler(packageDiagramComponent);

     new DiagramPaneMarqueeHandler(packageDiagramComponent);

     DefaultActionGroup actionGroup = initDiagramComponentActionGroup(packageDiagramComponent);

     ActionToolbar toolBar = ActionManager.getInstance().createActionToolbar("simpleUML.Toolbar", (ActionGroup)actionGroup, false);
     JPanel toolBarPanel = new JPanel(new BorderLayout());
     toolBarPanel.add(toolBar.getComponent(), "West");
     add(toolBarPanel, "West");

     add(this.scrollPane, "Center");
   }




   private DefaultActionGroup initDiagramComponentActionGroup(PackageDiagramComponent packageDiagramComponent) {
     DefaultActionGroup actionGroup = new DefaultActionGroup();
     return actionGroup;
   }






   private void addMouseWheelSupport(final JScrollPane comp, final PackageDiagramComponent packageDiagramComponent) {
     comp.addMouseWheelListener(new MouseWheelListener()
         {
           public void mouseWheelMoved(MouseWheelEvent e)
           {
             if (packageDiagramComponent.hasAdjustingComponents())
             {
               comp.getViewport().setScrollMode(0);
             }
             if (e.getWheelRotation() < 0) {

               if (comp.getVerticalScrollBar().isVisible())
               {
                 int maxScrollAmount = comp.getVerticalScrollBar().getValue();
                 int requestedScrollAmount = e.getScrollAmount() * comp.getVerticalScrollBar().getUnitIncrement();
                 int amount = -1 * Math.min(maxScrollAmount, requestedScrollAmount);
                 packageDiagramComponent.moveAdjustingComponents(0, amount);
                 Point viewPosition = comp.getViewport().getViewPosition();
                 viewPosition.y += amount;
                 comp.getViewport().setViewPosition(viewPosition);
               }
               else
               {
                 int maxScrollAmount = comp.getHorizontalScrollBar().getValue();
                 int requestedScrollAmount = e.getScrollAmount() * comp.getHorizontalScrollBar().getUnitIncrement();
                 int amount = -1 * Math.min(maxScrollAmount, requestedScrollAmount);
                 packageDiagramComponent.moveAdjustingComponents(amount, 0);
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
               packageDiagramComponent.moveAdjustingComponents(0, amount);
               Point viewPosition = comp.getViewport().getViewPosition();
               viewPosition.y += amount;
               comp.getViewport().setViewPosition(viewPosition);
             }
             else {

               BoundedRangeModel model = comp.getHorizontalScrollBar().getModel();
               int maxScrollAmount = model.getMaximum() - model.getExtent() - model.getValue();
               int requestedScrollAmount = e.getScrollAmount() * comp.getHorizontalScrollBar().getUnitIncrement();
               int amount = Math.min(maxScrollAmount, requestedScrollAmount);
               packageDiagramComponent.moveAdjustingComponents(amount, 0);
               Point viewPosition = comp.getViewport().getViewPosition();
               viewPosition.x += amount;
               comp.getViewport().setViewPosition(viewPosition);
             }

             if (packageDiagramComponent.hasAdjustingComponents())
             {
               comp.getViewport().setScrollMode(1);
             }
           }
         });
   }



   public void disposeUIResources() {
     this.packageDiagramComponent.disposeUIResources();
   }




   public String getDiagramType() {
     return "Packagediagram";
   }



   public boolean isShowingOnScreen() {
     return this.packageDiagramComponent.isShowing();
   }



   public int getMinimumComponentX() {
     return this.packageDiagramComponent.getMinimumComponentX();
   }



   public int getMinimumComponentY() {
     return this.packageDiagramComponent.getMinimumComponentY();
   }



   public int getMaximumComponentX() {
     return this.packageDiagramComponent.getMaximumComponentX();
   }



   public int getMaximumComponentY() {
     return this.packageDiagramComponent.getMaximumComponentY();
   }



   public int getRealWidth() {
     return this.packageDiagramComponent.getWidth();
   }



   public int getRealHeight() {
     return this.packageDiagramComponent.getHeight();
   }



   public void printAllContents(Graphics2D g2d) {
     this.packageDiagramComponent.printAll(g2d);
   }



   public boolean isReady() {
     return this.packageDiagramComponent.isVisible();
   }



   public JViewport getJViewport() {
     return this.scrollPane.getViewport();
   }



   public JScrollPane getScrollPane() {
     return this.scrollPane;
   }



   public void addChangeListener(ChangeListener changeListener) {
     this.packageDiagramComponent.addChangeListener(changeListener);
   }



   public void removeChangeListener(ChangeListener changeListener) {
     this.packageDiagramComponent.removeChangeListener(changeListener);
   }



   public void requestUpdate() {
     this.packageDiagramComponent.changesMade();
   }



   public Project getProject() {
     return this.packageDiagramComponent.getProject();
   }



   public String getFolderURL() {
     return this.folderURL;
   }



   public void setFolderURL(String folderURL) {
     this.folderURL = folderURL;
   }



   public AnAction getAddToDiagramComponentAction() {
     return (AnAction)new AddToPackageDiagramAction(this);
   }



   public AnAction getGoToDiagramComponentAction() {
     return (AnAction)new GoToPackageDiagramAction(this);
   }



   public DiagramSaver getDiagramSaver() {
     return new PackageDiagramSaver(this);
   }



   public String getDiagramName() {
     return this.diagramName;
   }



   public void setDiagramName(String name) {
     this.diagramName = name;
   }



   public ActionGroup getTabActionGroup() {
     DefaultActionGroup actionGroup = new DefaultActionGroup();

     actionGroup.add((AnAction)new RenameActiveDiagramAction());
     actionGroup.add((AnAction)new CloseActiveDiagramAction());
     actionGroup.add((AnAction)new DeleteActiveDiagramAction());

     return (ActionGroup)actionGroup;
   }



   protected void add(PsiElement psiElement, boolean update) {
     if (psiElement instanceof PsiPackage) {

       PsiPackage psiPackage = (PsiPackage)psiElement;
       PsiPackageComponent psiPackageComponent = new PsiPackageComponent(this.packageDiagramComponent, psiPackage, false);

       FigureComponentDragHandler dragHandler = new FigureComponentDragHandler(this.packageDiagramComponent, getScrollPane());
       dragHandler.install(psiPackageComponent);

       FigureComponentResizeHandler resizeHandler = new FigureComponentResizeHandler(this.packageDiagramComponent);
       resizeHandler.install(psiPackageComponent);

       this.packageDiagramComponent.addFigureComponent(psiPackageComponent, update);
     }
   }



   public void addPsiElements(AnActionEvent event) {
     addSelectedFilesToDiagram(event);
   }



   public boolean canHandle(PsiElement psiElement) {
     return psiElement instanceof PsiPackage;
   }



   public boolean contains(PsiElement psiElement) {
     if (psiElement instanceof PsiPackage) {

       PsiPackage psiPackage = (PsiPackage)psiElement;
       return this.packageDiagramComponent.containsPackage(psiPackage.getQualifiedName());
     }


     return false;
   }




   public void highlight(PsiElement psiElement) {
     if (psiElement instanceof PsiPackage) {

       PsiPackage psiPackage = (PsiPackage)psiElement;
       this.packageDiagramComponent.containsHighlightPackage(psiPackage.getQualifiedName());
     }
   }



   private void addSelectedFilesToDiagram(AnActionEvent event) {
     Project project = (Project)DataKeys.PROJECT.getData(event.getDataContext());
     ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("simpleUML");

     PsiElement[] selectedElements = (PsiElement[])DataKeys.PSI_ELEMENT_ARRAY.getData(event.getDataContext());
     PsiElement selectedElement = (PsiElement)DataKeys.PSI_ELEMENT.getData(event.getDataContext());


     if (selectedElements != null) {

       addSelectedPackages(selectedElements, project, 3);
       toolWindow.activate(null);
     }
     else if (selectedElement != null && selectedElement instanceof PsiPackage) {

       addSelectedPackages(new PsiElement[] { selectedElement }, project, 3);
       toolWindow.activate(null);
     }
   }



   public void addSelectedPackages(PsiElement[] pes, Project project, int recursively) {
     PackageDiagramComponentPanel packageDiagramComponentPanel = this.packageDiagramComponent.getPackageDiagramComponentPanel();

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

           PsiPackage psiPackage = JavaDirectoryService.getInstance().getPackage(pd);
           if (psiPackage != null && !packageDiagramComponentPanel.containsHighlight((PsiElement)psiPackage))
           {
             packageDiagramComponentPanel.addPsiElement((PsiElement)psiPackage, true);
           }

           PsiElement[] newPes = pd.getChildren();
           addSelectedPackages(newPes, project, 1);

         }
         else
         {
           PsiDirectory pd = (PsiDirectory)pe;
           PsiPackage psiPackage = JavaDirectoryService.getInstance().getPackage(pd);
           if (psiPackage != null && !packageDiagramComponentPanel.containsHighlight((PsiElement)psiPackage))
           {
             packageDiagramComponentPanel.addPsiElement((PsiElement)psiPackage, true);
           }
         }

       }
       else if (pe instanceof PsiPackage) {

         PsiPackage psiPackage = (PsiPackage)pe;
         if (!packageDiagramComponentPanel.containsHighlight((PsiElement)psiPackage))
         {
           packageDiagramComponentPanel.addPsiElement((PsiElement)psiPackage, true);
         }
       }
     }
   }



   public PackageDiagramComponentSettings getComponentSettings() {
     return this.packageDiagramComponent.getComponentSettings();
   }



   public PackageDiagramComponent getPackageDiagramComponent() {
     return this.packageDiagramComponent;
   }



   public JComponent getPreferredFocusedComponent() {
     return (JComponent)this.packageDiagramComponent;
   }



   public Previewable getPreviewable() {
     return (Previewable)this;
   }
 }


