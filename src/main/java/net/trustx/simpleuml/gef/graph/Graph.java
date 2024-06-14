 package net.trustx.simpleuml.gef.graph;

 import java.util.Collections;
 import java.util.HashSet;
 import java.util.Iterator;
 import java.util.LinkedList;
 import java.util.List;





 public class Graph
 {
   private HashSet edges = new HashSet();
   private HashSet vertices = new HashSet();




   public int numVertices() {
     return this.vertices.size();
   }



   public int numEdges() {
     return this.edges.size();
   }



   public Iterator incidentEdges(Vertex v) {
     return v.getIncidenceContainer().getIncidentEdgeIterator();
   }



   public Iterator inIncidentEdges(Vertex v) {
     return v.getIncidenceContainer().getInIncidentEdgeIterator();
   }



   public Iterator outIncidentEdges(Vertex v) {
     return v.getIncidenceContainer().getOutIncidentEdgeIterator();
   }



   public Vertex opposite(Vertex v, Edge edge) {
     if (edge.getFromVertex() == v)
     {
       return edge.getToVertex();
     }


     return edge.getFromVertex();
   }




   public void add(Vertex vertex) {
     this.vertices.add(vertex);
   }



   public void add(Edge edge) {
     this.edges.add(edge);
   }



   public int sizeVertices() {
     return this.vertices.size();
   }



   public int sizeEdges() {
     return this.edges.size();
   }



   public Iterator getVertexIterator() {
     return this.vertices.iterator();
   }



   public LinkedList getCycles() {
     LinkedList remainingVertices = new LinkedList(this.vertices);
     LinkedList<LinkedList<?>> topSortedList = new LinkedList();
     LinkedList<?> startVertices;
     while ((startVertices = findStartVertices(remainingVertices)).size() > 0) {

       topSortedList.add(startVertices);
       for (Iterator<?> iterator1 = startVertices.iterator(); iterator1.hasNext(); ) {

         Vertex vertex = (Vertex)iterator1.next();
         vertex.detachEdges();
       }
     }
     for (Iterator<Edge> iterator = this.edges.iterator(); iterator.hasNext(); ) {

       Edge edge = iterator.next();
       edge.attachToVertices();
     }

     return remainingVertices;
   }



   public LinkedList topSortToSpanList() {
     LinkedList remainingVertices = new LinkedList(this.vertices);
     LinkedList<LinkedList<?>> topSortedList = new LinkedList();
     LinkedList<?> startVertices;
     while ((startVertices = findStartVertices(remainingVertices)).size() > 0) {

       topSortedList.add(startVertices);
       for (Iterator<?> iterator1 = startVertices.iterator(); iterator1.hasNext(); ) {

         Vertex vertex = (Vertex)iterator1.next();
         vertex.detachEdges();
       }
     }

     for (Iterator<Edge> iterator = this.edges.iterator(); iterator.hasNext(); ) {

       Edge edge = iterator.next();
       edge.attachToVertices();
     }


     return topSortedList;
   }



   private LinkedList findStartVertices(LinkedList remainingVertices) {
     LinkedList<Vertex> startVerticesList = new LinkedList();
     for (Iterator<Vertex> iterator = remainingVertices.iterator(); iterator.hasNext(); ) {

       Vertex vertex = iterator.next();
       if (vertex.getIncidenceContainer().getOutIncidentEdgeCount() == 0) {

         startVerticesList.add(vertex);
         iterator.remove();
       }
     }
     return startVerticesList;
   }



   public LinkedList divideIntoSubgraphList() {
     Graph graphCopy = new Graph();
     HashSet edgesCopy = new HashSet(this.edges);
     HashSet verticesCopy = new HashSet(this.vertices);
     graphCopy.edges = edgesCopy;
     graphCopy.vertices = verticesCopy;
     return graphCopy.divide();
   }



   private LinkedList divide() {
     LinkedList<Graph> graphList = new LinkedList();

     while (this.vertices.iterator().hasNext()) {

       Graph subGraph = new Graph();
       DFS.FindAllVerticesDFS dfs = new DFS.FindAllVerticesDFS();
       List verticesList = (List)dfs.execute(this, this.vertices.iterator().next(), "null");
       for (Iterator<Vertex> iterator = verticesList.iterator(); iterator.hasNext(); ) {

         Vertex vertex = iterator.next();
         subGraph.add(vertex);
         for (Iterator<Edge> edgeIterator = vertex.getIncidenceContainer().getIncidentEdgeIterator(); edgeIterator.hasNext(); ) {

           Edge edge = edgeIterator.next();
           subGraph.add(edge);
         }
         this.vertices.remove(vertex);
       }

       graphList.add(subGraph);
     }
     Collections.sort(graphList, new GraphSizeComparator(false));
     return graphList;
   }
 }


