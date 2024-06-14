 package net.trustx.simpleuml.dependencydiagram.components;
 
 import com.intellij.openapi.actionSystem.DataKeys;
 import com.intellij.openapi.actionSystem.DataProvider;
 import com.intellij.psi.JavaPsiFacade;
 import com.intellij.psi.PsiElement;
 import com.intellij.psi.PsiPackage;
 import java.awt.BorderLayout;
 import java.awt.Color;
 import java.awt.Component;
 import java.awt.FlowLayout;
 import java.awt.event.KeyAdapter;
 import java.awt.event.KeyEvent;
 import java.awt.event.MouseAdapter;
 import java.awt.event.MouseEvent;
 import java.util.ArrayList;
 import javax.swing.JPanel;
 import javax.swing.SwingUtilities;
 import javax.swing.border.Border;
 import net.trustx.simpleuml.components.ActionContributorCommand;
 import net.trustx.simpleuml.components.ActionContributorCommandInfo;
 import net.trustx.simpleuml.components.UMLLabel;
 import net.trustx.simpleuml.components.border.LineBorder2D;
 import net.trustx.simpleuml.dependencydiagram.configuration.DependencyDiagramSettings;
 import net.trustx.simpleuml.gef.components.FigureComponent;
 import net.trustx.simpleuml.gef.components.FigureComponentSelectionHandler;
 import net.trustx.simpleuml.util.UMLUtils;
 
 

 public class PackageComponent
   extends FigureComponent
   implements DataProvider
 {
   private DependencyDiagramComponent dependencyDiagramComponent;
   private PsiElement lastPsiElement;
   private String qualifiedPackageName;
   private PsiPackage psiPackage;
   private JPanel tabPanel;
   private JPanel topPanel;
   private JPanel mainPanel;
   private UMLLabel packageLabel;
   
   public PackageComponent(final DependencyDiagramComponent dependencyDiagramComponent, String qualifiedPackageName) {
     super(dependencyDiagramComponent);
     setOpaque(false);
     this.dependencyDiagramComponent = dependencyDiagramComponent;
     this.qualifiedPackageName = qualifiedPackageName;
     
     this.mainPanel = new JPanel(new FlowLayout(0));
     this.mainPanel.setOpaque(true);
     
     this.topPanel = new JPanel(new BorderLayout());
     this.topPanel.setOpaque(false);
     
     String packageName = UMLUtils.getCompressedPackageName(qualifiedPackageName, dependencyDiagramComponent.getDiagramSettings().getPackageNameCompressionLevel());
     
     this.packageLabel = new UMLLabel(packageName);
     PsiPackage psiPackage = JavaPsiFacade.getInstance(dependencyDiagramComponent.getProject()).findPackage(packageName);
     this.packageLabel.setPsiElement((PsiElement)psiPackage);
     
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
     
     addMouseListener(new MouseAdapter()
         {
           public void mouseClicked(MouseEvent e) {}
 
 
 
 
           
           public void mouseReleased(MouseEvent e) {}
 
 
 
           
           public void mousePressed(MouseEvent e) {
             Component comp = SwingUtilities.getDeepestComponentAt((Component)PackageComponent.this, (e.getPoint()).x, (e.getPoint()).y);
             if (comp instanceof UMLLabel) {
               
               PackageComponent.this.lastPsiElement = ((UMLLabel)comp).getPsiElement();
             }
             else {
               
               PackageComponent.this.lastPsiElement = null;
             } 
           }
         });
     
     settingsChanged(dependencyDiagramComponent.getDiagramSettings());
   }
 
 
   
   private void initSelectionSupport() {
     FigureComponentSelectionHandler selectionHandler = new FigureComponentSelectionHandler(this.dependencyDiagramComponent);
     selectionHandler.install(this);
   }
 
 
   
   public PsiPackage getPsiPackage() {
     this.psiPackage = JavaPsiFacade.getInstance(this.dependencyDiagramComponent.getProject()).findPackage(this.qualifiedPackageName);
     return this.psiPackage;
   }
 
 
   
   public void addContentFigure(FigureComponent figureComponent) {
     this.mainPanel.add((Component)figureComponent);
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
 
 
 
   
   public void settingsChanged(DependencyDiagramSettings packageDiagramSettings) {
     String packageName = UMLUtils.getCompressedPackageName(this.qualifiedPackageName, this.dependencyDiagramComponent.getDiagramSettings().getPackageNameCompressionLevel());
     this.packageLabel.setText(packageName);
     if (packageDiagramSettings.getPackageNameCompressionLevel() > 0)
     {
       this.packageLabel.setToolTipText(this.qualifiedPackageName);
     }
     this.packageLabel.setFont(packageDiagramSettings.getDiagramFont());
     setColor(getColor());
     updateBorder();
     this.packageLabel.revalidate();
     this.topPanel.revalidate();
     this.mainPanel.revalidate();
     getContentPane().revalidate();
     revalidate();
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
 
 
   
   public String getKey() {
     return this.qualifiedPackageName;
   }
 
 
   
   public String toString() {
     return this.qualifiedPackageName;
   }
 
 
   
   public boolean equals(Object o) {
     if (!(o instanceof PackageComponent))
     {
       return false;
     }
     
     PackageComponent psiPackageComponent = (PackageComponent)o;
     
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
 
 
   
   public void updateBorder() {
     if (this.dependencyDiagramComponent == null) {
       return;
     }
 
     
     this.tabPanel.setBorder((Border)new LineBorder2D(Color.GRAY, 1, 1, 0, 1));
     this.mainPanel.setBorder((Border)new LineBorder2D(Color.GRAY, 1, 1, 1, 1));
   }
 
 
 
 
   
   public void setColor(Color color) {
     super.setColor(color);
     this.tabPanel.setBackground(color);
     this.mainPanel.setBackground(color);
     
     if (getResizeHandler() != null) {
       getResizeHandler().setBackground(color);
     }
     if (color == null) {
       
       this.tabPanel.setBackground(this.dependencyDiagramComponent.getDiagramSettings().getPackageBackgroundColor());
       this.mainPanel.setBackground(this.dependencyDiagramComponent.getDiagramSettings().getPackageBackgroundColor());
       if (getResizeHandler() != null) {
         getResizeHandler().setBackground(this.dependencyDiagramComponent.getDiagramSettings().getPackageBackgroundColor());
       }
     } 
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


