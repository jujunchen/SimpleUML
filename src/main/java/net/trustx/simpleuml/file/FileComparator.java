 package net.trustx.simpleuml.file;

 import java.io.File;
 import java.util.Comparator;





 public class FileComparator
   implements Comparator
 {
   public int compare(Object o1, Object o2) {
     File f1 = (File)o1;
     File f2 = (File)o2;

     boolean f1isDirectory = f1.isDirectory();
     boolean f2isDirectory = f2.isDirectory();

     if (f1isDirectory == f2isDirectory)
     {
       return f1.compareTo(f2);
     }


     if (f1isDirectory)
     {
       return -1;
     }


     return 1;
   }
 }


