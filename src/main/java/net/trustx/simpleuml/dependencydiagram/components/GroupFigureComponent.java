 package net.trustx.simpleuml.dependencydiagram.components;

 import com.intellij.openapi.actionSystem.DataKeys;
 import com.intellij.openapi.actionSystem.DataProvider;
 import com.intellij.psi.PsiElement;
 import java.awt.Color;
 import java.awt.Component;
 import java.awt.event.KeyAdapter;
 import java.awt.event.KeyEvent;
 import java.awt.event.MouseAdapter;
 import java.awt.event.MouseEvent;
 import java.util.ArrayList;
 import javax.swing.BorderFactory;
 import javax.swing.Box;
 import javax.swing.BoxLayout;
 import javax.swing.SwingUtilities;
 import javax.swing.border.Border;
 import net.trustx.simpleuml.components.ActionContributorCommand;
 import net.trustx.simpleuml.components.ActionContributorCommandInfo;
 import net.trustx.simpleuml.components.UMLLabel;
 import net.trustx.simpleuml.components.border.LineBorder2D;
 import net.trustx.simpleuml.gef.components.FigureComponent;
 import net.trustx.simpleuml.gef.components.FigureComponentSelectionHandler;




 public class GroupFigureComponent
   extends FigureComponent
   implements DataProvider
 {
   private PsiElement lastPsiElement;
   private DependencyDiagramComponent dependencyDiagramComponent;

   public GroupFigureComponent(final DependencyDiagramComponent dependencyDiagramComponent) {
     super(dependencyDiagramComponent);
     this.dependencyDiagramComponent = dependencyDiagramComponent;
     BoxLayout boxLayout = new BoxLayout(getContentPane(), 1);
     getContentPane().setLayout(boxLayout);
     setBorder(BorderFactory.createCompoundBorder((Border)new LineBorder2D(Color.BLACK, 1), BorderFactory.createEmptyBorder(0, 5, 0, 5)));



     getContentPane().add(Box.createVerticalStrut(5));

     initSelectionSupport();
     setSelectionManager(dependencyDiagramComponent.getSelectionManager());
     setSelected(false);

     addMouseListener(new MouseAdapter()
         {
           public void mouseClicked(MouseEvent e) {}





           public void mouseReleased(MouseEvent e) {}




           public void mousePressed(MouseEvent e) {
             Component comp = SwingUtilities.getDeepestComponentAt((Component)GroupFigureComponent.this, (e.getPoint()).x, (e.getPoint()).y);
             if (comp instanceof UMLLabel) {

               GroupFigureComponent.this.lastPsiElement = ((UMLLabel)comp).getPsiElement();
             }
             else {

               GroupFigureComponent.this.lastPsiElement = null;
             }
           }
         });

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
   }



   public boolean isFocusable() {
     return true;
   }



   private void initSelectionSupport() {
     FigureComponentSelectionHandler selectionHandler = new FigureComponentSelectionHandler(this.dependencyDiagramComponent);
     selectionHandler.install(this);
   }



   public void addContentFigure(FigureComponent figureComponent) {
     getContentPane().add((Component)figureComponent);
     getContentPane().add(Box.createVerticalStrut(5));
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



   public void updateBorder() {
     Color color = Color.BLACK;

     if (isSelected())
     {
       color = Color.BLUE;
     }

     setBorder(BorderFactory.createCompoundBorder((Border)new LineBorder2D(color, 1), BorderFactory.createEmptyBorder(0, 5, 0, 5)));
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


