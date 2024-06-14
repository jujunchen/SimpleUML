 package net.trustx.simpleuml.sequencediagram.util;
 
 import com.intellij.psi.PsiClass;
 import com.intellij.psi.PsiElement;
 import com.intellij.psi.PsiFile;
 import com.intellij.psi.PsiLocalVariable;
 import com.intellij.psi.PsiMethod;
 
 
 
 
 
 
 public class PsiClassUtil
 {
   public static String getClassName(PsiClass psiClass) {
     String name = null;
     if (psiClass.getName() != null) {
       name = psiClass.getName();
     } else {
       
       PsiClass psiClass1 = psiClass;
       String variableName = "unknown";
       String methodName = "unknown";
       String className = null;
       String fileName = "unknown";
       while (psiClass1.getParent() != null) {
         
         if (psiClass1.getParent() instanceof PsiLocalVariable) {
           variableName = ((PsiLocalVariable)psiClass1.getParent()).getName();
         } else if (psiClass1.getParent() instanceof PsiClass) {
           className = ((PsiClass)psiClass1.getParent()).getName();
         } else if (psiClass1.getParent() instanceof PsiMethod) {
           methodName = ((PsiMethod)psiClass1.getParent()).getName();
         } else if (psiClass1.getParent() instanceof PsiFile) {
           fileName = ((PsiFile)psiClass1.getParent()).getName();
         }  PsiElement psiElement = psiClass1.getParent();
       } 
       className = (className == null) ? fileName : className;
       name = "anon@" + className + "." + methodName + "():" + variableName;
     } 
     return name;
   }
 
   
   public static boolean equal(PsiClass psiClass, PsiClass psiClass1) {
     if (psiClass == null || psiClass1 == null) return false;
     
     String name1 = psiClass.getQualifiedName();
     String name2 = psiClass1.getQualifiedName();
     if (name1 == null && name2 == null) {
       return getClassName(psiClass).equals(getClassName(psiClass1));
     }
     
     if (name1 == null || name2 == null) return false;
     
     return name1.equals(name2);
   }
 }


