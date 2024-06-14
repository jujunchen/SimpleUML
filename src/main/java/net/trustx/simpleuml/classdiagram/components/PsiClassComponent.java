 package net.trustx.simpleuml.classdiagram.components;
 
 import com.intellij.openapi.actionSystem.DataKeys;
 import com.intellij.openapi.actionSystem.DataProvider;
 import com.intellij.psi.*;
 import com.intellij.psi.search.GlobalSearchScope;
 import java.awt.Color;
 import java.awt.Component;
 import java.awt.Dimension;
 import java.awt.EventQueue;
 import java.awt.GridBagConstraints;
 import java.awt.GridBagLayout;
 import java.awt.Point;
 import java.awt.Rectangle;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.awt.event.KeyAdapter;
 import java.awt.event.KeyEvent;
 import java.awt.event.MouseAdapter;
 import java.awt.event.MouseEvent;
 import java.util.ArrayList;
 import java.util.Collection;
 import java.util.HashSet;
 import java.util.Iterator;
 import java.util.LinkedList;
 import java.util.List;
 import java.util.Map;
 import javax.swing.Box;
 import javax.swing.BoxLayout;
 import javax.swing.JComponent;
 import javax.swing.JPanel;
 import javax.swing.SwingUtilities;
 import net.trustx.simpleuml.classdiagram.actions.AddAllInheritorsAction;
 import net.trustx.simpleuml.classdiagram.actions.AddDependenciesAction;
 import net.trustx.simpleuml.classdiagram.actions.AddDirectInheritorsAction;
 import net.trustx.simpleuml.classdiagram.actions.AddExtendedClassesAction;
 import net.trustx.simpleuml.classdiagram.actions.AddImplementedClassesAction;
 import net.trustx.simpleuml.classdiagram.actions.AddInnerClassesAction;
 import net.trustx.simpleuml.classdiagram.actions.AddUpdateDependencyConnectorsAction;
 import net.trustx.simpleuml.classdiagram.actions.PinAction;
 import net.trustx.simpleuml.classdiagram.actions.ReloadAction;
 import net.trustx.simpleuml.classdiagram.actions.RemoveDependencyConnectorsAction;
 import net.trustx.simpleuml.classdiagram.actions.UnpinAction;
 import net.trustx.simpleuml.classdiagram.configuration.ClassDiagramSettings;
 import net.trustx.simpleuml.classdiagram.configuration.ClassDiagramSettingsListener;
 import net.trustx.simpleuml.classdiagram.util.PsiComponentUtils;
 import net.trustx.simpleuml.components.ActionContributorCommand;
 import net.trustx.simpleuml.components.ActionContributorCommandInfo;
 import net.trustx.simpleuml.components.ExpandComponent;
 import net.trustx.simpleuml.components.UMLLabel;
 import net.trustx.simpleuml.gef.components.ChangeColorCommand;
 import net.trustx.simpleuml.gef.components.FigureComponent;
 import net.trustx.simpleuml.gef.components.FigureComponentSelectionHandler;
 import net.trustx.simpleuml.gef.components.RemoveFigureComponentCommand;
 import net.trustx.simpleuml.gef.connector.ConnectorDirect;
 
 

 
 public class PsiClassComponent
   extends FigureComponent
   implements ClassDiagramSettingsListener, DataProvider
 {
   private PsiClass psiClass;
   private JPanel centerPanel;
   private String qualifiedClassName;
   private String[] qualifiedInnerClassNames;
   private Box headerBox;
   private Box fieldBox;
   private Box constructorBox;
   private Box methodBox;
   private int posX;
   private int posY;
   private ClassDiagramComponent classDiagramComponent;
   private ExpandComponent toggleFieldButton;
   private ExpandComponent toggleConstructorButton;
   private ExpandComponent toggleMethodButton;
   private Collection extendsClassnameSet;
   private Collection implementsClassnameSet;
   private Collection fieldsClassnameSet;
   private List fieldsList;
   private PsiElement lastPsiElement;
   private boolean pinned;
   private boolean highlighted;
   
   public PsiClassComponent(ClassDiagramComponent classDiagramComponent, PsiClass psiClass) {
     this(classDiagramComponent, psiClass.getQualifiedName());
     this.psiClass = psiClass;
   }
 
 
   
   public PsiClassComponent(final ClassDiagramComponent classDiagramComponent, String qualifiedClassName) {
     super(classDiagramComponent);
     
     this.qualifiedClassName = qualifiedClassName;
     this.classDiagramComponent = classDiagramComponent;
     
     this.extendsClassnameSet = new HashSet();
     this.implementsClassnameSet = new HashSet();
     this.fieldsClassnameSet = new HashSet();
     
     this.fieldsList = new LinkedList();
 
     
     this.qualifiedInnerClassNames = new String[0];
     
     getContentPane().setLayout(new GridBagLayout());
     
     this.centerPanel = new JPanel();
     this.centerPanel.setOpaque(false);
     this.centerPanel.setLayout(new BoxLayout(this.centerPanel, 1));
     getContentPane().add(this.centerPanel, new GridBagConstraints());
 
     
     this.posX = 0;
     this.posY = 0;
     
     ActionListener actionListener = new ActionListener()
       {
         public void actionPerformed(ActionEvent e)
         {
           PsiClassComponent.this.rebuildComponent(true);
           classDiagramComponent.changesMade(true);
         }
       };
     
     addKeyListener(new KeyAdapter()
         {
           public void keyPressed(KeyEvent e)
           {
             classDiagramComponent.dispatchEvent(e);
           }
 
 
           
           public void keyReleased(KeyEvent e) {
             classDiagramComponent.dispatchEvent(e);
           }
 
 
           
           public void keyTyped(KeyEvent e) {
             classDiagramComponent.dispatchEvent(e);
           }
         });
     
     initSelectionSupport();
     setSelectionManager(classDiagramComponent.getSelectionManager());
     setSelected(false);
 
     
     PsiClass psiClass = getPsiClass();
     if (psiClass == null) {
       
       UMLLabel errorLabel = new UMLLabel(qualifiedClassName);
       errorLabel.setFont(classDiagramComponent.getDiagramSettings().getDiagramTitleFont());
       errorLabel.setForeground(Color.RED);
       this.centerPanel.add((Component)errorLabel);
       setBorder(classDiagramComponent.getDiagramSettings().getDefaultDiagramComponentBorder());
       this.toggleFieldButton = new ExpandComponent("fields", false, false, false);
       this.toggleConstructorButton = new ExpandComponent("constructors", false, false, false);
       this.toggleMethodButton = new ExpandComponent("methods", false, false, false);
     }
     else {
       
       ClassDiagramSettings diagramSettings = classDiagramComponent.getDiagramSettings();
       this.toggleFieldButton = new ExpandComponent("fields", classDiagramComponent.getDiagramSettings().isDefaultFieldsExpanded(), PsiComponentUtils.hasVisibleFields(psiClass, diagramSettings), false);
       
       this.toggleFieldButton.addActionListener(actionListener);
 
       
       this.toggleConstructorButton = new ExpandComponent("constructors", classDiagramComponent.getDiagramSettings().isDefaultContructorsExpanded(), PsiComponentUtils.hasVisibleConstructors(psiClass, diagramSettings), false);
       
       this.toggleConstructorButton.addActionListener(actionListener);
 
       
       this.toggleMethodButton = new ExpandComponent("methods", classDiagramComponent.getDiagramSettings().isDefaultMethodsExpanded(), PsiComponentUtils.hasVisibleMethods(psiClass, diagramSettings), true);
       
       this.toggleMethodButton.addActionListener(actionListener);
     } 
 
 
     
     buildContentBoxes(classDiagramComponent);
     
     rebuildComponent(false);
     
     addMouseListener(new MouseAdapter()
         {
           public void mousePressed(MouseEvent e)
           {
             PsiClassComponent.this.focusGained();
           }
         });
 
     
     addMouseListener(new MouseAdapter()
         {
           public void mouseClicked(MouseEvent e) {}
 
 
 
 
           
           public void mouseReleased(MouseEvent e) {}
 
 
 
           
           public void mousePressed(MouseEvent e) {
             Component comp = SwingUtilities.getDeepestComponentAt((Component)PsiClassComponent.this, (e.getPoint()).x, (e.getPoint()).y);
             if (comp instanceof UMLLabel) {
               
               PsiClassComponent.this.lastPsiElement = ((UMLLabel)comp).getPsiElement();
             }
             else {
               
               PsiClassComponent.this.lastPsiElement = null;
             } 
           }
         });
   }
 
 
 
   
   private void initSelectionSupport() {
     FigureComponentSelectionHandler selectionHandler = new FigureComponentSelectionHandler(this.classDiagramComponent);
     selectionHandler.install(this);
   }
 
 
   
   public String getQualifiedClassName() {
     return this.qualifiedClassName;
   }
 
 
   
   private void buildContentBoxes(ClassDiagramComponent classDiagramComponent) {
     if (getPsiClass() == null) {
       
       this.qualifiedInnerClassNames = new String[0];
       return;
     } 
     PsiClass[] innerClasses = getPsiClass().getInnerClasses();
     this.qualifiedInnerClassNames = new String[innerClasses.length];
     for (int i = 0; i < innerClasses.length; i++)
     {
       this.qualifiedInnerClassNames[i] = innerClasses[i].getQualifiedName();
     }
 
     
     classDiagramComponent.temporarlyDisposeConnectors(this);
     this.extendsClassnameSet.clear();
     this.implementsClassnameSet.clear();
     this.fieldsClassnameSet.clear();
     
     this.fieldsList.clear();
     
     this.headerBox = Box.createVerticalBox();
     
     this.headerBox.add(new PsiClassTitlePanel(classDiagramComponent, this));
     this.headerBox.add(new PsiClassImplementsPanel(classDiagramComponent, this));
 
     
     this.fieldBox = Box.createVerticalBox();
     PsiField[] fields = PsiComponentUtils.getVisibleFields(getPsiClass(), classDiagramComponent.getDiagramSettings());
     for (int j = 0; j < fields.length; j++) {
       
       PsiField field = fields[j];
       PsiFieldComponent fieldComp = new PsiFieldComponent(classDiagramComponent, this, field);
       this.fieldsList.add(fieldComp);
       this.fieldBox.add(fieldComp);
     } 
 
     
     this.constructorBox = Box.createVerticalBox();
     PsiMethod[] constructors = PsiComponentUtils.getVisibleConstructors(getPsiClass(), classDiagramComponent.getDiagramSettings());
     for (int k = 0; k < constructors.length; k++) {
       
       PsiMethod constructor = constructors[k];
       this.constructorBox.add(new PsiConstructorComponent(classDiagramComponent, this, constructor));
     } 
 
     
     this.methodBox = Box.createVerticalBox();
     
     PsiMethod[] methods = PsiComponentUtils.getVisibleMethods(getPsiClass(), classDiagramComponent.getDiagramSettings());
     for (int m = 0; m < methods.length; m++) {
       
       PsiMethod method = methods[m];
       if (!method.isConstructor())
       {
         this.methodBox.add(new PsiMethodComponent(this, classDiagramComponent, method));
       }
     } 
     
     updateBorder();
     
     classDiagramComponent.layoutContainer();
     classDiagramComponent.getConnectorUpdater().updateConnectors(this);
     classDiagramComponent.getConnectorManager().validateConnectors(this);
 
     
     classDiagramComponent.revalidate();
   }
 
 
   
   public ExpandComponent getToggleConstructorButton() {
     return this.toggleConstructorButton;
   }
 
 
   
   public ExpandComponent getToggleFieldButton() {
     return this.toggleFieldButton;
   }
 
 
   
   public ExpandComponent getToggleMethodButton() {
     return this.toggleMethodButton;
   }
 
 
 
 
   
   public void rebuildComponent(boolean scroll) {
     this.centerPanel.removeAll();
     if (getPsiClass() == null) {
       
       UMLLabel errorLabel = new UMLLabel(this.qualifiedClassName);
       errorLabel.setFont(this.classDiagramComponent.getDiagramSettings().getDiagramTitleFont());
       errorLabel.setForeground(Color.RED);
       this.centerPanel.add((Component)errorLabel);
       
       updateBorder();
       
       this.toggleFieldButton.setExpandable(false);
       this.toggleMethodButton.setExpandable(false);
       this.toggleConstructorButton.setExpandable(false);
       revalidate();
       this.classDiagramComponent.revalidate();
       this.classDiagramComponent.getConnectorManager().validateConnectors(this);
 
       
       return;
     } 
     
     ClassDiagramSettings diagramSettings = this.classDiagramComponent.getDiagramSettings();
     
     boolean hasFields = PsiComponentUtils.hasVisibleFields(getPsiClass(), diagramSettings);
     this.toggleFieldButton.setExpandable(hasFields);
     
     boolean hasConstructors = PsiComponentUtils.hasVisibleConstructors(getPsiClass(), diagramSettings);
     this.toggleConstructorButton.setExpandable(hasConstructors);
     
     boolean hasMethods = PsiComponentUtils.hasVisibleMethods(getPsiClass(), diagramSettings);
     this.toggleMethodButton.setExpandable(hasMethods);
 
     
     this.headerBox = Box.createVerticalBox();
     
     this.headerBox.add(new PsiClassTitlePanel(this.classDiagramComponent, this));
     this.headerBox.add(new PsiClassImplementsPanel(this.classDiagramComponent, this));
     
     this.centerPanel.add(this.headerBox);
 
     
     if ((diagramSettings.getCompartmentBehaviour() == 4 && hasFields) || diagramSettings.getCompartmentBehaviour() == 1)
     {
       this.centerPanel.add((Component)this.toggleFieldButton);
     }
     
     if (this.toggleFieldButton.isSelected() && diagramSettings.getCompartmentBehaviour() != 2)
     {
       this.centerPanel.add(this.fieldBox);
     }
 
     
     if (!getPsiClass().isInterface()) {
       
       if ((diagramSettings.getCompartmentBehaviour() == 4 && hasConstructors) || diagramSettings.getCompartmentBehaviour() == 1)
       {
         this.centerPanel.add((Component)this.toggleConstructorButton);
       }
       
       if (this.toggleConstructorButton.isSelected() && diagramSettings.getCompartmentBehaviour() != 2)
       {
         this.centerPanel.add(this.constructorBox);
       }
     } 
 
     
     if ((diagramSettings.getCompartmentBehaviour() == 4 && hasMethods) || diagramSettings.getCompartmentBehaviour() == 1)
     {
       this.centerPanel.add((Component)this.toggleMethodButton);
     }
     
     if (this.toggleMethodButton.isSelected() && diagramSettings.getCompartmentBehaviour() != 2)
     {
       this.centerPanel.add(this.methodBox);
     }
 
     
     revalidate();
     this.centerPanel.revalidate();
     this.classDiagramComponent.layoutContainer();
     this.classDiagramComponent.getConnectorManager().validateConnectors(this);
     
     this.classDiagramComponent.layoutContainer();
 
     
     if (scroll) {
       
       Runnable runnable = new Runnable()
         {
           public void run()
           {
             PsiClassComponent.this.classDiagramComponent.scrollRectToVisible(PsiClassComponent.this.getBounds());
           }
         };
       EventQueue.invokeLater(runnable);
     } 
     this.classDiagramComponent.changesMade(false);
   }
 
 
 
 
 
 
 
 
 
   
   public Dimension getMinimumComponentSize() {
     return new Dimension(this.classDiagramComponent.getDiagramSettings().getMinimumFigureSize());
   }
 
 
   
   private void focusGained() {
     this.classDiagramComponent.setChangingComponent(this);
   }
 
 
   
   public int getPosX() {
     return this.posX;
   }
 
 
   
   public void setPosX(int posX) {
     this.posX = posX;
   }
 
 
   
   public int getPosY() {
     return this.posY;
   }
 
 
   
   public void setPosY(int posY) {
     this.posY = posY;
   }
 
 
   
   public String getKey() {
     return getQualifiedClassName();
   }
 
 
   
   public boolean isPinned() {
     return this.pinned;
   }
 
 
   
   public void setPinned(boolean pinned) {
     this.pinned = pinned;
     updateBorder();
   }
 
 
   
   public Rectangle getConnectorBounds(JComponent parent) {
     return getBounds();
   }
 
 
 
   
   public boolean isOnFigureBounds(Point location) {
     return true;
   }
 
 
 
   
   public boolean isCenteredOnSide(Point location) {
     return true;
   }
 
 
   
   public void setClassDiagramComponent(ClassDiagramComponent classDiagramComponent) {
     this.classDiagramComponent = classDiagramComponent;
   }
 
 
   
   public int getComponentWidth() {
     if ((getMinimumComponentSize()).width > (getPreferredSize()).width)
     {
       return (getMinimumComponentSize()).width;
     }
 
     
     return (getPreferredSize()).width;
   }
 
 
 
   
   public int getComponentHeight() {
     if ((getMinimumComponentSize()).height > (getPreferredSize()).height)
     {
       return (getMinimumComponentSize()).height;
     }
 
     
     return (getPreferredSize()).height;
   }
 
 
 
 
 
   
   public PsiClass getPsiClass() {
     this.psiClass = JavaPsiFacade.getInstance(this.classDiagramComponent.getProject()).findClass(this.qualifiedClassName, GlobalSearchScope.allScope(this.classDiagramComponent.getProject()));
     
     return this.psiClass;
   }
 
 
   
   public void settingsChanged(ClassDiagramSettings settings) {
     clearExtendsClassnames();
     clearImplementsClassnames();
     clearFieldsClassnames();
     
     buildContentBoxes(this.classDiagramComponent);
     rebuildComponent(false);
   }
 
 
   
   public void reload() {
     Map map = this.classDiagramComponent.getPsiClassComponentMap();
     for (int i = 0; i < this.qualifiedInnerClassNames.length; i++) {
       
       String qualifiedInnerClassName = this.qualifiedInnerClassNames[i];
       if (map.containsKey(qualifiedInnerClassName))
       {
         ((PsiClassComponent)map.get(qualifiedInnerClassName)).reload();
       }
     } 
     
     buildContentBoxes(this.classDiagramComponent);
     rebuildComponent(false);
   }
 
 
 
   
   public void addExtendsClassname(String qualifiedName) {
     this.extendsClassnameSet.add(qualifiedName);
   }
 
 
   
   public boolean containsExtendsClassname(String qualifiedName) {
     return this.extendsClassnameSet.contains(qualifiedName);
   }
 
 
 
   
   public void clearExtendsClassnames() {
     this.extendsClassnameSet.clear();
   }
 
 
 
   
   public Iterator getExtendsClassnameIterator() {
     return this.extendsClassnameSet.iterator();
   }
 
 
 
   
   public void addImplementsClassname(String qualifiedName) {
     this.implementsClassnameSet.add(qualifiedName);
   }
 
 
   
   public boolean containsImplementsClassname(String qualifiedName) {
     return this.implementsClassnameSet.contains(qualifiedName);
   }
 
 
   
   public void clearImplementsClassnames() {
     this.implementsClassnameSet.clear();
   }
 
 
   
   public Iterator getImplementsClassnameIterator() {
     return this.implementsClassnameSet.iterator();
   }
 
 
 
   
   public void addFieldsClassname(String qualifiedName) {
     this.fieldsClassnameSet.add(qualifiedName);
   }
 
 
   
   public boolean containsFieldsClassname(String qualifiedName) {
     return this.fieldsClassnameSet.contains(qualifiedName);
   }
 
 
   
   public void clearFieldsClassnames() {
     this.fieldsClassnameSet.clear();
   }
 
 
   
   public Iterator getFieldsClassnameIterator() {
     return this.fieldsClassnameSet.iterator();
   }
 
 
   
   public List getFieldsList() {
     return this.fieldsList;
   }
 
 
   
   public String[] getQualifiedInnerClassNames() {
     return this.qualifiedInnerClassNames;
   }
 
 
   
   public void setHighlighted(boolean highlighted) {
     this.highlighted = highlighted;
     updateBorder();
   }
 
 
   
   public boolean equals(Object o) {
     if (!(o instanceof PsiClassComponent))
     {
       return false;
     }
     
     PsiClassComponent psiClassComponent = (PsiClassComponent)o;
     
     if (this.qualifiedClassName == null || psiClassComponent.qualifiedClassName == null)
     {
       return false;
     }
     
     return this.qualifiedClassName.equals(psiClassComponent.qualifiedClassName);
   }
 
 
 
   
   public int hashCode() {
     if (this.qualifiedClassName == null)
     {
       return super.hashCode();
     }
 
     
     return this.qualifiedClassName.hashCode();
   }
 
 
 
   
   public String toString() {
     return this.qualifiedClassName;
   }
 
 
   
   public Object getData(String s) {
     if (DataKeys.PROJECT.getName().equals(s))
     {
       return this.classDiagramComponent.getProject();
     }
     if (DataKeys.PSI_ELEMENT.getName().equals(s))
     {
       return this.lastPsiElement;
     }
     
     return null;
   }
 
 
   
   public boolean isFocusable() {
     return true;
   }
 
 
 
   
   public void removeConnector(ConnectorDirect connectorDirect) {}
 
 
   
   public void updateBorder() {
     if (this.classDiagramComponent == null) {
       return;
     }
 
     
     if (this.highlighted) {
       
       setBorder(this.classDiagramComponent.getDiagramSettings().getHighlightDiagramComponentBorder());
     }
     else if (isSelected()) {
       
       setBorder(this.classDiagramComponent.getDiagramSettings().getSelectedDiagramComponentBorder());
       if (this.pinned)
       {
         setBorder(this.classDiagramComponent.getDiagramSettings().getSelectedPinnedDiagramComponentBorder());
       }
     }
     else if (this.pinned) {
       
       setBorder(this.classDiagramComponent.getDiagramSettings().getPinnedDiagramComponentBorder());
     }
     else {
       
       setBorder(this.classDiagramComponent.getDiagramSettings().getDefaultDiagramComponentBorder());
     } 
   }
 
 
 
   
   public boolean containsInnerClassname(String qualifiedClassName) {
     for (int i = 0; i < this.qualifiedInnerClassNames.length; i++) {
       
       String qualifiedInnerClassName = this.qualifiedInnerClassNames[i];
       if (qualifiedClassName.equals(qualifiedInnerClassName))
       {
         return true;
       }
     } 
     
     return false;
   }
 
 
   
   public ActionContributorCommandInfo[] getActionContributorCommandInfos() {
     ArrayList<ActionContributorCommandInfo> commandInfos = new ArrayList();
     
     PsiClass psiClass = getPsiClass();
     
     commandInfos.add(new ActionContributorCommandInfo("Refactor", new String[] { "Figure" }, "IDEAActionGroup", "RefactoringMenu"));
     
     commandInfos.add(new ActionContributorCommandInfo("Change Color", new String[] { "Figure" }));
     commandInfos.add(new ActionContributorCommandInfo("Pin", new String[] { "Figure" }));
     commandInfos.add(new ActionContributorCommandInfo("Unpin", new String[] { "Figure" }));
     
     if (psiClass != null) {
       
       commandInfos.add(new ActionContributorCommandInfo("Direct Inheritors", new String[] { "Figure", "Add" }));
       commandInfos.add(new ActionContributorCommandInfo("All Inheritors", new String[] { "Figure", "Add" }));
       commandInfos.add(new ActionContributorCommandInfo("Inner Classes", new String[] { "Figure", "Add" }));
       commandInfos.add(new ActionContributorCommandInfo("Extended Classes", new String[] { "Figure", "Add" }));
     } 
     
     if (psiClass != null && !psiClass.isInterface())
     {
       commandInfos.add(new ActionContributorCommandInfo("Implemented Classes", new String[] { "Figure", "Add" }));
     }
     
     if (psiClass != null) {
       
       commandInfos.add(new ActionContributorCommandInfo("Dependencies", new String[] { "Figure", "Add" }));
       
       commandInfos.add(new ActionContributorCommandInfo("Add/Update Connectors", new String[] { "Figure", "Dependencies" }, "SimpleUMLCommand", "RefactoringMenu"));
       commandInfos.add(new ActionContributorCommandInfo("Remove Connectors", new String[] { "Figure", "Dependencies" }));
     } 
     
     commandInfos.add(new ActionContributorCommandInfo("Reload", new String[] { "Figure" }));
     commandInfos.add(new ActionContributorCommandInfo("Remove", new String[] { "Figure" }));
     
     return commandInfos.<ActionContributorCommandInfo>toArray(new ActionContributorCommandInfo[commandInfos.size()]);
   }
 
 
   
   public ActionContributorCommand getActionContributorCommand(ActionContributorCommandInfo info) {
     if ((info.getGroupNames()).length == 0 || !"Figure".equals(info.getGroupNames()[0]))
     {
       return null;
     }
     
     if ("Remove".equals(info.getActionName()))
     {
       return (ActionContributorCommand)new RemoveFigureComponentCommand("Remove", this);
     }
     if ("Reload".equals(info.getActionName()))
     {
       return (ActionContributorCommand)new ReloadAction(this);
     }
     if ("Change Color".equals(info.getActionName()))
     {
       return (ActionContributorCommand)new ChangeColorCommand("Change Color", this);
     }
     if ("Pin".equals(info.getActionName()))
     {
       return (ActionContributorCommand)new PinAction(this);
     }
     if ("Unpin".equals(info.getActionName()))
     {
       return (ActionContributorCommand)new UnpinAction(this);
     }
     if ("Direct Inheritors".equals(info.getActionName()))
     {
       return (ActionContributorCommand)new AddDirectInheritorsAction(this);
     }
     if ("All Inheritors".equals(info.getActionName()))
     {
       return (ActionContributorCommand)new AddAllInheritorsAction(this);
     }
     if ("Inner Classes".equals(info.getActionName()))
     {
       return (ActionContributorCommand)new AddInnerClassesAction(this);
     }
     if ("Extended Classes".equals(info.getActionName()))
     {
       return (ActionContributorCommand)new AddExtendedClassesAction(this);
     }
     if ("Implemented Classes".equals(info.getActionName()))
     {
       return (ActionContributorCommand)new AddImplementedClassesAction(this);
     }
     if ("Dependencies".equals(info.getActionName()))
     {
       return (ActionContributorCommand)new AddDependenciesAction(this);
     }
     if ("Add/Update Connectors".equals(info.getActionName()))
     {
       return (ActionContributorCommand)new AddUpdateDependencyConnectorsAction(this);
     }
     if ("Remove Connectors".equals(info.getActionName()))
     {
       return (ActionContributorCommand)new RemoveDependencyConnectorsAction(this);
     }
 
     
     return null;
   }
 
 
   
   public void setColor(Color color) {
     super.setColor(color);
     setBackground(color);
     if (color == null)
     {
       rebuildComponent(false);
     }
   }
 }


