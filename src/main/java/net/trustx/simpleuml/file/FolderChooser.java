 package net.trustx.simpleuml.file;

 import com.intellij.openapi.project.Project;
 import com.intellij.openapi.ui.DialogWrapper;
 import com.intellij.openapi.vfs.VfsUtil;
 import com.intellij.openapi.vfs.VirtualFile;
 import java.awt.BorderLayout;
 import java.awt.Dimension;
 import java.io.File;
 import java.net.MalformedURLException;
 import javax.swing.Action;
 import javax.swing.JComponent;
 import javax.swing.JPanel;
 import javax.swing.JScrollPane;
 import javax.swing.JTree;
 import javax.swing.event.TreeSelectionEvent;
 import javax.swing.event.TreeSelectionListener;
 import javax.swing.tree.TreePath;
 import net.trustx.simpleuml.plugin.UMLToolWindowPlugin;





 public class FolderChooser
   extends DialogWrapper
 {
   private JPanel centerPanel;
   private VirtualFile selectedFile;

   public FolderChooser(Project project, String selectedFolderURL) {
     super(project, false);

     this.centerPanel = new JPanel(new BorderLayout());
     getContentPane().setLayout(new BorderLayout());
     getContentPane().add(this.centerPanel, "Center");

     UMLFileFilter fileFilter = new UMLFileFilter(UMLFileFilter.DIRS_ONLY);
     FileTreeNode root = new FileTreeNode(fileFilter);
     JTree tree = new JTree(root);
     tree.setShowsRootHandles(true);
     tree.setRootVisible(false);
     tree.setCellRenderer(new UMLFileTreeCellRenderer());
     this.centerPanel.add(new JScrollPane(tree), "Center");

     tree.addTreeSelectionListener(new TreeSelectionListener()
         {
           public void valueChanged(TreeSelectionEvent e)
           {
             TreePath tp = e.getPath();
             if (tp != null) {

               try {

                 FolderChooser.this.selectedFile = VfsUtil.findFileByURL(((FileTreeNode)tp.getLastPathComponent()).getFile().toURI().toURL());
                 FolderChooser.this.setOKActionEnabled(true);
               }
               catch (MalformedURLException e1) {

                 FolderChooser.this.selectedFile = null;
                 e1.printStackTrace();
                 FolderChooser.this.setOKActionEnabled(false);
               }
             }
           }
         });


     try {
       String url = selectedFolderURL;
       if (selectedFolderURL == null) {

         UMLFileManager fileManager = UMLToolWindowPlugin.getUMLToolWindowPlugin(project).getUMLFileManager();
         url = fileManager.getDefaultFileLocationURL();
       }

       int index = url.indexOf("//");
       url = url.substring(index + 2);
       url = url.replace('/', File.separatorChar);
       File file = new File(url);

       TreePath tp = root.getTreePath(file);

       tree.setSelectionPath(tp);
       tree.scrollPathToVisible(tp);
     }
     catch (Throwable ex) {}




     this.centerPanel.setPreferredSize(new Dimension(400, 500));

     init();
     setOKActionEnabled((tree.getLastSelectedPathComponent() != null));
     setTitle("Choose a Folder");
   }



   public VirtualFile getSelectedFile() {
     return this.selectedFile;
   }



   protected JComponent createCenterPanel() {
     return this.centerPanel;
   }



   protected Action[] createActions() {
     setOKButtonText("Ok");
     setCancelButtonText("Cancel");
     getOKAction().putValue("DefaultAction", Boolean.TRUE);
     getCancelAction().putValue("DefaultAction", Boolean.FALSE);
     return new Action[] { getOKAction(), getCancelAction() };
   }
 }


