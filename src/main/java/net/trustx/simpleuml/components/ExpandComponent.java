 package net.trustx.simpleuml.components;

 import java.awt.Color;
 import java.awt.Font;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.awt.event.MouseAdapter;
 import java.awt.event.MouseEvent;
 import java.util.ArrayList;
 import java.util.Iterator;
 import javax.swing.BoxLayout;
 import javax.swing.JLabel;
 import javax.swing.JPanel;



 public class ExpandComponent
   extends JPanel
 {
   private ExpandLabel expandLabel;
   private JLabel label;
   private ArrayList listenerList;

   public ExpandComponent(String text, boolean expanded, boolean expandable, boolean dashed) {
     setOpaque(false);
     this.listenerList = new ArrayList();

     setLayout(new BoxLayout(this, 0));
     this.expandLabel = new ExpandLabel(dashed);
     this.expandLabel.setExpanded(expanded);
     this.expandLabel.setExpandable(expandable);

     this.expandLabel.addMouseListener(new MouseAdapter()
         {
           public void mousePressed(MouseEvent e)
           {
             ExpandComponent.this.expandLabel.setExpanded(!ExpandComponent.this.expandLabel.isExpanded());
             ExpandComponent.this.notifyActionListeners();
           }
         });

     this.label = new UMLLabel(text);
     this.label.setFont(new Font("sansserif", 0, 10));
     this.label.setForeground(Color.darkGray);

     add(this.expandLabel);
     add(this.label);
     add(new Separator(dashed));
   }



   private void notifyActionListeners() {
     for (Iterator<ActionListener> iterator = this.listenerList.iterator(); iterator.hasNext(); ) {

       ActionListener listener = iterator.next();
       listener.actionPerformed(new ActionEvent(this, 0, ""));
     }
   }



   public void addActionListener(ActionListener listener) {
     this.listenerList.add(listener);
   }



   public void removeActionListener(ActionListener listener) {
     this.listenerList.remove(listener);
   }



   public boolean isSelected() {
     return this.expandLabel.isExpanded();
   }



   public void setSelected(boolean b) {
     this.expandLabel.setExpanded(b);
   }



   public void setExpandable(boolean expandable) {
     this.expandLabel.setExpandable(expandable);
   }



   public boolean isExpandable() {
     return this.expandLabel.isExpandable();
   }
 }


