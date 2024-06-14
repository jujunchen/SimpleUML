 package net.trustx.simpleuml.gef.connector;
 
 import java.awt.Polygon;
 
 

 
 public class ConnectorDecoratorExtends
   extends ConnectorDecoratorEmpty
 {
   public ConnectorDecoratorExtends() {
     Polygon pointer = new Polygon();
     pointer.addPoint(0, 0);
     pointer.addPoint(6, 15);
     pointer.addPoint(-6, 15);
     setEndShape(pointer);
   }
 
 
   
   public int hashCode() {
     return getClass().getName().hashCode();
   }
 
 
   
   public int getType() {
     return 2;
   }
 
 
   
   public boolean equals(Object obj) {
     return (super.equals(obj) && obj instanceof ConnectorDecoratorExtends);
   }
 }


