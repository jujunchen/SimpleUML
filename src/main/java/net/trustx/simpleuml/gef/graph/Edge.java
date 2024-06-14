 package net.trustx.simpleuml.gef.graph;
 
 

 
 public class Edge
 {
   private int edgeNumber;
   private Vertex fromVertex;
   private Vertex toVertex;
   
   public Edge(Vertex fromVertex, Vertex toVertex) {
     this.fromVertex = fromVertex;
     this.toVertex = toVertex;
     
     attachToVertices();
   }
 
 
   
   public void attachToVertices() {
     this.fromVertex.getIncidenceContainer().addOutIncidentEdge(this);
     this.toVertex.getIncidenceContainer().addInIncidentEdge(this);
   }
 
 
   
   public void detachFromVertices() {
     this.fromVertex.getIncidenceContainer().removeOutIncidentEdge(this);
     this.toVertex.getIncidenceContainer().removeInIncidentEdge(this);
   }
 
 
   
   public Vertex getFromVertex() {
     return this.fromVertex;
   }
 
 
   
   public void setFromVertex(Vertex fromVertex) {
     this.fromVertex = fromVertex;
   }
 
 
   
   public Vertex getToVertex() {
     return this.toVertex;
   }
 
 
   
   public void setToVertex(Vertex toVertex) {
     this.toVertex = toVertex;
   }
 
 
   
   public boolean equals(Object o) {
     if (!(o instanceof Edge)) {
       return false;
     }
     Edge edge = (Edge)o;
     
     if (!this.fromVertex.equals(edge.fromVertex))
       return false;
     return this.toVertex.equals(edge.toVertex);
   }
 
 
 
 
   
   public int hashCode() {
     int result = this.fromVertex.hashCode();
     result = 29 * result + this.toVertex.hashCode();
     return result;
   }
 
 
   
   public String toString() {
     return this.fromVertex + " --> " + this.toVertex;
   }
 
 
   
   public int getEdgeNumber() {
     return this.edgeNumber;
   }
 
 
   
   public void setEdgeNumber(int edgeNumber) {
     this.edgeNumber = edgeNumber;
   }
 }


