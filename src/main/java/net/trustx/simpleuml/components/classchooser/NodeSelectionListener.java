 package net.trustx.simpleuml.components.classchooser;

 import java.awt.event.MouseAdapter;
 import java.awt.event.MouseEvent;
 import javax.swing.JTree;
 import javax.swing.tree.DefaultTreeModel;
 import javax.swing.tree.TreePath;




 public class NodeSelectionListener
   extends MouseAdapter
 {
   JTree tree;

   NodeSelectionListener(JTree tree) {
     this.tree = tree;
   }



   public void mouseReleased(MouseEvent e) {
     int x = e.getX();
     int y = e.getY();
     int row = this.tree.getRowForLocation(x, y);
     TreePath path = this.tree.getPathForRow(row);

     if (path != null) {

       CheckBoxNode node = (CheckBoxNode)path.getLastPathComponent();
       boolean isSelected = !node.isSelected();
       node.setSelected(isSelected);
       if (node.getSelectionMode() == 4)
       {
         if (isSelected) {

           this.tree.expandPath(path);
         }
         else {

           this.tree.collapsePath(path);
         }
       }
       ((DefaultTreeModel)this.tree.getModel()).nodeChanged(node);

       if (row == 0) {

         this.tree.revalidate();
         this.tree.repaint();
       }
     }
   }
 }


