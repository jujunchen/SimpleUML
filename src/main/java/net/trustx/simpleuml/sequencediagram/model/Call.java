 package net.trustx.simpleuml.sequencediagram.model;

 import com.intellij.psi.PsiElement;
 import com.intellij.psi.PsiMethod;






















 public class Call
   extends Link
 {
   public Call(ObjectInfo from, ObjectInfo to, String name) {
     super(from, to, name);
   }



   public Call(ObjectInfo from, ObjectInfo to, PsiMethod method) {
     super(from, to, method.getName());
     setPsiElement((PsiElement)method.getNameIdentifier());
   }



   public String toString() {
     return "Calling " + this.name + " on " + this.to + " from " + this.from;
   }



   public void delete() {
     this.deleted = true;
   }
 }


