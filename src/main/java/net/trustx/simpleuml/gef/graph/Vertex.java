 package net.trustx.simpleuml.gef.graph;
 
 
 
 

 
 
 public abstract class Vertex
 {
   public double x;
   public int xAlign;
   public int requestedXAlign;
   private IncidenceContainer incidenceContainer = new IncidenceContainer();
 
 
 
   
   public IncidenceContainer getIncidenceContainer() {
     return this.incidenceContainer;
   }
 
 
   
   public void setIncidenceContainer(IncidenceContainer incidenceContainer) {
     this.incidenceContainer = incidenceContainer;
   }
 
 
   
   public void detachEdges() {
     Edge[] edges = this.incidenceContainer.getIncidentEdges();
     for (int i = 0; i < edges.length; i++) {
       
       Edge edge = edges[i];
       edge.detachFromVertices();
     } 
   }
 
 
   
   public boolean hasConnectionTo(Vertex vertex) {
     return getIncidenceContainer().hasConnectionTo(vertex);
   }
   
   public abstract int getWidth();
   
   public abstract int getHeight();
   
   public abstract void setPosX(int paramInt);
   
   public abstract void setPosY(int paramInt);
   
   public abstract int getOrderID();
 }


