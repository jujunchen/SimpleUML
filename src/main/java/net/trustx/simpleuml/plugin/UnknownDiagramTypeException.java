 package net.trustx.simpleuml.plugin;
























 public class UnknownDiagramTypeException
   extends Exception
 {
   private String type;

   public UnknownDiagramTypeException(String message, String type) {
     super(message);
     this.type = type;
   }



   public String getType() {
     return this.type;
   }
 }


