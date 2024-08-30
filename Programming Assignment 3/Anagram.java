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
This is because the algorithm first sorts the list of words by their sorted version, which is O(n log n) time complexity.
Then it loops through the list of words, comparing each word to the next word in the list. This is O(n) time complexity.
Therefore, the total time complexity is O(n log n + n) = O(n log n).
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
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

        //make a list of anagramWord objects to store the words
        List<anagramWord> anagramWords = new ArrayList<>();

        //we will pre-sort each word into the sorted version.
        //for example, test becomes estt
        for (String word : rawWords) {
            anagramWords.add(new anagramWord(word));
        }

        //make a list of list of strings to store the anagram sets
        List<List<String>> anagramSets = new ArrayList<>();

        //sort the wordDataList
        anagramWords.sort((a, b) -> a.compareTo(b));

        for (int wordIndex = 0; wordIndex < anagramWords.size(); wordIndex++) {
            //make a new anagram set
            List<String> anagramSet = new ArrayList<>();
            //add the current first word to the set
            anagramSet.add(anagramWords.get(wordIndex).originalWord);

            //loop forward in the list to find all matching anagrams
            for (int followingIndex = wordIndex + 1; followingIndex < anagramWords.size(); followingIndex++) {
                // if the starting word is an anagram of the following word, add it to the set
                //we break out of the loop if the following word is not an anagram, as we have already sorted the list by characters
                if (anagramWords.get(wordIndex).equals(anagramWords.get(followingIndex))) {
                    anagramSet.add(anagramWords.get(followingIndex).originalWord);
                } else {
                    break;
                }
            }

            anagramSets.add(anagramSet);
            //shift the wordIndex forward to the end of the found anagrams to do the next search
            wordIndex += anagramSet.size() - 1;
        }

        //print each anagram set
        for (List<String> anagramSet : anagramSets) {
            System.out.println(anagramSet);
        }

        //print the number of anagram sets
        System.out.println("Number of anagram sets: " + anagramSets.size());
    }
}
