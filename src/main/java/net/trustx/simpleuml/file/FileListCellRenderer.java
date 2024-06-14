 package net.trustx.simpleuml.file;
 
 import com.intellij.openapi.vfs.VirtualFile;
 import java.awt.Color;
 import java.awt.Component;
 import java.awt.Dimension;
 import javax.swing.Box;
 import javax.swing.DefaultListCellRenderer;
 import javax.swing.JLabel;
 import javax.swing.JList;
 import javax.swing.UIManager;
 
 
 

 public class FileListCellRenderer
   extends DefaultListCellRenderer
 {
   private JLabel longestLabel;
   
   public FileListCellRenderer(JLabel longestLabel) {
     this.longestLabel = longestLabel;
   }
 
 
   
   public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
     VirtualFile vf = (VirtualFile)value;
     Box box = Box.createHorizontalBox();
     box.add(Box.createHorizontalStrut(5));
     
     final Dimension dimension = new Dimension(this.longestLabel.getPreferredSize());
     
     JLabel nameLabel = new JLabel(vf.getNameWithoutExtension())
       {
         public Dimension getPreferredSize()
         {
           return dimension;
         }
 
 
         
         public Dimension getMinimumSize() {
           return dimension;
         }
 
 
         
         public Dimension getMaximumSize() {
           return dimension;
         }
       };
     nameLabel.setOpaque(true);
     
     if (isSelected) {
       
       nameLabel.setBackground(list.getSelectionBackground());
       nameLabel.setForeground(list.getSelectionForeground());
     }
     else {
       
       nameLabel.setBackground(list.getBackground());
       nameLabel.setForeground(list.getForeground());
     } 
     
     nameLabel.setEnabled(list.isEnabled());
     nameLabel.setFont(list.getFont());
     nameLabel.setBorder(cellHasFocus ? UIManager.getBorder("List.focusCellHighlightBorder") : noFocusBorder);
     
     nameLabel.setPreferredSize(new Dimension(this.longestLabel.getPreferredSize()));
     
     box.add(nameLabel);
     
     JLabel pathLabel = new JLabel("(" + vf.getPresentableUrl() + ")");
     pathLabel.setForeground(Color.GRAY);
     box.add(pathLabel);
     return box;
   }
 }


