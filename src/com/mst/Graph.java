package com.mst;

import java.util.*;

public class Graph {
    private int vertices;
    private List<Edge> edges;
    private List<List<Edge>> adjacencyList;

    public Graph(int vertices) {
        this.vertices = vertices;
        this.edges = new ArrayList<>();
        this.adjacencyList = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }

    public void addEdge(int src, int dest, int weight) {
        Edge edge = new Edge(src, dest, weight);
        edges.add(edge);
        adjacencyList.get(src).add(edge);
        adjacencyList.get(dest).add(edge);
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public int getVertices() {
        return vertices;
    }

    public List<List<Edge>> getAdjacencyList() {
        return adjacencyList;
    }

    // Kruskal's algorithm for MST
    public List<Edge> kruskalMST() {
        List<Edge> mst = new ArrayList<>();
        edges.sort(Comparator.comparingInt(Edge::getWeight));

        UnionFind uf = new UnionFind(vertices);

        for (Edge edge : edges) {
            if (uf.find(edge.getSrc()) != uf.find(edge.getDest())) {
                mst.add(edge);
                uf.union(edge.getSrc(), edge.getDest());
            }
        }

        return mst;
    }

    // Prim's algorithm for MST (alternative implementation)
    public List<Edge> primMST() {
        List<Edge> mst = new ArrayList<>();
        boolean[] visited = new boolean[vertices];
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(Edge::getWeight));

        // Start from vertex 0
        visited[0] = true;
        pq.addAll(adjacencyList.get(0));

        while (!pq.isEmpty() && mst.size() < vertices - 1) {
            Edge edge = pq.poll();

            if (visited[edge.getSrc()] && visited[edge.getDest()]) {
                continue;
            }

            mst.add(edge);

            int newVertex = visited[edge.getSrc()] ? edge.getDest() : edge.getSrc();
            visited[newVertex] = true;

            for (Edge adjEdge : adjacencyList.get(newVertex)) {
                if (!visited[adjEdge.getOtherVertex(newVertex)]) {
                    pq.add(adjEdge);
                }
            }
        }

        return mst;
    }

    // Find replacement edge after removing an edge from MST
    public Edge findReplacementEdge(List<Edge> mst, Edge removedEdge) {
        // Remove the edge from MST
        List<Edge> remainingMST = new ArrayList<>(mst);
        remainingMST.remove(removedEdge);

        // Find the two components created by removal
        Set<Integer> component1 = findConnectedComponent(remainingMST, removedEdge.getSrc());
        Set<Integer> component2 = findConnectedComponent(remainingMST, removedEdge.getDest());

        // Find the minimum weight edge connecting the two components
        Edge replacement = null;
        int minWeight = Integer.MAX_VALUE;

        for (Edge edge : edges) {
            if (edge.equals(removedEdge)) continue;

            boolean srcInComp1 = component1.contains(edge.getSrc());
            boolean destInComp1 = component1.contains(edge.getDest());
            boolean srcInComp2 = component2.contains(edge.getSrc());
            boolean destInComp2 = component2.contains(edge.getDest());

            // Check if edge connects the two components
            if ((srcInComp1 && destInComp2) || (srcInComp2 && destInComp1)) {
                if (edge.getWeight() < minWeight) {
                    minWeight = edge.getWeight();
                    replacement = edge;
                }
            }
        }

        return replacement;
    }

    private Set<Integer> findConnectedComponent(List<Edge> edges, int startVertex) {
        Set<Integer> component = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();

        queue.add(startVertex);
        component.add(startVertex);

        // Build adjacency list from edges
        Map<Integer, List<Integer>> adjList = new HashMap<>();
        for (Edge edge : edges) {
            adjList.computeIfAbsent(edge.getSrc(), k -> new ArrayList<>()).add(edge.getDest());
            adjList.computeIfAbsent(edge.getDest(), k -> new ArrayList<>()).add(edge.getSrc());
        }

        while (!queue.isEmpty()) {
            int current = queue.poll();

            if (adjList.containsKey(current)) {
                for (int neighbor : adjList.get(current)) {
                    if (!component.contains(neighbor)) {
                        component.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
        }

        return component;
    }

    public static class Edge {
        private int src;
        private int dest;
        private int weight;

        public Edge(int src, int dest, int weight) {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
        }

        public int getSrc() { return src; }
        public int getDest() { return dest; }
        public int getWeight() { return weight; }

        public int getOtherVertex(int vertex) {
            if (vertex == src) return dest;
            if (vertex == dest) return src;
            throw new IllegalArgumentException("Vertex not part of edge");
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Edge edge = (Edge) obj;
            return src == edge.src && dest == edge.dest && weight == edge.weight;
        }

        @Override
        public int hashCode() {
            return Objects.hash(src, dest, weight);
        }

        @Override
        public String toString() {
            return src + " - " + dest + " (weight: " + weight + ")";
        }
    }

    // Union-Find data structure for Kruskal's algorithm
    private static class UnionFind {
        private int[] parent;
        private int[] rank;

        public UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX != rootY) {
                if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                } else if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                } else {
                    parent[rootY] = rootX;
                    rank[rootX]++;
                }
            }
        }
    }
}