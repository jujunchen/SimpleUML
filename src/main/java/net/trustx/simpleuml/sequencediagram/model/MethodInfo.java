 package net.trustx.simpleuml.sequencediagram.model;






















 public class MethodInfo
 {
   protected ObjectInfo obj;
   protected String method;
   protected int startSeq;
   protected int endSeq;

   public MethodInfo(ObjectInfo obj, String method, int startSeq, int endSeq) {
     this.obj = obj;
     this.method = method;
     this.startSeq = startSeq;
     this.endSeq = endSeq;

     obj.addMethod(this);
   }


   public int getStartSeq() {
     return this.startSeq;
   }


   public int getEndSeq() {
     return this.endSeq;
   }


   public String getName() {
     return this.method;
   }


   public String toString() {
     return "Method " + this.method + " on " + this.obj + " from " + this.startSeq + " to " + this.endSeq;
   }
 }


