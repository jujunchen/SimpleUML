 package net.trustx.simpleuml.sequencediagram.model;
 
 import java.util.EventObject;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class ModelTextEvent
   extends EventObject
 {
   private String text;
   
   ModelTextEvent(Object source, String text) {
     super(source);
     this.text = text;
   }
 
   
   public String getText() {
     return this.text;
   }
 }


