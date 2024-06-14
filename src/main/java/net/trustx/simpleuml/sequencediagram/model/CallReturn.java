 package net.trustx.simpleuml.sequencediagram.model;

 import com.intellij.psi.PsiElement;























 public class CallReturn
   extends Link
 {
   public CallReturn(ObjectInfo from, ObjectInfo to) {
     super(from, to, "void");
   }


   public CallReturn(ObjectInfo from, ObjectInfo to, PsiElement calledFrom) {
     this(from, to);
     setPsiElement(calledFrom);
   }


   public CallReturn(ObjectInfo from, ObjectInfo to, String name) {
     super(from, to, name);
   }


   public String toString() {
     return "Returning " + this.name + " on " + this.from + " to " + this.to;
   }
 }


