package com.mst;

public class Main {
    public static void main(String[] args) {
        // Create a sample graph
        Graph graph = createSampleGraph();

        // Demonstrate MST with edge removal and replacement
        MSTManager mstManager = new MSTManager(graph);
        mstManager.demonstrateEdgeRemoval();
    }

    private static Graph createSampleGraph() {
        Graph graph = new Graph(6);

        // Add edges (undirected graph)
        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 3);
        graph.addEdge(1, 2, 1);
        graph.addEdge(1, 3, 2);
        graph.addEdge(2, 3, 4);
        graph.addEdge(2, 4, 5);
        graph.addEdge(3, 4, 7);
        graph.addEdge(3, 5, 3);
        graph.addEdge(4, 5, 2);

        return graph;
    }
}