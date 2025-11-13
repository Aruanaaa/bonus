package com.mst;

import java.util.*;

public class MSTManager {
    private Graph graph;
    private List<Graph.Edge> originalMST;

    public MSTManager(Graph graph) {
        this.graph = graph;
        this.originalMST = graph.kruskalMST();
    }

    public void demonstrateEdgeRemoval() {
        System.out.println("=== Minimum Spanning Tree Edge Replacement Demo ===\n");

        // Display original MST
        displayOriginalMST();

        if (originalMST.isEmpty()) {
            System.out.println("No edges in MST to remove.");
            return;
        }

        // Remove a random edge from MST
        Random random = new Random();
        Graph.Edge removedEdge = originalMST.get(random.nextInt(originalMST.size()));
        removeAndReplaceEdge(removedEdge);

        // Also demonstrate with the first edge for consistency
        System.out.println("\n" + "=".repeat(60) + "\n");
        System.out.println("Demonstrating with specific edge removal:");
        removeAndReplaceEdge(originalMST.get(0));
    }

    private void displayOriginalMST() {
        System.out.println("ORIGINAL MINIMUM SPANNING TREE:");
        System.out.println("Edges in MST:");
        int totalWeight = 0;
        for (Graph.Edge edge : originalMST) {
            System.out.println("  " + edge);
            totalWeight += edge.getWeight();
        }
        System.out.println("Total weight: " + totalWeight);
        System.out.println();
    }

    private void removeAndReplaceEdge(Graph.Edge removedEdge) {
        System.out.println("REMOVING EDGE: " + removedEdge);

        // Create MST without the removed edge
        List<Graph.Edge> remainingMST = new ArrayList<>(originalMST);
        remainingMST.remove(removedEdge);

        // Display remaining MST edges
        System.out.println("\nRemaining MST edges after removal:");
        for (Graph.Edge edge : remainingMST) {
            System.out.println("  " + edge);
        }

        // Find connected components using the public method
        Set<Integer> component1 = graph.findConnectedComponent(remainingMST, removedEdge.getSrc());
        Set<Integer> component2 = graph.findConnectedComponent(remainingMST, removedEdge.getDest());

        System.out.println("\nResulting Components:");
        System.out.println("Component 1: " + component1);
        System.out.println("Component 2: " + component2);

        // Find replacement edge
        Graph.Edge replacementEdge = graph.findReplacementEdge(originalMST, removedEdge);

        if (replacementEdge != null) {
            System.out.println("\nREPLACEMENT EDGE FOUND: " + replacementEdge);

            // Create new MST with replacement
            List<Graph.Edge> newMST = new ArrayList<>(remainingMST);
            newMST.add(replacementEdge);

            System.out.println("\nNEW MINIMUM SPANNING TREE:");
            int totalWeight = 0;
            for (Graph.Edge edge : newMST) {
                System.out.println("  " + edge);
                totalWeight += edge.getWeight();
            }
            System.out.println("Total weight: " + totalWeight);

            // Verify the new MST is valid
            if (isValidMST(newMST)) {
                System.out.println("New MST is valid and connects all vertices");
            } else {
                System.out.println("New MST is invalid");
            }
        } else {
            System.out.println("\nNo replacement edge found! Graph cannot be reconnected.");
        }
    }

    private boolean isValidMST(List<Graph.Edge> mst) {
        // Check if MST has exactly V-1 edges
        if (mst.size() != graph.getVertices() - 1) {
            return false;
        }

        // Check if all vertices are connected
        Set<Integer> visited = new HashSet<>();
        Map<Integer, List<Integer>> adjList = new HashMap<>();

        for (Graph.Edge edge : mst) {
            adjList.computeIfAbsent(edge.getSrc(), k -> new ArrayList<>()).add(edge.getDest());
            adjList.computeIfAbsent(edge.getDest(), k -> new ArrayList<>()).add(edge.getSrc());
        }

        // BFS to check connectivity
        Queue<Integer> queue = new LinkedList<>();
        queue.add(0);
        visited.add(0);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            if (adjList.containsKey(current)) {
                for (int neighbor : adjList.get(current)) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
        }

        return visited.size() == graph.getVertices();
    }

    public List<Graph.Edge> getOriginalMST() {
        return originalMST;
    }
}