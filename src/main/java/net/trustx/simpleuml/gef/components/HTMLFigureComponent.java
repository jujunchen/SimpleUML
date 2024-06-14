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
 import javax.swing.JEditorPane;
 import javax.swing.JLabel;
 import javax.swing.JScrollPane;
 import javax.swing.JTextArea;
 import javax.swing.JTextField;
 import javax.swing.SwingUtilities;
 import net.trustx.simpleuml.components.ActionContributorCommand;
 import net.trustx.simpleuml.components.ActionContributorCommandInfo;
 
 
 public class HTMLFigureComponent
   extends FigureComponent
 {
   private static final String DEFAULT_TITLE_TEXT = "Title (doubleclick to edit)";
   private static final Color DEFAULT_COLOR = Color.LIGHT_GRAY;
   
   private static final String TYPE_HTML = "html";
   
   public static final String TYPE_PLAIN = "plain";
   
   private JEditorPane editorArea;
   
   private JLabel titleLabel;
   
   private JTextField titleTextField;
   
   private HashMap actionContributorCommandMap;
   private JTextArea textArea;
   private JScrollPane editorAreaScrollPane;
   private JScrollPane textAreaScrollPane;
   private String type;
   
   public HTMLFigureComponent(DiagramPane diagramPane) {
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
             HTMLFigureComponent.this.stopEditTitle();
           }
         });
     
     this.titleTextField.addKeyListener(new KeyAdapter()
         {
           public void keyReleased(KeyEvent e)
           {
             if (e.getKeyCode() == 10)
             {
               HTMLFigureComponent.this.stopEditTitle();
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
               
               HTMLFigureComponent.this.titleTextField.setText(HTMLFigureComponent.this.titleLabel.getText());
               HTMLFigureComponent.this.getContentPane().remove(HTMLFigureComponent.this.titleLabel);
               HTMLFigureComponent.this.getContentPane().add(HTMLFigureComponent.this.titleTextField, "North");
               HTMLFigureComponent.this.getContentPane().revalidate();
               HTMLFigureComponent.this.titleTextField.requestFocusInWindow();
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
             HTMLFigureComponent.this.dispatchEvent(SwingUtilities.convertMouseEvent(e.getComponent(), e, HTMLFigureComponent.this));
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
             HTMLFigureComponent.this.dispatchEvent(SwingUtilities.convertMouseEvent(e.getComponent(), e, HTMLFigureComponent.this));
           }
         });
 
     
     getContentPane().add(this.titleLabel, "North");
     
     this.editorArea = new JEditorPane();
     this.editorArea.setContentType("text/html");
     this.editorArea.setEditable(false);
     
     this.textArea = new JTextArea();
     this.textAreaScrollPane = new JScrollPane(this.textArea);
     
     this.editorAreaScrollPane = new JScrollPane(this.editorArea);
     getContentPane().add(this.editorAreaScrollPane, "Center");
     
     initActionContributorCommandMap();
     
     setType("plain");
   }
 
 
   
   public JLabel getTitleLabel() {
     return this.titleLabel;
   }
 
 
   
   public JTextField getTitleTextField() {
     return this.titleTextField;
   }
 
 
   
   public JTextArea getTextArea() {
     return this.textArea;
   }
 
 
   
   private void stopEditTitle() {
     this.titleLabel.setText(this.titleTextField.getText());
     getContentPane().remove(this.titleTextField);
     getContentPane().add(this.titleLabel, "North");
     getContentPane().revalidate();
     getContentPane().repaint();
     this.editorArea.requestFocusInWindow();
   }
 
 
   
   public void setTitleText(String text) {
     this.titleLabel.setText(text);
   }
 
 
   
   public String getTitleText() {
     return this.titleLabel.getText();
   }
 
 
   
   public void setContentText(String text) {
     this.textArea.setText(text);
     this.editorArea.setText(text);
   }
 
 
   
   public String getContentText() {
     return this.textArea.getText();
   }
 
 
   
   public String getType() {
     return this.type;
   }
 
 
   
   public void setType(String type) {
     this.type = type;
     if ("plain".equals(type)) {
       
       getContentPane().remove(this.editorAreaScrollPane);
       getContentPane().remove(this.textAreaScrollPane);
       getContentPane().add(this.textAreaScrollPane, "Center");
       getContentPane().revalidate();
       getContentPane().repaint();
     }
     else {
       
       getContentPane().remove(this.textAreaScrollPane);
       getContentPane().remove(this.editorAreaScrollPane);
       getContentPane().add(this.editorAreaScrollPane, "Center");
       this.editorArea.setText(this.textArea.getText());
       getContentPane().revalidate();
       getContentPane().repaint();
     } 
   }
 
 
   
   private void initActionContributorCommandMap() {
     this.actionContributorCommandMap = new LinkedHashMap<Object, Object>();
     this.actionContributorCommandMap.put(new ActionContributorCommandInfo("Remove", new String[] { "Figure" }), new RemoveFigureComponentCommand("Remove", this));
     this.actionContributorCommandMap.put(new ActionContributorCommandInfo("Change Color", new String[] { "Figure" }), new ChangeColorCommand("Change Color", this));
     
     if ("plain".equals(this.type)) {
       
       this.actionContributorCommandMap.put(new ActionContributorCommandInfo("HTML", new String[] { "Figure", "Type" }), new ChangeTypeCommand(this, "html"));
     }
     else {
       
       this.actionContributorCommandMap.put(new ActionContributorCommandInfo("Text", new String[] { "Figure", "Type" }), new ChangeTypeCommand(this, "plain"));
     } 
   }
 
 
 
   
   public ActionContributorCommand getActionContributorCommand(ActionContributorCommandInfo info) {
     initActionContributorCommandMap();
     return (ActionContributorCommand)this.actionContributorCommandMap.get(info);
   }
 
 
   
   public ActionContributorCommandInfo[] getActionContributorCommandInfos() {
     initActionContributorCommandMap();
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
 
   
   private class ChangeTypeCommand
     extends ActionContributorCommand
   {
     private HTMLFigureComponent htmlFigureComponent;
     
     private String type;
 
     
     public ChangeTypeCommand(HTMLFigureComponent htmlFigureComponent, String type) {
       this.htmlFigureComponent = htmlFigureComponent;
       this.type = type;
     }
 
 
     
     public void executeCommand(HashMap commandProperties, boolean first, boolean last) {
       this.htmlFigureComponent.setType(this.type);
     }
   }
 }


