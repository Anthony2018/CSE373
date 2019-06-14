package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.concrete.dictionaries.KVPair;
import misc.Sorter;
import misc.exceptions.NoPathExistsException;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import datastructures.interfaces.IPriorityQueue;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IEdge;
import datastructures.interfaces.IDisjointSet;


/**
 * Represents an undirected, weighted graph, possibly containing self-loops, parallel edges,
 * and unconnected components.
 *
 * Note: This class is not meant to be a full-featured way of representing a graph.
 * We stick with supporting just a few, core set of operations needed for the
 * remainder of the project.
 */
public class Graph<V, E extends IEdge<V> & Comparable<E>> {
    // NOTE 1:
    //
    // Feel free to add as many fields, private helper methods, and private
    // inner classes as you want.
    //
    // And of course, as always, you may also use any of the data structures
    // and algorithms we've implemented so far.
    //
    // Note: If you plan on adding a new class, please be sure to make it a private
    // static inner class contained within this file. Our testing infrastructure
    // works by copying specific files from your project to ours, and if you
    // add new files, they won't be copied and your code will not compile.
    //
    //
    // NOTE 2:
    //
    // You may notice that the generic types of Graph are a little bit more
    // complicated than usual.
    //
    // This class uses two generic parameters: V and E.
    //
    // - 'V' is the type of the vertices in the graph. The vertices can be
    //   any type the client wants -- there are no restrictions.
    //
    // - 'E' is the type of the edges in the graph. We've constrained Graph
    //   so that E *must* always be an instance of IEdge<V> AND Comparable<E>.
    //
    //   What this means is that if you have an object of type E, you can use
    //   any of the methods from both the IEdge interface and from the Comparable
    //   interface
    //
    // If you have any additional questions about generics, or run into issues while
    // working with them, please ask ASAP either on Piazza or during office hours.
    //
    // Working with generics is really not the focus of this class, so if you
    // get stuck, let us know we'll try and help you get unstuck as best as we can.
    private IList<V> vertices;
    private IList<E> edges;
    private IDictionary<V, IList<E>> graph;
    private IDisjointSet<V> paths;

    /**
     * Constructs a new graph based on the given vertices and edges.
     *
     * Note that each edge in 'edges' represents a unique edge. For example, if 'edges'
     * contains an entry for '(A,B)' and for '(B,A)', that means there are two parallel
     * edges between vertex 'A' and vertex 'B'.
     *
     * @throws IllegalArgumentException if any edges have a negative weight
     * @throws IllegalArgumentException if any edges connect to a vertex not present in 'vertices'
     * @throws IllegalArgumentException if 'vertices' or 'edges' are null or contain null
     * @throws IllegalArgumentException if 'vertices' contains duplicates
     */
    public Graph(IList<V> vertices, IList<E> edges) {
        //throw new NotYetImplementedException();
        this.graph = new ChainedHashDictionary<>();
        this.paths = new ArrayDisjointSet<>();
        this.vertices = vertices;
        this.edges = edges;
        // IllegalArgumentException
        for (E edge: edges) {
            if (edge == null) {
                throw new IllegalArgumentException();
            }
            if (edge.getWeight()<0) {
                throw new IllegalArgumentException();
            }
            V v1 = edge.getVertex1();
            V v2 = edge.getVertex2();
            if (!vertices.contains(v1)||!vertices.contains(v2)){
                throw new IllegalArgumentException();
            }
        }
        IDictionary<V, Integer> tempDic = new ChainedHashDictionary<>();
        for (V v : vertices) {
            if (v == null) {
                throw new IllegalArgumentException();
            } else if (tempDic.containsKey(v)){
                throw new IllegalArgumentException();
            } else {
                tempDic.put(v, 1);
            }
        }

        for (V vertex : this.vertices){
            this.paths.makeSet(vertex);
            IList<E> edgeList = new DoubleLinkedList<>();
            for (E edge : edges) {
                if (edge.getVertex1().equals(vertex)|| edge.getVertex2().equals(vertex)) {
                    edgeList.add(edge);
                }
            }
            this.graph.put(vertex, edgeList);
        }
        // sort by weight
        this.edges = Sorter.topKSort(edges.size(), edges);
    }

    /**ChainedHashDictionary
     * Sometimes, we store vertices and edges as sets instead of lists, so we
     * provide this extra constructor to make converting between the two more
     * convenient.
     *
     * @throws IllegalArgumentException if any of the edges have a negative weight
     * @throws IllegalArgumentException if one of the edges connects to a vertex not
     *                                  present in the 'vertices' list
     * @throws IllegalArgumentException if vertices or edges are null or contain null
     */
    public Graph(ISet<V> vertices, ISet<E> edges) {
        // You do not need to modify this method.
        this(setToList(vertices), setToList(edges));
    }

    // You shouldn't need to call this helper method -- it only needs to be used
    // in the constructor above.
    private static <T> IList<T> setToList(ISet<T> set) {
        if (set == null) {
            throw new IllegalArgumentException();
        }
        IList<T> output = new DoubleLinkedList<>();
        for (T item : set) {
            output.add(item);
        }
        return output;
    }

    /**
     * Returns the number of vertices contained within this graph.
     */
    public int numVertices() {
        //throw new NotYetImplementedException();
        return this.graph.size();
    }

    /**
     * Returns the number of edges contained within this graph.
     */
    public int numEdges() {
        //throw new NotYetImplementedException();
        return this.edges.size();
    }

    /**
     * Returns the set of all edges that make up the minimum spanning tree of
     * this graph.
     *
     * If there exists multiple valid MSTs, return any one of them.
     *
     * Precondition: the graph does not contain any unconnected components.
     */
    public ISet<E> findMinimumSpanningTree() {
        //throw new NotYetImplementedException();
        ISet<E> mst = new ChainedHashSet<>();
        for (E edge : this.edges) {
            V v1 = edge.getVertex1();
            V v2 = edge.getVertex2();
            if (this.paths.findSet(v1) != this.paths.findSet(v2)){
                mst.add(edge);
                this.paths.union(v1, v2);
            }
        }
        return mst;
    }

    /**
     * Returns the edges that make up the shortest path from the start
     * to the end.
     *
     * The first edge in the output list should be the edge leading out
     * of the starting node; the last edge in the output list should be
     * the edge connecting to the end node.
     *
     * Return an empty list if the start and end vertices are the same.
     *
     * @throws NoPathExistsException  if there does not exist a path from the start to the end
     * @throws IllegalArgumentException if start or end is null or not in the graph
     */
    public IList<E> findShortestPathBetween(V start, V end) {
        //throw new NotYetImplementedException();
        if (start.equals(end)) {
            return new DoubleLinkedList<>();
        }
        IList<E> output= this.dijkstra(start, end, this.graph);
        if (output.isEmpty()) {
            throw new NoPathExistsException();
        } else {
            return output;
        }
    }

    private IList<E> dijkstra(V start, V end, IDictionary<V, IList<E>> maze) {
        IList<NewV<V, E>> newVertex = new DoubleLinkedList<>();
        IPriorityQueue<NewV<V, E>> toProcess = new ArrayHeap<>();
        ISet<V> processed = new ChainedHashSet<>();
        IList<E> output = new DoubleLinkedList<>();
        // initial the dijkstra
        for (KVPair<V, IList<E>> pair : maze) {
            V vertex = pair.getKey();
            if (vertex.equals(start)) {
                newVertex.add(new NewV<>(vertex, 0.0));
                toProcess.add(new NewV<>(vertex, 0.0));
            }else  {
                newVertex.add(new NewV<>(vertex));
            }
        }
        while (toProcess.size() != 0) {
            NewV<V, E> u = toProcess.peekMin();
            V vertexOfu = u.getVertex();
            // built exit
            if (vertexOfu.equals(end)) {
                return u.path;
            }else if (processed.contains(vertexOfu)){
                toProcess.removeMin();
            }else {
                IList<E> currEdges = maze.get(vertexOfu);
                for (E edge : currEdges) {
                    V otherVertex = edge.getOtherVertex(vertexOfu);
                    for (NewV<V, E> v : newVertex) {
                        if (v.getVertex().equals(otherVertex)){
                            //update
                            if (u.getDistance()+ edge.getWeight()< v.getDistance()) {
                                IList<E> findPath = new DoubleLinkedList<>();
                                for (E e: u.path){
                                    findPath.add(e);
                                }
                                findPath.add(edge);
                                NewV<V, E> finish = new NewV<>(otherVertex, u.getDistance()+edge.getWeight(), findPath);
                                toProcess.add(finish);
                            }
                            break;
                        }
                    }

                }
                processed.add(toProcess.removeMin().getVertex());
            }


        }
        return output;
    }

    private static class NewV<V, E> implements Comparable<NewV<V, E>> {
        public V vertex;
        public double distance;
        IList<E> path;

        public NewV(V vertex) {
            this.vertex =vertex;
            this.distance = Double.POSITIVE_INFINITY;
            this.path =new DoubleLinkedList<>();
        }

        public  NewV(V vertex, Double dist) {
            this.vertex = vertex;
            this.distance = dist;
            this.path = new DoubleLinkedList<>();
        }

        public  NewV(V vertex, Double dist, IList<E> path) {
            this.vertex = vertex;
            this.distance = dist;
            this.path = path;
        }

        public V getVertex(){
            return this.vertex;
        }

        public double getDistance(){
            return this.distance;
        }

        @Override
        public int compareTo(NewV<V, E> newVertex){
            if (this.distance == newVertex.getDistance()){
                return 0;
            } else if (this.distance > newVertex.getDistance()) {
                return 1;
            } else {
                return -1;
            }
        }

    }

}
