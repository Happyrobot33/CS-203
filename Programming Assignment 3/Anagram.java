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
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class Anagram {
    public static void main(String[] args) {
        String filePath = "";
        String currentDirectory = System.getProperty("user.dir");

        //check if no args are passed
        if (args.length == 0) {
            //prompt the user
            System.out.println("Please enter the name of the file you would like to read from in the current working directory (" + currentDirectory + "): ");
            Scanner scanner = new Scanner(System.in);
            filePath = scanner.nextLine();
            scanner.close();
            //add the current directory to the file path
            filePath = currentDirectory + "/" + filePath;
        } else if (args.length > 1) {
            System.out.println("Please provide only one file name as an argument.");
            return;
        }

        //test if the file exists
        //if it does not exist, print an error message and return
        if (!new File(filePath).exists()) {
            System.out.println("The file " + filePath + " does not exist.");
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

        //make a histogram for each line
        String[] rawWords = fileContents.split("\n");

        //make a list of list of strings to store the anagram sets
        List<List<String>> anagramSets = new ArrayList<>();

        //make a list for each word so we can remove sets of words that are anagrams
        //this is a copy on write array list so we can remove elements while iterating. This has a performance hit but makes the iteration easier
        List<String> words = new CopyOnWriteArrayList<>();

        //initialize the list of words
        for (String word : rawWords) {
            words.add(word);
        }

        //check line 1 and 2
        //System.out.println(areAnagrams(lines[0].toLowerCase(), lines[1].toLowerCase()));

        //we will pick out a word from the list, to compare to the rest of the words
        //after comparing, we will remove the word we picked and all other words that are anagrams of it
        //we use a trick of iterating backwards so we can remove elements while iterating through the list to avoid "ghost elements"
        for (int i = words.size() - 1; i >= 0; i--) {
            //check if in bounds of the list still
            if (i >= words.size()) {
                continue;
            }

            String word = words.get(i);
            //remove the word from the list
            words.remove(word);
            //add the word to the anagram set
            List<String> anagramSet = new ArrayList<>();
            anagramSet.add(word);

            //check if the word is an anagram of any other word in the list
            for (String wordTwo : words) {
                if (areAnagrams(word.toLowerCase(), wordTwo.toLowerCase())) {
                    //add the word to the anagram set
                    anagramSet.add(wordTwo);
                    words.remove(wordTwo);
                }
            }

            anagramSets.add(anagramSet);
        }

        //print each anagram set
        for (List<String> anagramSet : anagramSets) {
            System.out.println(anagramSet);
        }

        System.out.println("Number of anagram sets: " + anagramSets.size());
    }

    static boolean areAnagrams(String baseWord, String wordTwo) {
        //if they arent the same length they can never be anaagrams
        if (baseWord.length() != wordTwo.length()) {
            return false;
        }

        Map<Character, Integer> histogram = new HashMap<>();

        for (int i = 0; i < baseWord.length(); i++) {
            histogram.compute(baseWord.charAt(i), (k, v) -> (v == null ? 0 : v) + 1);
            histogram.compute(wordTwo.charAt(i), (k, v) -> (v == null ? 0 : v) - 1);
        }

        for (int count : histogram.values()) {
            if (count != 0) {
                return false;
            }
        }

        return true;
    }
}
