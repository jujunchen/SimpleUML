 package net.trustx.simpleuml.components.packagechooser;

 import java.util.Enumeration;
 import javax.swing.tree.DefaultMutableTreeNode;
 import javax.swing.tree.TreeNode;




 public class CheckBoxNode
   extends DefaultMutableTreeNode
 {
   public static final int SINGLE_SELECTION = 0;
   public static final int AUTOMATIC_CHILDREN_SELECTION = 4;
   protected int selectionMode;
   protected boolean selected;

   public CheckBoxNode() {
     this(null);
   }



   public CheckBoxNode(Object userObject) {
     this(userObject, true, false);
   }



   public CheckBoxNode(Object userObject, boolean allowsChildren, boolean isSelected) {
     super(userObject, allowsChildren);
     this.selected = isSelected;
     setSelectionMode(4);
   }



   public void setSelectionMode(int mode) {
     this.selectionMode = mode;
   }



   public int getSelectionMode() {
     return this.selectionMode;
   }



   public void setSelected(boolean selected) {
     this.selected = selected;

     if (this.selectionMode == 4 && this.children != null) {

       Enumeration<TreeNode> e = this.children.elements();
       while (e.hasMoreElements()) {

         CheckBoxNode node = (CheckBoxNode)e.nextElement();
         node.setSelected(selected);
       }
     }
   }



   public boolean isSelected() {
     return this.selected;
   }
 }


