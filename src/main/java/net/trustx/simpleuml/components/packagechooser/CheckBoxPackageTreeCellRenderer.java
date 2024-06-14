 package net.trustx.simpleuml.components.packagechooser;
 
 import com.intellij.psi.PsiPackage;
 import java.awt.Color;
 import java.awt.Component;
 import java.awt.Dimension;
 import java.awt.Graphics;
 import java.awt.LayoutManager;
 import javax.swing.Icon;
 import javax.swing.ImageIcon;
 import javax.swing.JCheckBox;
 import javax.swing.JLabel;
 import javax.swing.JPanel;
 import javax.swing.JTree;
 import javax.swing.UIManager;
 import javax.swing.tree.TreeCellRenderer;
 

 public class CheckBoxPackageTreeCellRenderer
   extends JPanel
   implements TreeCellRenderer
 {
   protected JCheckBox checkBox;
   protected TreeLabel treeLabel;
   private ImageIcon packageImageIcon;
   
   public CheckBoxPackageTreeCellRenderer() {
     this.packageImageIcon = new ImageIcon(CheckBoxPackageTreeCellRenderer.class.getResource("/nodes/packageClosed.png"));
     
     setLayout((LayoutManager)null);
     add(this.checkBox = new JCheckBox());
     add(this.treeLabel = new TreeLabel());
     this.checkBox.setBackground(UIManager.getColor("Tree.textBackground"));
     this.treeLabel.setForeground(UIManager.getColor("Tree.textForeground"));
   }
 
 
   
   public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
     setEnabled(tree.isEnabled());
     this.checkBox.setSelected(((CheckBoxNode)value).isSelected());
     this.treeLabel.setFont(tree.getFont());
     this.treeLabel.setSelected(isSelected);
     this.treeLabel.setFocus(hasFocus);
     
     Object userObject = ((CheckBoxNode)value).getUserObject();
     initLabels(userObject);
     
     return this;
   }
 
 
   
   private void initLabels(Object userObject) {
     if (userObject != null && userObject instanceof PsiPackage) {
       
       PsiPackage psiPackage = (PsiPackage)userObject;
       String niceName = psiPackage.getQualifiedName();
       
       this.treeLabel.setText(niceName);
       this.treeLabel.setIcon(this.packageImageIcon);
     } 
   }
 
 
   
   public Dimension getPreferredSize() {
     Dimension checkBoxDimension = this.checkBox.getPreferredSize();
     Dimension labelDimension = this.treeLabel.getPreferredSize();
     return new Dimension(checkBoxDimension.width + labelDimension.width + 3, (checkBoxDimension.height < labelDimension.height) ? labelDimension.height : checkBoxDimension.height);
   }
 
 
   
   public void doLayout() {
     Dimension checkBoxDimension = this.checkBox.getPreferredSize();
     Dimension labelDimension = this.treeLabel.getPreferredSize();
     int checkBoxAlignment = 0;
     int labelAlignment = 0;
     if (checkBoxDimension.height < labelDimension.height) {
       
       checkBoxAlignment = (labelDimension.height - checkBoxDimension.height) / 2;
     }
     else {
       
       labelAlignment = (checkBoxDimension.height - labelDimension.height) / 2;
     } 
     this.checkBox.setLocation(0, checkBoxAlignment);
     this.checkBox.setBounds(0, checkBoxAlignment, checkBoxDimension.width, checkBoxDimension.height);
     
     this.treeLabel.setLocation(checkBoxDimension.width, labelAlignment);
     this.treeLabel.setBounds(checkBoxDimension.width, labelAlignment, labelDimension.width, labelDimension.height);
   }
 
 
   
   public void setBackground(Color color) {
     if (color instanceof javax.swing.plaf.ColorUIResource)
     {
       color = null;
     }
     super.setBackground(color);
   }
 
   
   public class TreeLabel
     extends JLabel
   {
     boolean selected;
     
     boolean hasFocus;
 
     
     public void setBackground(Color color) {
       if (color instanceof javax.swing.plaf.ColorUIResource)
       {
         color = null;
       }
       super.setBackground(color);
     }
 
 
     
     public void paint(Graphics g) {
       String str;
       if ((str = getText()) != null)
       {
         if (0 < str.length()) {
           
           if (this.selected) {
             
             g.setColor(UIManager.getColor("Tree.selectionBackground"));
           }
           else {
             
             g.setColor(UIManager.getColor("Tree.textBackground"));
           } 
           Dimension d = getPreferredSize();
           int imageOffset = 0;
           Icon icon = getIcon();
           if (icon != null)
           {
             imageOffset = icon.getIconWidth() + Math.max(0, getIconTextGap() - 1);
           }
           g.fillRect(imageOffset, 0, d.width - 1 - imageOffset, d.height);
           if (this.hasFocus) {
             
             g.setColor(UIManager.getColor("Tree.selectionBorderColor"));
             g.drawRect(imageOffset, 0, d.width - 1 - imageOffset, d.height - 1);
           } 
         } 
       }
       super.paint(g);
     }
 
 
     
     public Dimension getPreferredSize() {
       Dimension retDimension = super.getPreferredSize();
       if (retDimension != null)
       {
         retDimension = new Dimension(retDimension.width + 3, retDimension.height);
       }
       return retDimension;
     }
 
 
     
     public void setSelected(boolean selected) {
       this.selected = selected;
     }
 
 
     
     public void setFocus(boolean hasFocus) {
       this.hasFocus = hasFocus;
     }
   }
 }


