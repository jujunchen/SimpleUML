 package net.trustx.simpleuml.dependencydiagram.components;

 import com.intellij.openapi.actionSystem.ActionGroup;
 import com.intellij.openapi.actionSystem.ActionManager;
 import com.intellij.openapi.actionSystem.ActionToolbar;
 import com.intellij.openapi.actionSystem.AnAction;
 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.actionSystem.DataKeys;
 import com.intellij.openapi.actionSystem.DefaultActionGroup;
 import com.intellij.openapi.application.ApplicationManager;
 import com.intellij.openapi.progress.ProgressIndicator;
 import com.intellij.openapi.progress.ProgressManager;
 import com.intellij.openapi.project.Project;
 import com.intellij.openapi.wm.ToolWindow;
 import com.intellij.openapi.wm.ToolWindowManager;
 import com.intellij.psi.JavaPsiFacade;
 import com.intellij.psi.PsiClass;
 import com.intellij.psi.PsiElement;
 import com.intellij.psi.PsiFile;
 import com.intellij.psi.PsiJavaFile;
 import com.intellij.psi.PsiPackage;
 import com.intellij.psi.search.GlobalSearchScope;
 import java.awt.BorderLayout;
 import java.awt.Component;
 import java.awt.EventQueue;
 import java.awt.Graphics2D;
 import java.util.HashMap;
 import java.util.HashSet;
 import java.util.Iterator;
 import javax.swing.JComponent;
 import javax.swing.JPanel;
 import javax.swing.JProgressBar;
 import javax.swing.JScrollPane;
 import javax.swing.JViewport;
 import javax.swing.event.ChangeListener;
 import net.trustx.simpleuml.components.Birdviewable;
 import net.trustx.simpleuml.components.DiagramComponent;
 import net.trustx.simpleuml.components.DiagramSaver;
 import net.trustx.simpleuml.components.Previewable;
 import net.trustx.simpleuml.dependencydiagram.actions.AddToDependencyDiagramAction;
 import net.trustx.simpleuml.dependencydiagram.actions.GoToDependencyDiagramAction;
 import net.trustx.simpleuml.dependencydiagram.actions.RefreshAction;
 import net.trustx.simpleuml.dependencydiagram.actions.ToggleShowLibraryDependencies;
 import net.trustx.simpleuml.gef.Figure;
 import net.trustx.simpleuml.gef.anchor.FigureAnchor;
 import net.trustx.simpleuml.gef.components.ActionContributorCommandPopupHandler;
 import net.trustx.simpleuml.gef.components.DiagramPaneDragHandler;
 import net.trustx.simpleuml.gef.components.FigureComponentDragHandler;
 import net.trustx.simpleuml.gef.connector.Connector;
 import net.trustx.simpleuml.gef.connector.ConnectorDecorator;
 import net.trustx.simpleuml.gef.connector.ConnectorDecoratorDepends;
 import net.trustx.simpleuml.gef.connector.ConnectorDecoratorExtends;
 import net.trustx.simpleuml.gef.connector.ConnectorDecoratorImplements;
 import net.trustx.simpleuml.gef.connector.ConnectorDecoratorSettings;
 import net.trustx.simpleuml.gef.connector.ConnectorDecoratorUses;
 import net.trustx.simpleuml.gef.connector.ConnectorDirect;
 import net.trustx.simpleuml.plugin.actions.CloseActiveDiagramAction;
 import net.trustx.simpleuml.plugin.actions.DeleteActiveDiagramAction;
 import net.trustx.simpleuml.plugin.actions.RenameActiveDiagramAction;
 import net.trustx.simpleuml.util.UMLUtils;

 public class DependencyDiagramComponentPanel
   extends DiagramComponent
   implements Birdviewable {
   private DependencyDiagramComponent dependencyDiagramComponent;
   private JScrollPane scrollPane;
   private String folderURL;
   private String diagramName;
   private PsiDependencyComponent psiDependencyComponent;
   private int space = 50;

   private int firstColumnWidth;

   private int middleColumnWidth;

   private int lastColumnWidth;

   private int firstRowHeight;

   private int middleRowHeight;

   private int lastRowHeight;

   private int usedHeight;

   private int dependHeight;

   private int extendWidth;

   private int extendedWidth;

   private GroupFigureComponent extendGroupComponent;

   private GroupFigureComponent implementGroupComponent;

   private GroupFigureComponent usedBothGroupComponent;

   private GroupFigureComponent usedGroupComponent;
   private GroupFigureComponent userGroupComponent;
   private GroupFigureComponent dependGroupComponent;
   private GroupFigureComponent dependingGroupComponent;
   private GroupFigureComponent dependBothGroupComponent;
   private GroupFigureComponent extendingGroupComponent;
   private GroupFigureComponent implementingGroupComponent;

   public DependencyDiagramComponentPanel(DependencyDiagramComponent dependencyDiagramComponent, String folderURL, String name) {
     super(new BorderLayout());

     this.dependencyDiagramComponent = dependencyDiagramComponent;
     this.folderURL = folderURL;
     this.diagramName = name;
     dependencyDiagramComponent.setDependencyDiagramComponentPanel(this);

     this.scrollPane = new JScrollPane((Component)dependencyDiagramComponent);

     this.scrollPane.getVerticalScrollBar().setUnitIncrement(20);
     this.scrollPane.getHorizontalScrollBar().setUnitIncrement(20);
     this.scrollPane.getViewport().setScrollMode(1);



     DiagramPaneDragHandler dragHandler = new DiagramPaneDragHandler();
     dragHandler.install(dependencyDiagramComponent, getScrollPane());

     new ActionContributorCommandPopupHandler(dependencyDiagramComponent);



     DefaultActionGroup actionGroup = initDiagramComponentActionGroup();

     ActionToolbar toolBar = ActionManager.getInstance().createActionToolbar("simpleUML.Toolbar", (ActionGroup)actionGroup, false);
     JPanel toolBarPanel = new JPanel(new BorderLayout());
     toolBarPanel.add(toolBar.getComponent(), "West");
     add(toolBarPanel, "West");

     add(this.scrollPane, "Center");
   }




   private DefaultActionGroup initDiagramComponentActionGroup() {
     DefaultActionGroup actionGroup = new DefaultActionGroup();

     RefreshAction refreshAction = new RefreshAction(this);
     actionGroup.add((AnAction)refreshAction);

     ToggleShowLibraryDependencies toggleShowLibraryDependencies = new ToggleShowLibraryDependencies(this.dependencyDiagramComponent);
     actionGroup.add((AnAction)toggleShowLibraryDependencies);

     return actionGroup;
   }



   public void disposeUIResources() {
     this.dependencyDiagramComponent.disposeUIResources();
   }



   public String getDiagramType() {
     return "Dependencydiagram";
   }



   public boolean isShowingOnScreen() {
     return this.dependencyDiagramComponent.isShowing();
   }



   public int getMinimumComponentX() {
     return this.dependencyDiagramComponent.getMinimumComponentX();
   }



   public int getMinimumComponentY() {
     return this.dependencyDiagramComponent.getMinimumComponentY();
   }



   public int getMaximumComponentX() {
     return this.dependencyDiagramComponent.getMaximumComponentX();
   }



   public int getMaximumComponentY() {
     return this.dependencyDiagramComponent.getMaximumComponentY();
   }



   public int getRealWidth() {
     return this.dependencyDiagramComponent.getWidth();
   }



   public int getRealHeight() {
     return this.dependencyDiagramComponent.getHeight();
   }



   public void printAllContents(Graphics2D g2d) {
     this.dependencyDiagramComponent.printAll(g2d);
   }



   public boolean isReady() {
     return this.dependencyDiagramComponent.isVisible();
   }



   public JViewport getJViewport() {
     return this.scrollPane.getViewport();
   }



   public JScrollPane getScrollPane() {
     return this.scrollPane;
   }



   public void addChangeListener(ChangeListener changeListener) {
     this.dependencyDiagramComponent.addChangeListener(changeListener);
   }



   public void removeChangeListener(ChangeListener changeListener) {
     this.dependencyDiagramComponent.removeChangeListener(changeListener);
   }



   public void requestUpdate() {
     this.dependencyDiagramComponent.changesMade();
   }



   public Project getProject() {
     return this.dependencyDiagramComponent.getProject();
   }



   public String getFolderURL() {
     return this.folderURL;
   }



   public void setFolderURL(String folderURL) {
     this.folderURL = folderURL;
   }



   public AnAction getAddToDiagramComponentAction() {
     return (AnAction)new AddToDependencyDiagramAction(this);
   }



   public AnAction getGoToDiagramComponentAction() {
     return (AnAction)new GoToDependencyDiagramAction(this);
   }



   public DiagramSaver getDiagramSaver() {
     return new DependencyDiagramSaver(this);
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



   public void refresh() {
     if (this.psiDependencyComponent != null && this.psiDependencyComponent.getPsiClass() != null)
     {
       add((PsiElement)this.psiDependencyComponent.getPsiClass(), true);
     }
   }



   public void add(PsiElement psiElement, boolean update) {
     if (!(psiElement instanceof PsiClass)) {
       return;
     }

     final PsiClass psiClass = (PsiClass)psiElement;
     this.psiDependencyComponent = new PsiDependencyComponent(this.dependencyDiagramComponent, psiClass);


     clearContents();
     repaint();

     JProgressBar progressBar = new JProgressBar();
     progressBar.setIndeterminate(true);

     ProgressManager.getInstance().runProcessWithProgressSynchronously(new Runnable()
         {
           public void run() {
             final ProgressIndicator progressWindow = ProgressManager.getInstance().getProgressIndicator();
             ApplicationManager.getApplication().runReadAction(new Runnable()
                 {
                   public void run()
                   {
                     DependencyAnalyzer dependencyAnalyzer = new DependencyAnalyzer(psiClass, DependencyDiagramComponentPanel.this.dependencyDiagramComponent, progressWindow);

                     progressWindow.setText("Creating components...");
                     DependencyDiagramComponentPanel.this.createComponents(dependencyAnalyzer);
                     progressWindow.stop();
                   }
                 });
           }
         }, "Analyzing Class...", false, getProject());
   }



   public void createComponents(final DependencyAnalyzer dependencyAnalyzer) {
     EventQueue.invokeLater(new Runnable()
         {
           public void run()
           {
             DependencyDiagramComponentPanel.this.createExtendGroup(dependencyAnalyzer);
             DependencyDiagramComponentPanel.this.createImplementGroup(dependencyAnalyzer);

             DependencyDiagramComponentPanel.this.createUsedBothGroup(dependencyAnalyzer);
             DependencyDiagramComponentPanel.this.createUsedGroup(dependencyAnalyzer);
             DependencyDiagramComponentPanel.this.createUserGroup(dependencyAnalyzer);

             DependencyDiagramComponentPanel.this.createDependBothGroup(dependencyAnalyzer);
             DependencyDiagramComponentPanel.this.createDependGroup(dependencyAnalyzer);
             DependencyDiagramComponentPanel.this.createDependingGroup(dependencyAnalyzer);

             DependencyDiagramComponentPanel.this.createExtendingGroup(dependencyAnalyzer);
             DependencyDiagramComponentPanel.this.createImplementingGroup(dependencyAnalyzer);

             DependencyDiagramComponentPanel.this.dependencyDiagramComponent.addFigureComponent(DependencyDiagramComponentPanel.this.psiDependencyComponent, true);
             (new FigureComponentDragHandler(DependencyDiagramComponentPanel.this.dependencyDiagramComponent, DependencyDiagramComponentPanel.this.getScrollPane())).install(DependencyDiagramComponentPanel.this.psiDependencyComponent);

             DependencyDiagramComponentPanel.this.layoutClassComponents();
             DependencyDiagramComponentPanel.this.dependencyDiagramComponent.layoutContainer();
             DependencyDiagramComponentPanel.this.dependencyDiagramComponent.getConnectorManager().validateConnectors();
             DependencyDiagramComponentPanel.this.dependencyDiagramComponent.repaint();
           }
         });
   }



   public void clearContents() {
     this.dependencyDiagramComponent.getFigureMap().clear();
     this.dependencyDiagramComponent.getConnectorManager().clearAll();
     this.dependencyDiagramComponent.getSelectionManager().clear();
     this.dependencyDiagramComponent.removeAll();
   }



   private GroupFigureComponent createGroupFigureComponent(HashSet classesSet) {
     GroupFigureComponent groupComponent = new GroupFigureComponent(this.dependencyDiagramComponent);
     new ActionContributorCommandPopupHandler(this.dependencyDiagramComponent, groupComponent);
     HashMap<Object, Object> packagesMap = new HashMap<Object, Object>();

     for (Iterator<String> iterator = classesSet.iterator(); iterator.hasNext(); ) {

       String qualifiedClassName = iterator.next();
       PsiClass dependClass = JavaPsiFacade.getInstance(getProject()).findClass(qualifiedClassName, GlobalSearchScope.allScope(getProject()));



       if (dependClass == null) {

         iterator.remove();

         continue;
       }
       SimpleClassFigureComponent simpleClassComponent = new SimpleClassFigureComponent(this.dependencyDiagramComponent, dependClass);
       new ActionContributorCommandPopupHandler(this.dependencyDiagramComponent, simpleClassComponent);
       if (dependClass.isInterface()) {

         simpleClassComponent.setColor(this.dependencyDiagramComponent.getDiagramSettings().getInterfaceBackgroundColor());
       }
       else if (dependClass.getModifierList().hasModifierProperty("abstract")) {

         simpleClassComponent.setColor(this.dependencyDiagramComponent.getDiagramSettings().getAbstractClassBackgroundColor());
       }
       else {

         simpleClassComponent.setColor(this.dependencyDiagramComponent.getDiagramSettings().getClassBackgroundColor());
       }

       PsiPackage psiPackage = UMLUtils.getPackage(dependClass);
       PackageComponent packageComponent = (PackageComponent)packagesMap.get(psiPackage.getQualifiedName());
       if (packageComponent == null) {

         packageComponent = new PackageComponent(this.dependencyDiagramComponent, psiPackage.getQualifiedName());
         new ActionContributorCommandPopupHandler(this.dependencyDiagramComponent, packageComponent);
         packagesMap.put(psiPackage.getQualifiedName(), packageComponent);
         groupComponent.addContentFigure(packageComponent);
       }
       packageComponent.addContentFigure(simpleClassComponent);
     }

     return groupComponent;
   }



   private void createExtendGroup(DependencyAnalyzer dependencyAnalyzer) {
     if (dependencyAnalyzer.getExtendedClasses().size() == 0) {

       this.extendGroupComponent = null;

       return;
     }
     this.extendGroupComponent = createGroupFigureComponent(dependencyAnalyzer.getExtendedClasses());

     ConnectorDecoratorExtends connectorDecoratorExtends = new ConnectorDecoratorExtends();
     connectorDecoratorExtends.setAntialiased(this.dependencyDiagramComponent.getDiagramSettings().isUseAntialiasedConnectors());
     connectorDecoratorExtends.setFillColor(this.dependencyDiagramComponent.getDiagramSettings().getDiagramBackgroundColor());
     ConnectorDirect cd = new ConnectorDirect(this.dependencyDiagramComponent, (ConnectorDecorator)connectorDecoratorExtends, new FigureAnchor((Figure)this.psiDependencyComponent), new FigureAnchor((Figure)this.extendGroupComponent));
     this.dependencyDiagramComponent.addConnector((Connector)cd);
     cd.setVisible(true);
     cd.setActionContributor(new ConnectorActionContributor(this.dependencyDiagramComponent, (Connector)cd));

     this.dependencyDiagramComponent.addFigureComponent(this.extendGroupComponent, true);

     (new FigureComponentDragHandler(this.dependencyDiagramComponent, getScrollPane())).install(this.extendGroupComponent);
   }



   private void createImplementGroup(DependencyAnalyzer dependencyAnalyzer) {
     if (dependencyAnalyzer.getImplementedClasses().size() == 0) {

       this.implementGroupComponent = null;

       return;
     }
     this.implementGroupComponent = createGroupFigureComponent(dependencyAnalyzer.getImplementedClasses());

     ConnectorDecoratorImplements connectorDecoratorImplements = new ConnectorDecoratorImplements();
     connectorDecoratorImplements.setAntialiased(this.dependencyDiagramComponent.getDiagramSettings().isUseAntialiasedConnectors());
     connectorDecoratorImplements.setFillColor(this.dependencyDiagramComponent.getDiagramSettings().getDiagramBackgroundColor());
     ConnectorDirect cd = new ConnectorDirect(this.dependencyDiagramComponent, (ConnectorDecorator)connectorDecoratorImplements, new FigureAnchor((Figure)this.psiDependencyComponent), new FigureAnchor((Figure)this.implementGroupComponent));
     this.dependencyDiagramComponent.addConnector((Connector)cd);
     cd.setVisible(true);
     cd.setActionContributor(new ConnectorActionContributor(this.dependencyDiagramComponent, (Connector)cd));

     this.dependencyDiagramComponent.addFigureComponent(this.implementGroupComponent, true);

     (new FigureComponentDragHandler(this.dependencyDiagramComponent, getScrollPane())).install(this.implementGroupComponent);
   }



   private void createExtendingGroup(DependencyAnalyzer dependencyAnalyzer) {
     if (dependencyAnalyzer.getExtendingClasses().size() == 0) {

       this.extendingGroupComponent = null;

       return;
     }
     this.extendingGroupComponent = createGroupFigureComponent(dependencyAnalyzer.getExtendingClasses());

     ConnectorDecoratorExtends connectorDecoratorExtends = new ConnectorDecoratorExtends();
     connectorDecoratorExtends.setAntialiased(this.dependencyDiagramComponent.getDiagramSettings().isUseAntialiasedConnectors());
     connectorDecoratorExtends.setFillColor(this.dependencyDiagramComponent.getDiagramSettings().getDiagramBackgroundColor());
     ConnectorDirect cd = new ConnectorDirect(this.dependencyDiagramComponent, (ConnectorDecorator)connectorDecoratorExtends, new FigureAnchor((Figure)this.extendingGroupComponent), new FigureAnchor((Figure)this.psiDependencyComponent));
     this.dependencyDiagramComponent.addConnector((Connector)cd);
     cd.setVisible(true);
     cd.setActionContributor(new ConnectorActionContributor(this.dependencyDiagramComponent, (Connector)cd));

     this.dependencyDiagramComponent.addFigureComponent(this.extendingGroupComponent, true);

     (new FigureComponentDragHandler(this.dependencyDiagramComponent, getScrollPane())).install(this.extendingGroupComponent);
   }



   private void createImplementingGroup(DependencyAnalyzer dependencyAnalyzer) {
     if (dependencyAnalyzer.getImplementingClasses().size() == 0) {

       this.implementingGroupComponent = null;

       return;
     }
     this.implementingGroupComponent = createGroupFigureComponent(dependencyAnalyzer.getImplementingClasses());

     ConnectorDecoratorImplements connectorDecoratorImplements = new ConnectorDecoratorImplements();
     connectorDecoratorImplements.setAntialiased(this.dependencyDiagramComponent.getDiagramSettings().isUseAntialiasedConnectors());
     connectorDecoratorImplements.setFillColor(this.dependencyDiagramComponent.getDiagramSettings().getDiagramBackgroundColor());
     ConnectorDirect cd = new ConnectorDirect(this.dependencyDiagramComponent, (ConnectorDecorator)connectorDecoratorImplements, new FigureAnchor((Figure)this.implementingGroupComponent), new FigureAnchor((Figure)this.psiDependencyComponent));
     this.dependencyDiagramComponent.addConnector((Connector)cd);
     cd.setVisible(true);
     cd.setActionContributor(new ConnectorActionContributor(this.dependencyDiagramComponent, (Connector)cd));

     this.dependencyDiagramComponent.addFigureComponent(this.implementingGroupComponent, true);

     (new FigureComponentDragHandler(this.dependencyDiagramComponent, getScrollPane())).install(this.implementingGroupComponent);
   }



   private void createUsedBothGroup(DependencyAnalyzer dependencyAnalyzer) {
     if (dependencyAnalyzer.getUsedBothClasses().size() == 0) {

       this.usedBothGroupComponent = null;

       return;
     }
     this.usedBothGroupComponent = createGroupFigureComponent(dependencyAnalyzer.getUsedBothClasses());

     ConnectorDecoratorSettings decoratorSettings = new ConnectorDecoratorSettings(this.dependencyDiagramComponent.getDiagramSettings().isUseAntialiasedConnectors(), this.dependencyDiagramComponent.getDiagramSettings().getDiagramFont(), this.dependencyDiagramComponent.getDiagramSettings().getDiagramBackgroundColor());


     ConnectorDecoratorUses connectorDecoratorUses1 = new ConnectorDecoratorUses(decoratorSettings);
     ConnectorDirect cd = new ConnectorDirect(this.dependencyDiagramComponent, (ConnectorDecorator)connectorDecoratorUses1, new FigureAnchor((Figure)this.usedBothGroupComponent), new FigureAnchor((Figure)this.psiDependencyComponent));
     this.dependencyDiagramComponent.addConnector((Connector)cd);
     cd.setVisible(true);
     cd.setActionContributor(new ConnectorActionContributor(this.dependencyDiagramComponent, (Connector)cd));

     ConnectorDecoratorSettings decoratorSettings2 = new ConnectorDecoratorSettings(this.dependencyDiagramComponent.getDiagramSettings().isUseAntialiasedConnectors(), this.dependencyDiagramComponent.getDiagramSettings().getDiagramFont(), this.dependencyDiagramComponent.getDiagramSettings().getDiagramBackgroundColor());


     ConnectorDecoratorUses connectorDecoratorUses2 = new ConnectorDecoratorUses(decoratorSettings2);
     ConnectorDirect cd2 = new ConnectorDirect(this.dependencyDiagramComponent, (ConnectorDecorator)connectorDecoratorUses2, new FigureAnchor((Figure)this.psiDependencyComponent), new FigureAnchor((Figure)this.usedBothGroupComponent));
     this.dependencyDiagramComponent.addConnector((Connector)cd2);
     cd2.setVisible(true);
     cd2.setActionContributor(new ConnectorActionContributor(this.dependencyDiagramComponent, (Connector)cd2));

     this.dependencyDiagramComponent.addFigureComponent(this.usedBothGroupComponent, true);

     (new FigureComponentDragHandler(this.dependencyDiagramComponent, getScrollPane())).install(this.usedBothGroupComponent);
   }



   private void createUsedGroup(DependencyAnalyzer dependencyAnalyzer) {
     if (dependencyAnalyzer.getUsedClasses().size() == 0) {

       this.usedGroupComponent = null;

       return;
     }
     this.usedGroupComponent = createGroupFigureComponent(dependencyAnalyzer.getUsedClasses());

     ConnectorDecoratorSettings decoratorSettings = new ConnectorDecoratorSettings(this.dependencyDiagramComponent.getDiagramSettings().isUseAntialiasedConnectors(), this.dependencyDiagramComponent.getDiagramSettings().getDiagramFont(), this.dependencyDiagramComponent.getDiagramSettings().getDiagramBackgroundColor());


     ConnectorDecoratorUses connectorDecoratorUses = new ConnectorDecoratorUses(decoratorSettings);

     ConnectorDirect cd = new ConnectorDirect(this.dependencyDiagramComponent, (ConnectorDecorator)connectorDecoratorUses, new FigureAnchor((Figure)this.psiDependencyComponent), new FigureAnchor((Figure)this.usedGroupComponent));
     this.dependencyDiagramComponent.getConnectorManager().addConnector((Connector)cd);
     cd.setVisible(true);
     cd.setActionContributor(new ConnectorActionContributor(this.dependencyDiagramComponent, (Connector)cd));

     this.dependencyDiagramComponent.addFigureComponent(this.usedGroupComponent, true);

     (new FigureComponentDragHandler(this.dependencyDiagramComponent, getScrollPane())).install(this.usedGroupComponent);
   }



   private void createUserGroup(DependencyAnalyzer dependencyAnalyzer) {
     if (dependencyAnalyzer.getUserClasses().size() == 0) {

       this.userGroupComponent = null;

       return;
     }
     this.userGroupComponent = createGroupFigureComponent(dependencyAnalyzer.getUserClasses());

     ConnectorDecoratorSettings decoratorSettings = new ConnectorDecoratorSettings(this.dependencyDiagramComponent.getDiagramSettings().isUseAntialiasedConnectors(), this.dependencyDiagramComponent.getDiagramSettings().getDiagramFont(), this.dependencyDiagramComponent.getDiagramSettings().getDiagramBackgroundColor());


     ConnectorDecoratorUses connectorDecoratorUses = new ConnectorDecoratorUses(decoratorSettings);

     ConnectorDirect cd = new ConnectorDirect(this.dependencyDiagramComponent, (ConnectorDecorator)connectorDecoratorUses, new FigureAnchor((Figure)this.userGroupComponent), new FigureAnchor((Figure)this.psiDependencyComponent));
     this.dependencyDiagramComponent.getConnectorManager().addConnector((Connector)cd);
     cd.setVisible(true);
     cd.setActionContributor(new ConnectorActionContributor(this.dependencyDiagramComponent, (Connector)cd));

     this.dependencyDiagramComponent.addFigureComponent(this.userGroupComponent, true);

     (new FigureComponentDragHandler(this.dependencyDiagramComponent, getScrollPane())).install(this.userGroupComponent);
   }



   private void createDependBothGroup(DependencyAnalyzer dependencyAnalyzer) {
     if (dependencyAnalyzer.getDependBothClasses().size() == 0) {

       this.dependBothGroupComponent = null;

       return;
     }
     this.dependBothGroupComponent = createGroupFigureComponent(dependencyAnalyzer.getDependBothClasses());

     ConnectorDecoratorDepends connectorDecoratorDepends1 = new ConnectorDecoratorDepends();
     connectorDecoratorDepends1.setAntialiased(this.dependencyDiagramComponent.getDiagramSettings().isUseAntialiasedConnectors());
     ConnectorDirect cd = new ConnectorDirect(this.dependencyDiagramComponent, (ConnectorDecorator)connectorDecoratorDepends1, new FigureAnchor((Figure)this.psiDependencyComponent), new FigureAnchor((Figure)this.dependBothGroupComponent));
     this.dependencyDiagramComponent.addConnector((Connector)cd);
     cd.setVisible(true);
     cd.setActionContributor(new ConnectorActionContributor(this.dependencyDiagramComponent, (Connector)cd));

     ConnectorDecoratorDepends connectorDecoratorDepends2 = new ConnectorDecoratorDepends();
     connectorDecoratorDepends2.setAntialiased(this.dependencyDiagramComponent.getDiagramSettings().isUseAntialiasedConnectors());
     ConnectorDirect cd2 = new ConnectorDirect(this.dependencyDiagramComponent, (ConnectorDecorator)connectorDecoratorDepends2, new FigureAnchor((Figure)this.dependBothGroupComponent), new FigureAnchor((Figure)this.psiDependencyComponent));
     this.dependencyDiagramComponent.addConnector((Connector)cd2);
     cd2.setVisible(true);
     cd2.setActionContributor(new ConnectorActionContributor(this.dependencyDiagramComponent, (Connector)cd2));

     this.dependencyDiagramComponent.addFigureComponent(this.dependBothGroupComponent, true);

     (new FigureComponentDragHandler(this.dependencyDiagramComponent, getScrollPane())).install(this.dependBothGroupComponent);
   }



   private void createDependGroup(DependencyAnalyzer dependencyAnalyzer) {
     if (dependencyAnalyzer.getDependClasses().size() == 0) {

       this.dependGroupComponent = null;

       return;
     }
     this.dependGroupComponent = createGroupFigureComponent(dependencyAnalyzer.getDependClasses());

     ConnectorDecoratorDepends connectorDecoratorDepends = new ConnectorDecoratorDepends();
     connectorDecoratorDepends.setAntialiased(this.dependencyDiagramComponent.getDiagramSettings().isUseAntialiasedConnectors());
     ConnectorDirect cd = new ConnectorDirect(this.dependencyDiagramComponent, (ConnectorDecorator)connectorDecoratorDepends, new FigureAnchor((Figure)this.psiDependencyComponent), new FigureAnchor((Figure)this.dependGroupComponent));
     this.dependencyDiagramComponent.addConnector((Connector)cd);
     cd.setVisible(true);
     cd.setActionContributor(new ConnectorActionContributor(this.dependencyDiagramComponent, (Connector)cd));

     this.dependencyDiagramComponent.addFigureComponent(this.dependGroupComponent, true);
     (new FigureComponentDragHandler(this.dependencyDiagramComponent, getScrollPane())).install(this.dependGroupComponent);
   }



   private void createDependingGroup(DependencyAnalyzer dependencyAnalyzer) {
     if (dependencyAnalyzer.getDependingClasses().size() == 0) {

       this.dependingGroupComponent = null;

       return;
     }
     this.dependingGroupComponent = createGroupFigureComponent(dependencyAnalyzer.getDependingClasses());

     ConnectorDecoratorDepends connectorDecoratorDepends = new ConnectorDecoratorDepends();
     connectorDecoratorDepends.setAntialiased(this.dependencyDiagramComponent.getDiagramSettings().isUseAntialiasedConnectors());
     ConnectorDirect cd = new ConnectorDirect(this.dependencyDiagramComponent, (ConnectorDecorator)connectorDecoratorDepends, new FigureAnchor((Figure)this.dependingGroupComponent), new FigureAnchor((Figure)this.psiDependencyComponent));
     this.dependencyDiagramComponent.addConnector((Connector)cd);
     cd.setVisible(true);
     cd.setActionContributor(new ConnectorActionContributor(this.dependencyDiagramComponent, (Connector)cd));

     this.dependencyDiagramComponent.addFigureComponent(this.dependingGroupComponent, true);
     (new FigureComponentDragHandler(this.dependencyDiagramComponent, getScrollPane())).install(this.dependingGroupComponent);
   }



   private void layoutClassComponents() {
     collectSizes();

     int centerX = this.firstColumnWidth + this.middleColumnWidth / 2;
     int centerY = this.firstRowHeight + this.middleRowHeight / 2;


     this.psiDependencyComponent.setPosX(centerX - (this.psiDependencyComponent.getComponentWidth() + this.space) / 2 + this.space / 2);
     this.psiDependencyComponent.setPosY(centerY - (this.psiDependencyComponent.getComponentHeight() + this.space) / 2 + this.space / 2);


     int currentX = 0;
     int offsetY = (this.middleRowHeight - this.usedHeight) / 2;
     int currentY = this.firstRowHeight + offsetY;

     if (this.usedBothGroupComponent != null) {

       this.usedBothGroupComponent.setPosX(currentX + this.space / 2);
       this.usedBothGroupComponent.setPosY(currentY + this.space / 2);
       currentY += this.usedBothGroupComponent.getComponentHeight() + this.space;
     }
     if (this.usedGroupComponent != null) {

       this.usedGroupComponent.setPosX(currentX + this.space / 2);
       this.usedGroupComponent.setPosY(currentY + this.space / 2);
       currentY += this.usedGroupComponent.getComponentHeight() + this.space;
     }
     if (this.userGroupComponent != null) {

       this.userGroupComponent.setPosX(currentX + this.space / 2);
       this.userGroupComponent.setPosY(currentY + this.space / 2);
     }



     currentX = this.firstColumnWidth + this.middleColumnWidth;
     offsetY = (this.middleRowHeight - this.dependHeight) / 2;
     currentY = this.firstRowHeight + offsetY;

     if (this.dependBothGroupComponent != null) {

       this.dependBothGroupComponent.setPosX(currentX + this.space / 2);
       this.dependBothGroupComponent.setPosY(currentY + this.space / 2);
       currentY += this.dependBothGroupComponent.getComponentHeight() + this.space;
     }
     if (this.dependGroupComponent != null) {

       this.dependGroupComponent.setPosX(currentX + this.space / 2);
       this.dependGroupComponent.setPosY(currentY + this.space / 2);
       currentY += this.dependGroupComponent.getComponentHeight() + this.space;
     }
     if (this.dependingGroupComponent != null) {

       this.dependingGroupComponent.setPosX(currentX + this.space / 2);
       this.dependingGroupComponent.setPosY(currentY + this.space / 2);
     }



     int offsetX = (this.middleColumnWidth - this.extendWidth) / 2;
     currentX = this.firstColumnWidth + offsetX;
     currentY = 0;
     if (this.extendGroupComponent != null) {

       this.extendGroupComponent.setPosX(currentX + this.space / 2);
       this.extendGroupComponent.setPosY(currentY + this.space / 2);
       currentX += this.extendGroupComponent.getComponentWidth() + this.space;
     }
     if (this.implementGroupComponent != null) {

       this.implementGroupComponent.setPosX(currentX + this.space / 2);
       this.implementGroupComponent.setPosY(currentY + this.space / 2);
     }



     offsetX = (this.middleColumnWidth - this.extendedWidth) / 2;
     currentX = this.firstColumnWidth + offsetX;
     currentY = this.firstRowHeight + this.middleRowHeight;

     if (this.extendingGroupComponent != null) {

       this.extendingGroupComponent.setPosX(currentX + this.space / 2);
       this.extendingGroupComponent.setPosY(currentY + this.space / 2);
       currentX += this.extendingGroupComponent.getComponentWidth() + this.space;
     }
     if (this.implementingGroupComponent != null) {

       this.implementingGroupComponent.setPosX(currentX + this.space / 2);
       this.implementingGroupComponent.setPosY(currentY + this.space / 2);
     }


     this.dependencyDiagramComponent.getConnectorManager().validateConnectors();
   }





   private void collectSizes() {
     this.firstColumnWidth = 0;
     if (this.usedBothGroupComponent != null)
       this.firstColumnWidth = Math.max(this.firstColumnWidth, this.usedBothGroupComponent.getComponentWidth() + this.space);
     if (this.usedGroupComponent != null)
       this.firstColumnWidth = Math.max(this.firstColumnWidth, this.usedGroupComponent.getComponentWidth() + this.space);
     if (this.userGroupComponent != null) {
       this.firstColumnWidth = Math.max(this.firstColumnWidth, this.userGroupComponent.getComponentWidth() + this.space);
     }

     this.middleColumnWidth = 0;
     this.extendWidth = 0;
     if (this.extendGroupComponent != null)
       this.extendWidth += this.extendGroupComponent.getComponentWidth() + this.space;
     if (this.implementGroupComponent != null)
       this.extendWidth += this.implementGroupComponent.getComponentWidth() + this.space;
     this.middleColumnWidth = this.extendWidth;
     this.middleColumnWidth = Math.max(this.middleColumnWidth, this.psiDependencyComponent.getComponentWidth() + this.space);
     this.extendedWidth = 0;
     if (this.extendingGroupComponent != null)
       this.extendedWidth += this.extendingGroupComponent.getComponentWidth() + this.space;
     if (this.implementingGroupComponent != null)
       this.extendedWidth += this.implementingGroupComponent.getComponentWidth() + this.space;
     this.middleColumnWidth = Math.max(this.middleColumnWidth, this.extendedWidth);


     this.lastColumnWidth = 0;
     if (this.dependBothGroupComponent != null)
       this.lastColumnWidth = Math.max(this.lastColumnWidth, this.dependBothGroupComponent.getComponentWidth() + this.space);
     if (this.dependGroupComponent != null)
       this.lastColumnWidth = Math.max(this.lastColumnWidth, this.dependGroupComponent.getComponentWidth() + this.space);
     if (this.dependingGroupComponent != null) {
       this.lastColumnWidth = Math.max(this.lastColumnWidth, this.dependingGroupComponent.getComponentWidth() + this.space);
     }



     this.firstRowHeight = 0;
     if (this.extendGroupComponent != null)
       this.firstRowHeight = Math.max(this.firstRowHeight, this.extendGroupComponent.getComponentHeight() + this.space);
     if (this.implementGroupComponent != null) {
       this.firstRowHeight = Math.max(this.firstRowHeight, this.implementGroupComponent.getComponentHeight() + this.space);
     }

     this.middleRowHeight = 0;
     this.usedHeight = 0;
     if (this.usedBothGroupComponent != null)
       this.usedHeight += this.usedBothGroupComponent.getComponentHeight() + this.space;
     if (this.usedGroupComponent != null)
       this.usedHeight += this.usedGroupComponent.getComponentHeight() + this.space;
     if (this.userGroupComponent != null)
       this.usedHeight += this.userGroupComponent.getComponentHeight() + this.space;
     this.middleRowHeight = Math.max(this.middleRowHeight, this.usedHeight);
     this.middleRowHeight = Math.max(this.middleRowHeight, this.psiDependencyComponent.getComponentHeight() + this.space);
     this.dependHeight = 0;
     if (this.dependBothGroupComponent != null)
       this.dependHeight += this.dependBothGroupComponent.getComponentHeight() + this.space;
     if (this.dependGroupComponent != null)
       this.dependHeight += this.dependGroupComponent.getComponentHeight() + this.space;
     if (this.dependingGroupComponent != null)
       this.dependHeight += this.dependingGroupComponent.getComponentHeight() + this.space;
     this.middleRowHeight = Math.max(this.middleRowHeight, this.dependHeight);


     this.lastRowHeight = 0;
     if (this.extendingGroupComponent != null)
       this.lastRowHeight = Math.max(this.lastRowHeight, this.extendingGroupComponent.getComponentHeight() + this.space);
     if (this.implementingGroupComponent != null) {
       this.lastRowHeight = Math.max(this.lastRowHeight, this.implementingGroupComponent.getComponentHeight() + this.space);
     }
   }


   public void addPsiElements(AnActionEvent event) {
     Project project = (Project)DataKeys.PROJECT.getData(event.getDataContext());
     ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("simpleUML");

     PsiFile psiFile = (PsiFile)DataKeys.PSI_FILE.getData(event.getDataContext());

     PsiElement selectedElement = (PsiElement)DataKeys.PSI_ELEMENT.getData(event.getDataContext());

     if (selectedElement != null && selectedElement instanceof PsiClass) {

       addPsiElement(selectedElement, true);
       toolWindow.activate(null);
     }
     else if (psiFile != null && psiFile instanceof PsiJavaFile) {

       PsiJavaFile psiJavaFile = (PsiJavaFile)psiFile;
       PsiClass[] psiClasses = psiJavaFile.getClasses();
       if (psiClasses.length > 0) {

         addPsiElement((PsiElement)psiClasses[0], true);
         toolWindow.activate(null);
       }
     }
   }



   public boolean canAdd(AnActionEvent event) {
     PsiFile psiFile = (PsiFile)DataKeys.PSI_FILE.getData(event.getDataContext());

     PsiElement selectedElement = (PsiElement)DataKeys.PSI_ELEMENT.getData(event.getDataContext());

     if (selectedElement != null && selectedElement instanceof PsiClass)
     {
       return true;
     }
     if (psiFile != null && psiFile instanceof PsiJavaFile) {

       PsiJavaFile psiJavaFile = (PsiJavaFile)psiFile;
       PsiClass[] psiClasses = psiJavaFile.getClasses();
       if (psiClasses.length > 0)
       {
         return true;
       }
     }
     return false;
   }



   public boolean canHandle(PsiElement psiElement) {
     return psiElement instanceof PsiClass;
   }



   public boolean contains(PsiElement psiElement) {
     if (psiElement instanceof PsiClass) {

       PsiClass psiClass = (PsiClass)psiElement;
       return this.dependencyDiagramComponent.containsDependency(psiClass.getQualifiedName());
     }


     return false;
   }




   public void highlight(PsiElement psiElement) {
     if (psiElement instanceof PsiClass) {

       PsiClass psiClass = (PsiClass)psiElement;
       this.dependencyDiagramComponent.containsHighlightDependency(psiClass.getQualifiedName());
     }
   }



   public DependencyDiagramComponentSettings getComponentSettings() {
     return this.dependencyDiagramComponent.getComponentSettings();
   }



   public DependencyDiagramComponent getDependencyDiagramComponent() {
     return this.dependencyDiagramComponent;
   }



   public String getPsiDependencyClassName() {
     if (this.psiDependencyComponent == null)
     {
       return "";
     }

     PsiClass psiClass = this.psiDependencyComponent.getPsiClass();
     if (psiClass == null)
     {
       return "";
     }

     return psiClass.getQualifiedName();
   }



   public JComponent getPreferredFocusedComponent() {
     return (JComponent)this.dependencyDiagramComponent;
   }



   public Previewable getPreviewable() {
     return (Previewable)this;
   }
 }


