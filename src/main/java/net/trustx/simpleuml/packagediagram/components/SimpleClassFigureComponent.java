 package net.trustx.simpleuml.packagediagram.components;

 import com.intellij.openapi.actionSystem.DataKeys;
 import com.intellij.openapi.actionSystem.DataProvider;
 import com.intellij.psi.PsiClass;
 import com.intellij.psi.PsiElement;
 import java.awt.Color;
 import java.awt.Component;
 import java.awt.GridBagConstraints;
 import java.awt.GridBagLayout;
 import java.awt.event.KeyAdapter;
 import java.awt.event.KeyEvent;
 import java.awt.event.MouseAdapter;
 import java.awt.event.MouseEvent;
 import java.util.ArrayList;
 import javax.swing.JComponent;
 import javax.swing.SwingUtilities;
 import javax.swing.border.Border;
 import net.trustx.simpleuml.components.ActionContributorCommand;
 import net.trustx.simpleuml.components.ActionContributorCommandInfo;
 import net.trustx.simpleuml.components.UMLLabel;
 import net.trustx.simpleuml.components.border.LineBorder2D;
 import net.trustx.simpleuml.gef.components.FigureComponent;
 import net.trustx.simpleuml.gef.components.FigureComponentSelectionHandler;
 import net.trustx.simpleuml.util.EditorHelper;
 import net.trustx.simpleuml.util.UMLUtils;



 public class SimpleClassFigureComponent
   extends FigureComponent
   implements DataProvider
 {
   private PackageDiagramComponent packageDiagramComponent;
   private PsiElement lastPsiElement;

   public SimpleClassFigureComponent(final PackageDiagramComponent packageDiagramComponent, PsiClass psiClass) {
     super(packageDiagramComponent);
     this.packageDiagramComponent = packageDiagramComponent;
     setOpaque(true);

     String displayName = UMLUtils.getNiceClassName(psiClass);
     String tooltipText = psiClass.getQualifiedName();
     UMLLabel label = new UMLLabel(displayName);
     label.setPsiElement((PsiElement)psiClass);

     EditorHelper.initQuickSourceSupport(this.packageDiagramComponent.getDiagramSettings().getQuickSourceLinkColor(), this.packageDiagramComponent.getProject(), (JComponent)this, label, psiClass.getNameIdentifier(), false, false, true);









     label.setToolTipText(tooltipText);

     GridBagConstraints gridBagConstraints = new GridBagConstraints();
     gridBagConstraints.weightx = 1.0D;
     gridBagConstraints.anchor = 17;
     getContentPane().setLayout(new GridBagLayout());
     getContentPane().add((Component)label, gridBagConstraints);

     setBorder((Border)new LineBorder2D(Color.GRAY, 1));


     initSelectionSupport();
     setSelectionManager(this.packageDiagramComponent.getSelectionManager());
     setSelected(false);

     addMouseListener(new MouseAdapter()
         {
           public void mouseClicked(MouseEvent e) {}





           public void mouseReleased(MouseEvent e) {}




           public void mousePressed(MouseEvent e) {
             Component comp = SwingUtilities.getDeepestComponentAt((Component)SimpleClassFigureComponent.this, (e.getPoint()).x, (e.getPoint()).y);
             if (comp instanceof UMLLabel) {

               SimpleClassFigureComponent.this.lastPsiElement = ((UMLLabel)comp).getPsiElement();
             }
             else {

               SimpleClassFigureComponent.this.lastPsiElement = null;
             }
           }
         });

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
   }



   private void initSelectionSupport() {
     FigureComponentSelectionHandler selectionHandler = new FigureComponentSelectionHandler(this.packageDiagramComponent);
     selectionHandler.install(this);
   }



   public void setColor(Color color) {
     super.setColor(color);
     setBackground(color);
   }



   public void updateBorder() {
     if (isSelected()) {

       setBorder((Border)new LineBorder2D(Color.BLUE, 1));
     }
     else {

       setBorder((Border)new LineBorder2D(Color.GRAY, 1));
     }
   }



   public boolean isFocusable() {
     return true;
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



   public ActionContributorCommandInfo[] getActionContributorCommandInfos() {
     ArrayList<ActionContributorCommandInfo> commandInfos = new ArrayList();
     commandInfos.add(new ActionContributorCommandInfo("Refactor", new String[] { "Figure" }, "IDEAActionGroup", "RefactoringMenu"));
     return commandInfos.<ActionContributorCommandInfo>toArray(new ActionContributorCommandInfo[commandInfos.size()]);
   }



   public ActionContributorCommand getActionContributorCommand(ActionContributorCommandInfo info) {
     return null;
   }
 }


