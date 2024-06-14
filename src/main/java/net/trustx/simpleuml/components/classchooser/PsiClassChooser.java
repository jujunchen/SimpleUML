 package net.trustx.simpleuml.components.classchooser;
 
 import com.intellij.openapi.project.Project;
 import com.intellij.openapi.ui.DialogWrapper;
 import com.intellij.psi.PsiClass;
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
 

 
 public class PsiClassChooser
   extends DialogWrapper
 {
   private JPanel centerPanel;
   private JTree tree;
   private CheckBoxNode root;
   
   public PsiClassChooser(Project project, Collection psiClasses, boolean selected) {
     super(project, false);
     init(psiClasses, selected);
   }
 
 
   
   public void init(Collection psiClasses, boolean selected) {
     getContentPane().setLayout(new BorderLayout());
     this.centerPanel = new JPanel(new BorderLayout(5, 5));
     getContentPane().add(this.centerPanel, "Center");
     
     this.root = new CheckBoxNode("root", true, false);
     
     for (Iterator<PsiClass> iterator = psiClasses.iterator(); iterator.hasNext(); ) {
       
       PsiClass psiClass = iterator.next();
       CheckBoxNode child = new CheckBoxNode(psiClass);
       child.setSelected(selected);
       this.root.add(child);
     } 
 
     
     this.tree = new JTree(this.root);
     this.tree.setRootVisible(false);
     
     this.tree.setCellRenderer(new CheckBoxTreeCellRenderer());
     this.tree.getSelectionModel().setSelectionMode(1);
     this.tree.addMouseListener(new NodeSelectionListener(this.tree));
     
     this.centerPanel.add(new JScrollPane(this.tree), "Center");
     
     this.centerPanel.setPreferredSize(new Dimension(400, 500));
     
     init();
     setOKActionEnabled(true);
     setTitle("Choose the Classes to Add");
   }
 
 
   
   public Collection getSelectedPsiClasses() {
     LinkedList<Object> list = new LinkedList();
     Enumeration<TreeNode> enumeration = this.root.breadthFirstEnumeration();
     while (enumeration.hasMoreElements()) {
       
       CheckBoxNode checkNode = (CheckBoxNode)enumeration.nextElement();
       if (checkNode.isSelected() && checkNode.getUserObject() instanceof PsiClass)
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


