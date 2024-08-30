/*
 * Matthew Herber
 * CS-203, 2024
 * Programming Assignment 3
 * AnagramWord Class, represents a word and its sorted version
 */

import java.util.Arrays;

/*
 * This class represents a anagramWord object.
 * This object contains the original word and a sorted version of the word.
 * This is used to compare two words based on their sorted version.
 */
public class anagramWord
{
    public String originalWord;
    public String sortedWord;

    /*
     * This is a constructor for the anagramWord object.
     * it takes in a string, and will construct a sorted version of the string internally for comparison.
     * 
     * @param originalWord - the original word
     */
    public anagramWord(String originalWord)
    {
        this.originalWord = originalWord;

        char[] characters = originalWord.toLowerCase().toCharArray();
        Arrays.sort(characters);
        this.sortedWord = new String(characters);
    }
    
    /*
     * This is a compare function between two anagramWord objects.
     * This compares the internal sorted word value instead of the original word.
     * 
     * @oaram other - the other anagramWord object to compare to
     * @return 0 if the two sorted words are the same. negative integer if this sorted word is less than the other sorted word. positive integer if this sorted word is greater than the other sorted word
     */
    public int compareTo(anagramWord other)
    {
        return this.sortedWord.compareTo(other.sortedWord);
    }

    /*
     * This is a custom equals method to check if two anagramWord objects are the same, by comparing their sorted words.
     * 
     * @oaram other - the other anagramWord object to compare to
     * @return true if the two sorted words are the same. false otherwise
     */
    public Boolean equals(anagramWord other)
    {
        return this.sortedWord.equals(other.sortedWord);
    }

    /*
     * This is a string representation of the anagramWord object.
     * 
     * @return the original word and the sorted word in parentheses
     */
    public String toString()
    {
        return originalWord + " (" + sortedWord + ")";
    }
}
