package search;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Stores a dictionary that provides definitions given a word
 * or partial matching for words in the dictionary.
 *
 * @author Ryan Rivera
 * @version 1.0
 */
public class DictionarySearch implements IDictionary
{
    //Fields
    private HashMap<String, String> dictionary;
    private Tree tree;

    /**
     * Creates a new search object with a dictionary loaded and
     * ready for searching.
     */
    public DictionarySearch()
    {
        //Create a new Tree and a HashMap to store the word->definition pairs
        dictionary = new HashMap<>();
        tree = new Tree();

        try{
            //Create a File object for the dictionary file and a Scanner to read it
            File file = new File("files/dictionary80000.txt");
            Scanner scan = new Scanner(file);
            //Initialize a String to temporarily hold split lines
            String[] temp;

            //While there is a line to scan
            while(scan.hasNextLine()){
                //Split the line at ": ", storing them word in temp
                temp = scan.nextLine().split(": ");
                //Add the word and definition in temp to the dictionary as key-value pair
                dictionary.put(temp[0], temp[1]);
                //Add the word to the tree
                tree.add(temp[0]);
            }
        }
        //If the dictionary file is not found
        catch(FileNotFoundException e){
            System.out.println("Error: File not found at the given filepath");
        }
    }

    @Override
    public String getDefinition(String word)
    {
        return dictionary.getOrDefault(word, "Word not found...");
    }

    @Override
    public String[] getPartialMatches(String search)
    {
        return tree.getMatches(search);
    }

    @Override
    public String toString()
    {
        return "DictionarySearch{" +
                "dictionary=" + dictionary +
                ", tree=" + tree +
                '}';
    }
}
