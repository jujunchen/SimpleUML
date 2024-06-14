 package net.trustx.simpleuml.file;

 import com.intellij.openapi.actionSystem.ActionGroup;
 import com.intellij.openapi.actionSystem.ActionManager;
 import com.intellij.openapi.actionSystem.ActionPopupMenu;
 import com.intellij.openapi.actionSystem.AnAction;
 import com.intellij.openapi.actionSystem.AnActionEvent;
 import com.intellij.openapi.actionSystem.DefaultActionGroup;
 import com.intellij.openapi.project.Project;
 import com.intellij.openapi.ui.DialogWrapper;
 import com.intellij.openapi.vfs.VfsUtil;
 import com.intellij.openapi.vfs.VirtualFile;
 import java.awt.BorderLayout;
 import java.awt.event.MouseAdapter;
 import java.awt.event.MouseEvent;
 import java.io.File;
 import java.net.MalformedURLException;
 import java.util.Iterator;
 import java.util.Set;
 import javax.swing.Action;
 import javax.swing.BorderFactory;
 import javax.swing.JComponent;
 import javax.swing.JLabel;
 import javax.swing.JList;
 import javax.swing.JPanel;
 import javax.swing.JPopupMenu;
 import javax.swing.JScrollPane;
 import javax.swing.JSplitPane;
 import javax.swing.JTree;
 import javax.swing.border.Border;
 import javax.swing.event.ListSelectionEvent;
 import javax.swing.event.ListSelectionListener;
 import javax.swing.event.TreeSelectionEvent;
 import javax.swing.event.TreeSelectionListener;
 import javax.swing.tree.TreePath;
 import net.trustx.simpleuml.plugin.UMLToolWindowPlugin;
 import net.trustx.simpleuml.util.UMLConstants;





 public class KnownFilesChooser
   extends DialogWrapper
   implements UMLConstants
 {
   private JTree tree;
   private JList fileList;
   private VirtualFile selectedFile;
   private JPanel centerPanel;
   private JSplitPane splitPane;
   private Project project;

   public KnownFilesChooser(Project project, Set knownVirtulFilesSet) {
     super(project, false);
     this.project = project;
     init(knownVirtulFilesSet);
   }



   public void init(Set knownVirtulFilesSet) {
     getContentPane().setLayout(new BorderLayout());
     this.centerPanel = new JPanel(new BorderLayout(5, 5));
     getContentPane().add(this.centerPanel, "Center");

     UMLFileFilter fileFilter = new UMLFileFilter(UMLFileFilter.DIRS_AND_SUML);
     FileTreeNode root = new FileTreeNode(fileFilter);
     this.tree = new JTree(root);
     this.tree.setCellRenderer(new UMLFileTreeCellRenderer());
     this.tree.getSelectionModel().setSelectionMode(1);
     this.tree.setRootVisible(false);
     this.tree.setShowsRootHandles(true);

     this.tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener()
         {
           public void valueChanged(TreeSelectionEvent e)
           {
             if (KnownFilesChooser.this.tree.getSelectionCount() > 0) {

               KnownFilesChooser.this.fileList.getSelectionModel().clearSelection();
               if (KnownFilesChooser.this.tree.getSelectionPath() == null) {
                 return;
               }


               File file = ((FileTreeNode)KnownFilesChooser.this.tree.getSelectionPath().getLastPathComponent()).getFile();
               if (!file.getName().endsWith(".suml")) {

                 KnownFilesChooser.this.selectedFile = null;
                 KnownFilesChooser.this.setOKActionEnabled(false);

                 return;
               }

               try {
                 KnownFilesChooser.this.selectedFile = VfsUtil.findFileByURL(file.toURI().toURL());
                 KnownFilesChooser.this.setOKActionEnabled((KnownFilesChooser.this.selectedFile != null));
               }
               catch (MalformedURLException ex) {

                 ex.printStackTrace();
               }
             }
           }
         });


     this.tree.addMouseListener(new MouseAdapter()
         {
           public void mouseClicked(MouseEvent e)
           {
             if (KnownFilesChooser.this.tree.getSelectionPath() == null) {
               return;
             }

             File file = ((FileTreeNode)KnownFilesChooser.this.tree.getSelectionPath().getLastPathComponent()).getFile();
             if (e.getClickCount() == 2 && file.getName().endsWith(".suml")) {

               try {

                 KnownFilesChooser.this.selectedFile = VfsUtil.findFileByURL(file.toURI().toURL());
                 KnownFilesChooser.this.close(0);
               }
               catch (MalformedURLException ex) {

                 ex.printStackTrace();
               }
             }
           }
         });




     FileListModel fileListModel = new FileListModel(knownVirtulFilesSet);
     this.fileList = new JList(fileListModel);
     this.fileList.setSelectionMode(0);

     JLabel longestLabel = new JLabel("");
     for (Iterator<VirtualFile> iterator = knownVirtulFilesSet.iterator(); iterator.hasNext(); ) {

       VirtualFile virtualFile = iterator.next();
       JLabel label = new JLabel(virtualFile.getName());
       label.setFont(this.fileList.getFont());
       if ((label.getPreferredSize()).width > (longestLabel.getPreferredSize()).width)
       {
         longestLabel = label;
       }
     }

     this.fileList.setCellRenderer(new FileListCellRenderer(longestLabel));

     this.fileList.addListSelectionListener(new ListSelectionListener()
         {
           public void valueChanged(ListSelectionEvent e)
           {
             if (KnownFilesChooser.this.fileList.getSelectedIndex() != -1) {

               KnownFilesChooser.this.tree.getSelectionModel().clearSelection();
               KnownFilesChooser.this.selectedFile = KnownFilesChooser.this.fileList.getSelectedValue();
               KnownFilesChooser.this.setOKActionEnabled(true);
             }
           }
         });

     this.fileList.addMouseListener(new MouseAdapter()
         {
           public void mouseClicked(MouseEvent e)
           {
             if (e.getClickCount() == 2 && KnownFilesChooser.this.fileList.getSelectedIndex() != -1) {

               KnownFilesChooser.this.selectedFile = KnownFilesChooser.this.fileList.getSelectedValue();
               KnownFilesChooser.this.close(0);
             }
           }
         });

     addPopupMenus();


     JScrollPane listScrollPane = new JScrollPane(this.fileList);
     JPanel listPanel = new JPanel(new BorderLayout());
     listPanel.add(listScrollPane, "Center");
     JLabel knownFielsLabel = new JLabel("Known Diagrams");
     knownFielsLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
     listPanel.add(knownFielsLabel, "North");

     this.splitPane = new JSplitPane(1, false, new JScrollPane(this.tree), listPanel);
     this.splitPane.setOneTouchExpandable(true);
     this.splitPane.setBorder((Border)null);


     try {
       UMLFileManager fileManager = UMLToolWindowPlugin.getUMLToolWindowPlugin(this.project).getUMLFileManager();
       String url = fileManager.getDefaultFileLocationURL();
       int index = url.indexOf("//");
       url = url.substring(index + 2);
       url = url.replace('/', File.separatorChar);
       File file = new File(url);

       TreePath tp = root.getTreePath(file);

       this.tree.expandPath(tp);
       this.tree.scrollPathToVisible(tp);
     }
     catch (Throwable ex) {}




     this.centerPanel.add(this.splitPane, "Center");

     init();
     setOKActionEnabled(false);
     setTitle("Choose a Diagram File");
   }



   private void addPopupMenus() {
     AnAction removeAction = new AnAction("Remove")
       {
         public void actionPerformed(AnActionEvent event)
         {
           if (KnownFilesChooser.this.fileList.getSelectedIndex() != -1) {

             VirtualFile vf = KnownFilesChooser.this.fileList.getSelectedValue();
             ((FileListModel)KnownFilesChooser.this.fileList.getModel()).removeVirtualFile(KnownFilesChooser.this.fileList.getSelectedIndex());
             UMLToolWindowPlugin.getUMLToolWindowPlugin(KnownFilesChooser.this.project).getUMLFileManager().removeURL(vf.getUrl());
             KnownFilesChooser.this.fileList.clearSelection();
             KnownFilesChooser.this.setOKActionEnabled(false);
           }
         }
       };

     DefaultActionGroup actionGroup = new DefaultActionGroup();

     actionGroup.add(removeAction);

     ActionPopupMenu actionPopupMenu = ActionManager.getInstance().createActionPopupMenu("Popup", (ActionGroup)actionGroup);

     final JPopupMenu popupMenu = actionPopupMenu.getComponent();

     this.fileList.addMouseListener(new MouseAdapter()
         {
           public void mouseClicked(MouseEvent e)
           {
             KnownFilesChooser.this.showPopupMenu(e, popupMenu);
           }



           public void mouseReleased(MouseEvent e) {
             KnownFilesChooser.this.showPopupMenu(e, popupMenu);
           }



           public void mousePressed(MouseEvent e) {
             KnownFilesChooser.this.showPopupMenu(e, popupMenu);
           }
         });
   }



   private void showPopupMenu(MouseEvent e, JPopupMenu popupMenu) {
     int index = this.fileList.locationToIndex(e.getPoint());
     if (index != -1) {

       this.fileList.setSelectedIndex(index);
     }
     else {

       this.fileList.clearSelection();
     }

     if (e.isPopupTrigger() && this.fileList.getSelectedIndex() != -1)
     {
       popupMenu.show(this.fileList, e.getX(), e.getY());
     }
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


