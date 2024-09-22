
//struct for the returned data
import java.util.ArrayList;
import java.util.List;

/**
 * The CanoeData class represents data related to canoe costs and optimal paths.
 * It includes the cost associated with the canoe data, an optimal matrix, and the time taken in nanoseconds.
 * The class provides methods to print the optimal matrix, print the cost, and print the optimal path.
 * 
 * <p>Constructor:</p>
 * <ul>
 * <li>{@link #CanoeData(int, int[][], long)}: Constructs a CanoeData object with the specified cost, optimal matrix, and time in nanoseconds.</li>
 * </ul>
 * 
 * <p>Methods:</p>
 * <ul>
 * <li>{@link #printOptimalMatrix()}: Prints the optimal matrix.</li>
 * <li>{@link #printCost()}: Prints the cost.</li>
 * <li>{@link #printPath()}: Prints the optimal path.</li>
 * </ul>
 */
class CanoeData {
    int cost;
    int[][] optimalMatrix;
    long nanoSeconds;

    /**
     * Constructs a CanoeData object with the specified cost, optimal matrix, and time in nanoseconds.
     *
     * @param cost the cost associated with the canoe data
     * @param optimalMatrix a 2D array representing the optimal matrix
     * @param nanoSeconds the time taken in nanoseconds
     */
    public CanoeData(int cost, int[][] optimalMatrix, long nanoSeconds) {
        this.cost = cost;
        this.optimalMatrix = optimalMatrix;
        this.nanoSeconds = nanoSeconds;
    }

    /**
     * Prints the optimal matrix in a formatted manner.
     * 
     * The method iterates through each element of the optimalMatrix and prints it.
     * If the element is Integer.MAX_VALUE, 0, or -1, it prints spaces instead.
     * Otherwise, it prints the element followed by a space.
     * Each row of the matrix is printed on a new line.
     */
    public void printOptimalMatrix() {
        System.out.println("Optimal Matrix:");
        for (int row = 0; row < optimalMatrix.length; row++) {
            for (int col = 0; col < optimalMatrix[row].length; col++) {
                if (optimalMatrix[row][col] == Integer.MAX_VALUE || optimalMatrix[row][col] == 0 || optimalMatrix[row][col] == -1) {
                    System.out.print("   ");
                } else {
                    System.out.print(optimalMatrix[row][col] + " ");
                }
            }
            System.out.println();
        }
    }

    /**
     * Prints the cost of the canoe to the standard output.
     * The cost is displayed in the format "Cost: <cost>".
     */
    public void printCost() {
        System.out.println("Cost: " + cost);
    }

    /**
     * Prints the optimal path based on the optimalMatrix.
     * The method starts at the first row, finds the lowest cost in the first row 
     * that is not zero, and then adds the start and end posts to the path.
     * Finally, it prints the path.
     */
    public void printPath() {
        List<Integer> optimalPath = new ArrayList<>();
        //we HAVE to start at the 0th post
        int row = 0;

        optimalPath.add(row); // Add the start post

        while (row != optimalMatrix.length - 1) {
            int min = Integer.MAX_VALUE;
            int minIndex = 0;
            for (int col = 0; col < optimalMatrix[row].length; col++) {
                if (optimalMatrix[row][col] < min && optimalMatrix[row][col] > 0) {
                    min = optimalMatrix[row][col];
                    minIndex = col;
                }
            }

            optimalPath.add(minIndex);
            row = minIndex;
        }

        System.out.print("Optimal Path: ");
        //print out the path
        for (int optimalPathIndex = 0; optimalPathIndex < optimalPath.size() - 1; optimalPathIndex++) {
            System.out.print(optimalPath.get(optimalPathIndex) + " -> ");
        }
        System.out.println(optimalPath.get(optimalPath.size() - 1)); // Print the end post
    }
}
