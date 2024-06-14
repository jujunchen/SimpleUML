 package net.trustx.simpleuml.gef.components;

 import java.awt.BorderLayout;
 import java.awt.Color;
 import java.awt.Dimension;
 import java.awt.event.FocusAdapter;
 import java.awt.event.FocusEvent;
 import java.awt.event.KeyAdapter;
 import java.awt.event.KeyEvent;
 import java.awt.event.MouseAdapter;
 import java.awt.event.MouseEvent;
 import java.awt.event.MouseMotionAdapter;
 import java.util.HashMap;
 import java.util.LinkedHashMap;
 import javax.swing.BorderFactory;
 import javax.swing.JLabel;
 import javax.swing.JScrollPane;
 import javax.swing.JTextArea;
 import javax.swing.JTextField;
 import javax.swing.SwingUtilities;
 import net.trustx.simpleuml.components.ActionContributorCommand;
 import net.trustx.simpleuml.components.ActionContributorCommandInfo;




 public class TextAreaFigureComponent
   extends FigureComponent
 {
   private static final String DEFAULT_TITLE_TEXT = "Title (doubleclick to edit)";
   private static final Color DEFAULT_COLOR = Color.LIGHT_GRAY;

   private JTextArea textArea;

   private JLabel titleLabel;

   private JTextField titleTextField;

   private HashMap actionContributorCommandMap;

   public TextAreaFigureComponent(DiagramPane diagramPane) {
     super(diagramPane);
     setOpaque(true);
     setMinimumSize(new Dimension(10, 10));

     getContentPane().setLayout(new BorderLayout());

     this.titleTextField = new JTextField("Title (doubleclick to edit)");
     this.titleTextField.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
     this.titleTextField.addFocusListener(new FocusAdapter()
         {
           public void focusLost(FocusEvent e)
           {
             TextAreaFigureComponent.this.stopEditTitle();
           }
         });

     this.titleTextField.addKeyListener(new KeyAdapter()
         {
           public void keyReleased(KeyEvent e)
           {
             if (e.getKeyCode() == 10)
             {
               TextAreaFigureComponent.this.stopEditTitle();
             }
           }
         });

     this.titleLabel = new JLabel("Title (doubleclick to edit)");
     this.titleLabel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

     this.titleLabel.addMouseListener(new MouseAdapter()
         {
           public void mouseClicked(MouseEvent e)
           {
             if (e.getClickCount() == 2) {


               TextAreaFigureComponent.this.titleTextField.setText(TextAreaFigureComponent.this.titleLabel.getText());
               TextAreaFigureComponent.this.getContentPane().remove(TextAreaFigureComponent.this.titleLabel);
               TextAreaFigureComponent.this.getContentPane().add(TextAreaFigureComponent.this.titleTextField, "North");
               TextAreaFigureComponent.this.getContentPane().revalidate();
               TextAreaFigureComponent.this.titleTextField.requestFocusInWindow();
             }
             else {

               dispatchToParent(e);
             }
           }



           public void mouseEntered(MouseEvent e) {
             dispatchToParent(e);
           }



           public void mouseExited(MouseEvent e) {
             dispatchToParent(e);
           }



           public void mousePressed(MouseEvent e) {
             dispatchToParent(e);
           }



           public void mouseReleased(MouseEvent e) {
             dispatchToParent(e);
           }




           private void dispatchToParent(MouseEvent e) {
             TextAreaFigureComponent.this.dispatchEvent(SwingUtilities.convertMouseEvent(e.getComponent(), e, TextAreaFigureComponent.this));
           }
         });

     this.titleLabel.addMouseMotionListener(new MouseMotionAdapter()
         {
           public void mouseDragged(MouseEvent e)
           {
             dispatchToParent(e);
           }



           public void mouseMoved(MouseEvent e) {
             dispatchToParent(e);
           }



           private void dispatchToParent(MouseEvent e) {
             TextAreaFigureComponent.this.dispatchEvent(SwingUtilities.convertMouseEvent(e.getComponent(), e, TextAreaFigureComponent.this));
           }
         });


     getContentPane().add(this.titleLabel, "North");

     this.textArea = new JTextArea();
     getContentPane().add(new JScrollPane(this.textArea), "Center");

     initActionContributorCommandMap();
   }



   private void stopEditTitle() {
     this.titleLabel.setText(this.titleTextField.getText());
     getContentPane().remove(this.titleTextField);
     getContentPane().add(this.titleLabel, "North");
     getContentPane().revalidate();
     getContentPane().repaint();
     this.textArea.requestFocusInWindow();
   }



   public JTextArea getTextArea() {
     return this.textArea;
   }



   public JLabel getTitleLabel() {
     return this.titleLabel;
   }



   public JTextField getTitleTextField() {
     return this.titleTextField;
   }



   private void initActionContributorCommandMap() {
     this.actionContributorCommandMap = new LinkedHashMap<Object, Object>();
     this.actionContributorCommandMap.put(new ActionContributorCommandInfo("Remove", new String[] { "Figure" }), new RemoveFigureComponentCommand("Remove", this));
     this.actionContributorCommandMap.put(new ActionContributorCommandInfo("Change Color", new String[] { "Figure" }), new ChangeColorCommand("Change Color", this));
   }



   public ActionContributorCommand getActionContributorCommand(ActionContributorCommandInfo info) {
     return (ActionContributorCommand)this.actionContributorCommandMap.get(info);
   }



   public ActionContributorCommandInfo[] getActionContributorCommandInfos() {
     return (ActionContributorCommandInfo[])this.actionContributorCommandMap.keySet().toArray((Object[])new ActionContributorCommandInfo[this.actionContributorCommandMap.keySet().size()]);
   }



   public void setColor(Color color) {
     if (color == null) {

       super.setColor(DEFAULT_COLOR);
     }
     else {

       super.setColor(color);
     }
     updateColor();
   }



   private void updateColor() {
     setBackground(getColor());
   }
 }


