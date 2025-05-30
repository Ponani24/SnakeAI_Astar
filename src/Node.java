import java.util.Objects;

public class Node {
	int x, y;
    int g, h, f;
    Node parent;

    // Constructor
    public Node(int x, int y, Node parent) {
        this.x = x;
        this.y = y;
        this.g = 0;
        this.h = 0;
        this.f = 0;
        this.parent = parent;
    }

    // Calculate g value
    public void calculateG() {
        if (parent != null) {
            // Assume the cost from parent to this node is 1 for simplicity
            g = parent.g + 1;
        }
    }

    // Calculate h value
    public void calculateH(Node goal) {
        h = Math.abs(this.x - goal.x) + Math.abs(this.y - goal.y);
    }

    // Calculate f value
    public void calculateF(Node goal) {
        //calculateG();  // Ensure g is updated
        //calculateH(goal);  // Ensure h is updated
        f = g + h;  // Calculate f using the updated g and h
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Node node = (Node) obj;
        return x == node.x && y == node.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
