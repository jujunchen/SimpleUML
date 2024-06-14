 package net.trustx.simpleuml.file;
 
 import java.io.File;
 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.Collections;
 import java.util.Enumeration;
 import java.util.Iterator;
 import java.util.LinkedList;
 import java.util.List;
 import javax.swing.filechooser.FileSystemView;
 import javax.swing.tree.TreeNode;
 import javax.swing.tree.TreePath;
 
 

 
 public class FileTreeNode
   implements TreeNode
 {
   private File file;
   private boolean traversable;
   private FileTreeNode parent;
   private File[] children;
   private FileTreeNode[] childNodes;
   private String name;
   private UMLFileFilter fileFilter;
   
   public FileTreeNode(UMLFileFilter fileFilter) {
     this.fileFilter = fileFilter;
     
     File[] files = File.listRoots();
     ArrayList<File> list = new ArrayList();
     for (int i = 0; i < files.length; i++) {
       
       File file = files[i];
       if (!FileSystemView.getFileSystemView().isFloppyDrive(file))
       {
         list.add(file);
       }
     } 
     this.children = list.<File>toArray(new File[list.size()]);
     sortChildren();
     this.name = "root";
     this.traversable = false;
   }
 
 
   
   public FileTreeNode(File file, FileTreeNode parent, UMLFileFilter fileFilter) {
     this.file = file;
     this.parent = parent;
     this.fileFilter = fileFilter;
     
     this.name = FileSystemView.getFileSystemView().getSystemDisplayName(file);
     if (this.name == null || this.name.trim().length() == 0)
     {
       this.name = file.toString();
     }
     
     this.traversable = FileSystemView.getFileSystemView().isTraversable(file).booleanValue();
   }
 
 
   
   private void sortChildren() {
     Arrays.sort(this.children, new FileComparator());
   }
 
 
   
   public boolean getAllowsChildren() {
     return this.traversable;
   }
 
 
   
   public boolean isLeaf() {
     if (this.file == null)
     {
       return false;
     }
     return !this.traversable;
   }
 
 
   
   public Enumeration children() {
     initChildren();
     return Collections.enumeration(Arrays.asList((Object[])this.childNodes));
   }
 
 
   
   public int getChildCount() {
     initChildren();
     return this.children.length;
   }
 
 
   
   public TreeNode getParent() {
     return this.parent;
   }
 
 
   
   public int getIndex(TreeNode node) {
     for (int i = 0; i < this.childNodes.length; i++) {
       
       FileTreeNode childNode = this.childNodes[i];
       if (childNode.equals(this))
       {
         return i;
       }
     } 
     return -1;
   }
 
 
   
   public TreeNode getChildAt(int index) {
     initChildren();
     return this.childNodes[index];
   }
 
 
   
   public boolean equals(Object o) {
     if (this == o)
       return true; 
     if (!(o instanceof FileTreeNode)) {
       return false;
     }
     FileTreeNode fileTreeNode = (FileTreeNode)o;
     
     return this.file.equals(fileTreeNode.file);
   }
 
 
 
   
   public int hashCode() {
     if (this.file != null)
     {
       return this.file.hashCode();
     }
 
     
     return 0;
   }
 
 
 
   
   public String toString() {
     return this.name;
   }
 
 
   
   public String getName() {
     return this.name;
   }
 
 
   
   public void setName(String name) {
     this.name = name;
   }
 
 
   
   private void initChildren() {
     if (this.childNodes == null) {
       
       if (this.children == null) {
         
         this.children = this.file.listFiles(this.fileFilter);
         if (this.children == null)
         {
           this.children = new File[0];
         }
         sortChildren();
       } 
       
       this.childNodes = new FileTreeNode[this.children.length];
       for (int i = 0; i < this.children.length; i++) {
         
         File child = this.children[i];
         this.childNodes[i] = new FileTreeNode(child, this, this.fileFilter);
       } 
     } 
   }
 
 
   
   public File getFile() {
     return this.file;
   }
 
 
   
   public TreePath getTreePath(File fileToFind) {
     List files = breakToComponents(fileToFind);
     LinkedList<FileTreeNode> nodeList = new LinkedList();
     FileTreeNode root = this;
     nodeList.add(root);
     for (Iterator<File> iterator = files.iterator(); iterator.hasNext(); ) {
       
       File file = iterator.next();
       int size = root.getChildCount();
       for (int n = 0; n < size; n++) {
         
         FileTreeNode newRoot = (FileTreeNode)root.getChildAt(n);
         if (newRoot.getFile().equals(file)) {
           
           nodeList.add(newRoot);
           root = newRoot;
           break;
         } 
       } 
     } 
     TreePath tp = new TreePath(nodeList.toArray());
     return tp;
   }
 
 
   
   private List breakToComponents(File file) {
     LinkedList<File> list = new LinkedList();
     list.add(file);
     while (file.getParentFile() != null) {
       
       list.addFirst(file.getParentFile());
       file = file.getParentFile();
     } 
     return list;
   }
 }


