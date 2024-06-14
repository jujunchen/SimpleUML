 package net.trustx.simpleuml.gef.graph;

 import java.util.Hashtable;
 import java.util.Iterator;
 import java.util.LinkedList;
 import java.util.List;






 public abstract class DFS
 {
   private Graph graph;
   private Hashtable markedVerts;
   private Hashtable markedEdges;

   public static FindAllVerticesDFS getFindAllVerticesDFSInstance() {
     return new FindAllVerticesDFS();
   }



   public static FindCycleDFS getFindCycleDFSInstance() {
     return new FindCycleDFS();
   }




   public DFS() {
     this.markedVerts = new Hashtable<Object, Object>();
     this.markedEdges = new Hashtable<Object, Object>();
   }



   public Object execute(Graph g, Vertex start, Object info) {
     this.graph = g;
     return null;
   }



   protected Object dfsVisit(Vertex v) {
     initResult();
     startVisit(v);
     mark(v);

     for (Iterator<Edge> inEdges = getIncidentEdges(v); inEdges.hasNext(); ) {

       Edge nextEdge = inEdges.next();
       if (!isMarked(nextEdge)) {

         mark(nextEdge);
         Vertex w = this.graph.opposite(v, nextEdge);

         if (!isMarked(w)) {

           mark(nextEdge);
           traverseDiscovery(nextEdge, v);
           if (!isDone())
           {
             dfsVisit(w);
           }

           continue;
         }
         traverseBack(nextEdge, v);
       }
     }

     finishVisit(v);
     return result();
   }



   protected Iterator getIncidentEdges(Vertex v) {
     return this.graph.incidentEdges(v);
   }



   protected void mark(Vertex v) {
     this.markedVerts.put(v, v);
   }



   protected void mark(Edge e) {
     this.markedEdges.put(e, e);
   }



   protected boolean isMarked(Vertex v) {
     return this.markedVerts.contains(v);
   }



   protected boolean isMarked(Edge e) {
     return this.markedEdges.contains(e);
   }




   protected void initResult() {}




   protected void startVisit(Vertex v) {}




   protected void traverseDiscovery(Edge e, Vertex v) {}




   protected void traverseBack(Edge e, Vertex v) {}



   protected boolean isDone() {
     return false;
   }




   protected void finishVisit(Vertex v) {}



   protected Object result() {
     return new Object();
   }



   public Graph getGraph() {
     return this.graph;
   }



   public static class FindAllVerticesDFS
     extends DFS
   {
     private List vertices;



     public Object execute(Graph g, Vertex start, Object info) {
       super.execute(g, start, info);
       this.vertices = new LinkedList();
       dfsVisit(start);
       return this.vertices;
     }



     protected Iterator getIncidentEdges(Vertex v) {
       return getGraph().incidentEdges(v);
     }



     protected void startVisit(Vertex v) {
       this.vertices.add(v);
     }
   }


   public static class FindCycleDFS
     extends DFS
   {
     private LinkedList path;

     private boolean done;

     private Vertex cycleStart;


     public Object execute(Graph g, Vertex start, Object info) {
       super.execute(g, start, info);
       this.path = new LinkedList();
       this.done = false;
       dfsVisit(start);
       LinkedList<Vertex> theCycle = new LinkedList();
       Iterator<Vertex> iter = this.path.iterator();
       while (iter.hasNext()) {

         Vertex v = iter.next();
         theCycle.addFirst(v);
         if (v == this.cycleStart) {
           break;
         }
       }

       return theCycle;
     }



     protected Iterator getIncidentEdges(Vertex v) {
       return getGraph().outIncidentEdges(v);
     }



     protected void startVisit(Vertex v) {
       this.path.addFirst(v);
     }



     protected void finishVisit(Vertex v) {
       if (!this.done)
       {
         this.path.remove(this.path.getFirst());
       }
     }



     protected void traverseBack(Edge e, Vertex v) {
       this.cycleStart = getGraph().opposite(v, e);
       this.done = true;
     }



     protected boolean isDone() {
       return this.done;
     }
   }
 }


