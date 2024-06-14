 package net.trustx.simpleuml.gef.graph;

 import java.util.Comparator;



 public class GraphSizeComparator
   implements Comparator
 {
   private boolean ascending;

   public GraphSizeComparator(boolean ascending) {
     this.ascending = ascending;
   }



   public int compare(Object o1, Object o2) {
     if (o1 instanceof Graph && o2 instanceof Graph)
     {
       return compare((Graph)o1, (Graph)o2);
     }
     return 0;
   }



   public int compare(Graph g1, Graph g2) {
     int g1Size = g1.sizeVertices();
     int g2Size = g2.sizeVertices();
     if (g1Size < g2Size)
     {
       return this.ascending ? -1 : 1;
     }
     if (g1Size > g2Size)
     {
       return this.ascending ? 1 : -1;
     }


     return 0;
   }
 }


