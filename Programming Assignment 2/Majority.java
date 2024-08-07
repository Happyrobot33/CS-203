/*
 * Matthew Herber
 * CS-203, 2024
 * Programming Assignment 2
 * Majority Class, finds the majority element in an array
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The Majority class represents an algorithm to find the majority element in an
 * array. A majority element is an element that appears more than half the size
 * of the array. This class provides methods to generate a test array, find the
 * majority element using a divide-and-conquer approach, and print the result
 * along with the comparison count and the time taken.
 */
public class Majority {

    /**
     * Random number generator used across the program to avoid spurious object
     * creation
     */
    static Random random = new Random();

    /**
     * The number of comparisons made during the execution of the algorithm.
     * Reset with each call to findMajorityBF
     */
    static int comparisonCount = 0;

    /**
     * The main method is the entry point of the program. It generates a test
     * array, finds the majority element using a brute-force approach, and
     * prints the result along with the comparison count and the time taken.
     *
     * @param args The command-line arguments passed to the program.
     */
    public static void main(String[] args) {
        // generate a test array
        int[] data = makeArray(10, 10);
        algorithmTest(data);
        data = new int[]{1, 2, 2, 2, 3};
        algorithmTest(data);
        data = new int[]{1, 2, 3, 4, 5};
        algorithmTest(data);
        //make a list for recording the results of the tests
        List<TestResult> results = new ArrayList<>();
        //run the test on a increasingly large array, to see how the time taken scales with the size of the array
        for (int i = 1; i <= 1000; i++) {
            data = makeArray(i, i);
            results.add(algorithmTest(data));
        }
        //print them out in a nice format
        System.out.println("Results:");
        System.out.println("Size, Duration, Unique Count, Majority Element, Comparison Count");
        for (TestResult result : results) {
            System.out.println(result.size + ", " + result.duration + ", " + result.majorityElement + ", " + result.comparisonCount);
        }
    }

    /**
     * Represents the result of a test.
     */
    public record TestResult(
            int size,
            long duration,
            int majorityElement,
            int comparisonCount) {

    }

    /**
     * Tests the majority element algorithm on the given data array. Prints the
     * input array, the majority element, the comparison count, and the time
     * taken.
     *
     * @param data the input array of integers
     */
    private static TestResult algorithmTest(int[] data) {
        //print the array we are working with
        printArray(data);

        //run the algorithm and record the time taken
        long startTime = System.nanoTime();
        int majorityElement = findMajorityDC(data);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);

        //print out the results
        System.out.println("Majority element: " + majorityElement);
        System.out.println("Comparison count: " + comparisonCount);
        System.out.println("Time taken: " + duration + " nanoseconds");

        return new TestResult(data.length, duration, majorityElement, comparisonCount);
    }

    /**
     * This method will create an int array of the specified size, filled with
     * non-negative integers from the range [0..max]
     *
     * @param size the size of the array
     * @param max the maximum value for each element in the array
     * @return the created array
     */
    public static int[] makeArray(int size, int max) {
        //create a random intstream, with the specified size, and the range [0..max], and then convert it to an array
        return random.ints(size, 0, max + 1).toArray();
    }

    /**
     * Prints the elements of an integer array, in the format [element1,
     * element2, ...]
     *
     * @param data the integer array to be printed
     */
    public static void printArray(int[] data) {
        String finalString = "";
        finalString += "[";
        for (int iterator = 0; iterator < data.length; iterator++) {
            finalString += data[iterator];

            //only add the comma if it's not the last element
            if (iterator != data.length - 1) {
                finalString += ", ";
            }
        }
        finalString += "]";
        System.out.println(finalString);
    }

    /**
     * Finds the majority element in the given array using the divide and conquer
     * approach. A majority element is an element that appears more than half
     * the size of the array. For example, in the array [1, 2, 2, 2, 3], the
     * majority element is 2. If there is no majority element, the method will
     * return -1.
     *
     * @param data the array of integers
     * @return the majority element if it exists, -1 otherwise
     */
    public static int findMajorityDC(int[] data) {
        comparisonCount = 0;

        return recursivelyFindMajority(data);
    }

    /**
     * Recursively finds the majority element in an array of integers.
     * 
     * @param data The array of integers to search for the majority element.
     * @return The majority element if it exists, otherwise -1.
     */
    private static int recursivelyFindMajority(int[] data) {
        //if the array is of size 1, return the element
        if (data.length == 1) {
            return data[0];
        }

        //split the array into 2 halves
        int[] left = new int[data.length / 2];
        int[] right = new int[data.length - data.length / 2];
        System.arraycopy(data, 0, left, 0, data.length / 2);
        System.arraycopy(data, data.length / 2, right, 0, data.length - data.length / 2);

        //return a -1 if both halves are not the same, otherwise return the majority element
        int leftMajority = recursivelyFindMajority(left);
        int rightMajority = recursivelyFindMajority(right);

        //if the left and right majority are the same, return it directly
        comparisonCount++;
        if (leftMajority == rightMajority) {
            return leftMajority;
        } else {
            //we need to check both values we got back as candidates for the majority element, since at this point we cant be sure
            int leftCount = 0;
            int rightCount = 0;

            for (int element : data) {
                comparisonCount++;
                comparisonCount++;
                if (element == leftMajority) {
                    leftCount++;
                } else if (element == rightMajority) {
                    rightCount++;
                }
            }

            //if the count of the left majority is greater than half the size of the array, return it
            if (leftCount > data.length / 2) {
                return leftMajority;
            }

            //if the count of the right majority is greater than half the size of the array, return it
            if (rightCount > data.length / 2) {
                return rightMajority;
            }
        }

        //return that there is no majority element
        return -1;
    }
}
