/*
 * Matthew Herber
 * CS-203, 2024
 * Programming Assignment 4
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class reads a file and parses the contents to find the optimal path for
 * a canoe to travel from the first post to the last post. The canoe can only
 * travel from post to post in a straight line, and the cost of traveling from
 * post i to post j is given in the file. The goal is to find the path that
 * minimizes the cost of traveling from the first post to the last post.
 */
public class Canoe {

    public static void main(String[] args) {
        String filePath = "";
        String currentDirectory = System.getProperty("user.dir");

        //TODO: REMOVE THIS!
        //hardcode the path for now for easier testing
        args = new String[1];
        args[0] = currentDirectory + "/Programming Assignment 4/smallTest3.txt";

        //check if no args are passed
        if (args.length == 0) {
            //prompt the user
            System.out.println("Please enter the name of the file you would like to read from in the current working directory (" + currentDirectory + "): ");

            //wait for input
            try (Scanner scanner = new Scanner(System.in)) {
                filePath = scanner.nextLine();
            }
            //add the current directory to the file path
            filePath = currentDirectory + "/" + filePath;
        } else if (args.length > 1) {
            System.out.println("Please provide only one file name as an argument. Exiting");
            return;
        } else {
            //get the file path from the args
            filePath = args[0];
        }

        //test if the file exists
        //if it does not exist, print an error message and return
        if (!new File(filePath).exists()) {
            System.out.println("The file " + filePath + " does not exist. Exiting");
            return;
        }

        //read the file contents
        String fileContents = "";
        try {
            fileContents = new Scanner(new File(filePath)).useDelimiter("\\Z").next();
        } catch (FileNotFoundException error) {
            error.printStackTrace();
        }

        //if it does exist, check if it is in an ascii format
        //if it is not, print an error message and return
        if (!fileContents.matches("\\A\\p{ASCII}*\\z")) {
            System.out.println("The file " + filePath + " is not in an ASCII format.");
            return;
        }

        //get each line
        String[] lines = fileContents.split("\n");

        //now that we have a fully valid file, we need to parse the file contents
        //extract the number of posts, which is the first line of the file
        int numPosts = Integer.parseInt(lines[0].trim());

        System.out.println("Number of posts: " + numPosts);

        //make a 2d array to store the costs
        int[][] costs = new int[numPosts][numPosts]; //values will be initialized to -1
        //initialize the costs array
        for (int row = 0; row < numPosts; row++) {
            for (int col = 0; col < numPosts; col++) {
                costs[row][col] = Integer.MAX_VALUE;
            }
        }

        // Fill the array from the right
        for (int row = 0; row < numPosts; row++) {
            //check if we are past what data we have
            if (row + 1 >= lines.length) {
                break;
            }

            // Split the line into numbers
            String[] numbers = lines[row + 1].trim().split("\\s+");
            int rowLength = numbers.length;

            // Place the numbers from the right
            for (int col = 0; col < rowLength; col++) {
                costs[row][numPosts - rowLength + col] = Integer.parseInt(numbers[col]);
            }
        }

        //print out the array, with the first index being x and the second index being y
        System.out.println("Costs Matrix:");
        for (int[] cost : costs) {
            for (int col = 0; col < cost.length; col++) {
                if (cost[col] == Integer.MAX_VALUE) {
                    System.out.print("   ");
                } else {
                    System.out.print(cost[col] + " ");
                }
            }
            System.out.println();
        }

        //cache the timer. This makes sure both exist in memory first to avoid alloc time
        long startTime = 0;
        long endTime = 0;

        // warm up the JVM. Not doing this makes the first run take WAY longer
        memoizationApproach(0, numPosts - 1, costs, numPosts);

        //start a timer
        CanoeData memoization = memoizationApproach(0, numPosts - 1, costs, numPosts);

        //warm up the JVM. Not doing this makes the first run take WAY longer
        dynamicProgrammingApproach(0, numPosts - 1, costs, numPosts);
        //start a timer
        CanoeData dynamic = dynamicProgrammingApproach(0, numPosts - 1, costs, numPosts);

        //print the info for the memoization approach
        System.out.println("Memoization Approach");
        memoization.printOptimalMatrix();
        memoization.printCost();
        memoization.printPath();
        System.out.println("Time: " + memoization.nanoSeconds + " ns");

        //print the info for the dynamic programming approach
        System.out.println("Dynamic Programming Approach");
        dynamic.printOptimalMatrix();
        dynamic.printCost();
        dynamic.printPath();
        System.out.println("Time: " + dynamic.nanoSeconds + " ns");
    }

    /**
     * A 2D array used for memoization to store the optimal costs. Each entry
     * memoOptimalCosts[i][j] represents the optimal cost for a specific
     * subproblem defined by indices i and j. This is external to avoid passing
     * by value, since you cannot pass by reference in Java unlike C# where you
     * can :(
     */
    public static int[][] memoOptimalCosts;

    /**
     * Computes the minimum cost to travel from the start post to the end post
     * using a memoization approach.
     *
     * @param start The starting post index.
     * @param end The ending post index.
     * @param costMatrix A 2D array where costMatrix[i][j] represents the cost
     * to travel directly from post i to post j.
     * @param numPosts The total number of posts.
     * @return A CanoeData object containing the minimum cost, the optimal cost
     * matrix, and the computation time.
     */
    private static CanoeData memoizationApproach(int start, int end, int[][] costMatrix, int numPosts) {
        // Initialize memoization table with -1 (indicating uncomputed values)
        memoOptimalCosts = new int[numPosts][numPosts];
        for (int row = 0; row < numPosts; row++) {
            for (int col = 0; col < numPosts; col++) {
                if (row == col) {
                    memoOptimalCosts[row][col] = 0;
                } else {
                    memoOptimalCosts[row][col] = -1;
                }
            }
        }

        long startTime = System.nanoTime();

        //we technically dont need to do this additional looping, but it ensures that the resulting optimal cost matrix is fully populated
        for (int startpost = 0; startpost < numPosts; startpost++) {
            for (int endpost = startpost + 1; endpost < numPosts; endpost++) {
            }
        }
        computeOptimalCost(start, end, costMatrix);
        long endTime = System.nanoTime();

        // Return the result in CanoeData
        CanoeData data = new CanoeData(memoOptimalCosts[start][end], memoOptimalCosts, endTime - startTime);
        return data;
    }

    private static int computeOptimalCost(int startpost, int endpost, int[][] costMatrix) {
        // If the value is already computed, return the cached result
        if (memoOptimalCosts[startpost][endpost] != -1) {
            return memoOptimalCosts[startpost][endpost];
        }

        // Initialize the optimal cost to a large value
        int minCost = Integer.MAX_VALUE;

        // Try every possible intermediate point intermediatePost (between row+1 and col)
        for (int intermediatePost = startpost + 1; intermediatePost <= endpost; intermediatePost++) {
            int cost = costMatrix[startpost][intermediatePost] + computeOptimalCost(intermediatePost, endpost, costMatrix);
            minCost = Math.min(minCost, cost);
        }

        // Store the computed value in the memoization table
        memoOptimalCosts[startpost][endpost] = minCost;

        // Return the computed cost
        return minCost;
    }

    /**
     * Computes the minimum cost to travel from the start post to the end post
     * using a dynamic programming approach.
     *
     * @param start The starting post index.
     * @param end The ending post index.
     * @param costMatrix A 2D array where costMatrix[i][j] represents the cost
     * to travel directly from post i to post j.
     * @param numPosts The total number of posts.
     * @return A CanoeData object containing the minimum cost, the optimal cost
     * matrix, and the computation time.
     */
    private static CanoeData dynamicProgrammingApproach(int start, int end, int[][] costMatrix, int numPosts) {
        int[][] optimalCosts = new int[numPosts][numPosts];

        // Initialize the base cases: cost from any post to itself is 0
        for (int diagonal = 0; diagonal < numPosts; diagonal++) {
            optimalCosts[diagonal][diagonal] = 0;
        }

        long startTime = System.nanoTime();
        // Fill the optimalCosts array using the dynamic programming recurrence
        for (int length = 2; length <= numPosts; length++) {  // subproblem length (distance between row and col)
            for (int startPost = 0; startPost <= numPosts - length; startPost++) {
                int subEndPost = startPost + length - 1;  // The end point of the current subproblem
                optimalCosts[startPost][subEndPost] = Integer.MAX_VALUE;  // Initialize with a large number

                // Try every possible intermediate point intermediatePost
                for (int intermediatePost = startPost + 1; intermediatePost <= subEndPost; intermediatePost++) {
                    optimalCosts[startPost][subEndPost] = Math.min(optimalCosts[startPost][subEndPost], costMatrix[startPost][intermediatePost] + optimalCosts[intermediatePost][subEndPost]);
                }
            }
        }
        long endTime = System.nanoTime();

        //return the data
        CanoeData data = new CanoeData(optimalCosts[start][end], optimalCosts, endTime - startTime);
        return data;
    }
}
