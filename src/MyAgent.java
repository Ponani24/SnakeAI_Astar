import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import za.ac.wits.snake.DevelopmentAgent;

public class MyAgent extends DevelopmentAgent {

    public static void main(String args[]) {
        MyAgent agent = new MyAgent();
        MyAgent.start(agent, args);
    }

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String initString = br.readLine();
            String[] temp = initString.split(" ");
            int nSnakes = Integer.parseInt(temp[0]);

            while (true) {
                String line = br.readLine();
                if (line.contains("Game Over")) {
                    break;
                }

                String apple1 = line;
                String[] applecoords = apple1.split(" ");
                int applex = Integer.parseInt(applecoords[0]);
                int appley = Integer.parseInt(applecoords[1]);
                //do stuff with apples
                Node goalNode = new Node(applex, appley, null);
                HashMap<Coordinate, Boolean> obstacleNodes = new HashMap<>();
                HashMap<Coordinate, Boolean> zombieNodes = new HashMap<>();
                HashMap<Coordinate, Boolean> mysnake = new HashMap<>();
                HashMap<Coordinate, Boolean> other_snakes = new HashMap<>();
                HashMap<Coordinate, Boolean> allObstacles = new HashMap<>();
                ArrayList<Integer> thing = new ArrayList<>();
                HashMap<Coordinate, Boolean> thing2 = new HashMap<>();
               
                // read in obstacles and do something with them!
                int nObstacles = 3;
                for (int obstacle = 0; obstacle < nObstacles; obstacle++) {
                    String obs = br.readLine();
                    /// do something with obs
                    String[] coords = obs.split(" ");
                    for(int j = 0; j < coords.length - 1; j++){
                        drawLine(coords[j], coords[j+1],obstacleNodes);
                    }
                    
                   allObstacles.putAll(obstacleNodes);
                }

                // read in zombies and do something with them!
                int nZombies = 3;
                for (int zombie = 0; zombie < nZombies; zombie++) {
                    String zom = br.readLine();
                    /// do something with zom
                    String[] coords = zom.split(" ");
                    
                    String[] headParts = coords[0].split(",");
                    
                    int headX = Integer.parseInt(headParts[0]);
                    int headY = Integer.parseInt(headParts[1]);
                    getNeighbors(headX,headY,obstacleNodes,zombieNodes);
                   
                    
                    for(int j = 0; j < coords.length - 1; j++){
                        drawLine(coords[j], coords[j+1],zombieNodes);
                    }
                    allObstacles.putAll(zombieNodes);
                }
            

                int mySnakeNum = Integer.parseInt(br.readLine());
                Node startNode = null;
                for (int i = 0; i < nSnakes; i++) {
                    String snakeLine = br.readLine();
                    String[] parts = snakeLine.split(" ");
                    String ch = parts[0];
                 // Skip the first three pieces of information
                    int startIdx = 3;  // Index where coordinates start
                    String[] coords = Arrays.copyOfRange(parts, startIdx, parts.length);
                    if (i == mySnakeNum) {
                        //hey! That's me :)
                    	String[] headParts = coords[0].split(",");
                    	String[] tailParts = coords[coords.length-1].split(",");
                        int headX = Integer.parseInt(headParts[0]);
                        int headY = Integer.parseInt(headParts[1]);
                        //tailx = Integer.parseInt(tailParts[0]);
                        //taily = Integer.parseInt(tailParts[1]);
                        // Create and add the head node as the start node
                        startNode = new Node(headX, headY, null);
                        for(int j = 0; j < coords.length - 1; j++){
                            drawLine(coords[j], coords[j+1],mysnake);
                        }
                        allObstacles.putAll(mysnake);
                        continue;
                    }
                    if(ch.equals("dead")) {
                    	continue;
                    }
                    //do stuff with other snakes
                    else {
                        // Handle other snakes as usual
                    	String[] headParts = coords[0].split(",");
                        int headX = Integer.parseInt(headParts[0]);
                        int headY = Integer.parseInt(headParts[1]);
                        thing.add(headX);
                        thing.add(headY);
                        for(int j = 0; j < coords.length - 1; j++){
                            drawLine(coords[j], coords[j+1],other_snakes);
                    }
                        allObstacles.putAll(other_snakes); 
                        
                        
                    }
                }
               
                HashMap<Coordinate, Boolean> nei = new HashMap<>();
                int xm = startNode.x;
                int ym = startNode.y;
                getNeighbors(xm,ym,obstacleNodes,nei);
                for (int i = thing.size() - 2; i >= 0; i -= 2) {
                    //Node node = new Node(thing.get(i), thing.get(i + 1), null);
                    Coordinate node = new Coordinate(thing.get(i), thing.get(i + 1));
                    if (nei.containsKey(node)) {
                        thing.remove(i + 1);  // Remove the second element first
                        thing.remove(i);      // Then remove the first element
                    }
                }
                
                for(int i = 0; i < thing.size();i+=2) {
                	int x = thing.get(i);
                	int y = thing.get(i+1);
                	getNeighbors2(x,y,thing2);
                	
                }
                for (Coordinate key : thing2.keySet()) {
                    if (!allObstacles.containsKey(key)) {
                        allObstacles.put(key, false); // Add the key with a value of false
                    }
                }

                
                AStar aStar = new AStar(startNode, goalNode,allObstacles);
                List<Node> path = aStar.findPath();
                if (path.size() > 1) {
                    Node startNode1 = path.get(0);      // The start node
                    Node nextNode1 = path.get(1);       // The node right after the start node

                // Compare their x and y values to determine the direction
                if (nextNode1.x > startNode1.x) {
                    System.out.println(3);//right
                } else if (nextNode1.x < startNode1.x) {
                    System.out.println(2);//left
                } else if (nextNode1.y > startNode1.y) {
                    System.out.println(1);//down
                } else if (nextNode1.y < startNode1.y) {
                    System.out.println(0);//up
                }

            }
                else {
                	List<Coordinate> path2 = aStar.dfsLongestPath(startNode);
                	
                	
                	/*List<Node> path2 = aStar.findSafeMove();
                	if (path2.size() > 1) {
                        Node startNode1 = path2.get(0);      // The start node
                        Node nextNode1 = path2.get(1);       // The node right after the start node

                    // Compare their x and y values to determine the direction
                    if (nextNode1.x > startNode1.x) {
                        System.out.println(3);//right
                    } else if (nextNode1.x < startNode1.x) {
                        System.out.println(2);//left
                    } else if (nextNode1.y > startNode1.y) {
                        System.out.println(1);//down
                    } else if (nextNode1.y < startNode1.y) {
                        System.out.println(0);//up
                    }

                }*/
                	if (path2.size() > 1) {
                        Coordinate startNode1 = path2.get(0);      // The start node
                        Coordinate nextNode1 = path2.get(1);       // The node right after the start node

                    // Compare their x and y values to determine the direction
                    if (nextNode1.x > startNode1.x) {
                        System.out.println(3);//right
                    } else if (nextNode1.x < startNode1.x) {
                        System.out.println(2);//left
                    } else if (nextNode1.y > startNode1.y) {
                        System.out.println(1);//down
                    } else if (nextNode1.y < startNode1.y) {
                        System.out.println(0);//up
                    }

                }
                }
                
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void drawLine(String coord1, String coord2,HashMap<Coordinate, Boolean> thing) {
    	
    	
        String[] coord1Parts = coord1.split(",");
        String[] coord2Parts = coord2.split(",");

        int x1 = Integer.parseInt(coord1Parts[0]);
        int y1 = Integer.parseInt(coord1Parts[1]);

        int x2 = Integer.parseInt(coord2Parts[0]);
        int y2 = Integer.parseInt(coord2Parts[1]);

        if (x1 == x2) {
            if (y1 < y2) {
                for (int i = y1; i <= y2; i++) {
                    
                   
                    Coordinate coord = new Coordinate(x1, i);
               	 	thing.put(coord, false);
                }
            } else {
                for (int i = y2; i <= y1; i++) {
                    
                     
                     Coordinate coord = new Coordinate(x1, i);
                	 thing.put(coord, false);
                     
                }
            }
        } else {
            if (x1 < x2) {
                for (int i = x1; i <= x2; i++) {
                    
                     
                     Coordinate coord = new Coordinate(i, y1);
                	 thing.put(coord, false);
                }
            } else {
                for (int i = x2; i <= x1; i++) {
                    
                     
                     Coordinate coord = new Coordinate(i, y1);
                	 thing.put(coord, false);
                }
            }
        }
    }

    public void getNeighbors(int x,int y,HashMap<Coordinate, Boolean> thing,HashMap<Coordinate, Boolean> thing2) {
        
    	//Node node = new Node(x,y,null);
    	//Coordinate coord = new Coordinate(x, y);// you are not actually using this

            if (x + 1 < 50) {
           // Node right = new Node(node.x + 1, node.y, node);
            Coordinate right = new Coordinate(x+1, y);
            if (!thing.containsKey(right) ) {
                //thing2.add(right);
                thing2.put(right, false);
            }
        }
        if (x - 1 >= 0) {
           // Node left = new Node(node.x - 1, node.y, node);
            Coordinate left = new Coordinate(x-1, y);
            if (!thing.containsKey(left)) {
                //thing2.add(left);
                thing2.put(left, false);
            }
        }
        if (y + 1 < 50) {
            //Node down = new Node(node.x, node.y + 1, node);
            Coordinate down = new Coordinate(x, y+1);
            if (!thing.containsKey(down)) {
                //thing2.add(down);
                thing2.put(down, false);
            }
        }
        if (y - 1 >= 0) {
            //Node up = new Node(node.x, node.y - 1, node);
            Coordinate up = new Coordinate(x, y-1);
            if (!thing.containsKey(up)) {
                //thing2.add(up);
                thing2.put(up, false);
            }
        }


            
        }
    
    public void getNeighbors2(int x,int y,HashMap<Coordinate, Boolean> thing) {
        //List<Node> neighbors = new ArrayList<>();
        
        // Create neighbors: right, left, down, up
    Node node = new Node(x,y,null);

        if (x + 1 < 50) {
        //Node right = new Node(node.x + 1, node.y, node);
        Coordinate right = new Coordinate(x+1, y);
        if (!thing.containsKey(right)) {
            //thing.add(right);
            thing.put(right, false);
        }
        
        }
        if (x + 2 < 50) {
            //Node right2 = new Node(node.x + 2, node.y, node);
            Coordinate right2 = new Coordinate(x+2, y);
            if (!thing.containsKey(right2)) {
               
                thing.put(right2, false);
            }
            
        
    }
        
    if (x - 1 >= 0) {
        //Node left = new Node(node.x - 1, node.y, node);
        Coordinate left = new Coordinate(x-1, y);
        if (!thing.containsKey(left)) {
           
            thing.put(left, false);
        }
    }

    if (x - 2 >= 0) {
        //Node left2 = new Node(node.x - 2, node.y, node);
        Coordinate left2 = new Coordinate(x-2, y);
        if (!thing.containsKey(left2)) {
            
            thing.put(left2, false);
        }
    }

    if (y + 1 < 50) {
       // Node down = new Node(node.x, node.y + 1, node);
        Coordinate down = new Coordinate(x, y+1);
        if (!thing.containsKey(down)) {
            //thing.add(down);
            thing.put(down, false);
        }
    }

    if (y + 2 < 50) {
        //Node down2 = new Node(node.x, node.y + 2, node);
        Coordinate down2 = new Coordinate(x, y+2);
        if (!thing.containsKey(down2)) {
           // thing.add(down2);
            thing.put(down2, false);
        }
    }

    if (y - 1 >= 0) {
        //Node up = new Node(node.x, node.y - 1, node);
        Coordinate up = new Coordinate(x, y-1);
        if (!thing.containsKey(up)) {
            //thing.add(up);
            thing.put(up, false);
        }
    }

    if (y - 2 >= 0) {
        //Node up2 = new Node(node.x, node.y - 2, node);
        Coordinate up2 = new Coordinate(x, y-2);
        if (!thing.containsKey(up2)) {
            //thing.add(up2);
            thing.put(up2, false);
        }
    }

        
    }
}