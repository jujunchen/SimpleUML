 package net.trustx.simpleuml.gef.connector;

 import java.awt.BasicStroke;
 import java.awt.Polygon;
 import java.awt.Stroke;





 public class ConnectorDecoratorImplements
   extends ConnectorDecoratorEmpty
 {
   public ConnectorDecoratorImplements() {
     Polygon pointer = new Polygon();
     pointer.addPoint(0, 0);
     pointer.addPoint(6, 15);
     pointer.addPoint(-6, 15);
     setEndShape(pointer);

     Stroke stroke = new BasicStroke(1.0F, 0, 0, 1.0F, new float[] { 5.0F, 5.0F }, 5.0F);
     setLineStyle(stroke);
   }



   public int hashCode() {
     return getClass().getName().hashCode();
   }



   public int getType() {
     return 1;
   }



   public boolean equals(Object obj) {
     return (super.equals(obj) && obj instanceof ConnectorDecoratorImplements);
   }
 }


