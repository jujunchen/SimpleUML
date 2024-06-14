 package net.trustx.simpleuml.file;
 
 import com.intellij.openapi.vfs.VirtualFile;
 import java.util.Arrays;
 import java.util.Collection;
 import java.util.Comparator;
 import javax.swing.AbstractListModel;
 
 
 

 
 public class FileListModel
   extends AbstractListModel
 {
   private Object[] objects;
   private Collection collection;
   
   public FileListModel(Collection collection) {
     this.collection = collection;
     fillModel();
   }
 
 
   
   private void fillModel() {
     this.objects = this.collection.toArray();
     Arrays.sort(this.objects, new Comparator()
         {
           public int compare(Object o1, Object o2)
           {
             String n1 = ((VirtualFile)o1).getName();
             String n2 = ((VirtualFile)o2).getName();
             
             return n1.compareToIgnoreCase(n2);
           }
         });
   }
 
 
   
   public int getSize() {
     return this.objects.length;
   }
 
 
   
   public Object getElementAt(int index) {
     return this.objects[index];
   }
 
 
   
   public void removeVirtualFile(int index) {
     this.collection.remove(this.objects[index]);
     fillModel();
     fireIntervalRemoved(this, index, index);
   }
 }


