import javax.imageio.ImageIO;
import javax.swing.border.StrokeBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class Maze {

    private Point startingPoint;
    private Point destinationPoint;
    private ArrayList<Point> walls;
    private ArrayList<Point> path;

    private int maxX;
    private int maxY;
    private Node solution;

    public Maze(String maze) {
        /*
        ##A####
        #
        #### #
        #### #
        ###  #
        ##B  ##
        */
        ArrayList<Point> walls = new ArrayList<>();
        ArrayList<Point> path = new ArrayList<>();
        int x = 0;
        int y = 0;
        for (int i = 0; i < maze.length(); i++)  {
            if (maze.charAt(i) == '\n') {
                y++;
                x = 0;
                continue;
            }
            if (x > maxX) maxX = x;
            if (y > maxY) maxY = y;
            if (maze.charAt(i) == '#') {
                walls.add(new Point(x, y));
            } else if (maze.charAt(i) == ' ') {
                path.add(new Point(x, y));
            } else if (maze.charAt(i) == 'A') {
                startingPoint = new Point(x, y);
            } else if (maze.charAt(i) == 'B') {
                destinationPoint = new Point(x, y);
                path.add(destinationPoint);
            }
            x++;
        }
        this.walls = walls;
        this.path = path;
    }

    private ArrayList<Point> getNeighbors(Point point) {
        ArrayList<Point> result = new ArrayList<>();
        Point top = new Point(point.x, point.y - 1);
        Point bottom = new Point(point.x, point.y + 1);
        Point left = new Point(point.x - 1, point.y);
        Point right = new Point(point.x + 1, point.y);
        if (path.contains(top)) result.add(top);
        if (path.contains(bottom)) result.add(bottom);
        if (path.contains(right)) result.add(right);
        if (path.contains(left)) result.add(left);
        return result;
    }

    public Node solve() {
        ArrayList<Point> visited = new ArrayList<>();

        // Create frontier that has only the initial state.
        Stack<Node> frontier = new Stack<>(); // OR QUEUE.
        frontier.add(new Node(startingPoint, null));

        // Repeat the following:
        //      1. If frontier is empty, return no solution.
        //      2. Remove an element, if it's out destination, we're done.
        //      3. Add neighbours to the frontier
        while (true) {
            if (frontier.isEmpty()) return null;
            Node temp = frontier.pop();
            if (temp.getState().equals(destinationPoint)) return solution = temp;

            for (Point point : getNeighbors(temp.getState())) {
               if (!visited.contains(point)) {
                    visited.add(point);
                    frontier.add(new Node(point, temp));
                }
            }

        }

    }

    public void print() throws IOException {
        if (solution == null) return;
        Node tempSolution = solution;
        ArrayList<Point> correctPath = new ArrayList<>();
        while (tempSolution.getParent() != null) {
            correctPath.add(tempSolution.getState());
            tempSolution = tempSolution.getParent();
        }
        final int SQUARE_SIDE_LENGTH = 50;
        BufferedImage bufferedImageSolution = new BufferedImage((maxX + 1) * SQUARE_SIDE_LENGTH, (maxY + 1) * SQUARE_SIDE_LENGTH, BufferedImage.TYPE_INT_RGB);
        BufferedImage bufferedImageMaze = new BufferedImage((maxX + 1) * SQUARE_SIDE_LENGTH, (maxY + 1) * SQUARE_SIDE_LENGTH, BufferedImage.TYPE_INT_RGB);

        Graphics solutionGraphics = bufferedImageSolution.createGraphics();
        Graphics mazeGraphics = bufferedImageMaze.createGraphics();

        solutionGraphics.setColor(Color.black);
        solutionGraphics.fillRect(0, 0, (maxX + 1) * SQUARE_SIDE_LENGTH, (maxY + 1) * SQUARE_SIDE_LENGTH);

        mazeGraphics.setColor(Color.black);
        mazeGraphics.fillRect(0, 0, (maxX + 1) * SQUARE_SIDE_LENGTH, (maxY + 1) * SQUARE_SIDE_LENGTH);

        for (int i = 0; i <= maxY; i++) {
            for (int j = 0; j <= maxX; j++) {
                solutionGraphics.setColor(Color.white);
                mazeGraphics.setColor(Color.white);
                solutionGraphics.fillRect(j * SQUARE_SIDE_LENGTH, i * SQUARE_SIDE_LENGTH, SQUARE_SIDE_LENGTH, SQUARE_SIDE_LENGTH);
                mazeGraphics.fillRect(j * SQUARE_SIDE_LENGTH, i * SQUARE_SIDE_LENGTH, SQUARE_SIDE_LENGTH, SQUARE_SIDE_LENGTH);
                Point temp = new Point(j, i);
                if (startingPoint.equals(temp)) {
                    solutionGraphics.setColor(Color.blue);
                    mazeGraphics.setColor(Color.blue);
                    System.out.printf("A");
                } else if (destinationPoint.equals(temp)) {
                    solutionGraphics.setColor(Color.blue);
                    mazeGraphics.setColor(Color.blue);
                    System.out.printf("B");
                } else if (walls.contains(temp)) {
                    solutionGraphics.setColor(Color.gray);
                    mazeGraphics.setColor(Color.gray);

                    System.out.printf("%c", '#');
                } else if (correctPath.contains(temp)) {
                    solutionGraphics.setColor(Color.red);
                    mazeGraphics.setColor(Color.black);
                    System.out.printf("+");
                } else {
                    solutionGraphics.setColor(Color.black);
                    mazeGraphics.setColor(Color.black);
                    System.out.printf(" ");
                }
                solutionGraphics.fillRect(1 + j * SQUARE_SIDE_LENGTH, 1 + i * SQUARE_SIDE_LENGTH, SQUARE_SIDE_LENGTH - 2, SQUARE_SIDE_LENGTH - 2);
                mazeGraphics.fillRect(1 + j * SQUARE_SIDE_LENGTH, 1 + i * SQUARE_SIDE_LENGTH, SQUARE_SIDE_LENGTH - 2, SQUARE_SIDE_LENGTH - 2);

            }
            System.out.println();
        }
        ImageIO.write(bufferedImageSolution, "jpg", new File("MazeSolved.jpg"));
        ImageIO.write(bufferedImageMaze, "jpg", new File("Maze.jpg"));
    }
}
