import java.awt.*;

public class Node {
    private Point state;
    private Node parent;

    public Node(Point state, Node parent) {
        this.state = state;
        this.parent = parent;
    }

    public Point getState() {
        return state;
    }

    public Node getParent() {
        return parent;
    }
}
