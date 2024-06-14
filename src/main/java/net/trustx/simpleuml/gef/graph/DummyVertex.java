 package net.trustx.simpleuml.gef.graph;
 
 
 

 
 
 public class DummyVertex
   extends Vertex
 {
   private int width = 1;
   private int height = 1;
   
   private Object obj;
 
   
   public DummyVertex(Object obj) {
     this.obj = obj;
   }
 
 
   
   public int getWidth() {
     return this.height;
   }
 
 
   
   public int getHeight() {
     return this.width;
   }
 
 
 
   
   public void setPosX(int x) {}
 
 
 
   
   public void setPosY(int y) {}
 
 
   
   public int getOrderID() {
     return 0;
   }
 
 
   
   public String toString() {
     return this.obj.toString();
   }
 }


