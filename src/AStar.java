import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class AStar {
	private Node startNode;
    private Node goalNode;
    private Set<Node> closedList;
    private PriorityQueue<Node> openList;
    private Map<Node, Node> cameFrom;
    //private List<Node> obstacles;  // New list for obstacles
    private Map<Coordinate, Boolean> obstacles; 
    //private List<Node> zombieNodes;
    //private List<Node> mysnake;
    //private List<Node>  other_snakes;
    

    
    public AStar(Node start, Node goal,Map<Coordinate, Boolean> obstacles) {
        this.startNode = start;
        this.goalNode = goal;
        this.obstacles = obstacles;  // Store obstacles
        /*this.zombieNodes = zombieNodes; 
        this.mysnake = mysnake; 
        this.other_snakes = other_snakes;*/ 
        this.closedList = new HashSet<>();
        this.openList = new PriorityQueue<>(Comparator.comparingInt(n -> n.f));
        this.cameFrom = new HashMap<>();
    }


    // Find path using A* algorithm
    public List<Node> findPath() {
        startNode.calculateF(goalNode);  // Initialize start node
        openList.add(startNode);

        while (!openList.isEmpty()) {
            Node current = openList.poll();  // Get the node with the lowest f

            if (current.equals(goalNode)) {
                return reconstructPath(current);
            }

            closedList.add(current);

            for (Node neighbor : getNeighbors(current)) {
                if (closedList.contains(neighbor)) {
                    continue;  // Skip nodes already processed
                }

                int tentativeGScore = current.g + distance(current, neighbor);

                if (!openList.contains(neighbor)) {
                    neighbor.calculateG();  // Ensure g is updated
                    neighbor.calculateH(goalNode);  // Ensure h is updated
                    neighbor.calculateF(goalNode);  // Calculate f using updated g and h
                    openList.add(neighbor);
                } else if (tentativeGScore >= neighbor.g) {
                    continue;  // Skip if this path is not better
                }

                cameFrom.put(neighbor, current);
                neighbor.g = tentativeGScore;
                neighbor.calculateF(goalNode);  // Update f value
            }
        }

        return Collections.emptyList();  // Return empty list if no path found
    }

    // Reconstruct the path from goal to start
    private List<Node> reconstructPath(Node current) {
        List<Node> path = new ArrayList<>();
        while (current != null) {
            path.add(current);
            current = cameFrom.get(current);  // Use the parent reference to trace back
        }
        Collections.reverse(path);  // Reverse to get the path from start to goal
        return path;
    }

    
    
    // Generate neighbors, avoiding obstacles

private List<Node> getNeighbors(Node node) {
    List<Node> neighbors = new ArrayList<>();
    int x = node.x;
    int y =node.y;
    // Create neighbors: right, left, down, up


   



if (x + 1 < 50) {
    // Node right = new Node(node.x + 1, node.y, node);
     Coordinate rightt = new Coordinate(x+1, y);
     
     if (!obstacles.containsKey(rightt) ) {
         //thing2.add(right);
    	 Node right = new Node(node.x + 1, node.y, node);
         neighbors.add(right);
     }
 }
 if (x - 1 >= 0) {
    // Node left = new Node(node.x - 1, node.y, node);
	 
     Coordinate leftt = new Coordinate(x-1, y);
     if (!obstacles.containsKey(leftt)) {
         //thing2.add(left);
    	 Node left = new Node(node.x - 1, node.y, node);
    	 neighbors.add(left);
         
     }
 }
 if (y + 1 < 50) {
     //Node down = new Node(node.x, node.y + 1, node);
	 
     Coordinate downn = new Coordinate(x, y+1);
     if (!obstacles.containsKey(downn)) {
         //thing2.add(down);
         //thing2.put(down, false);
    	 Node down = new Node(node.x, node.y + 1, node);
         neighbors.add(down);
     }
 }
 if (y - 1 >= 0) {
     //Node up = new Node(node.x, node.y - 1, node);
	
     Coordinate upp = new Coordinate(x, y-1);
     if (!obstacles.containsKey(upp)) {
         //thing2.add(up);
        // thing2.put(up, false);
    	 Node up = new Node(node.x, node.y - 1, node);
         neighbors.add(up);
     }
 }

    return neighbors;
}

    // Calculate distance between two nodes (Manhattan distance)
    private int distance(Node a, Node b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }
    
    public List<Node> findSafeMove(){
    	 List<Node> safeMoves = new ArrayList<>();
    	 safeMoves.add(startNode);
         // Check neighboring cells around the start node (snake head)
         for (Node neighbor : getNeighbors(startNode)) {
             if (!obstacles.containsKey(neighbor)) {
                 safeMoves.add(neighbor);  // Add all valid safe moves
             }
         }

         // If no safe moves are found, return an empty list
         if (safeMoves.isEmpty()) {
             return Collections.emptyList();
         }

         // Return the list of safe moves
         return safeMoves;
    }
    
    public List<Coordinate> dfsLongestPath(Node startNode) {
        List<Coordinate> longestPath = new ArrayList<>();
        List<Coordinate> currentPath = new ArrayList<>();
        Set<Coordinate> visited = new HashSet<>();
        Coordinate first = new Coordinate(startNode.x,startNode.y);
        dfs(first, visited, currentPath, longestPath);
        if (longestPath.isEmpty()) {
            return Collections.emptyList();
        }
        return longestPath;  // Return the longest path found
    }
    
    
    
    private void dfs(Coordinate first, Set<Coordinate> visited, List<Coordinate> currentPath, List<Coordinate> longestPath) {
    	if (currentPath.size() >= 13) {  // Limit path size to 10
            return;  // Stop further recursion
        }

    	visited.add(first);
        currentPath.add(first);  // Add the current coordinate to the path

        List<Coordinate> neighbors = getNeighborss(first.x, first.y);
        boolean hasUnvisitedNeighbor = false;

        for (Coordinate neighbor : neighbors) {
            if (!visited.contains(neighbor)) {
                hasUnvisitedNeighbor = true;
                dfs(neighbor, visited, currentPath, longestPath);
            }
        }

        // If no unvisited neighbors, check the current path length
        if (!hasUnvisitedNeighbor) {
            if (currentPath.size() > longestPath.size()) {
                longestPath.clear();
                longestPath.addAll(currentPath);  // Store the current path
            }
        }

        // Backtrack
        visited.remove(first);
        currentPath.remove(currentPath.size() - 1);  // Remove the last coordinate from the current path
        if (longestPath.isEmpty()) {
            return;
        }
    }

    
    

    public ArrayList<Coordinate> getNeighborss(int x,int y) {
        ArrayList<Coordinate> thing = new ArrayList<>();
    	//Node node = new Node(x,y,null);
    	//Coordinate coord = new Coordinate(x, y);// you are not actually using this
        if (x - 1 >= 0) {
            // Node left = new Node(node.x - 1, node.y, node);
             Coordinate left = new Coordinate(x-1, y);
             if (!obstacles.containsKey(left)) {
                 thing.add(left);
                 
             }
         }
            if (x + 1 < 50) {
           // Node right = new Node(node.x + 1, node.y, node);
            Coordinate right = new Coordinate(x+1, y);
            if (!obstacles.containsKey(right) ) {
                thing.add(right);
                
            }
        }
        
        if (y - 1 >= 0) {
            //Node up = new Node(node.x, node.y - 1, node);
            Coordinate up = new Coordinate(x, y-1);
            if (!obstacles.containsKey(up)) {
                thing.add(up);
                
            }
        }

        if (y + 1 < 50) {
            //Node down = new Node(node.x, node.y + 1, node);
            Coordinate down = new Coordinate(x, y+1);
            if (!obstacles.containsKey(down)) {
                thing.add(down);
                
            }
        }
        
return thing;
            
        }
    
    
}
