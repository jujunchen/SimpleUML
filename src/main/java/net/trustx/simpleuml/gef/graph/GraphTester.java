 package net.trustx.simpleuml.gef.graph;

 import java.util.Iterator;
 import java.util.LinkedList;
 import java.util.List;





 public class GraphTester
 {
   public static void main(String[] args) {
     Graph graph = new Graph();

     Vertex v1 = new DummyVertex("v1");
     Vertex v2 = new DummyVertex("v2");
     Vertex v3 = new DummyVertex("v3");
     Vertex v4 = new DummyVertex("v4");
     Vertex v5 = new DummyVertex("v5");
     Vertex v6 = new DummyVertex("v6");
     Vertex v7 = new DummyVertex("v7");

     Edge e1 = new Edge(v1, v3);
     Edge e2 = new Edge(v2, v3);
     Edge e3 = new Edge(v3, v4);
     Edge e4 = new Edge(v3, v5);
     Edge e5 = new Edge(v6, v7);
     Edge e6 = new Edge(v2, v5);

     graph.add(v1);
     graph.add(v2);
     graph.add(v3);
     graph.add(v4);
     graph.add(v5);
     graph.add(v6);
     graph.add(v7);

     graph.add(e1);
     graph.add(e2);
     graph.add(e3);
     graph.add(e4);
     graph.add(e5);
     graph.add(e6);

     System.out.println("connected vertices");
     DFS.FindAllVerticesDFS dfs = DFS.getFindAllVerticesDFSInstance();
     List list = (List)dfs.execute(graph, v3, "null");
     for (Iterator<Vertex> iterator = list.iterator(); iterator.hasNext(); ) {

       Vertex v = iterator.next();
       System.out.println("v = " + v);
     }

     System.out.println("cycle");
     DFS.FindCycleDFS fcDFS = DFS.getFindCycleDFSInstance();
     List cycleList = (List)fcDFS.execute(graph, v3, "null");
     for (Iterator<Vertex> iterator1 = cycleList.iterator(); iterator1.hasNext(); ) {

       Vertex v = iterator1.next();
       System.out.println("v = " + v);
     }


     LinkedList subGraphs = graph.divideIntoSubgraphList();
     for (Iterator<Graph> iterator2 = subGraphs.iterator(); iterator2.hasNext(); ) {

       Graph subGraph = iterator2.next();
       System.out.println("TopSort");
       LinkedList topSortedList = subGraph.topSortToSpanList();
       int level = 0;
       for (Iterator<LinkedList> spanIter = topSortedList.iterator(); spanIter.hasNext(); ) {

         System.out.print("level: " + level + " --> ");
         LinkedList spanList = spanIter.next();
         for (Iterator<Vertex> vertIter = spanList.iterator(); vertIter.hasNext(); ) {

           Vertex vertex = vertIter.next();
           System.out.print(vertex + " ");
         }
         System.out.println();
         level++;
       }
     }
   }
 }


