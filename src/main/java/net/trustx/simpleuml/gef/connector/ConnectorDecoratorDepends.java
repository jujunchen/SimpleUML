 package net.trustx.simpleuml.gef.connector;
 
 import java.awt.BasicStroke;
 import java.awt.Stroke;
 import java.awt.geom.GeneralPath;
 
 

 
 public class ConnectorDecoratorDepends
   extends ConnectorDecoratorEmpty
 {
   public ConnectorDecoratorDepends() {
     GeneralPath generalPath = new GeneralPath();
 
     
     generalPath.moveTo(0.0F, 0.0F);
     generalPath.lineTo(4.0F, 10.0F);
     generalPath.moveTo(0.0F, 0.0F);
     generalPath.lineTo(-4.0F, 10.0F);
     setEndShape(generalPath);
     
     Stroke stroke = new BasicStroke(1.0F, 0, 0, 1.0F, new float[] { 5.0F, 5.0F }, 5.0F);
     setLineStyle(stroke);
   }
 
 
   
   public int hashCode() {
     return getClass().getName().hashCode();
   }
 
 
   
   public int getType() {
     return 16;
   }
 
 
   
   public boolean equals(Object obj) {
     return (super.equals(obj) && obj instanceof ConnectorDecoratorDepends);
   }
 }


