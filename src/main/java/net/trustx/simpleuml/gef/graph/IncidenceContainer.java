 package net.trustx.simpleuml.gef.graph;
 
 import java.util.HashSet;
 import java.util.Iterator;
 import java.util.Set;
 
 
 

 
 
 public class IncidenceContainer
 {
   private Set inIncidentEdges = new HashSet();
   private Set outIncidentEdges = new HashSet();
   
   private Set incidentEdges = new HashSet();
 
 
 
   
   public void addOutIncidentEdge(Edge e) {
     this.outIncidentEdges.add(e);
     e.setEdgeNumber(this.outIncidentEdges.size());
     this.incidentEdges.add(e);
   }
 
 
   
   public void removeOutIncidentEdge(Edge e) {
     this.outIncidentEdges.remove(e);
     this.incidentEdges.remove(e);
   }
 
 
   
   public void addInIncidentEdge(Edge e) {
     this.inIncidentEdges.add(e);
     e.setEdgeNumber(this.inIncidentEdges.size());
     this.incidentEdges.add(e);
   }
 
 
   
   public void removeInIncidentEdge(Edge e) {
     this.inIncidentEdges.remove(e);
     this.incidentEdges.remove(e);
   }
 
 
   
   public Iterator getIncidentEdgeIterator() {
     return this.incidentEdges.iterator();
   }
 
 
   
   public Iterator getInIncidentEdgeIterator() {
     return this.inIncidentEdges.iterator();
   }
 
 
   
   public Iterator getOutIncidentEdgeIterator() {
     return this.outIncidentEdges.iterator();
   }
 
 
   
   public Edge[] getIncidentEdges() {
     return (Edge[])this.incidentEdges.toArray((Object[])new Edge[this.incidentEdges.size()]);
   }
 
 
   
   public int getIncidentEdgeCount() {
     return this.incidentEdges.size();
   }
 
 
   
   public int getInIncidentEdgeCount() {
     return this.inIncidentEdges.size();
   }
 
 
   
   public int getOutIncidentEdgeCount() {
     return this.outIncidentEdges.size();
   }
 
 
   
   public boolean hasConnectionTo(Vertex vertex) {
     for (Iterator<Edge> iterator = this.outIncidentEdges.iterator(); iterator.hasNext(); ) {
       
       Edge edge = iterator.next();
       if (edge.getToVertex() == vertex)
       {
         return true;
       }
     } 
     return false;
   }
 }


