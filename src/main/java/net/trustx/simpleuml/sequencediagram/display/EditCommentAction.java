 package net.trustx.simpleuml.sequencediagram.display;
 
 import java.awt.BorderLayout;
 import java.awt.FlowLayout;
 import java.awt.Point;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.awt.event.MouseEvent;
 import javax.swing.JButton;
 import javax.swing.JCheckBox;
 import javax.swing.JDialog;
 import javax.swing.JPanel;
 import javax.swing.JScrollPane;
 import javax.swing.JTextArea;
 import javax.swing.JViewport;
 import javax.swing.plaf.basic.BasicBorders;
 import net.trustx.simpleuml.sequencediagram.components.DiagramSelectionListener;
 import net.trustx.simpleuml.sequencediagram.model.Link;
 import net.trustx.simpleuml.sequencediagram.model.Model;
 
 public class EditCommentAction
   extends DiagramSelectionListener
   implements AggregateActionListener {
   Model model;
   private CommentDialog dialog = new CommentDialog();
   
   private static final String EDIT_COMMENT_NAME = "Edit Link Comment ...";
   private DiagramSelectionListener aggregateListener;
   
   public EditCommentAction(Display d, JViewport view, Model m) {
     super(d, view);
     this.model = m;
   }
 
 
   
   public void mouseClicked(MouseEvent e) {
     DisplayLink dl = null;
     Point p = getRelativePosition(e);
     
     if (e.isControlDown() && e.getModifiers() == 16) {
 
       
       dl = this.disp.getLinkAt(p);
       this.dialog.show(dl);
     }
     else if (this.disp.getLinkAt(p) == null && (dl = this.disp.getCommentAt(p)) != null && e.getModifiers() == 16) {
 
       
       this.dialog.show(dl);
     } 
   }
 
   
   public String getName() {
     return "Edit Link Comment ...";
   }
 
   
   public void setPointer(DiagramSelectionListener l) {
     this.aggregateListener = l;
   }
 
   
   public void actionPerformed(ActionEvent e) {
     if (this.aggregateListener != null) {
       
       DisplayLink dl = this.disp.getLinkAt(this.aggregateListener.getCurrentRelativePosition());
       if (dl != null)
       {
         this.dialog.show(dl);
       }
     } 
   }
   
   private class CommentDialog
     extends JDialog
   {
     private JPanel mainPanel = new JPanel();
     private JPanel onDiagramPanel = new JPanel();
     private JPanel buttonPanel = new JPanel();
     
     private BorderLayout mainLayout = new BorderLayout();
     private FlowLayout onDiagramLayout = new FlowLayout(0);
     private FlowLayout buttonLayout = new FlowLayout(1);
     
     private JTextArea commentText = new JTextArea();
     private JScrollPane commentPane = new JScrollPane(this.commentText);
     private JCheckBox onDiagramBox = new JCheckBox();
     private JButton okButton = new JButton("OK");
     private JButton cancelButton = new JButton("Cancel");
     
     private Link link;
 
     
     public CommentDialog() {
       initDialog();
     }
 
     
     public void show(DisplayLink l) {
       this.link = EditCommentAction.this.model.getLink(l.getSeq());
       setTitle((this.link.getVerticalSeq() + 1) + ". " + this.link.getName());
       this.onDiagramBox.setSelected(this.link.getCommentOnDiagram());
       this.commentText.setText(this.link.getCallComment());
       setVisible(true);
     }
 
     
     private void initDialog() {
       this.mainPanel.setLayout(this.mainLayout);
       this.onDiagramPanel.setLayout(this.onDiagramLayout);
       this.buttonPanel.setLayout(this.buttonLayout);
       this.commentText.setBorder(BasicBorders.getTextFieldBorder());
       this.commentText.setRows(5);
       this.commentText.setColumns(60);
       this.commentText.setWrapStyleWord(true);
       this.commentText.setLineWrap(true);
       this.onDiagramBox.setText("Show Comment On Diagram");
       this.onDiagramPanel.add(this.onDiagramBox);
       this.buttonPanel.add(this.okButton);
       this.okButton.addActionListener(new ActionListener()
           {
             
             public void actionPerformed(ActionEvent e)
             {
               CommentDialog.this.link.setCallComment(CommentDialog.this.commentText.getText());
               CommentDialog.this.link.setCommentsOnDiagram(CommentDialog.this.onDiagramBox.isSelected());
               EditCommentAction.this.model.refresh(this);
               CommentDialog.this.dispose();
             }
           });
       
       this.buttonPanel.add(this.cancelButton);
       this.cancelButton.addActionListener(new ActionListener()
           {
             
             public void actionPerformed(ActionEvent e)
             {
               CommentDialog.this.dispose();
             }
           });
       
       this.buttonPanel.add(this.cancelButton);
       this.cancelButton.addActionListener(new ActionListener()
           {
             
             public void actionPerformed(ActionEvent e)
             {
               CommentDialog.this.dispose();
             }
           });
       
       this.mainPanel.add(this.onDiagramPanel, "North");
       this.mainPanel.add(this.commentPane, "Center");
       this.mainPanel.add(this.buttonPanel, "South");
       getContentPane().add(this.mainPanel);
       pack();
     }
   }
 }


