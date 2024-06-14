 package net.trustx.simpleuml.gef.graph;
 
 import net.trustx.simpleuml.gef.Figure;
 
 

 
 
 public class FigureVertex
   extends Vertex
 {
   private Figure figure;
   
   public FigureVertex(Figure figure) {
     this.figure = figure;
   }
 
 
   
   public String toString() {
     return this.figure.toString();
   }
 
 
   
   public boolean equals(Object o) {
     if (!(o instanceof FigureVertex)) {
       return false;
     }
     FigureVertex vertex = (FigureVertex)o;
     
     return this.figure.equals(vertex.figure);
   }
 
 
 
 
   
   public int hashCode() {
     int result = this.figure.hashCode();
     return result;
   }
 
 
   
   public int getWidth() {
     return this.figure.getWidth();
   }
 
 
   
   public int getHeight() {
     return this.figure.getHeight();
   }
 
 
   
   public void setPosX(int x) {
     this.figure.setPosX(x);
   }
 
 
   
   public void setPosY(int y) {
     this.figure.setPosY(y);
   }
 
 
   
   public int getOrderID() {
     return this.figure.toString().hashCode() % 100;
   }
 
 
   
   public Figure getFigure() {
     return this.figure;
   }
 }


