 package net.trustx.simpleuml.gef.connector;

 import java.awt.Shape;
 import java.awt.geom.Ellipse2D;
 import java.awt.geom.GeneralPath;





 public class ConnectorDecoratorInner
   extends ConnectorDecoratorEmpty
 {
   public ConnectorDecoratorInner() {
     Shape shape = new Ellipse2D.Float(-7.0F, 0.0F, 14.0F, 14.0F);
     GeneralPath generalPath = new GeneralPath(shape);

     generalPath.moveTo(0.0F, 0.5F);
     generalPath.lineTo(0.0F, 13.5F);

     generalPath.moveTo(-6.5F, 7.0F);
     generalPath.lineTo(6.5F, 7.0F);

     setEndShape(generalPath);
   }



   public int hashCode() {
     return getClass().getName().hashCode();
   }



   public int getType() {
     return 8;
   }



   public boolean equals(Object obj) {
     return (super.equals(obj) && obj instanceof ConnectorDecoratorInner);
   }
 }


