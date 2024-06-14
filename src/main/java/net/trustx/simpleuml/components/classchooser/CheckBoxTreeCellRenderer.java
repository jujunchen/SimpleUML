 package net.trustx.simpleuml.components.classchooser;

 import com.intellij.psi.PsiClass;
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
 import net.trustx.simpleuml.util.UMLUtils;



 public class CheckBoxTreeCellRenderer
   extends JPanel
   implements TreeCellRenderer
 {
   protected JCheckBox checkBox;
   protected TreeLabel treeLabel;
   protected JLabel packageLabel;
   private ImageIcon classImageIcon;
   private ImageIcon interfaceImageIcon;
   private ImageIcon classStaticImageIcon;
   private ImageIcon interfaceStaticImageIcon;

   public CheckBoxTreeCellRenderer() {
     this.classImageIcon = new ImageIcon(CheckBoxTreeCellRenderer.class.getResource("/net/trustx/simpleuml/icons/class.png"));
     this.interfaceImageIcon = new ImageIcon(CheckBoxTreeCellRenderer.class.getResource("/net/trustx/simpleuml/icons/interface.png"));
     this.classStaticImageIcon = new ImageIcon(CheckBoxTreeCellRenderer.class.getResource("/net/trustx/simpleuml/icons/staticClass.png"));
     this.interfaceStaticImageIcon = new ImageIcon(CheckBoxTreeCellRenderer.class.getResource("/net/trustx/simpleuml/icons/staticInterface.png"));

     setLayout((LayoutManager)null);
     add(this.checkBox = new JCheckBox());
     add(this.treeLabel = new TreeLabel());
     add(this.packageLabel = new JLabel());
     this.checkBox.setBackground(UIManager.getColor("Tree.textBackground"));
     this.treeLabel.setForeground(UIManager.getColor("Tree.textForeground"));
     this.packageLabel.setForeground(Color.GRAY);
   }



   public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
     setEnabled(tree.isEnabled());
     this.checkBox.setSelected(((CheckBoxNode)value).isSelected());
     this.treeLabel.setFont(tree.getFont());
     this.treeLabel.setSelected(isSelected);
     this.treeLabel.setFocus(hasFocus);
     this.packageLabel.setFont(tree.getFont());

     Object userObject = ((CheckBoxNode)value).getUserObject();
     initLabels(userObject);

     return this;
   }



   private void initLabels(Object userObject) {
     if (userObject != null && userObject instanceof PsiClass) {

       PsiClass psiClass = (PsiClass)userObject;
       String niceName = UMLUtils.getNiceClassName(psiClass);
       String fqn = psiClass.getQualifiedName();
       if (fqn != null && fqn.length() > niceName.length()) {

         fqn = fqn.substring(0, fqn.length() - niceName.length());
         if (fqn.length() > 0)
         {
           fqn = fqn.substring(0, fqn.length() - 1);
         }
         fqn = "(" + fqn + ")";
       }
       else {

         fqn = "";
       }


       this.treeLabel.setText(niceName);
       this.packageLabel.setText(fqn);

       if (psiClass.isInterface()) {

         if (psiClass.getModifierList().hasModifierProperty("static"))
         {
           this.treeLabel.setIcon(this.interfaceStaticImageIcon);
         }
         else
         {
           this.treeLabel.setIcon(this.interfaceImageIcon);

         }

       }
       else if (psiClass.getModifierList().hasModifierProperty("static")) {

         this.treeLabel.setIcon(this.classStaticImageIcon);
       }
       else {

         this.treeLabel.setIcon(this.classImageIcon);
       }
     }
   }




   public Dimension getPreferredSize() {
     Dimension checkBoxDimension = this.checkBox.getPreferredSize();
     Dimension labelDimension = this.treeLabel.getPreferredSize();
     Dimension packageDimension = this.packageLabel.getPreferredSize();
     return new Dimension(checkBoxDimension.width + labelDimension.width + packageDimension.width + 3, (checkBoxDimension.height < labelDimension.height) ? labelDimension.height : checkBoxDimension.height);
   }



   public void doLayout() {
     Dimension checkBoxDimension = this.checkBox.getPreferredSize();
     Dimension labelDimension = this.treeLabel.getPreferredSize();
     Dimension packageDimension = this.packageLabel.getPreferredSize();
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

     this.packageLabel.setLocation(checkBoxDimension.width + labelDimension.width + 3, labelAlignment);
     this.packageLabel.setBounds(checkBoxDimension.width + labelDimension.width + 3, labelAlignment, packageDimension.width, labelDimension.height);
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


