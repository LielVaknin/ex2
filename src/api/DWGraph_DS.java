package api;

import java.util.Collection;
import java.util.HashMap;

/**
 * This class represents a directed weighted graph.
 *
 * @authors Liel.Vaknin & Renana.Levy.
 */
public class DWGraph_DS implements directed_weighted_graph{

    private int nodeSize;
    private int MC;
    private int edgeSize;

    public HashMap<Integer, node_data> nodes;
    private HashMap<Integer, HashMap<Integer, edge_data>> edges;

    private int ID;

    /**
     * Default constructor.
     */
    public DWGraph_DS() {
        this.MC = 0;
        this.nodeSize = 0;
        this.edgeSize = 0;
        this.nodes = new HashMap<>();
        this.edges = new HashMap<>();
    }

    /**
     * Copy constructor - Performs a deep copy of a given graph.
     *
     * @param graph represents the given graph.
     */
    public DWGraph_DS(directed_weighted_graph graph) {
        this.nodes = new HashMap<>();
        this.edges = new HashMap<>();
        if(graph == null)
            return;
        for (node_data n: graph.getV()){
            node_data newN= new NodeData(n);
            this.nodes.put(newN.getKey(), newN);

            if(graph.getE(n.getKey()) != null) {
                HashMap<Integer, edge_data> newEdges = new HashMap<>();
                for (edge_data e: graph.getE(n.getKey())){
                    edge_data newE = new EdgeData(e);
                    newEdges.put(e.getDest(), newE);
                    MC++;
                }
                this.edges.put(n.getKey(), newEdges);
            }
            MC++;
        }
        this.nodeSize = graph.nodeSize();
        this.edgeSize = graph.edgeSize();
    }

    /**
     * Returns the node_data by the node_id.
     *
     * @param key represents the node_id.
     * @return the node_data by the given node_id, null if none.
     */
    @Override
    public node_data getNode(int key){ return this.nodes.get(key);}

    /**
     * Returns the data of the edge between src to dest, null if none.
     *
     * @param src represents the key of the source node.
     * @param dest represents the key of the destination node.
     * @return the data of the edge between src to dest, null if none.
     */
    @Override
    public edge_data getEdge(int src, int dest){
        if (!this.nodes.containsKey(src) || !this.nodes.containsKey(dest) || src == dest)
            return null;
        return this.edges.get(src).get(dest);
    }

    /**
     * Adds a new node to the graph with a given node_data.
     *
     * @param n represents the given new node for adding to the graph.
     */
    @Override
    public void addNode(node_data n){
        ((NodeData)n).setKey(ID);
        if (!this.nodes.containsKey(n.getKey())) {
            this.nodes.put(n.getKey(), n);
            ID++;
            MC++;
            nodeSize++;
        }
    }

    /**
     * Connects an edge with weight w between node src to node dest.
     *
     * @param src represents the key of the source node of the edge.
     * @param dest represents the key of the destination node of the edge.
     * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w){
        if (!this.nodes.containsKey(src) || !this.nodes.containsKey(dest) || src == dest || w < 0)
            return;
        if(this.edges.get(src) == null) {
            HashMap<Integer, edge_data> innerHashMap = new HashMap<>();
            this.edges.put(src, innerHashMap);
        }
        if (getEdge(src, dest) == null){
            edge_data newEdge = new EdgeData(src, dest, w);
            this.edges.get(src).put(dest, newEdge);
            edgeSize++;
            MC++;
        }
    }

    /**
     * Returns a pointer (shallow copy) for the
     * collection representing all the nodes in the graph.
     *
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_data> getV(){
        return this.nodes.values();
    }

    /**
     * Returns a pointer (shallow copy) for the
     * collection representing all the edges getting out of
     * the given node (all the edges starting (source) at the given node).
     *
     * @return Collection<edge_data>
     */
    @Override
    public Collection<edge_data> getE(int node_id){
        if(this.edges.get(node_id) == null)
            return null;
        return this.edges.get(node_id).values();
    }

    /**
     * Deletes the node (with the given ID) from the graph
     * and removes all edges which starts or ends at this node.
     *
     * @param key represents the key of the node which should be deleted.
     * @return the data of the removed node (null if none).
     */
    @Override
    public node_data removeNode(int key) {
        if (!this.nodes.containsKey(key))
            return null;
        for (node_data n : this.getV()) {
            if(!this.edges.containsKey(n.getKey())){
                continue;
            }
            if (this.edges.get(n.getKey()).containsKey(key)) {
                this.removeEdge(n.getKey(), key);
            }
        }
        edgeSize = edgeSize - this.edges.get(key).size();
        this.edges.remove(key);
        nodeSize--;
        MC++;
        return this.nodes.remove(key);
    }

    /**
     * Deletes from the graph the directed edge between src to dest.
     *
     * @param src represents the key of the source node of the edge.
     * @param dest represents the key of the destination node of the edge.
     * @return the data of the removed edge (null if none).
     */
    @Override
    public edge_data removeEdge(int src, int dest){
        if (!this.nodes.containsKey(src) || !this.nodes.containsKey(dest) || src == dest)
            return null;
        if (this.edges.get(src).containsKey(dest)) {
            edge_data edge = edges.get(src).get(dest);
            this.edges.get(src).remove(dest);
            edgeSize--;
            MC++;
            return edge;
        }
        else
            return null;
    }

    /**
     * Returns the number of vertices (nodes) in the graph.
     *
     * @return nodeSize.
     */
    @Override
    public int nodeSize(){return this.nodeSize;}

    /**
     * Returns the number of directional edges in the graph.
     *
     * @return edgeSize.
     */
    @Override
    public int edgeSize(){return this.edgeSize;}

    /**
     * Returns the Mode Count - for testing changes in the graph.
     *
     * @return MC.
     */
    @Override
    public int getMC(){return this.MC;}

    /**
     * ToString method.
     *
     * @return String which represents all the nodes in this graph by their keys
     * and the edges which connected to each one of them (the neighbor and the edge's weight).
     */
    @Override
    public String toString(){
        String g = "[";
        int siN = 0;
        for(int n: this.nodes.keySet()){
            g = g+"("+n+": {";
            int siE = 0;
            for (edge_data e: getE(n)){
                if(siE == getE(n).size()-1){
                    g = g+e.getDest()+"- weight = "+e.getWeight()+"}";
                }else {
                    g = g+e.getDest()+"- weight = "+e.getWeight()+", ";
                }
                siE++;
            }
            g = g+")";
            if(siN == this.nodes.keySet().size()-1){
                g = g+"]";
            } else {
                g = g+" , ";
            }
            siN++;
        }
        return g;
    }

    /**
     * Equals method.
     *
     * @param o represents a given object.
     * @return true if this object and a given object are equals, false if not.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || /*getClass() != o.getClass()*/ !(o instanceof directed_weighted_graph))
            return false;
        directed_weighted_graph that = (directed_weighted_graph)o;
        if(this.nodeSize != that.nodeSize() || this.edgeSize != that.edgeSize())
            return false;
        for(node_data ng: that.getV()) {
            int keyG = ng.getKey();
            if(this.nodes.containsKey(keyG)) {
                node_data nt = this.getNode(keyG);
                if((!(nt).equals(ng)))
                    return false;
                else {
                    for (edge_data e: that.getE(keyG)){
                        Collection<edge_data> thisE = this.getE(keyG);
                        if(!thisE.contains(e))
                            return false;
                    }
                }
            }
            else
                return false;
        }
        return true;
    }
}

