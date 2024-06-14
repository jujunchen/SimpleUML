 package net.trustx.simpleuml.file;

 import com.intellij.openapi.ui.Messages;





 public class FileUtils
 {
   public static boolean isValidDiagramName(String diagramName, boolean interactive) {
     if (diagramName.trim().length() == 0) {

       if (interactive)
       {
         Messages.showMessageDialog("Name is not valid", "Error", Messages.getErrorIcon());
       }
       return false;
     }

     char[] test = diagramName.toCharArray();
     for (int i = 0; i < test.length; i++) {

       char c = test[i];
       if (!Character.isLetterOrDigit(c) && "_.-$ #".indexOf(c) == -1) {

         if (interactive)
         {
           Messages.showMessageDialog("Name is not valid", "Error", Messages.getErrorIcon());
         }
         return false;
       }
     }

     return true;
   }
 }


