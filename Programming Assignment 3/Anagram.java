/*
 * Matthew Herber
 * CS-203, 2024
 * Programming Assignment 3
 * Anagram Class, finds anagrams in a file
 */

 /*
Write a Java program which begins by prompting the user for the name of a file in the
current directory. The file will contain an unknown number of words, with a single word on
each line. (You may assume that there are no non-alphabetic characters in the file.) Your
program will read the file and store the words internally in an appropriate format.
Your program will then find all sets of anagrams of words in the file in an efficient
manner. Your anagram-detection algorithms should ignore distinctions between upper-case
and lower-case letters. For example, “Elvis” and “lives” should be identified as anagrams of
one another.
Upon completion, the program will print the detected sets of anagrams to standard
output. The manner in which you print those sets is up to you; as long as the results are
neat and readable, you are free to use any format you like.


What is the theoretical worst-case running time of the algorithm you implemented (i.e.
in Θ-notation), expressed in terms of the number of words n in the input file? Justify
your answer.
the theoretical worst case running time of the algorithm implemented here is O(n log n) where n is the number of words in the input file.
by counting the frequency of the characters in the current word, we can put each word into a list that represents the different frequency sets of characters.
the reason this becomes O(n log n) is because the sorting of the frequency sets of characters is O(n log n) and the insertion of the words into the list is O(n).
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * This class represents a program to find anagrams in a file. The program will
 * read a file and find all sets of anagrams in the file. The program will print
 * the sets of anagrams to standard output, as well as the number of sets found.
 */
public class Anagram {

    public static void main(String[] args) {
        String filePath = "";
        String currentDirectory = System.getProperty("user.dir");

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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //if it does exist, check if it is in an ascii format
        //if it is not, print an error message and return
        if (!fileContents.matches("\\A\\p{ASCII}*\\z")) {
            System.out.println("The file " + filePath + " is not in an ASCII format.");
            return;
        }

        //make a array of words from the file contents
        String[] rawWords = fileContents.split("\n");

        ArrayList<ArrayList<String>> anagrams = findAnagrams(rawWords);

        //print each anagram set
        for (ArrayList<String> anagramSet : anagrams) {
            System.out.println(anagramSet);
        }

        //print the number of anagram sets
        System.out.println("Number of anagram sets: " + anagrams.size());
    }

    /**
     * This method finds the anagrams in the inputWords array. The method will
     * return a hashmap with the key being the sorted version of the anagram,
     * and the value being a list of the words that match as a anagram from the
     * inputWords array.
     *
     * @param inputWords the array of words to find anagrams in
     * @return a hashmap with the key being the sorted version of the anagram,
     * and the value being a list of the words that match as a anagram from the
     * inputWords array.
     */
    private static ArrayList<ArrayList<String>> findAnagrams(String[] inputWords) {

        //Inner hashmap counts the frequency of each character in a string.
        //The outer hashmap stores the inner hashmap as a key and the value is a list of strings that have the same frequency of characters.
        HashMap<HashMap<Character, Integer>, ArrayList<String>> map = new HashMap<HashMap<Character, Integer>, ArrayList<String>>();

        for (String word : inputWords) {
            HashMap<Character, Integer> frequencyMap = new HashMap<>();

            //ensure the word is lowercase
            word = word.toLowerCase();

            //count the frequency of each character in the string
            for (int characterIndex = 0; characterIndex < word.length(); characterIndex++) {
                char character = word.charAt(characterIndex);
                //If the character is already in the frequency map, increment the count
                if (frequencyMap.containsKey(character)) {
                    int x = frequencyMap.get(character);
                    frequencyMap.put(character, ++x); //must use prefix increment to increment the value before putting it back in the map. This is important!
                } else {
                    //If the character is not in the frequency map, add it with a count of 1
                    frequencyMap.put(character, 1);
                }
            }

            //If the same frequency map is already in the map, add the string to the list of strings with the same frequency map
            if (map.containsKey(frequencyMap)) {
                map.get(frequencyMap).add(word); 
            }else {
                //If the frequency map is not in the map, add it with the string as a list
                ArrayList<String> tempList = new ArrayList<>();
                tempList.add(word);
                map.put(frequencyMap, tempList);
            }
        }

        //return the values of the map as a list of lists
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        for (HashMap<Character, Integer> temp : map.keySet()) {
            result.add(map.get(temp));
        }
        return result;
    }
}
