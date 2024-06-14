 package net.trustx.simpleuml.packagediagram.components;
 
 import com.intellij.openapi.actionSystem.DataKeys;
 import com.intellij.openapi.actionSystem.DataProvider;
 import com.intellij.psi.JavaPsiFacade;
 import com.intellij.psi.PsiClass;
 import com.intellij.psi.PsiElement;
 import com.intellij.psi.PsiPackage;
 import java.awt.BorderLayout;
 import java.awt.Color;
 import java.awt.Component;
 import java.awt.event.KeyAdapter;
 import java.awt.event.KeyEvent;
 import java.awt.event.MouseAdapter;
 import java.awt.event.MouseEvent;
 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.Comparator;
 import javax.swing.BorderFactory;
 import javax.swing.Box;
 import javax.swing.BoxLayout;
 import javax.swing.JPanel;
 import javax.swing.JScrollPane;
 import javax.swing.SwingUtilities;
 import net.trustx.simpleuml.components.ActionContributorCommand;
 import net.trustx.simpleuml.components.ActionContributorCommandInfo;
 import net.trustx.simpleuml.components.UMLLabel;
 import net.trustx.simpleuml.gef.components.ActionContributorCommandPopupHandler;
 import net.trustx.simpleuml.gef.components.ChangeColorCommand;
 import net.trustx.simpleuml.gef.components.FigureComponent;
 import net.trustx.simpleuml.gef.components.FigureComponentSelectionHandler;
 import net.trustx.simpleuml.gef.components.RemoveFigureComponentCommand;
 import net.trustx.simpleuml.packagediagram.actions.AddDependenciesAction;
 import net.trustx.simpleuml.packagediagram.actions.AddUpdateDependencyConnectorsAction;
 import net.trustx.simpleuml.packagediagram.actions.RemoveDependencyConnectorsAction;
 import net.trustx.simpleuml.packagediagram.actions.ToggleClassesAction;
 import net.trustx.simpleuml.packagediagram.configuration.PackageDiagramSettings;
 import net.trustx.simpleuml.packagediagram.configuration.PackageDiagramSettingsListener;
 import net.trustx.simpleuml.util.UMLUtils;
 
 

 
 public class PsiPackageComponent
   extends FigureComponent
   implements PackageDiagramSettingsListener, DataProvider
 {
   private PackageDiagramComponent packageDiagramComponent;
   private PsiElement lastPsiElement;
   private String qualifiedPackageName;
   private PsiPackage psiPackage;
   private boolean highlighted;
   private JPanel tabPanel;
   private JPanel topPanel;
   private JPanel mainPanel;
   private JPanel classesPanel;
   private UMLLabel packageLabel;
   private boolean showClasses;
   
   public PsiPackageComponent(PackageDiagramComponent packageDiagramComponent, PsiPackage psiPackage, boolean showClasses) {
     this(packageDiagramComponent, psiPackage.getQualifiedName(), showClasses);
   }
 
 
   
   public PsiPackageComponent(final PackageDiagramComponent packageDiagramComponent, String qualifiedPackageName, boolean showClasses) {
     super(packageDiagramComponent);
     this.showClasses = showClasses;
     setOpaque(false);
     this.packageDiagramComponent = packageDiagramComponent;
     this.qualifiedPackageName = qualifiedPackageName;
     
     this.psiPackage = JavaPsiFacade.getInstance(packageDiagramComponent.getProject()).findPackage(qualifiedPackageName);
     
     this.classesPanel = new JPanel();
     BoxLayout boxLayout = new BoxLayout(this.classesPanel, 1);
     this.classesPanel.setLayout(boxLayout);
     
     this.mainPanel = new JPanel(new BorderLayout());
     this.mainPanel.setOpaque(true);
     
     JScrollPane jScrollPane = new JScrollPane(this.classesPanel);
     jScrollPane.setOpaque(false);
     jScrollPane.getViewport().setOpaque(false);
     this.classesPanel.setOpaque(false);
     jScrollPane.setBorder(BorderFactory.createEmptyBorder());
     this.mainPanel.add(jScrollPane, "Center");
     
     this.topPanel = new JPanel(new BorderLayout());
     this.topPanel.setOpaque(false);
     
     String packageName = UMLUtils.getCompressedPackageName(qualifiedPackageName, packageDiagramComponent.getDiagramSettings().getPackageNameCompressionLevel());
     
     this.packageLabel = new UMLLabel(packageName);
     
     this.packageLabel.setToolTipText(qualifiedPackageName);
     UMLUtils.dispatchMouseToParent((Component)this.packageLabel, (Component)this);
     
     this.packageLabel.setOpaque(false);
     
     this.tabPanel = new JPanel(new BorderLayout());
     this.tabPanel.add((Component)this.packageLabel, "Center");
     
     this.topPanel.add(this.tabPanel, "West");
 
     
     getContentPane().setLayout(new BorderLayout());
     getContentPane().add(this.mainPanel, "Center");
     getContentPane().add(this.topPanel, "North");
     
     addKeyListener(new KeyAdapter()
         {
           public void keyPressed(KeyEvent e)
           {
             packageDiagramComponent.dispatchEvent(e);
           }
 
 
           
           public void keyReleased(KeyEvent e) {
             packageDiagramComponent.dispatchEvent(e);
           }
 
 
           
           public void keyTyped(KeyEvent e) {
             packageDiagramComponent.dispatchEvent(e);
           }
         });
     
     initSelectionSupport();
     setSelectionManager(packageDiagramComponent.getSelectionManager());
     setSelected(false);
     
     addMouseListener(new MouseAdapter()
         {
           public void mousePressed(MouseEvent e)
           {
             PsiPackageComponent.this.focusGained();
           }
         });
 
     
     addMouseListener(new MouseAdapter()
         {
           public void mouseClicked(MouseEvent e) {}
 
 
 
 
           
           public void mouseReleased(MouseEvent e) {}
 
 
 
           
           public void mousePressed(MouseEvent e) {
             Component comp = SwingUtilities.getDeepestComponentAt((Component)PsiPackageComponent.this, (e.getPoint()).x, (e.getPoint()).y);
             if (comp instanceof UMLLabel) {
               
               PsiPackageComponent.this.lastPsiElement = ((UMLLabel)comp).getPsiElement();
             }
             else {
               
               PsiPackageComponent.this.lastPsiElement = null;
             } 
           }
         });
     
     settingsChanged(packageDiagramComponent.getDiagramSettings());
   }
 
 
   
   private void focusGained() {
     this.packageDiagramComponent.setChangingComponent(this);
   }
 
 
   
   private void initSelectionSupport() {
     FigureComponentSelectionHandler selectionHandler = new FigureComponentSelectionHandler(this.packageDiagramComponent);
     selectionHandler.install(this);
   }
 
 
   
   public PsiPackage getPsiPackage() {
     this.psiPackage = JavaPsiFacade.getInstance(this.packageDiagramComponent.getProject()).findPackage(this.qualifiedPackageName);
     return this.psiPackage;
   }
 
 
   
   public int getComponentWidth() {
     if ((getMinimumSize()).width > (getPreferredSize()).width)
     {
       return (getMinimumSize()).width;
     }
 
     
     return (getPreferredSize()).width;
   }
 
 
 
   
   public int getComponentHeight() {
     if ((getMinimumSize()).height > (getPreferredSize()).height)
     {
       return (getMinimumSize()).height;
     }
 
     
     return (getPreferredSize()).height;
   }
 
 
 
   
   public void settingsChanged(PackageDiagramSettings packageDiagramSettings) {
     String packageName = UMLUtils.getCompressedPackageName(this.qualifiedPackageName, this.packageDiagramComponent.getDiagramSettings().getPackageNameCompressionLevel());
     this.packageLabel.setText(packageName);
     if (packageDiagramSettings.getPackageNameCompressionLevel() > 0)
     {
       this.packageLabel.setToolTipText(this.qualifiedPackageName);
     }
     this.packageLabel.setFont(packageDiagramSettings.getDiagramTitleFont());
     setColor(getColor());
     updateBorder();
     addClasses();
     this.packageLabel.revalidate();
     this.topPanel.revalidate();
     this.mainPanel.revalidate();
     getContentPane().revalidate();
     revalidate();
   }
 
 
   
   public Object getData(String s) {
     if (DataKeys.PROJECT.getName().equals(s))
     {
       return this.packageDiagramComponent.getProject();
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
 
 
   
   public void setHighlighted(boolean highlighted) {
     this.highlighted = highlighted;
     updateBorder();
   }
 
 
   
   public ActionContributorCommandInfo[] getActionContributorCommandInfos() {
     ArrayList<ActionContributorCommandInfo> commandInfos = new ArrayList();
     
     commandInfos.add(new ActionContributorCommandInfo("Refactor", new String[] { "Figure" }, "IDEAActionGroup", "RefactoringMenu"));
     
     if (this.showClasses) {
       
       commandInfos.add(new ActionContributorCommandInfo("Hide Classes", new String[] { "Figure" }));
     }
     else {
       
       commandInfos.add(new ActionContributorCommandInfo("Show Classes", new String[] { "Figure" }));
     } 
 
     
     commandInfos.add(new ActionContributorCommandInfo("Change Color", new String[] { "Figure" }));
     
     commandInfos.add(new ActionContributorCommandInfo("Dependencies", new String[] { "Figure", "Add" }));
     
     commandInfos.add(new ActionContributorCommandInfo("Add/Update Connectors", new String[] { "Figure", "Dependencies" }, "SimpleUMLCommand", "RefactoringMenu"));
     commandInfos.add(new ActionContributorCommandInfo("Remove Connectors", new String[] { "Figure", "Dependencies" }));
     
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
     if ("Change Color".equals(info.getActionName()))
     {
       return (ActionContributorCommand)new ChangeColorCommand("Change Color", this);
     }
     if ("Dependencies".equals(info.getActionName()))
     {
       return (ActionContributorCommand)new AddDependenciesAction(this);
     }
     if ("Show Classes".equals(info.getActionName()))
     {
       return (ActionContributorCommand)new ToggleClassesAction(this, true);
     }
     if ("Hide Classes".equals(info.getActionName()))
     {
       return (ActionContributorCommand)new ToggleClassesAction(this, false);
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
 
 
   
   public void updateBorder() {
     if (this.packageDiagramComponent == null) {
       return;
     }
 
     
     if (this.highlighted) {
       
       this.tabPanel.setBorder(this.packageDiagramComponent.getDiagramSettings().getHighlightDiagramComponentBorder());
       this.mainPanel.setBorder(this.packageDiagramComponent.getDiagramSettings().getHighlightDiagramComponentBorder());
     }
     else if (isSelected()) {
       
       this.tabPanel.setBorder(this.packageDiagramComponent.getDiagramSettings().getSelectedDiagramComponentBorder());
       this.mainPanel.setBorder(this.packageDiagramComponent.getDiagramSettings().getSelectedDiagramComponentBorder());
       if (isPinned())
       {
         this.tabPanel.setBorder(this.packageDiagramComponent.getDiagramSettings().getSelectedPinnedDiagramComponentBorder());
         this.mainPanel.setBorder(this.packageDiagramComponent.getDiagramSettings().getSelectedPinnedDiagramComponentBorder());
       }
     
     } else if (isPinned()) {
       
       this.tabPanel.setBorder(this.packageDiagramComponent.getDiagramSettings().getPinnedDiagramComponentBorder());
       this.mainPanel.setBorder(this.packageDiagramComponent.getDiagramSettings().getPinnedDiagramComponentBorder());
     }
     else {
       
       this.tabPanel.setBorder(this.packageDiagramComponent.getDiagramSettings().getDefaultDiagramComponentBorder());
       this.mainPanel.setBorder(this.packageDiagramComponent.getDiagramSettings().getDefaultDiagramComponentBorder());
     } 
   }
 
 
 
   
   public String getKey() {
     return this.qualifiedPackageName;
   }
 
 
   
   public String toString() {
     return this.qualifiedPackageName;
   }
 
 
   
   public boolean equals(Object o) {
     if (!(o instanceof PsiPackageComponent))
     {
       return false;
     }
     
     PsiPackageComponent psiPackageComponent = (PsiPackageComponent)o;
     
     if (this.qualifiedPackageName == null || psiPackageComponent.qualifiedPackageName == null)
     {
       return false;
     }
     
     return this.qualifiedPackageName.equals(psiPackageComponent.qualifiedPackageName);
   }
 
 
 
   
   public int hashCode() {
     return this.qualifiedPackageName.hashCode();
   }
 
 
   
   public String getPackageName() {
     return this.qualifiedPackageName;
   }
 
 
   
   public void setPackageDiagramComponent(PackageDiagramComponent packageDiagramComponent) {
     this.packageDiagramComponent = packageDiagramComponent;
   }
 
 
   
   private void addClasses() {
     this.classesPanel.removeAll();
     if (this.psiPackage != null && this.showClasses) {
       
       PsiClass[] psiClasses = this.psiPackage.getClasses();
       
       Arrays.sort(psiClasses, new Comparator<PsiClass>()
           {
             public int compare(PsiClass o1, PsiClass o2)
             {
               if (o1 != null && o2 != null) {
                 
                 PsiClass p1 = (PsiClass)o1;
                 PsiClass p2 = (PsiClass)o2;
                 String name1 = p1.getQualifiedName();
                 String name2 = p2.getQualifiedName();
                 if (name1 == null || name2 == null) return 0;
                 
                 return name1.compareTo(name2);
               } 
               return 0;
             }
 
 
             
             public boolean equals(Object obj) {
               return false;
             }
           });
       
       Arrays.sort(psiClasses, new Comparator<PsiClass>()
           {
             public int compare(PsiClass o1, PsiClass o2)
             {
               if (o1 != null && o2 != null) {
                 
                 PsiClass p1 = (PsiClass)o1;
                 PsiClass p2 = (PsiClass)o2;
                 if (p1.isInterface() && p2.hasModifierProperty("abstract"))
                 {
                   return -1;
                 }
                 if (p1.isInterface() && !p2.isInterface() && !p2.hasModifierProperty("abstract"))
                 {
                   return -1;
                 }
                 if (p1.hasModifierProperty("abstract") && !p2.isInterface() && !p2.hasModifierProperty("abstract"))
                 {
                   return -1;
                 }
                 if (p2.isInterface() && p1.hasModifierProperty("abstract"))
                 {
                   return 1;
                 }
                 if (p2.isInterface() && !p1.isInterface() && !p1.hasModifierProperty("abstract"))
                 {
                   return 1;
                 }
                 if (p2.hasModifierProperty("abstract") && !p1.isInterface() && !p1.hasModifierProperty("abstract"))
                 {
                   return 1;
                 }
               } 
               return 0;
             }
 
 
             
             public boolean equals(Object obj) {
               return false;
             }
           });
       
       for (int i = 0; i < psiClasses.length; i++) {
         
         PsiClass psiClass = psiClasses[i];
         SimpleClassFigureComponent simpleClassFigureComponent = new SimpleClassFigureComponent(this.packageDiagramComponent, psiClass);
         if (psiClass.isInterface()) {
           
           simpleClassFigureComponent.setColor(this.packageDiagramComponent.getDiagramSettings().getInterfaceBackgroundColor());
         }
         else if (psiClass.getModifierList().hasModifierProperty("abstract")) {
           
           simpleClassFigureComponent.setColor(this.packageDiagramComponent.getDiagramSettings().getAbstractClassBackgroundColor());
         }
         else {
           
           simpleClassFigureComponent.setColor(this.packageDiagramComponent.getDiagramSettings().getClassBackgroundColor());
         } 
         new ActionContributorCommandPopupHandler(this.packageDiagramComponent, simpleClassFigureComponent);
         this.classesPanel.add((Component)simpleClassFigureComponent);
         this.classesPanel.add(Box.createVerticalStrut(2));
       } 
     } 
   }
 
 
   
   public void setShowClasses(boolean showClasses) {
     this.showClasses = showClasses;
     addClasses();
   }
 
 
   
   public boolean isShowClasses() {
     return this.showClasses;
   }
 
 
   
   protected void resizingSupportChanged() {
     getResizeHandler().setOpaque(true);
     remove(getResizeHandler());
     this.mainPanel.add(getResizeHandler(), "South");
     setColor(getColor());
   }
 
 
   
   public void setColor(Color color) {
     super.setColor(color);
     this.tabPanel.setBackground(color);
     this.mainPanel.setBackground(color);
     
     if (getResizeHandler() != null) {
       getResizeHandler().setBackground(color);
     }
     if (color == null) {
       
       this.tabPanel.setBackground(this.packageDiagramComponent.getDiagramSettings().getPackageBackgroundColor());
       this.mainPanel.setBackground(this.packageDiagramComponent.getDiagramSettings().getPackageBackgroundColor());
       if (getResizeHandler() != null)
         getResizeHandler().setBackground(this.packageDiagramComponent.getDiagramSettings().getPackageBackgroundColor()); 
     } 
   }
 }


