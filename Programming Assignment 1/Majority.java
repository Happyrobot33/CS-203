/*
 * Matthew Herber
 * CS-203, 2024
 * Programming Assignment 1
 * Majority Class, finds the majority element in an array
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * The Majority class represents an algorithm to find the majority element in an array.
 * A majority element is an element that appears more than half the size of the array.
 * This class provides methods to generate a test array, find the majority element using a brute-force approach,
 * and print the result along with the comparison count and the time taken.
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
            System.out.println(result.size + ", " + result.duration + ", " + result.uniqueCount + ", " + result.majorityElement + ", " + result.comparisonCount);
        }
    }

    /**
     * Represents the result of a test.
     */
    public record TestResult(
            int size,
            long duration,
            int uniqueCount,
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
        int majorityElement = findMajorityBF(data);
        long endTime = System.nanoTime();

        int uniqueCount = calculateFrequencyMap(data).size();
        long duration = (endTime - startTime);

        //print out the results
        System.out.println("Majority element: " + majorityElement);
        System.out.println("Comparison count: " + comparisonCount);
        System.out.println("Unique elements: " + uniqueCount);
        System.out.println("Time taken: " + duration + " nanoseconds");

        return new TestResult(data.length, duration, uniqueCount, majorityElement, comparisonCount);
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
        for (int i = 0; i < data.length; i++) {
            finalString += data[i];

            //only add the comma if it's not the last element
            if (i != data.length - 1) {
                finalString += ", ";
            }
        }
        finalString += "]";
        System.out.println(finalString);
    }

    /**
     * Finds the majority element in the given array using the brute force
     * approach. A majority element is an element that appears more than half
     * the size of the array. For example, in the array [1, 2, 2, 2, 3], the
     * majority element is 2. If there is no majority element, the method will
     * return -1.
     *
     * @param data the array of integers
     * @return the majority element if it exists, -1 otherwise
     */
    public static int findMajorityBF(int[] data) {
        //reset the comparison count
        comparisonCount = 0;

        Map<Integer, Integer> countMap = calculateFrequencyMap(data);

        //now that the map is populated, find the element with the highest count
        int majorityElement = -1; //This is the value we will return. -1 is used to indicate that there is no majority element
        int majorityCount = 0; //this is the count of the majority element between loop iterations
        for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
            int element = entry.getKey();
            int count = entry.getValue();

            //if the count of the current element is greater than the current majority count, update the majority element
            comparisonCount++;
            if (count > majorityCount) {
                majorityElement = element;
                majorityCount = count;
            }
        }

        //if the majority count is greater than half the size of the array, return the majority element
        if (majorityCount > data.length / 2) {
            return majorityElement;
        } else {
            return -1;
        }
    }

    /**
     * Calculates the frequency map of elements in the given array.
     *
     * @param data the array of integers
     * @return a map containing the count of each distinct element in the array
     */
    private static Map<Integer, Integer> calculateFrequencyMap(int[] data) {
        //make a map to store the count of each distinct element
        Map<Integer, Integer> map = new HashMap<>();

        //loop through the array and count the number of occurrences of each element
        for (int i = 0; i < data.length; i++) {
            int element = data[i];
            if (map.containsKey(element)) {
                map.put(element, map.get(element) + 1);
            } else {
                map.put(element, 1);
            }
        }
        return map;
    }
}
