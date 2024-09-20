/*
 * Matthew Herber
 * CS-203, 2024
 * Programming Assignment 4
 */

 /*

 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class represents a program to find anagrams in a file. The program will
 * read a file and find all sets of anagrams in the file. The program will print
 * the sets of anagrams to standard output, as well as the number of sets found.
 */
public class Canoe {

    public static void main(String[] args) {
        String filePath = "";
        String currentDirectory = System.getProperty("user.dir");

        //TODO: REMOVE THIS!
        //hardcode the path for now for easier testing
        args = new String[1];
        args[0] = currentDirectory + "/Programming Assignment 4/smallTest.txt";

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
        for (int row = 0; row < costs.length; row++) {
            for (int col = 0; col < costs[row].length; col++) {
                System.out.print(costs[row][col] + " ");
            }
            System.out.println();
        }

        memo = new int[numPosts][numPosts];
        int memoCalc = Memoization(0, numPosts - 1, costs);
        int dynamicCalc = dynamicProgramming(numPosts, costs)[0][numPosts - 1];

        System.out.println("Memoization: " + memoCalc);
        System.out.println("Dynamic Programming: " + dynamicCalc);
    }

    //memoization method
    private static int[][] memo; //data structure to store the memoized values
    public static int Memoization(int startingPost, int endingPost, int[][] costs) {
        //base case, no cost if your already at or beyond the end
        if (startingPost >= endingPost) {
            return 0;
        }

        //use the memoized value if it exists
        if (memo[startingPost][endingPost] != 0) {
            return memo[startingPost][endingPost];
        }

        //use the maximum value for the minCost
        int minCost = Integer.MAX_VALUE;

        //iterate through the intermediate posts
        for (int intermediatePost = startingPost + 1; intermediatePost <= endingPost; intermediatePost++) {
            int possibleLowerCost = costs[startingPost][intermediatePost] + Memoization(intermediatePost, endingPost, costs);
            if (possibleLowerCost < minCost) {
                minCost = possibleLowerCost;
            }
        }

        //store the minCost in the memo
        memo[startingPost][endingPost] = minCost;

        return minCost;
    }

    public static int[][] dynamicProgramming(int numPosts, int[][] costs) {
        //create a 2d array to store the costs
        int[][] dp = new int[numPosts][numPosts];
        for (int len = 2; len <= numPosts; len++) {
            for (int startingPost = 0; startingPost < numPosts - len + 1; startingPost++) {
                //calculate the ending post
                int endingPost = startingPost + len - 1;
                dp[startingPost][endingPost] = costs[startingPost][endingPost];

                //iterate through the intermediate posts
                for (int intermediatePost = startingPost + 1; intermediatePost < endingPost; intermediatePost++) {
                    dp[startingPost][endingPost] = Math.min(dp[startingPost][endingPost], costs[startingPost][intermediatePost] + dp[intermediatePost][endingPost]);
                }
            }
        }

        System.out.println("Dynamic Programming Optimal Cost Matrix: ");
        //print the DP
        for (int row = 0; row < dp.length; row++) {
            for (int col = 0; col < dp[row].length; col++) {
                System.out.print(dp[row][col] + " ");
            }
            System.out.println();
        }
        return dp;
    }
}
