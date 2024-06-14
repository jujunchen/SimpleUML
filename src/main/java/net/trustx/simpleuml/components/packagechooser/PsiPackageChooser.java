 package net.trustx.simpleuml.components.packagechooser;

 import com.intellij.openapi.project.Project;
 import com.intellij.openapi.ui.DialogWrapper;
 import com.intellij.psi.PsiPackage;
 import java.awt.BorderLayout;
 import java.awt.Dimension;
 import java.util.Collection;
 import java.util.Enumeration;
 import java.util.Iterator;
 import java.util.LinkedList;
 import javax.swing.JComponent;
 import javax.swing.JPanel;
 import javax.swing.JScrollPane;
 import javax.swing.JTree;
 import javax.swing.tree.TreeNode;


 public class PsiPackageChooser
   extends DialogWrapper
 {
   private JPanel centerPanel;
   private JTree tree;
   private CheckBoxNode root;

   public PsiPackageChooser(Project project, Collection psiPackages, boolean selected) {
     super(project, false);
     init(psiPackages, selected);
   }



   public void init(Collection psiPackages, boolean selected) {
     getContentPane().setLayout(new BorderLayout());
     this.centerPanel = new JPanel(new BorderLayout(5, 5));
     getContentPane().add(this.centerPanel, "Center");

     this.root = new CheckBoxNode("root", true, false);

     for (Iterator<PsiPackage> iterator = psiPackages.iterator(); iterator.hasNext(); ) {

       PsiPackage psiPackage = iterator.next();
       CheckBoxNode child = new CheckBoxNode(psiPackage);
       child.setSelected(selected);
       this.root.add(child);
     }


     this.tree = new JTree(this.root);
     this.tree.setRootVisible(false);

     this.tree.setCellRenderer(new CheckBoxPackageTreeCellRenderer());
     this.tree.getSelectionModel().setSelectionMode(1);
     this.tree.addMouseListener(new NodeSelectionListener(this.tree));

     this.centerPanel.add(new JScrollPane(this.tree), "Center");

     this.centerPanel.setPreferredSize(new Dimension(400, 500));

     init();
     setOKActionEnabled(true);
     setTitle("Choose the Classes to Add");
   }



   public Collection getSelectedPsiPackages() {
     LinkedList<Object> list = new LinkedList();
     Enumeration<TreeNode> enumeration = this.root.breadthFirstEnumeration();
     while (enumeration.hasMoreElements()) {

       CheckBoxNode checkNode = (CheckBoxNode)enumeration.nextElement();
       if (checkNode.isSelected() && checkNode.getUserObject() instanceof PsiPackage)
       {
         list.add(checkNode.getUserObject());
       }
     }
     return list;
   }



   protected JComponent createCenterPanel() {
     return this.centerPanel;
   }
 }


