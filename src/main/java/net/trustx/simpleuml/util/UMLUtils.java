 package net.trustx.simpleuml.util;
 
 import com.intellij.openapi.project.Project;
 import com.intellij.psi.JavaDirectoryService;
 import com.intellij.psi.PsiClass;
 import com.intellij.psi.PsiDirectory;
 import com.intellij.psi.PsiElement;
 import com.intellij.psi.PsiFile;
 import com.intellij.psi.PsiJavaCodeReferenceElement;
 import com.intellij.psi.PsiModifierList;
 import com.intellij.psi.PsiPackage;
 import com.intellij.psi.PsiReferenceList;
 import com.intellij.psi.util.PsiTreeUtil;
 import java.awt.Component;
 import java.awt.event.MouseAdapter;
 import java.awt.event.MouseEvent;
 import java.awt.event.MouseMotionAdapter;
 import javax.swing.SwingUtilities;
 import net.trustx.simpleuml.components.InternalErrorDialog;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class UMLUtils
   implements UMLConstants
 {
   public static void showInternalErrorMessageDialog(Project project, String title, String message, Throwable throwable) {
     InternalErrorDialog dialog = new InternalErrorDialog(project, title, message, throwable);
     dialog.setSize(400, 500);
     dialog.setModal(true);
     dialog.show();
   }
 
 
   
   public static void showInternalErrorMessageDialog(Project project, String title, String message, Throwable[] throwables) {
     InternalErrorDialog dialog = new InternalErrorDialog(project, title, message, throwables);
     dialog.setSize(400, 500);
     dialog.setModal(true);
     dialog.show();
   }
 
 
   
   public static String getUMLModifierString(PsiModifierList modifierList, boolean longVersion) {
     StringBuffer sb = new StringBuffer();
     if (modifierList.hasModifierProperty("public")) {
       
       sb.append("+ ");
     }
     else if (modifierList.hasModifierProperty("protected")) {
       
       sb.append("# ");
     }
     else if (modifierList.hasModifierProperty("packageLocal")) {
       
       sb.append("~ ");
     }
     else if (modifierList.hasModifierProperty("private")) {
       
       sb.append("- ");
     } 
     
     if (longVersion) {
       
       if (modifierList.hasModifierProperty("final"))
         sb.append("final "); 
       if (modifierList.hasModifierProperty("transient"))
         sb.append("transient "); 
       if (modifierList.hasModifierProperty("volatile"))
         sb.append("volatile "); 
       if (modifierList.hasModifierProperty("native"))
         sb.append("native "); 
       if (modifierList.hasModifierProperty("synchronized")) {
         sb.append("synchronized ");
       }
       if (modifierList.hasModifierProperty("strictfp")) {
         sb.append("strictfp ");
       }
     } 
     return sb.toString();
   }
 
 
   
   public static String getNiceClassName(PsiClass psiClass) {
     StringBuffer sb = new StringBuffer();
     sb.append(".");
     sb.append(psiClass.getName());
     while (psiClass.getParent() instanceof PsiClass) {
       
       psiClass = (PsiClass)psiClass.getParent();
       sb.insert(0, psiClass.getName());
       sb.insert(0, ".");
     } 
     return sb.deleteCharAt(0).toString();
   }
 
 
   
   public static String stripFileType(String string) {
     if (string != null && string.endsWith(".suml"))
     {
       string = string.substring(0, string.lastIndexOf(".suml"));
     }
     
     return string;
   }
 
 
   
   public static String getWholePath(String folderURL, String name) {
     return folderURL + "/" + name;
   }
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
   
   public static PsiClass findClass(PsiElement element) {
     PsiClass psiClass = (element instanceof PsiClass) ? (PsiClass)element : (PsiClass)PsiTreeUtil.getParentOfType(element, PsiClass.class);
 
     
     if (psiClass instanceof com.intellij.psi.PsiAnonymousClass)
       return findClass(psiClass.getParent()); 
     return psiClass;
   }
 
 
 
 
 
 
 
 
   
   public static PsiJavaCodeReferenceElement[] getReferenceElements(PsiReferenceList psiReferenceList) {
     if (psiReferenceList == null)
     {
       return new PsiJavaCodeReferenceElement[0];
     }
 
     
     return psiReferenceList.getReferenceElements();
   }
 
 
 
   
   public static String getCompressedPackageName(String qualifiedPackageName, int level) {
     if (level <= 0 || "".equals(qualifiedPackageName) || qualifiedPackageName == null)
     {
       return qualifiedPackageName;
     }
     
     StringBuffer cN = new StringBuffer();
     
     int lastDot = -1;
     
     for (int i = 0; i < level; i++) {
       
       char firstLetter = qualifiedPackageName.charAt(lastDot + 1);
       cN.append(firstLetter);
       cN.append('.');
       int newLastDot = qualifiedPackageName.indexOf(".", lastDot + 1);
       if (newLastDot == -1) {
         
         cN.deleteCharAt(cN.length() - 1);
         return cN.toString();
       } 
       if (i + 1 == level)
       {
         cN.append(qualifiedPackageName.substring(newLastDot + 1));
       }
       lastDot = newLastDot;
     } 
     
     return cN.toString();
   }
 
 
 
   
   public static void main(String[] args) {
     int i;
     for (i = 0; i < 5; i++)
     {
       System.out.println(getCompressedPackageName("net.trustx.simpleuml", i));
     }
     
     for (i = 0; i < 5; i++)
     {
       System.out.println(getCompressedPackageName("a.b.simpleuml", i));
     }
     
     for (i = 0; i < 5; i++)
     {
       System.out.println(getCompressedPackageName("a.b.c", i));
     }
     
     for (i = 0; i < 5; i++)
     {
       System.out.println(getCompressedPackageName("", i));
     }
   }
 
 
   
   public static void dispatchMouseToParent(final Component current, final Component parent) {
     current.addMouseMotionListener(new MouseMotionAdapter()
         {
           public void mouseDragged(MouseEvent e)
           {
             parent.dispatchEvent(SwingUtilities.convertMouseEvent(current, e, parent));
           }
 
 
           
           public void mouseMoved(MouseEvent e) {
             parent.dispatchEvent(SwingUtilities.convertMouseEvent(current, e, parent));
           }
         });
     
     current.addMouseListener(new MouseAdapter()
         {
           public void mouseClicked(MouseEvent e)
           {
             parent.dispatchEvent(SwingUtilities.convertMouseEvent(current, e, parent));
           }
 
 
           
           public void mousePressed(MouseEvent e) {
             parent.dispatchEvent(SwingUtilities.convertMouseEvent(current, e, parent));
           }
 
 
           
           public void mouseReleased(MouseEvent e) {
             parent.dispatchEvent(SwingUtilities.convertMouseEvent(current, e, parent));
           }
 
 
           
           public void mouseEntered(MouseEvent e) {
             parent.dispatchEvent(SwingUtilities.convertMouseEvent(current, e, parent));
           }
 
 
           
           public void mouseExited(MouseEvent e) {
             parent.dispatchEvent(SwingUtilities.convertMouseEvent(current, e, parent));
           }
         });
   }
 
 
   
   public static void dispatchMouseToParent(final Component current) {
     current.addMouseMotionListener(new MouseMotionAdapter()
         {
           public void mouseDragged(MouseEvent e)
           {
             current.getParent().dispatchEvent(SwingUtilities.convertMouseEvent(current, e, current.getParent()));
           }
 
 
           
           public void mouseMoved(MouseEvent e) {
             current.getParent().dispatchEvent(SwingUtilities.convertMouseEvent(current, e, current.getParent()));
           }
         });
     
     current.addMouseListener(new MouseAdapter()
         {
           public void mouseClicked(MouseEvent e)
           {
             current.getParent().dispatchEvent(SwingUtilities.convertMouseEvent(current, e, current.getParent()));
           }
 
 
           
           public void mousePressed(MouseEvent e) {
             current.getParent().dispatchEvent(SwingUtilities.convertMouseEvent(current, e, current.getParent()));
           }
 
 
           
           public void mouseReleased(MouseEvent e) {
             current.getParent().dispatchEvent(SwingUtilities.convertMouseEvent(current, e, current.getParent()));
           }
 
 
           
           public void mouseEntered(MouseEvent e) {
             current.getParent().dispatchEvent(SwingUtilities.convertMouseEvent(current, e, current.getParent()));
           }
 
 
           
           public void mouseExited(MouseEvent e) {
             current.getParent().dispatchEvent(SwingUtilities.convertMouseEvent(current, e, current.getParent()));
           }
         });
   }
 
 
   
   public static PsiPackage getPackage(PsiClass psiClass) {
     PsiFile containingFile = psiClass.getContainingFile();
     if (containingFile == null) return null;
     
     PsiDirectory containingDirectory = containingFile.getContainingDirectory();
     if (containingDirectory == null) return null;
     
     return JavaDirectoryService.getInstance().getPackage(containingDirectory);
   }
 }


