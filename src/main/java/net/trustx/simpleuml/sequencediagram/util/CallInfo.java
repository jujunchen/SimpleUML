 package net.trustx.simpleuml.sequencediagram.util;
 
 import com.intellij.psi.PsiElement;
 import net.trustx.simpleuml.sequencediagram.model.ObjectInfo;
 
 
 
 
 
 
 
 
 
 
 public class CallInfo
 {
   private ObjectInfo obj;
   private String method;
   private int startingSeq = -1;
   
   private PsiElement psiLocation;
   private static int nextID = 1;
   
   private int id;
   
   public int getStartingSeq() {
     return this.startingSeq;
   }
 
   
   CallInfo(ObjectInfo obj, String method, int startingSeq) {
     this.obj = obj;
     this.method = method;
     this.startingSeq = startingSeq;
   }
 
   
   CallInfo(ObjectInfo obj, String method, PsiElement calledFrom, int startingSeq) {
     this(obj, method, startingSeq);
     
     setPsiLocation(calledFrom);
     this.id = getNextID();
   }
 
   
   public void setPsiLocation(PsiElement psiLocation) {
     this.psiLocation = psiLocation;
   }
 
   
   private static int getNextID() {
     return nextID++;
   }
 
   
   ObjectInfo getObj() {
     return this.obj;
   }
 
   
   public String getMethod() {
     return this.method;
   }
 
   
   public PsiElement getPsiLocation() {
     return this.psiLocation;
   }
 
 
   
   int getStartingVerticalSeq() {
     return this.startingSeq;
   }
 
   
   public String toString() {
     return "Calling " + this.method + " on " + this.obj;
   }
 }


