package api;

import com.google.gson.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

/**
 * This class represents a Directed (positive) Weighted Graph Theory Algorithms including:
 * 0. clone(); (copy)
 * 1. init(graph);
 * 2. isConnected(); // strongly (all ordered pais connected)
 * 3. double shortestPathDist(int src, int dest);
 * 4. List<node_data> shortestPath(int src, int dest);
 * 5. Save(file); // JSON file
 * 6. Load(file); // JSON file
 *
 * @Authors Liel.Vaknin & Renana.Levy.
 *
 */
public class DWGraph_Algo implements dw_graph_algorithms {

   private directed_weighted_graph g = new DWGraph_DS();

   /**
    * This method initializes the graph with the set of algorithms operates on.
    *
    * @param g represents the given graph.
    */
   @Override
   public void init(directed_weighted_graph g) {
      this.g = g;
   }

   /**
    * Returns the underlying graph of which this class works.
    *
    * @return g.
    */
   @Override
   public directed_weighted_graph getGraph() {
      return g;
   }

   /**
    * Performs a deep copy of this directed weighted graph.
    *
    * @return copied graph.
    */
   @Override
   public directed_weighted_graph copy() {
      if (this.g == null)
         return null;
      return new DWGraph_DS(g);
   }

   /**
    * This method uses the DFS (Depth-first search) algorithm,
    * private method which helps with the implementation of isConnected method in DWGraph_Algo class.
    * Uses "info" field for representing colors : "white", "grey" and "black" -
    * each color represents a different status of a node.
    *
    * @param graph represents the given graph.
    * @param src represents the given source node.
    */
   private void dfsVisit(directed_weighted_graph graph, node_data src) {
      if (graph.getE(src.getKey()) == null) {
         return;
      }
      src.setInfo("grey");
      for (edge_data e : graph.getE(src.getKey())) {
         if (graph.getNode(e.getDest()).getInfo().equals("white")) {
            dfsVisit(graph, graph.getNode(e.getDest()));
         }
      }
      src.setInfo("black");
   }

   /**
    * Returns true if and only if (iff) there is a valid path from every node to each
    * other node (strongly connected) using DFS algorithm (dfsVisit method).
    *
    * @return true if this graph is strongly connected, false if not.
    */
   @Override
   public boolean isConnected() {
      if(g ==null || g.nodeSize()<2)
         return true;
      for (node_data n : this.g.getV()) {
         n.setInfo("white");
      }
      int c = 0;
      while ((g.getNode(c)) == null){
         c++;
      }
      dfsVisit(this.g, (g.getNode(c)));
      for (node_data n : this.g.getV()) {
         if (n.getInfo().equals("white"))
            return false;
      }

      directed_weighted_graph gr = new DWGraph_DS();
      for (node_data n : this.g.getV()){
         node_data newN = new NodeData(n.getKey());
         gr.addNode(newN);
      }
      for (node_data n : this.g.getV()){
         for (edge_data e : this.g.getE(n.getKey())){
            gr.connect(e.getDest(), e.getSrc(), e.getWeight());
         }
      }

      for (node_data n : gr.getV()) {
         n.setInfo("white");
      }

      dfsVisit(gr, (gr.getNode(c)));
      for (node_data n : gr.getV()) {
         if (n.getInfo().equals("white"))
            return false;
      }
      return true;
   }

   /**
    * This method implements the Dijkstra's algorithm.
    * Dijkstra is an algorithm for finding the shortest paths between nodes in a graph.
    *
    * @param src represents a given source node.
    */
   private void Dijkstra(node_data src) {
      for (node_data n : this.g.getV()) {
         n.setInfo("unvisited");
         ((NodeData) n).dist = Double.MAX_VALUE;
      }
      ((NodeData)src).dist = 0;
      Queue<node_data> q= new PriorityQueue<>();
      for (node_data n: g.getV()) {
         q.add(n);
      }
      while(!q.isEmpty()) {
         node_data rm= q.poll();
         for(edge_data edge: g.getE(rm.getKey())) {
            if(g.getNode(edge.getDest()).getInfo().equals("unvisited")) {
               double weight= g.getEdge(rm.getKey(), edge.getDest()).getWeight();
               double path= ((NodeData)rm).dist+weight;
               if(((NodeData)g.getNode(edge.getDest())).dist > path) {
                  q.remove(g.getNode(edge.getDest()));
                  ((NodeData)g.getNode(edge.getDest())).dist = path;
                  ((NodeData)g.getNode(edge.getDest())).prev = rm;
                  q.add(g.getNode(edge.getDest()));
               }
            }
         }
         rm.setInfo("visited");
      }
   }

   /**
    * Returns the length of the shortest path between src to dest,
    * if there is no such path --> returns -1, using Dijkstra's algorithm.
    *
    * @param src represents the starting point.
    * @param dest represents the ending point.
    * @return length of shortest path.
    */
   @Override
   public double shortestPathDist(int src, int dest) {
      if (g.getNode(src) == null || g.getNode(dest) == null) {
         return -1;
      }
      if(src == dest) {
         return 0;
      }
      Dijkstra(g.getNode(src));
      if (((NodeData)g.getNode(dest)).dist == Double.MAX_VALUE) {
         return -1;
      }
      return ((NodeData) g.getNode(dest)).dist;
   }

   /**
    * Returns the shortest path between src to dest - as an ordered List of nodes:
    * src --> n1 --> n2 -->...dest,
    * if no such path --> returns null.
    * Using Dijkstra's algorithm (shortestPathDist method).
    *
    * @param src represents the starting point.
    * @param dest represents the ending point.
    * @return the shortest path as an ordered list of nodes.
    */
   @Override
   public List<node_data> shortestPath(int src, int dest) {
      List<node_data> path = new LinkedList<>();
      if (src == dest && g.getNode(dest) != null) {
         path.add(g.getNode(dest));
         return path;
      }
      double dist = shortestPathDist(src, dest);
      if (dist == -1 || dist == Double.MAX_VALUE) {
         return null;
      } else {
         path.add(g.getNode(dest));
         node_data pr = ((NodeData) g.getNode(dest)).prev;
         int node0 = pr.getKey();
         while (node0 != src) {
            path.add(0, pr);
            pr = ((NodeData) g.getNode(node0)).prev;
            node0 = pr.getKey();
         }
         path.add(0, pr);
         return path;
      }
   }

   /**
    * Saves this directed weighted graph to the given
    * file name - in JSON format.
    *
    * @param file represents the file name (may include a relative path).
    * @return true iff the file was successfully saved.
    */
   @Override
   public boolean save(String file) {
      Gson gson = new Gson();
      String jsonG = gson.toJson(g);
      System.out.println(jsonG);
      try {
         FileWriter saveGraph = new FileWriter(file);
         saveGraph.write(jsonG);
         saveGraph.close();
         return true;
      } catch (Exception e) {
         return false;
      }
   }

   /**
    * Loads a graph to this graph algorithm.
    * If the file was successfully loaded - the underlying graph
    * of this class will be changed (to the loaded one), in case the
    * graph was not loaded the original graph should remain "as is".
    *
    * @param file represents the file name of JSON file.
    * @return true iff the graph was successfully loaded.
    */
   @Override
   public boolean load(String file) {
      try {
         Gson gson = new Gson();
         FileReader lodeGraph = new FileReader(file);
         JsonObject jsonOb = gson.fromJson(lodeGraph, JsonObject.class);
         initFromJson(jsonOb);
         return true;
      } catch (FileNotFoundException e) {
         return false;
      }
   }

   /**
    * This method loads the graph from the jsonObject.
    *
    * @param json represents the given jsonObject which represents a directed weighted graph.
    */
   private void initFromJson(JsonObject json){
      directed_weighted_graph newGraph = new DWGraph_DS();
      loadNodes(json, newGraph);
      loadEdges(json, newGraph);
      this.g = newGraph;
   }

   /**
    * This method loads the nodes of the graph from the jsonObject.
    *
    * @param json represents the given jsonObject which represents a directed weighted graph.
    * @param newGraph represents the graph which the nodes are loaded to it.
    */
   private void loadNodes(JsonObject json, directed_weighted_graph newGraph){
      JsonObject jsonNodes = json.get("nodes").getAsJsonObject();
      for (Map.Entry<String, JsonElement> node : jsonNodes.entrySet()){
         int hashKey = Integer.parseInt(node.getKey()); //the key of the hashmap
         JsonObject jsonN = node.getValue().getAsJsonObject();
         double nodeWeight = jsonN.get("nodeWeight").getAsDouble();
         node_data n = new NodeData(hashKey);
         n.setWeight(nodeWeight);
         if(jsonN.get("nodeGeoLocation") != null){
            JsonObject jsonLocation = jsonN.get("nodeGeoLocation").getAsJsonObject();
            int x = jsonLocation.get("x").getAsInt();
            int y = jsonLocation.get("y").getAsInt();
            int z = jsonLocation.get("z").getAsInt();
            geo_location pos  = new GeoLocation(x, y, z);
            n.setLocation(pos);
         }
         newGraph.addNode(n);
      }
   }

   /**
    * This method loads the edges of the graph from the jsonObject.
    *
    * @param json represents the given jsonObject which represents a directed weighted graph.
    * @param newGraph represents the graph which the edges are loaded to it.
    */
   private void loadEdges(JsonObject json, directed_weighted_graph newGraph){
      JsonObject jsonEdges = json.get("edges").getAsJsonObject();
      for (Map.Entry<String, JsonElement> setHashEdges : jsonEdges.entrySet()) {
         int src = Integer.parseInt(setHashEdges.getKey()); //the key of the hashmap
         JsonObject jsonEdgeByNode = setHashEdges.getValue().getAsJsonObject();
         for (Map.Entry<String, JsonElement> edge : jsonEdgeByNode.entrySet()) {
            JsonObject jsonE = edge.getValue().getAsJsonObject();
            int dest = jsonE.get("dest").getAsInt();
            double w = jsonE.get("edgeWeight").getAsDouble();
            newGraph.connect(src, dest, w);
         }
      }
   }
}