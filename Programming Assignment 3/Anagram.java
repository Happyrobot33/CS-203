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
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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

        List<wordData> wordDataList = new ArrayList<>();
        
        //we will pre-sort each word into the sorted version.
        //for example, test becomes estt
        //this is a direct comparison sort since each letter in ascii is already a ascending integer
        for (String word : rawWords)
        {
            wordDataList.add(new wordData(word));
        }

        //make a list of list of strings to store the anagram sets
        List<List<String>> anagramSets = new ArrayList<>();

        //sort the wordDataList
        wordDataList.sort((a, b) -> a.compareTo(b));

        for (int i = 0; i < wordDataList.size(); i++) {
            List<String> anagramSet = new ArrayList<>();
            anagramSet.add(wordDataList.get(i).originalWord);

            for (int j = i + 1; j < wordDataList.size(); j++) {
                if (wordDataList.get(i).sortedWord.equals(wordDataList.get(j).sortedWord)) {
                    anagramSet.add(wordDataList.get(j).originalWord);
                } else {
                    break;
                }
            }

            if (anagramSet.size() > 1) {
                anagramSets.add(anagramSet);
            }
        }

        //print each anagram set
        for (List<String> anagramSet : anagramSets) {
            System.out.println(anagramSet);
        }

        System.out.println("Number of anagram sets: " + anagramSets.size());
    }


    public static class wordData
    {
        public String originalWord;
        public String sortedWord;

        //initializer
        public wordData(String originalWord)
        {
            this.originalWord = originalWord;

            char[] characters = originalWord.toLowerCase().toCharArray();
            Arrays.sort(characters);
            this.sortedWord = new String(characters);
        }
        
        //make a compare to
        public int compareTo(wordData other)
        {
            return this.sortedWord.compareTo(other.sortedWord);
        }

        //tostring
        public String toString()
        {
            return originalWord + " (" + sortedWord + ")";
        }
    }
}
