 MST Edge Replacement


 What the program does

1. Builds a Minimum Spanning Tree using Kruskal’s algorithm.
2. Prints all MST edges and the total weight.
3. Removes one edge from the MST.
4. Shows which vertices became disconnected (two components).
5. Finds a new edge that reconnects them with the smallest possible weight.
6. Prints the new MST and checks if it’s still valid.

So basically, it demonstrates how to keep the MST connected after removing an edge.


How to run

Terminal

javac -d out src/com/mst/*.java
java -cp out com.mst.Main

--

Project structure

src/com/mst/
Main.java        // main class
Graph.java       // graph and MST algorithms
MSTManager.java  // handles removal and replacement logic
Example output

=== Minimum Spanning Tree Edge Replacement Demo ===

ORIGINAL MINIMUM SPANNING TREE:
1 - 2 (weight: 1)
1 - 3 (weight: 2)
4 - 5 (weight: 2)
0 - 2 (weight: 3)
3 - 5 (weight: 3)
Total weight: 11

REMOVING EDGE: 1 - 2 (weight: 1)

Resulting Components:
Component 1: [1, 3, 4, 5]
Component 2: [0, 2]

REPLACEMENT EDGE FOUND: 2 - 3 (weight: 4)
New MST is valid and connects all vertices.