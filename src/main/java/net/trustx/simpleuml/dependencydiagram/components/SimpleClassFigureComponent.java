 package net.trustx.simpleuml.dependencydiagram.components;

 import com.intellij.openapi.actionSystem.DataKeys;
 import com.intellij.openapi.actionSystem.DataProvider;
 import com.intellij.psi.PsiClass;
 import com.intellij.psi.PsiElement;
 import java.awt.Color;
 import java.awt.Component;
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
   private DependencyDiagramComponent dependencyDiagramComponent;
   private PsiElement lastPsiElement;

   public SimpleClassFigureComponent(DependencyDiagramComponent diagramPane, PsiClass psiClass) {
     super(diagramPane);
     this.dependencyDiagramComponent = diagramPane;
     setOpaque(true);

     String displayName = UMLUtils.getNiceClassName(psiClass);
     String tooltipText = psiClass.getQualifiedName();
     UMLLabel label = new UMLLabel(displayName);
     label.setPsiElement((PsiElement)psiClass);
     EditorHelper.initQuickSourceSupport(this.dependencyDiagramComponent.getDiagramSettings().getQuickSourceLinkColor(), this.dependencyDiagramComponent.getProject(), (JComponent)this, label, psiClass.getNameIdentifier(), false, false, true);









     label.setToolTipText(tooltipText);
     getContentPane().add((Component)label, "Center");
     setBorder((Border)new LineBorder2D(Color.GRAY, 1));


     initSelectionSupport();
     setSelectionManager(this.dependencyDiagramComponent.getSelectionManager());
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
             SimpleClassFigureComponent.this.dependencyDiagramComponent.dispatchEvent(e);
           }



           public void keyReleased(KeyEvent e) {
             SimpleClassFigureComponent.this.dependencyDiagramComponent.dispatchEvent(e);
           }



           public void keyTyped(KeyEvent e) {
             SimpleClassFigureComponent.this.dependencyDiagramComponent.dispatchEvent(e);
           }
         });
   }



   private void initSelectionSupport() {
     FigureComponentSelectionHandler selectionHandler = new FigureComponentSelectionHandler(this.dependencyDiagramComponent);
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
       return this.dependencyDiagramComponent.getProject();
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


