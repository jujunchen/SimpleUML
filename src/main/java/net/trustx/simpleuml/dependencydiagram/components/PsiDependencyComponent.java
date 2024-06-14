 package net.trustx.simpleuml.dependencydiagram.components;

 import com.intellij.openapi.actionSystem.DataKeys;
 import com.intellij.openapi.actionSystem.DataProvider;
 import com.intellij.psi.JavaPsiFacade;
 import com.intellij.psi.PsiClass;
 import com.intellij.psi.PsiElement;
 import com.intellij.psi.PsiField;
 import com.intellij.psi.PsiMethod;
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
 import javax.swing.Box;
 import javax.swing.BoxLayout;
 import javax.swing.JComponent;
 import javax.swing.JPanel;
 import javax.swing.SwingUtilities;
 import net.trustx.simpleuml.components.ActionContributorCommand;
 import net.trustx.simpleuml.components.ActionContributorCommandInfo;
 import net.trustx.simpleuml.components.ExpandComponent;
 import net.trustx.simpleuml.components.UMLLabel;
 import net.trustx.simpleuml.dependencydiagram.configuration.DependencyDiagramSettings;
 import net.trustx.simpleuml.dependencydiagram.configuration.DependencyDiagramSettingsListener;
 import net.trustx.simpleuml.gef.components.FigureComponent;
 import net.trustx.simpleuml.gef.components.FigureComponentSelectionHandler;
 import net.trustx.simpleuml.gef.connector.ConnectorDirect;





 public class PsiDependencyComponent
   extends FigureComponent
   implements DependencyDiagramSettingsListener, DataProvider
 {
   private PsiClass psiClass;
   private JPanel centerPanel;
   private String qualifiedClassName;
   private String[] qualifiedInnerClassNames;
   private Box headerBox;
   private Box fieldBox;
   private Box constructorBox;
   private Box methodBox;
   private DependencyDiagramComponent dependencyDiagramComponent;
   private ExpandComponent toggleFieldButton;
   private ExpandComponent toggleConstructorButton;
   private ExpandComponent toggleMethodButton;
   private Collection extendsClassnameSet;
   private Collection implementsClassnameSet;
   private Collection fieldsClassnameSet;
   private List fieldsList;
   private PsiElement lastPsiElement;
   private boolean highlighted;

   public PsiDependencyComponent(DependencyDiagramComponent dependencyDiagramComponent, PsiClass psiClass) {
     this(dependencyDiagramComponent, psiClass.getQualifiedName());
     this.psiClass = psiClass;
   }



   public PsiDependencyComponent(final DependencyDiagramComponent dependencyDiagramComponent, String qualifiedClassName) {
     super(dependencyDiagramComponent);

     this.qualifiedClassName = qualifiedClassName;
     this.dependencyDiagramComponent = dependencyDiagramComponent;

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

     setPosX(0);
     setPosY(0);


     addKeyListener(new KeyAdapter()
         {
           public void keyPressed(KeyEvent e)
           {
             dependencyDiagramComponent.dispatchEvent(e);
           }



           public void keyReleased(KeyEvent e) {
             dependencyDiagramComponent.dispatchEvent(e);
           }



           public void keyTyped(KeyEvent e) {
             dependencyDiagramComponent.dispatchEvent(e);
           }
         });

     initSelectionSupport();
     setSelectionManager(dependencyDiagramComponent.getSelectionManager());
     setSelected(false);

     ActionListener actionListener = new ActionListener()
       {
         public void actionPerformed(ActionEvent e) {}
       };





     PsiClass psiClass = getPsiClass();
     if (psiClass == null) {

       UMLLabel errorLabel = new UMLLabel(qualifiedClassName);
       errorLabel.setFont(this.dependencyDiagramComponent.getDiagramSettings().getDiagramTitleFont());
       errorLabel.setForeground(Color.RED);
       this.centerPanel.add((Component)errorLabel);
       setBorder(this.dependencyDiagramComponent.getDiagramSettings().getDefaultDiagramComponentBorder());
       this.toggleFieldButton = new ExpandComponent("fields", false, false, false);
       this.toggleConstructorButton = new ExpandComponent("constructors", false, false, false);
       this.toggleMethodButton = new ExpandComponent("methods", false, false, false);
     }
     else {

       this.toggleFieldButton = new ExpandComponent("fields", true, false, false);

       this.toggleFieldButton.addActionListener(actionListener);


       this.toggleConstructorButton = new ExpandComponent("constructors", true, false, false);

       this.toggleConstructorButton.addActionListener(actionListener);


       this.toggleMethodButton = new ExpandComponent("methods", true, false, true);

       this.toggleMethodButton.addActionListener(actionListener);
     }



     buildContentBoxes(dependencyDiagramComponent);

     rebuildComponent(false);

     addMouseListener(new MouseAdapter()
         {
           public void mousePressed(MouseEvent e)
           {
             PsiDependencyComponent.this.focusGained();
           }
         });


     addMouseListener(new MouseAdapter()
         {
           public void mouseClicked(MouseEvent e) {}





           public void mouseReleased(MouseEvent e) {}




           public void mousePressed(MouseEvent e) {
             Component comp = SwingUtilities.getDeepestComponentAt((Component)PsiDependencyComponent.this, (e.getPoint()).x, (e.getPoint()).y);
             if (comp instanceof UMLLabel) {

               PsiDependencyComponent.this.lastPsiElement = ((UMLLabel)comp).getPsiElement();
             }
             else {

               PsiDependencyComponent.this.lastPsiElement = null;
             }
           }
         });
   }




   private void initSelectionSupport() {
     FigureComponentSelectionHandler selectionHandler = new FigureComponentSelectionHandler(this.dependencyDiagramComponent);
     selectionHandler.install(this);
   }



   public String getQualifiedClassName() {
     return this.qualifiedClassName;
   }



   private void buildContentBoxes(DependencyDiagramComponent dependencyDiagramComponent) {
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


     this.extendsClassnameSet.clear();
     this.implementsClassnameSet.clear();
     this.fieldsClassnameSet.clear();

     this.fieldsList.clear();

     this.headerBox = Box.createVerticalBox();

     this.headerBox.add(new PsiClassTitlePanel(dependencyDiagramComponent, this));
     this.headerBox.add(new PsiClassImplementsPanel(dependencyDiagramComponent, this));


     this.fieldBox = Box.createVerticalBox();
     PsiField[] fields = this.psiClass.getFields();
     for (int j = 0; j < fields.length; j++) {

       PsiField field = fields[j];
       PsiFieldComponent fieldComp = new PsiFieldComponent(dependencyDiagramComponent, this, field);
       this.fieldsList.add(fieldComp);
       this.fieldBox.add(fieldComp);
     }


     this.constructorBox = Box.createVerticalBox();
     PsiMethod[] constructors = this.psiClass.getConstructors();
     for (int k = 0; k < constructors.length; k++) {

       PsiMethod constructor = constructors[k];
       this.constructorBox.add(new PsiConstructorComponent(dependencyDiagramComponent, this, constructor));
     }


     this.methodBox = Box.createVerticalBox();

     PsiMethod[] methods = this.psiClass.getMethods();
     for (int m = 0; m < methods.length; m++) {

       PsiMethod method = methods[m];
       if (!method.isConstructor())
       {
         this.methodBox.add(new PsiMethodComponent(this, dependencyDiagramComponent, method));
       }
     }

     updateBorder();

     dependencyDiagramComponent.layoutContainer();




     dependencyDiagramComponent.revalidate();
   }





   public void rebuildComponent(boolean scroll) {
     this.centerPanel.removeAll();
     if (getPsiClass() == null) {

       UMLLabel errorLabel = new UMLLabel(this.qualifiedClassName);
       errorLabel.setFont(this.dependencyDiagramComponent.getDiagramSettings().getDiagramTitleFont());
       errorLabel.setForeground(Color.RED);
       this.centerPanel.add((Component)errorLabel);

       updateBorder();

       revalidate();
       this.dependencyDiagramComponent.revalidate();
       this.dependencyDiagramComponent.getConnectorManager().validateConnectors(this);


       return;
     }

     this.headerBox = Box.createVerticalBox();

     this.headerBox.add(new PsiClassTitlePanel(this.dependencyDiagramComponent, this));
     this.headerBox.add(new PsiClassImplementsPanel(this.dependencyDiagramComponent, this));

     this.centerPanel.add(this.headerBox);


     this.centerPanel.add((Component)this.toggleFieldButton);
     this.centerPanel.add(this.fieldBox);


     if (!getPsiClass().isInterface()) {

       this.centerPanel.add((Component)this.toggleConstructorButton);
       this.centerPanel.add(this.constructorBox);
     }

     this.centerPanel.add((Component)this.toggleMethodButton);
     this.centerPanel.add(this.methodBox);

     revalidate();
     this.centerPanel.revalidate();
     this.dependencyDiagramComponent.layoutContainer();
     this.dependencyDiagramComponent.getConnectorManager().validateConnectors(this);

     this.dependencyDiagramComponent.layoutContainer();


     if (scroll) {

       Runnable runnable = new Runnable()
         {
           public void run()
           {
             PsiDependencyComponent.this.dependencyDiagramComponent.scrollRectToVisible(PsiDependencyComponent.this.getBounds());
           }
         };
       EventQueue.invokeLater(runnable);
     }
     this.dependencyDiagramComponent.changesMade();
   }



   public Dimension getMinimumComponentSize() {
     return new Dimension(this.dependencyDiagramComponent.getDiagramSettings().getMinimumFigureSize());
   }




   private void focusGained() {}




   public String getKey() {
     return getQualifiedClassName();
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



   public void setDependencyDiagramComponent(DependencyDiagramComponent dependencyDiagramComponent) {
     this.dependencyDiagramComponent = dependencyDiagramComponent;
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
     this.psiClass = JavaPsiFacade.getInstance(this.dependencyDiagramComponent.getProject()).findClass(this.qualifiedClassName, GlobalSearchScope.allScope(this.dependencyDiagramComponent.getProject()));

     return this.psiClass;
   }



   public void settingsChanged(DependencyDiagramSettings settings) {
     clearExtendsClassnames();
     clearImplementsClassnames();
     clearFieldsClassnames();

     buildContentBoxes(this.dependencyDiagramComponent);
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
     if (!(o instanceof PsiDependencyComponent))
     {
       return false;
     }

     PsiDependencyComponent psiClassComponent = (PsiDependencyComponent)o;

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
       return this.dependencyDiagramComponent.getProject();
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
     if (this.dependencyDiagramComponent == null) {
       return;
     }


     if (this.highlighted) {

       setBorder(this.dependencyDiagramComponent.getDiagramSettings().getHighlightDiagramComponentBorder());
     }
     else if (isSelected()) {

       setBorder(this.dependencyDiagramComponent.getDiagramSettings().getSelectedDiagramComponentBorder());

     }
     else {

       setBorder(this.dependencyDiagramComponent.getDiagramSettings().getDefaultDiagramComponentBorder());
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
     commandInfos.add(new ActionContributorCommandInfo("Refactor", new String[] { "Figure" }, "IDEAActionGroup", "RefactoringMenu"));
     return commandInfos.<ActionContributorCommandInfo>toArray(new ActionContributorCommandInfo[commandInfos.size()]);
   }



   public ActionContributorCommand getActionContributorCommand(ActionContributorCommandInfo info) {
     return null;
   }
 }


