 package net.trustx.simpleuml.file;
 
 import java.io.File;
 import java.io.FileFilter;
 import net.trustx.simpleuml.util.UMLConstants;
 

 
 
 public class UMLFileFilter
   implements FileFilter, UMLConstants
 {
   public static final String[] DIRS_ONLY = new String[0];
   public static final String[] DIRS_AND_SUML = new String[] { ".suml" };
 
   
   private String[] allowedFileTypes;
 
   
   public UMLFileFilter(String[] allowedFileTypes) {
     this.allowedFileTypes = allowedFileTypes;
   }
 
 
   
   public boolean accept(File file) {
     String name = file.getName();
     for (int i = 0; i < this.allowedFileTypes.length; i++) {
       
       String allowedFileType = this.allowedFileTypes[i];
       if (name.endsWith(allowedFileType))
       {
         return true;
       }
     } 
     return file.isDirectory();
   }
 }
