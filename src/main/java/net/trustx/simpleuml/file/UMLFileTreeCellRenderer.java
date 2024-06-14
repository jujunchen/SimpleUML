 package net.trustx.simpleuml.file;
 
 import java.awt.Component;
 import javax.swing.ImageIcon;
 import javax.swing.JTree;
 import javax.swing.tree.DefaultTreeCellRenderer;
 
 
 

 public class UMLFileTreeCellRenderer
   extends DefaultTreeCellRenderer
 {
   private static final ImageIcon OPEN_FOLDER_ICON = new ImageIcon(UMLFileTreeCellRenderer.class.getResource("/nodes/TreeOpen.png"));
   private static final ImageIcon CLOSED_FOLDER_ICON = new ImageIcon(UMLFileTreeCellRenderer.class.getResource("/nodes/TreeClosed.png"));
   private static final ImageIcon UML_ICON = new ImageIcon(UMLFileTreeCellRenderer.class.getResource("/net/trustx/simpleuml/icons/simpleUMLsmall.png"));
 
 
   
   public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
     super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
     
     if (!leaf) {
       
       if (expanded)
       {
         setIcon(OPEN_FOLDER_ICON);
       }
       else
       {
         setIcon(CLOSED_FOLDER_ICON);
       }
     
     } else {
       
       setIcon(UML_ICON);
     } 
     
     return this;
   }
 }


