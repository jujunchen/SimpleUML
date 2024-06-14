 package net.trustx.simpleuml.plugin;

 import com.intellij.openapi.actionSystem.ActionGroup;
 import com.intellij.openapi.actionSystem.ActionManager;
 import com.intellij.openapi.actionSystem.ActionPopupMenu;
 import com.intellij.openapi.actionSystem.ActionToolbar;
 import com.intellij.openapi.actionSystem.AnAction;
 import com.intellij.openapi.actionSystem.DefaultActionGroup;
 import com.intellij.openapi.application.ApplicationManager;
 import com.intellij.openapi.components.ProjectComponent;
 import com.intellij.openapi.fileEditor.FileEditorManager;
 import com.intellij.openapi.fileEditor.OpenFileDescriptor;
 import com.intellij.openapi.options.Configurable;
 import com.intellij.openapi.options.ConfigurationException;
 import com.intellij.openapi.project.Project;
 import com.intellij.openapi.ui.Messages;
 import com.intellij.openapi.vfs.VirtualFile;
 import com.intellij.openapi.vfs.VirtualFileManager;
 import com.intellij.openapi.wm.ToolWindow;
 import com.intellij.openapi.wm.ToolWindowAnchor;
 import com.intellij.openapi.wm.ToolWindowManager;
 import com.intellij.ui.content.Content;
 import java.awt.BorderLayout;
 import java.awt.Component;
 import java.awt.event.MouseAdapter;
 import java.awt.event.MouseEvent;
 import java.io.ByteArrayOutputStream;
 import java.io.IOException;
 import java.io.OutputStreamWriter;
 import java.util.Collection;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;
 import java.util.Set;
 import javax.swing.Icon;
 import javax.swing.ImageIcon;
 import javax.swing.JComponent;
 import javax.swing.JPanel;
 import javax.swing.JPopupMenu;
 import javax.swing.JTabbedPane;
 import javax.swing.event.AncestorEvent;
 import javax.swing.event.AncestorListener;
 import javax.swing.event.ChangeEvent;
 import javax.swing.event.ChangeListener;
 import net.trustx.simpleuml.components.BirdViewFrame;
 import net.trustx.simpleuml.components.Birdviewable;
 import net.trustx.simpleuml.components.DiagramComponent;
 import net.trustx.simpleuml.file.UMLFileManager;
 import net.trustx.simpleuml.plugin.actions.LoadDiagramAction;
 import net.trustx.simpleuml.plugin.actions.SaveAsImageAction;
 import net.trustx.simpleuml.plugin.actions.ShowBirdViewAction;
 import net.trustx.simpleuml.plugin.configuration.ConfigurationFactory;
 import net.trustx.simpleuml.util.UMLUtils;
 import org.jdom.Document;
 import org.jdom.Element;
 import org.jdom.output.XMLOutputter;















 public class UMLToolWindowPlugin
   implements ProjectComponent, Configurable
 {
   public static final String TOOL_WINDOW_ID = "simpleUML";
   private HashMap diagramComponentMap;
   private Project project;
   private static final HashMap umlToolWindowPlugins = new HashMap<Object, Object>();

   private ToolWindow myToolWindow;
   private JPanel umlToolWindowContentPanel;

   public static synchronized UMLToolWindowPlugin getUMLToolWindowPlugin(Project project) {
     return (UMLToolWindowPlugin)getUMLToolWindowPluginsMap().get(project);
   }

   private JTabbedPane diagramTabbedPane;

   public static HashMap getUMLToolWindowPluginsMap() {
     return umlToolWindowPlugins;
   }



   private ConfigurationFactory configurationFactory;


   private UMLFileManager umlFileManager;


   private BirdViewFrame birdViewFrame;


   private boolean birdViewShouldBeVisible;



   public UMLToolWindowPlugin(Project project) {
     this.project = project;
     this.diagramComponentMap = new HashMap<Object, Object>();
     this.diagramTabbedPane = new JTabbedPane(1);
   }



   public void projectOpened() {
     initToolWindow();
   }



   public void projectClosed() {
     unregisterToolWindow();
   }




   public void initComponent() {}




   public void disposeComponent() {}



   public String getComponentName() {
     return "simpleUML.UMLToolWindowPlugin";
   }



   public ConfigurationFactory getConfigurationFactory() {
     return this.configurationFactory;
   }



   public boolean existsDiagramOnDisk(String folderURL, String name) {
     String wholePath = UMLUtils.getWholePath(folderURL, name);
     return (VirtualFileManager.getInstance().findFileByUrl(wholePath) != null);
   }



   public DiagramComponent[] getDiagramComponentsOnUI() {
     Collection col = this.diagramComponentMap.values();
     return (DiagramComponent[])col.toArray((Object[])new DiagramComponent[col.size()]);
   }



   public Set getKnownDiagramComponentsNotOnUI() {
     Set kfs = this.umlFileManager.getKnownFilesURLSet();
     kfs.removeAll(this.diagramComponentMap.keySet());
     return kfs;
   }



   public UMLFileManager getUMLFileManager() {
     return this.umlFileManager;
   }



   public void setUmlFileManager(UMLFileManager umlFileManager) {
     this.umlFileManager = umlFileManager;
   }



   private void initToolWindow() {
     ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(this.project);

     this.umlToolWindowContentPanel = new JPanel(new BorderLayout());

     this.birdViewShouldBeVisible = false;
     initBirdViewFrameListeners();

     this.diagramTabbedPane.addChangeListener(new ChangeListener()
         {
           public void stateChanged(ChangeEvent e)
           {
             DiagramComponent diagramComponent = UMLToolWindowPlugin.this.getSelectedDiagramComponent();
             if (UMLToolWindowPlugin.this.birdViewFrame != null && diagramComponent != null)
             {
               if (diagramComponent instanceof Birdviewable) {

                 Birdviewable birdviewable = (Birdviewable)diagramComponent;
                 UMLToolWindowPlugin.this.birdViewFrame.setCurrentBirdviewable(birdviewable);
                 birdviewable.requestUpdate();
               }
               else {

                 UMLToolWindowPlugin.this.birdViewFrame.setCurrentBirdviewable(null);
               }
             }
           }
         });


     initTabPopupMenus();

     this.umlToolWindowContentPanel.add(this.diagramTabbedPane, "Center");

     if (this.configurationFactory == null)
     {
       initDefaultConfigurationFactory();
     }

     getUMLToolWindowPluginsMap().put(this.project, this);

     DefaultActionGroup actionGroup = initToolbarActionGroup();

     ActionToolbar toolBar = ActionManager.getInstance().createActionToolbar("simpleUML.Toolbar", (ActionGroup)actionGroup, false);
     this.umlToolWindowContentPanel.add(toolBar.getComponent(), "West");


     this.myToolWindow = toolWindowManager.registerToolWindow("simpleUML", false, ToolWindowAnchor.LEFT);
     Content content = this.myToolWindow.getContentManager().getFactory().createContent(this.umlToolWindowContentPanel, "Diagram", false);
     this.myToolWindow.getContentManager().addContent(content);
     this.myToolWindow.setIcon(new ImageIcon(UMLToolWindowPlugin.class.getResource("/net/trustx/simpleuml/icons/simpleUMLsmall.png")));


     this.birdViewFrame = new BirdViewFrame();
   }



   private void initBirdViewFrameListeners() {
     this.umlToolWindowContentPanel.addAncestorListener(new AncestorListener()
         {
           public void ancestorAdded(AncestorEvent event)
           {
             if (UMLToolWindowPlugin.this.birdViewShouldBeVisible && !UMLToolWindowPlugin.this.birdViewFrame.isShowing())
             {
               (new ShowBirdViewAction(UMLToolWindowPlugin.this)).showNewBirdView();
             }
           }



           public void ancestorRemoved(AncestorEvent event) {
             if (UMLToolWindowPlugin.this.birdViewFrame.isShowing())
             {
               UMLToolWindowPlugin.this.birdViewFrame.disposeBirdViewFrame(false);
             }
           }




           public void ancestorMoved(AncestorEvent event) {}
         });
   }



   public Collection getDiagramComponents() {
     return this.diagramComponentMap.values();
   }



   private DefaultActionGroup initToolbarActionGroup() {
     DefaultActionGroup actionGroup = new DefaultActionGroup();

     LoadDiagramAction loadDiagramAction = new LoadDiagramAction();
     SaveAsImageAction saveAsImageAction = new SaveAsImageAction(this);
     ShowBirdViewAction showBirdViewAction = new ShowBirdViewAction(this);

     actionGroup.add((AnAction)loadDiagramAction);
     actionGroup.add((AnAction)saveAsImageAction);
     actionGroup.add((AnAction)showBirdViewAction);
     return actionGroup;
   }



   public DiagramComponent getSelectedDiagramComponent() {
     int selectedIndex = this.diagramTabbedPane.getSelectedIndex();

     if (selectedIndex == -1)
     {
       return null;
     }


     return getDiagramComponentAtIndex(selectedIndex);
   }




   public BirdViewFrame getBirdViewFrame() {
     return this.birdViewFrame;
   }



   public void setBirdViewFrame(BirdViewFrame birdViewFrame) {
     if (this.birdViewFrame != null)
     {
       this.birdViewFrame.dispose();
     }
     this.birdViewFrame = birdViewFrame;
   }



   public boolean isBirdViewShouldBeVisible() {
     return this.birdViewShouldBeVisible;
   }



   public void setBirdViewShouldBeVisible(boolean birdViewShouldBeVisible) {
     this.birdViewShouldBeVisible = birdViewShouldBeVisible;
   }



   public DiagramComponent getDiagramComponentAtIndex(int index) {
     return (DiagramComponent)this.diagramTabbedPane.getComponentAt(index);
   }



   private void initTabPopupMenus() {
     this.diagramTabbedPane.addMouseListener(new MouseAdapter()
         {
           public void mouseReleased(MouseEvent e)
           {
             UMLToolWindowPlugin.this.showPopupForTab(e);
           }



           public void mousePressed(MouseEvent e) {
             UMLToolWindowPlugin.this.showPopupForTab(e);
           }



           public void mouseClicked(MouseEvent e) {
             UMLToolWindowPlugin.this.showPopupForTab(e);
           }
         });
   }



   private void showPopupForTab(MouseEvent e) {
     int index = this.diagramTabbedPane.indexAtLocation(e.getX(), e.getY());

     if (e.isPopupTrigger() && index != -1) {

       DiagramComponent diagramComponent = (DiagramComponent)this.diagramTabbedPane.getComponentAt(index);
       ActionPopupMenu actionPopupMenu = ActionManager.getInstance().createActionPopupMenu("Popup", diagramComponent.getTabActionGroup());
       JPopupMenu popupMenu = actionPopupMenu.getComponent();
       popupMenu.show(this.diagramTabbedPane, e.getX(), e.getY());
     }
   }



   public void deleteRemoveDiagramFromUI(final String folderURL, final String name) {
     int index = getIndexOfDiagramComponent(folderURL, name);

     if (this.diagramTabbedPane.getTabCount() == 1)
     {

       this.birdViewFrame.disposeBirdViewFrame(true);
     }

     final DiagramComponent removedDiagramComponent = (DiagramComponent)this.diagramComponentMap.remove(UMLUtils.getWholePath(folderURL, name));

     if (removedDiagramComponent == null) {

       Messages.showMessageDialog("Internal error: deleteRemoveDiagramFromUI", "Error", Messages.getErrorIcon());
     }
     else {

       ApplicationManager.getApplication().runWriteAction(new Runnable()
           {
             public void run()
             {
               UMLToolWindowPlugin.this.deleteDiagramOnDisk(folderURL, name);
               removedDiagramComponent.disposeUIResources();
             }
           });
     }

     this.diagramTabbedPane.removeTabAt(index);
   }



   private int getIndexOfDiagramComponent(String folderURL, String name) {
     for (int i = 0; i < this.diagramTabbedPane.getTabCount(); i++) {

       DiagramComponent diagramComponent = getDiagramComponentAtIndex(i);
       if (diagramComponent.getFolderURL().equals(folderURL) && diagramComponent.getDiagramName().equals(name))
       {
         return i;
       }
     }

     return -1;
   }



   public void deleteDiagramComponent(final String folderURL, final String name) {
     ApplicationManager.getApplication().runWriteAction(new Runnable()
         {
           public void run()
           {
             UMLToolWindowPlugin.this.deleteDiagramOnDisk(folderURL, name);
           }
         });
   }




   private void deleteDiagramOnDisk(String folderURL, String name) {
     try {
       VirtualFile diagramToRemove = VirtualFileManager.getInstance().findFileByUrl(UMLUtils.getWholePath(folderURL, name));
       if (diagramToRemove != null)
       {
         diagramToRemove.delete(this);
       }
     }
     catch (IOException ioEx) {

       Messages.showMessageDialog("Could not delete diagram on disk", "Error", Messages.getErrorIcon());
     }
   }



   public void storeRemoveDiagramFromUI(final String folderURL, final String name) {
     ApplicationManager.getApplication().runWriteAction(new Runnable()
         {
           public void run()
           {
             UMLToolWindowPlugin.this.storeDiagramComponentsOnDisk();

             if (UMLToolWindowPlugin.this.diagramTabbedPane.getTabCount() == 1)
             {
               UMLToolWindowPlugin.this.birdViewFrame.disposeBirdViewFrame(true);
             }

             DiagramComponent removedDiagramComponent = (DiagramComponent)UMLToolWindowPlugin.this.diagramComponentMap.remove(UMLUtils.getWholePath(folderURL, name));

             if (removedDiagramComponent == null) {

               Messages.showMessageDialog("Internal error: storeRemoveDiagramFromUI", "Error", Messages.getErrorIcon());
             }
             else {

               removedDiagramComponent.disposeUIResources();
             }

             UMLToolWindowPlugin.this.diagramTabbedPane.removeTabAt(UMLToolWindowPlugin.this.getIndexOfDiagramComponent(folderURL, name));
           }
         });
   }



   private void unregisterToolWindow() {
     getUMLToolWindowPluginsMap().remove(this);
     ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(this.project);
     toolWindowManager.unregisterToolWindow("simpleUML");
   }



   public void writeExternal(Element element) {
     if (this.configurationFactory == null || this.umlFileManager == null) {
       return;
     }


     this.configurationFactory.saveToElement(element);
     saveDiagramComponents();
   }



   public void saveDiagramComponents() {
     ApplicationManager.getApplication().runWriteAction(new Runnable()
         {
           public void run()
           {
             UMLToolWindowPlugin.this.storeDiagramComponentsOnDisk();
           }
         });
   }



   public void readExternal(Element element) {
     Map<Object, Object> settingsMap = new HashMap<Object, Object>();
     List<Element> entries = element.getChildren("option");
     for (int i = 0; i < entries.size(); i++) {

       Element entry = entries.get(i);
       String name = entry.getAttribute("name").getValue();
       String value = entry.getAttribute("value").getValue();
       settingsMap.put(name, value);
     }

     if (this.configurationFactory == null)
     {
       initDefaultConfigurationFactory();
     }
     this.configurationFactory.readFromElement(element);
   }




   public void apply() throws ConfigurationException {
     if (this.configurationFactory == null)
     {
       initDefaultConfigurationFactory();
     }
     this.configurationFactory.apply();
   }



   public void disposeUIResources() {
     if (this.configurationFactory == null)
     {
       initDefaultConfigurationFactory();
     }
     this.configurationFactory.disposeUIResources();
   }



   public JComponent createComponent() {
     if (this.configurationFactory == null)
     {
       initDefaultConfigurationFactory();
     }
     return this.configurationFactory.createComponent();
   }



   public String getDisplayName() {
     if (this.configurationFactory == null)
     {
       initDefaultConfigurationFactory();
     }
     return this.configurationFactory.getDisplayName();
   }



   public String getHelpTopic() {
     if (this.configurationFactory == null)
     {
       initDefaultConfigurationFactory();
     }
     return this.configurationFactory.getHelpTopic();
   }



   public Icon getIcon() {
     if (this.configurationFactory == null)
     {
       initDefaultConfigurationFactory();
     }
     return this.configurationFactory.getIcon();
   }



   public boolean isModified() {
     if (this.configurationFactory == null)
     {
       initDefaultConfigurationFactory();
     }
     return this.configurationFactory.isModified();
   }



   public void reset() {
     if (this.configurationFactory == null)
     {
       initDefaultConfigurationFactory();
     }
     this.configurationFactory.reset();
   }



   private void initDefaultConfigurationFactory() {
     if (this.configurationFactory == null)
     {
       this.configurationFactory = new ConfigurationFactory(this);
     }
   }



   private boolean isInEditor(DiagramComponent diagramComponent) {
     return (getIndexOfDiagramComponent(diagramComponent.getFolderURL(), diagramComponent.getDiagramName()) == -1);
   }



   public void showDiagramComponent(DiagramComponent diagramComponent) {
     if (diagramComponent != null)
     {
       if (isInEditor(diagramComponent)) {

         String wholePath = UMLUtils.getWholePath(diagramComponent.getFolderURL(), diagramComponent.getDiagramName());
         VirtualFile virtualFile = VirtualFileManager.getInstance().findFileByUrl(wholePath);
         if (virtualFile != null && virtualFile.isValid())
         {
           OpenFileDescriptor openFileDescriptor = new OpenFileDescriptor(this.project, virtualFile);
           FileEditorManager.getInstance(this.project).openTextEditor(openFileDescriptor, true);
         }

       } else {

         this.diagramTabbedPane.setSelectedIndex(getIndexOfDiagramComponent(diagramComponent.getFolderURL(), diagramComponent.getDiagramName()));
         ToolWindowManager.getInstance(this.project).getToolWindow("simpleUML").show(null);
       }
     }
   }



   public JTabbedPane getTabbedPane() {
     return this.diagramTabbedPane;
   }



   public Project getProject() {
     return this.project;
   }



   public Component getContentPane() {
     return this.umlToolWindowContentPanel;
   }



   public DiagramComponent getDiagramComponent(String folderURL, String diagramName) {
     return (DiagramComponent)this.diagramComponentMap.get(UMLUtils.getWholePath(folderURL, diagramName));
   }



   public Map getDiagramComponentMap() {
     return this.diagramComponentMap;
   }



   public boolean addDiagramComponent(DiagramComponent diagramComponent) {
     String folderURL = diagramComponent.getFolderURL();
     String diagramName = diagramComponent.getDiagramName();
     String displayName = UMLUtils.stripFileType(diagramName);
     if (!diagramComponent.canSave())
     {
       displayName = displayName + " readonly";
     }
     if (isDiagramComponentOnUI(folderURL, diagramName)) {

       Messages.showMessageDialog(this.project, "Diagram is already open", "ERROR", Messages.getErrorIcon());
       showDiagramComponent(getDiagramComponent(folderURL, diagramName));
       return false;
     }

     String holePath = UMLUtils.getWholePath(folderURL, diagramName);
     this.diagramComponentMap.put(holePath, diagramComponent);
     this.diagramTabbedPane.addTab(displayName, (Component)diagramComponent);
     this.umlFileManager.addURL(holePath);
     return true;
   }



   public boolean isDiagramComponentOnDisk(String folderURL, String diagramName) {
     String holePath = UMLUtils.getWholePath(folderURL, diagramName);
     VirtualFile virtualFile = VirtualFileManager.getInstance().findFileByUrl(holePath);
     return (virtualFile != null && virtualFile.isValid());
   }



   public boolean isDiagramComponentOnUI(String folderURL, String diagramName) {
     return (this.diagramComponentMap.get(UMLUtils.getWholePath(folderURL, diagramName)) != null);
   }



   public boolean isDiagramComponentInEditor(String folderURL, String diagramName) {
     return (isDiagramComponentOnUI(folderURL, diagramName) && getIndexOfDiagramComponent(folderURL, diagramName) == -1);
   }



   public boolean isDiagramComponentInToolWindow(String folderURL, String diagramName) {
     return (isDiagramComponentOnUI(folderURL, diagramName) && getIndexOfDiagramComponent(folderURL, diagramName) != -1);
   }




   private void storeDiagramComponentsOnDisk() {
     try {
       if (this.diagramTabbedPane != null) {

         int tabCount = this.diagramTabbedPane.getTabCount();
         for (int i = 0; i < tabCount; i++) {

           DiagramComponent diagramComponent = (DiagramComponent)this.diagramTabbedPane.getComponentAt(i);
           String diagramName = diagramComponent.getDiagramName();
           String displayName = UMLUtils.stripFileType(diagramName);
           if (!diagramComponent.canSave())
           {
             displayName = displayName + " readonly";
           }
           this.diagramTabbedPane.setTitleAt(i, displayName);
         }
       }

       Collection collection = this.diagramComponentMap.values();
       for (Iterator<DiagramComponent> iterator = collection.iterator(); iterator.hasNext(); )
       {
         DiagramComponent diagramComponent = iterator.next();
         Document document = diagramComponent.getDiagramSaver().saveToDocument();

         String folderURL = diagramComponent.getFolderURL();
         VirtualFile vf = VirtualFileManager.getInstance().findFileByUrl(UMLUtils.getWholePath(folderURL, diagramComponent.getDiagramName()));
         if (vf == null) {

           VirtualFile virtualFile = VirtualFileManager.getInstance().findFileByUrl(folderURL);
           if (virtualFile == null)
             continue;
           vf = virtualFile.createChildData(this, diagramComponent.getDiagramName());
         }

         this.umlFileManager.addURL(vf.getUrl());

         if (!diagramComponent.canSave()) {
           continue;
         }

         XMLOutputter outputter = new XMLOutputter();

         ByteArrayOutputStream baos = new ByteArrayOutputStream(8192);

         OutputStreamWriter outputStreamWriter = new OutputStreamWriter(baos);
         outputter.output(document, outputStreamWriter);
         outputStreamWriter.flush();
         outputStreamWriter.close();
         vf.setBinaryContent(baos.toByteArray());
       }

     } catch (IOException ioEx) {

       ioEx.printStackTrace();
     }
   }
 }


