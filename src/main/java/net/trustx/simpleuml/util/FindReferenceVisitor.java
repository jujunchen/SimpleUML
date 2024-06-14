 package net.trustx.simpleuml.util;



import com.intellij.psi.*;

import java.util.HashSet;
import java.util.Iterator;












 public class FindReferenceVisitor
   extends JavaElementVisitor
 {
   private HashSet list = new HashSet();
   private HashSet elementSet = new HashSet();




   public void visitElement(PsiElement element) {
     boolean wasNew = this.elementSet.add(element);
     if (!wasNew) {
       return;
     }


     PsiElement child = element.getFirstChild();

     while (child != null) {

       child.accept((PsiElementVisitor)this);
       child = child.getNextSibling();
     }
   }



   public void visitReferenceExpression(PsiReferenceExpression expression) {
     visitExpression((PsiExpression)expression);
     visitReferenceElement((PsiJavaCodeReferenceElement)expression);
   }



   public void visitClass(PsiClass aClass) {
     super.visitClass(aClass);
   }



   public void visitField(PsiField field) {
     addType((PsiVariable)field);
     super.visitField(field);
   }



   public void visitLocalVariable(PsiLocalVariable variable) {
     addType((PsiVariable)variable);
     super.visitLocalVariable(variable);
   }



   public void visitMethodCallExpression(PsiMethodCallExpression expression) {
     PsiMethod psiMethod = expression.resolveMethod();
     if (psiMethod != null)
     {
       this.list.add(psiMethod.getContainingClass());
     }
     super.visitMethodCallExpression(expression);
   }



   public void visitParameter(PsiParameter parameter) {
     addType((PsiVariable)parameter);
     super.visitParameter(parameter);
   }



   private void addType(PsiVariable field) {
     PsiType type = field.getType();
     if (type instanceof com.intellij.psi.PsiArrayType || type instanceof com.intellij.psi.PsiPrimitiveType) {
       return;
     }

     PsiClass psiClass = ((PsiClassType)type).resolve();
     if (psiClass != null && psiClass instanceof PsiClass)
     {
       this.list.add(psiClass);
     }
   }



   private void addType(PsiType type) {
     if (type == null) {
       return;
     }

     if (type instanceof com.intellij.psi.PsiArrayType || type instanceof com.intellij.psi.PsiPrimitiveType) {
       return;
     }

     PsiClass psiClass = ((PsiClassType)type).resolve();
     if (psiClass != null && psiClass instanceof PsiClass)
     {
       this.list.add(psiClass);
     }
   }



   private void addClass(PsiClass psiClass) {
     if (psiClass != null)
     {
       this.list.add(psiClass);
     }
   }



   public void visitInstanceOfExpression(PsiInstanceOfExpression expression) {
     PsiTypeElement element = expression.getCheckType();
     if (element == null)
       return;
     addType(element.getType());
     super.visitInstanceOfExpression(expression);
   }



   public void visitNewExpression(PsiNewExpression expression) {
     PsiJavaCodeReferenceElement referenceElement = expression.getClassReference();
     if (referenceElement != null) {

       PsiElement element = referenceElement.resolve();
       if (element != null && element instanceof PsiClass)
       {
         addClass((PsiClass)element);
       }
     }
     super.visitNewExpression(expression);
   }



   public void visitMethod(PsiMethod method) {
     if (method == null)
       return;
     PsiTypeElement returnType = method.getReturnTypeElement();
     if (returnType == null)
       return;
     addType(returnType.getType());
     PsiJavaCodeReferenceElement[] elements = UMLUtils.getReferenceElements(method.getThrowsList());
     for (int i = 0; i < elements.length; i++) {

       PsiJavaCodeReferenceElement element = elements[i];
       PsiElement psiElement = element.resolve();
       if (psiElement instanceof PsiClass)
       {
         addClass((PsiClass)psiElement);
       }
     }

     super.visitMethod(method);
   }



   public void visitThrowStatement(PsiThrowStatement statement) {
     PsiExpression exception = statement.getException();
     if (exception != null)
     {
       addType(exception.getType());
     }
     super.visitThrowStatement(statement);
   }



   public void printList() {
     for (Iterator<PsiClass> iterator = this.list.iterator(); iterator.hasNext(); ) {

       PsiClass psiClass = iterator.next();
       System.out.println("psiClass = " + psiClass);
     }
   }



   public HashSet getList() {
     return this.list;
   }
 }


