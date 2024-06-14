 package net.trustx.simpleuml.classdiagram.layout;
 
 import java.util.Collection;
 import java.util.Collections;
 import java.util.Comparator;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.LinkedList;
 import java.util.ListIterator;
 import java.util.Set;
 import net.trustx.simpleuml.classdiagram.components.ClassDiagramComponent;
 import net.trustx.simpleuml.gef.Figure;
 import net.trustx.simpleuml.gef.connector.Connector;
 import net.trustx.simpleuml.gef.graph.DummyVertex;
 import net.trustx.simpleuml.gef.graph.Edge;
 import net.trustx.simpleuml.gef.graph.FigureVertex;
 import net.trustx.simpleuml.gef.graph.Graph;
 import net.trustx.simpleuml.gef.graph.Vertex;
 

 
 public class DiagramLayouter
 {
   private ClassDiagramComponent classDiagramComponent;
   private Graph graph;
   
   public DiagramLayouter(ClassDiagramComponent classDiagramComponent) {
     this.classDiagramComponent = classDiagramComponent;
     
     this.graph = new Graph();
     
     HashMap psiMap = classDiagramComponent.getPsiClassComponentMap();
     
     Set connectorSet = classDiagramComponent.getConnectorManager().getConnectors();
     HashMap<Object, Object> vertexMap = new HashMap<Object, Object>();
     
     Collection psiCol = psiMap.values();
     for (Iterator<Figure> iterator1 = psiCol.iterator(); iterator1.hasNext(); ) {
       
       Figure psiComp = iterator1.next();
       FigureVertex figureVertex = new FigureVertex(psiComp);
       
       this.graph.add((Vertex)figureVertex);
       vertexMap.put(psiComp.getKey(), figureVertex);
     } 
     
     for (Iterator<Connector> iterator = connectorSet.iterator(); iterator.hasNext(); ) {
       
       Connector diagramConnector = iterator.next();
       if (diagramConnector.getConnectorDecorator() instanceof net.trustx.simpleuml.gef.connector.ConnectorDecoratorExtends || diagramConnector.getConnectorDecorator() instanceof net.trustx.simpleuml.gef.connector.ConnectorDecoratorImplements || diagramConnector.getConnectorDecorator() instanceof net.trustx.simpleuml.gef.connector.ConnectorDecoratorInner) {
         
         FigureVertex vertexFrom = (FigureVertex)vertexMap.get(diagramConnector.getStartFigureAnchor().getFigure().getKey());
         FigureVertex vertexTo = (FigureVertex)vertexMap.get(diagramConnector.getEndFigureAnchor().getFigure().getKey());
         Edge edge = new Edge((Vertex)vertexFrom, (Vertex)vertexTo);
         
         this.graph.add(edge);
       } 
     } 
   }
 
 
 
 
 
   
   public LinkedList getCyclePath() {
     return this.graph.getCycles();
   }
 
 
   
   private void sortSpans(LinkedList spanList) {
     for (Iterator<LinkedList> iterator = spanList.iterator(); iterator.hasNext(); ) {
       
       LinkedList<?> vertexList = iterator.next();
       Collections.sort(vertexList, new Comparator()
           {
             public int compare(Object o1, Object o2)
             {
               Vertex v1 = (Vertex)o1;
               Vertex v2 = (Vertex)o2;
               if (v1.x < v2.x)
               {
                 return -1;
               }
               if (v1.x > v2.x)
               {
                 return 1;
               }
               return 0;
             }
           });
     } 
   }
 
 
 
 
 
 
 
 
 
   
   private void printSpanList(LinkedList topSortedList) {}
 
 
 
 
 
 
 
 
 
   
   private void optimizeDownwards(LinkedList topSortedList) {
     ListIterator<LinkedList> spanIter = topSortedList.listIterator();
 
 
 
 
 
 
 
     
     double level = 0.01D;
     while (spanIter.hasNext()) {
       
       LinkedList vertList = spanIter.next();
 
 
       
       for (Iterator<Vertex> vertexIter = vertList.iterator(); vertexIter.hasNext(); ) {
         
         Vertex vertex = vertexIter.next();
         Iterator<Edge> targetEdgeIter = vertex.getIncidenceContainer().getOutIncidentEdgeIterator();
         double x = vertex.x;
         double count = 1.0D;
         while (targetEdgeIter.hasNext()) {
           
           Edge e = targetEdgeIter.next();
           Vertex v = e.getToVertex();
           
           x += v.x + e.getEdgeNumber() * level + v.getOrderID() / 1000000.0D;
           count++;
         } 
 
         
         Iterator<Edge> sourceEdgeIter = vertex.getIncidenceContainer().getInIncidentEdgeIterator();
         while (sourceEdgeIter.hasNext()) {
           
           Edge e = sourceEdgeIter.next();
           Vertex v = e.getFromVertex();
           
           x += v.x + e.getEdgeNumber() * level + v.getOrderID() / 1000000.0D;
           count++;
         } 
 
         
         if (count > 0.0D)
         {
           vertex.x = x / count;
         }
       } 
 
 
       
       level /= 4.0D;
     } 
   }
 
 
 
 
   
   private void optimizeUpwards(LinkedList topSortedList) {
     ListIterator<LinkedList> spanIter = topSortedList.listIterator();
     while (spanIter.hasNext())
     {
       spanIter.next();
     }
     spanIter.previous();
 
     
     while (spanIter.hasPrevious()) {
       
       LinkedList vertList = spanIter.previous();
 
       
       for (Iterator<Vertex> vertexIter = vertList.iterator(); vertexIter.hasNext(); ) {
         
         Vertex vertex = vertexIter.next();
         Iterator<Edge> targetEdgeIter = vertex.getIncidenceContainer().getInIncidentEdgeIterator();
         double x = vertex.x;
         double count = 1.0D;
         while (targetEdgeIter.hasNext()) {
           
           Edge e = targetEdgeIter.next();
           Vertex v = e.getFromVertex();
           x += v.x;
           
           count++;
         } 
         
         Iterator<Edge> sourceEdgeIter = vertex.getIncidenceContainer().getOutIncidentEdgeIterator();
         while (sourceEdgeIter.hasNext()) {
           
           Edge e = sourceEdgeIter.next();
           Vertex v = e.getToVertex();
           x += v.x;
           
           count++;
         } 
 
         
         if (count > 0.0D)
         {
           vertex.x = x / count;
         }
       } 
     } 
   }
 
 
 
 
 
 
 
 
   
   public void execute() {
     LinkedList subGraphs = this.graph.divideIntoSubgraphList();
     int graphOffsetX = 0;
     int graphOffsetY = 0;
     int previousGraphSize = 0;
     int maxGraphHeight = 0;
     int maxGraphWidth = 0;
     int graphOnSameRowCount = 0;
     
     for (Iterator<Graph> iterator = subGraphs.iterator(); iterator.hasNext(); ) {
       
       Graph subGraph = iterator.next();
       
       LinkedList topSortedList = subGraph.topSortToSpanList();
 
       
       insertDummyVertices(topSortedList);
 
       
       optimizeSpans2(topSortedList);
       
       int spanOffsetX = 0;
       int spanOffsetY = 0;
 
       
       if (previousGraphSize == subGraph.sizeVertices() && graphOnSameRowCount < 5) {
         
         graphOffsetX += maxGraphWidth + 100;
         maxGraphWidth = 0;
         graphOnSameRowCount++;
       }
       else {
         
         maxGraphWidth = 0;
         graphOffsetX = 0;
         graphOffsetY += maxGraphHeight + 100;
         maxGraphHeight = 0;
         graphOnSameRowCount = 0;
       } 
       previousGraphSize = subGraph.sizeVertices();
 
       
       for (Iterator<LinkedList> spanIter = topSortedList.iterator(); spanIter.hasNext(); ) {
 
         
         LinkedList spanList = spanIter.next();
         
         int compOffsetY = 0;
         int maxSpanHeight = 0;
         int maxSpanWidth = 0;
         
         for (Iterator<Vertex> vertIter = spanList.iterator(); vertIter.hasNext(); ) {
           
           Vertex vertex = vertIter.next();
 
 
 
 
 
 
 
 
 
 
 
 
           
           if (vertex instanceof FigureVertex) {
             
             FigureVertex fv = (FigureVertex)vertex;
             if (!fv.getFigure().isPinned()) {
               
               vertex.setPosX(graphOffsetX + spanOffsetX + vertex.xAlign - vertex.getWidth() / 2);
               vertex.setPosY(graphOffsetY + spanOffsetY + compOffsetY);
             } 
           } 
 
           
           if (vertex.getHeight() > maxSpanHeight)
           {
             maxSpanHeight = vertex.getHeight();
           }
           
           if (vertex.xAlign + vertex.getWidth() > maxSpanWidth)
           {
             maxSpanWidth = vertex.xAlign - 50 + vertex.getWidth();
           }
         } 
 
         
         spanOffsetY += maxSpanHeight + 50;
 
         
         if (spanOffsetY > maxGraphHeight)
         {
           maxGraphHeight = spanOffsetY;
         }
         
         if (maxSpanWidth > maxGraphWidth)
         {
           maxGraphWidth = maxSpanWidth;
         }
       } 
     } 
 
 
 
 
 
     
     this.classDiagramComponent.layoutContainer();
     this.classDiagramComponent.getConnectorManager().validateConnectors();
     this.classDiagramComponent.repaint();
   }
 
 
 
 
   
   private void insertDummyVertices(LinkedList topSortedList) {
     if (topSortedList.size() < 3) {
       return;
     }
 
     
     printSpanList(topSortedList);
     ListIterator<LinkedList> listIterator = topSortedList.listIterator(topSortedList.size());
     while (listIterator.previousIndex() > 1) {
       
       LinkedList vertexList = listIterator.previous();
       for (Iterator<Vertex> iterator = vertexList.iterator(); iterator.hasNext(); ) {
         
         LinkedList<DummyVertex> previousVertexList = listIterator.previous();
         listIterator.next();
         Vertex vertex = iterator.next();
         Iterator<Edge> edgeIter = vertex.getIncidenceContainer().getOutIncidentEdgeIterator();
         while (edgeIter.hasNext()) {
           
           Edge edge = edgeIter.next();
           if (!previousVertexList.contains(edge.getToVertex())) {
             
             DummyVertex dummyVertex = new DummyVertex("dummy");
             previousVertexList.add(dummyVertex);
             Vertex oldToVertex = edge.getToVertex();
             edge.setToVertex((Vertex)dummyVertex);
             new Edge((Vertex)dummyVertex, oldToVertex);
           } 
         } 
       } 
     } 
     printSpanList(topSortedList);
   }
 
 
 
   
   private void optimizeSpans2(LinkedList topSortedList) {
     int previousSize = -1;
     for (Iterator<LinkedList> iterator = topSortedList.iterator(); iterator.hasNext(); ) {
       
       LinkedList vertexList = iterator.next();
       if (previousSize < 2) {
         
         int i = 1000;
         for (Iterator<Vertex> vertexIter = vertexList.iterator(); vertexIter.hasNext(); ) {
           
           Vertex vertex = vertexIter.next();
           vertex.x = i;
         } 
       } 
 
       
       previousSize = vertexList.size();
     } 
     
     printSpanList(topSortedList);
     
     for (int n = 0; n < 20; n++) {
       
       optimizeDownwards(topSortedList);
       printSpanList(topSortedList);
       
       optimizeUpwards(topSortedList);
       printSpanList(topSortedList);
     } 
     optimizeDownwards(topSortedList);
     
     sortSpans(topSortedList);
     
     distributeGraph(topSortedList);
   }
 
 
   
   private void distributeGraph(LinkedList<LinkedList> topSortedList) {
     ListIterator<LinkedList> spanIter = topSortedList.listIterator();
     int maxWidth = 0;
     int maxWidthSpanIndex = 0;
     
     while (spanIter.hasNext()) {
       
       LinkedList vertexList = spanIter.next();
       int spanWidth = 0;
       for (Iterator<Vertex> vertexIter = vertexList.iterator(); vertexIter.hasNext(); ) {
         
         Vertex vertex = vertexIter.next();
         spanWidth += vertex.getWidth() + 50;
       } 
       
       if (spanWidth > maxWidth) {
         
         maxWidth = spanWidth;
         maxWidthSpanIndex = spanIter.nextIndex() - 1;
       } 
     } 
 
 
     
     spanIter = topSortedList.listIterator(maxWidthSpanIndex);
     
     distributeLongestVertexList(spanIter.next());
     distributeDownwards(topSortedList, maxWidthSpanIndex);
     distributeUpwards(topSortedList, maxWidthSpanIndex);
   }
 
 
   
   private void distributeUpwards(LinkedList topSortedList, int maxWidthSpanIndex) {
     ListIterator<LinkedList> spanIter = topSortedList.listIterator(maxWidthSpanIndex + 1);
     LinkedList previousVertexList = spanIter.previous();
     while (spanIter.hasPrevious()) {
       
       int previousVertexXAlign = 0;
       int previousVertexWidth = 0;
       LinkedList vertexList = spanIter.previous();
       for (Iterator<Vertex> iterator = vertexList.iterator(); iterator.hasNext(); ) {
         
         int count = 0;
         int xAlign = 0;
         Vertex vertex = iterator.next();
         Iterator<Edge> inIter = vertex.getIncidenceContainer().getInIncidentEdgeIterator();
         while (inIter.hasNext()) {
           
           Edge edge = inIter.next();
           if (previousVertexList.contains(edge.getFromVertex())) {
             
             xAlign += (edge.getFromVertex()).xAlign;
             count++;
           } 
         } 
         if (count > 0)
         {
           xAlign /= count;
         }
         
         if (xAlign - vertex.getWidth() / 2 < previousVertexXAlign + previousVertexWidth / 2 + 50)
         {
           xAlign = previousVertexXAlign + previousVertexWidth / 2 + vertex.getWidth() / 2 + 50;
         }
         vertex.xAlign = xAlign;
         previousVertexXAlign = xAlign;
         previousVertexWidth = vertex.getWidth();
       } 
       previousVertexList = vertexList;
     } 
   }
 
 
   
   private void distributeDownwards(LinkedList topSortedList, int maxWidthSpanIndex) {
     ListIterator<LinkedList> spanIter = topSortedList.listIterator(maxWidthSpanIndex);
     LinkedList previousVertexList = spanIter.next();
     while (spanIter.hasNext()) {
       
       int previousVertexXAlign = 0;
       int previousVertexWidth = 0;
       LinkedList vertexList = spanIter.next();
       for (Iterator<Vertex> iterator = vertexList.iterator(); iterator.hasNext(); ) {
         
         int count = 0;
         int xAlign = 0;
         Vertex vertex = iterator.next();
         Iterator<Edge> outIter = vertex.getIncidenceContainer().getOutIncidentEdgeIterator();
         while (outIter.hasNext()) {
           
           Edge edge = outIter.next();
           if (previousVertexList.contains(edge.getToVertex())) {
             
             xAlign += (edge.getToVertex()).xAlign;
             count++;
           } 
         } 
         xAlign /= count;
         vertex.requestedXAlign = xAlign;
         if (xAlign - vertex.getWidth() / 2 < previousVertexXAlign + previousVertexWidth / 2 + 50)
         {
           xAlign = previousVertexXAlign + previousVertexWidth / 2 + vertex.getWidth() / 2 + 50;
         }
         vertex.xAlign = xAlign;
         previousVertexXAlign = xAlign;
         previousVertexWidth = vertex.getWidth();
       } 
       
       LinkedList<Vertex> unhappyVertices = new LinkedList();
       ListIterator<Vertex> vertexIter = vertexList.listIterator();
       int xAlignDiff = 0;
       int smallestFixedX = 0;
       while (vertexIter.hasNext()) {
         
         Vertex v = vertexIter.next();
 
         
         if (v.xAlign - v.requestedXAlign > xAlignDiff && vertexIter.hasNext()) {
           
           unhappyVertices.add(v);
           xAlignDiff = v.xAlign - v.requestedXAlign;
         
         }
         else if (unhappyVertices.size() == 0) {
 
           
           unhappyVertices.add(v);
         }
         else if (unhappyVertices.size() == 1 && v.xAlign == v.requestedXAlign) {
           
           Vertex happyFirstVertex = unhappyVertices.getFirst();
           smallestFixedX = happyFirstVertex.xAlign + happyFirstVertex.getWidth() / 2 + 50;
           unhappyVertices.clear();
           unhappyVertices.add(v);
         }
         else {
           
           if (v.xAlign - v.requestedXAlign > xAlignDiff && !vertexIter.hasNext())
           {
             xAlignDiff = v.xAlign - v.requestedXAlign;
           }
 
           
           int optimalXToMove = xAlignDiff / 2;
           
           if (unhappyVertices.size() > 0) {
             
             Vertex firstUnhappy = unhappyVertices.getFirst();
             int maxMove = -(smallestFixedX - firstUnhappy.xAlign - firstUnhappy.getWidth() / 2);
             
             int xMove = Math.min(maxMove, optimalXToMove);
             
             for (Iterator<Vertex> iterator1 = unhappyVertices.iterator(); iterator1.hasNext(); ) {
               
               Vertex vertex = iterator1.next();
               vertex.xAlign -= xMove;
             } 
             Vertex lastUnhappy = unhappyVertices.getLast();
             smallestFixedX = lastUnhappy.xAlign + lastUnhappy.getWidth() / 2 + 50;
           } 
           
           if (v.xAlign == v.requestedXAlign) {
 
             
             unhappyVertices.clear();
             xAlignDiff = 0;
             smallestFixedX = v.xAlign + v.getWidth() / 2 + 50;
           
           }
           else {
 
             
             xAlignDiff = v.xAlign - v.requestedXAlign;
             
             unhappyVertices.clear();
             unhappyVertices.add(v);
           } 
         } 
         if (!vertexIter.hasNext() && unhappyVertices.size() > 0) {
           
           int optimalXToMove = xAlignDiff / 2;
           Vertex firstUnhappy = unhappyVertices.getFirst();
           int maxMove = -(smallestFixedX - firstUnhappy.xAlign - firstUnhappy.getWidth() / 2);
           
           int xMove = Math.min(maxMove, optimalXToMove);
           
           for (Iterator<Vertex> iterator1 = unhappyVertices.iterator(); iterator1.hasNext(); ) {
             
             Vertex vertex = iterator1.next();
             vertex.xAlign -= xMove;
           } 
           Vertex lastUnhappy = unhappyVertices.getLast();
           smallestFixedX = lastUnhappy.xAlign + lastUnhappy.getWidth() / 2 + 50;
         } 
       } 
 
 
       
       previousVertexList = vertexList;
     } 
   }
 
 
   
   private void distributeLongestVertexList(LinkedList longestVertexList) {
     ListIterator<Vertex> vertexIter = longestVertexList.listIterator();
     int nextX = 50;
     while (vertexIter.hasNext()) {
       
       Vertex vertex = vertexIter.next();
       vertex.xAlign = nextX + vertex.getWidth() / 2;
       nextX += vertex.getWidth() + 50;
     } 
   }
 }


