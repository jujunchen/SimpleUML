 package net.trustx.simpleuml.classdiagram.util;
 
 import com.intellij.psi.PsiClass;
 import com.intellij.psi.PsiField;
 import com.intellij.psi.PsiMethod;
 import com.intellij.psi.PsiModifierList;
 import java.util.ArrayList;
 import net.trustx.simpleuml.classdiagram.configuration.ClassDiagramSettings;
 

 public class PsiComponentUtils
 {
   public static final int ACCESS_PUBLIC = 1;
   public static final int ACCESS_PRIVATE = 2;
   public static final int ACCESS_PACKAGE = 4096;
   public static final int ACCESS_PROTECTED = 4;
   public static final int ACCESS_STATIC = 8;
   public static final int ACCESS_FINAL = 16;
   public static final int ACCESS_SYNCHRONIZED = 32;
   public static final int ACCESS_VOLATILE = 64;
   public static final int ACCESS_TRANSIENT = 128;
   public static final int ACCESS_NATIVE = 256;
   public static final int ACCESS_INTERFACE = 512;
   public static final int ACCESS_ABSTRACT = 1024;
   public static final int ACCESS_STRICT = 2048;
   
   public static PsiField[] getVisibleFields(PsiClass psiClass, ClassDiagramSettings diagramSettings) {
     PsiField[] fields = psiClass.getFields();
     ArrayList<PsiField> fieldsList = new ArrayList();
     for (int i = 0; i < fields.length; i++) {
       
       PsiField field = fields[i];
       if (isVisibleField(field, diagramSettings))
       {
         fieldsList.add(field);
       }
     } 
     
     return fieldsList.<PsiField>toArray(new PsiField[fieldsList.size()]);
   }
 
 
   
   public static boolean hasVisibleFields(PsiClass psiClass, ClassDiagramSettings diagramSettings) {
     PsiField[] fields = psiClass.getFields();
     for (int i = 0; i < fields.length; i++) {
       
       PsiField field = fields[i];
       if (isVisibleField(field, diagramSettings))
       {
         return true;
       }
     } 
     
     return false;
   }
 
 
   
   private static boolean isVisibleField(PsiField psiField, ClassDiagramSettings diagramSettings) {
     return isVisible(psiField.getModifierList(), diagramSettings.getHideFieldList(), diagramSettings.getShowFieldList());
   }
 
 
   
   public static boolean hasVisibleMethods(PsiClass psiClass, ClassDiagramSettings diagramSettings) {
     PsiMethod[] methods = psiClass.getMethods();
     for (int i = 0; i < methods.length; i++) {
       
       PsiMethod method = methods[i];
       if (isVisibleMethod(method, diagramSettings))
       {
         return true;
       }
     } 
     
     return false;
   }
 
 
   
   public static PsiMethod[] getVisibleMethods(PsiClass psiClass, ClassDiagramSettings diagramSettings) {
     PsiMethod[] methods = psiClass.getMethods();
     ArrayList<PsiMethod> methodsList = new ArrayList();
     for (int i = 0; i < methods.length; i++) {
       
       PsiMethod method = methods[i];
       if (isVisibleMethod(method, diagramSettings))
       {
         methodsList.add(method);
       }
     } 
     
     return methodsList.<PsiMethod>toArray(new PsiMethod[methodsList.size()]);
   }
 
 
   
   private static boolean isVisibleMethod(PsiMethod psiMethod, ClassDiagramSettings diagramSettings) {
     if (psiMethod.isConstructor())
     {
       return false;
     }
     
     return isVisible(psiMethod.getModifierList(), diagramSettings.getHideMethodList(), diagramSettings.getShowMethodList());
   }
 
 
   
   public static boolean hasVisibleConstructors(PsiClass psiClass, ClassDiagramSettings diagramSettings) {
     PsiMethod[] constructors = psiClass.getConstructors();
     for (int i = 0; i < constructors.length; i++) {
       
       PsiMethod constructor = constructors[i];
       if (isVisibleConstructor(constructor, diagramSettings))
       {
         return true;
       }
     } 
     
     return false;
   }
 
 
   
   public static PsiMethod[] getVisibleConstructors(PsiClass psiClass, ClassDiagramSettings diagramSettings) {
     PsiMethod[] constructors = psiClass.getConstructors();
     ArrayList<PsiMethod> constructorList = new ArrayList();
     for (int i = 0; i < constructors.length; i++) {
       
       PsiMethod constructor = constructors[i];
       if (isVisibleConstructor(constructor, diagramSettings))
       {
         constructorList.add(constructor);
       }
     } 
     
     return constructorList.<PsiMethod>toArray(new PsiMethod[constructorList.size()]);
   }
 
 
   
   private static boolean isVisibleConstructor(PsiMethod psiConstructor, ClassDiagramSettings diagramSettings) {
     return isVisible(psiConstructor.getModifierList(), diagramSettings.getHideConstructorList(), diagramSettings.getShowConstructorList());
   }
 
 
   
   private static boolean isVisible(PsiModifierList psiModifierList, int[] hideFilterList, int[] showFilterList) {
     int mod = convertModifierListToInt(psiModifierList);
     boolean visible = true; int i;
     for (i = 0; i < hideFilterList.length; i++) {
       
       int hideFilter = hideFilterList[i];
       if ((mod & hideFilter) == hideFilter) {
         
         visible = false;
         
         break;
       } 
     } 
     if (visible)
     {
       return visible;
     }
     
     for (i = 0; i < showFilterList.length; i++) {
       
       int showFilter = showFilterList[i];
       if ((mod & showFilter) == showFilter)
       {
         return true;
       }
     } 
     
     return visible;
   }
 
 
   
   private static int convertModifierListToInt(PsiModifierList modifierList) {
     int mod = 0;
     if (modifierList.hasModifierProperty("private")) {
       
       mod |= 0x2;
     }
     else if (modifierList.hasModifierProperty("protected")) {
       
       mod |= 0x4;
     }
     else if (modifierList.hasModifierProperty("public")) {
       
       mod |= 0x1;
     }
     else if (modifierList.hasModifierProperty("packageLocal")) {
       
       mod |= 0x1000;
     } 
     
     if (modifierList.hasModifierProperty("static"))
     {
       mod |= 0x8;
     }
     if (modifierList.hasModifierProperty("final"))
     {
       mod |= 0x10;
     }
     if (modifierList.hasModifierProperty("synchronized"))
     {
       mod |= 0x20;
     }
     if (modifierList.hasModifierProperty("volatile"))
     {
       mod |= 0x40;
     }
     if (modifierList.hasModifierProperty("transient"))
     {
       mod |= 0x80;
     }
     if (modifierList.hasModifierProperty("native"))
     {
       mod |= 0x100;
     }
     if (modifierList.hasModifierProperty("abstract"))
     {
       mod |= 0x400;
     }
     if (modifierList.hasModifierProperty("strictfp"))
     {
       mod |= 0x800;
     }
     
     return mod;
   }
 
 
   
   public static String convertModifierIntToString(int mod) {
     StringBuffer sb = new StringBuffer();
 
     
     if ((mod & 0x1) != 0) {
       sb.append("public ");
     } else if ((mod & 0x2) != 0) {
       sb.append("private ");
     } else if ((mod & 0x4) != 0) {
       sb.append("protected ");
     } else if ((mod & 0x1000) != 0) {
       sb.append("package ");
     } 
     if ((mod & 0x400) != 0)
       sb.append("abstract "); 
     if ((mod & 0x8) != 0)
       sb.append("static "); 
     if ((mod & 0x10) != 0)
       sb.append("final "); 
     if ((mod & 0x80) != 0)
       sb.append("transient "); 
     if ((mod & 0x40) != 0)
       sb.append("volatile "); 
     if ((mod & 0x100) != 0)
       sb.append("native "); 
     if ((mod & 0x20) != 0) {
       sb.append("synchronized ");
     }
     if ((mod & 0x200) != 0) {
       sb.append("interface ");
     }
     if ((mod & 0x800) != 0)
       sb.append("strictfp "); 
     int len;
     if ((len = sb.length()) > 0)
       return sb.toString().substring(0, len - 1); 
     return "";
   }
 }


