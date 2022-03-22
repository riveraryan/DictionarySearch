package search;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Stores the letters of words in a Tree structure of linked Node objects linked in
 * such a way that words can be spelled out as you traverse down any branch from the
 * overall root Node to any leaf.
 * @author Ryan Rivera
 * @version 1.0
 */
public class Tree
{
    //Fields
    final static int INDEX_OFFSET = 97;
    private final static int ALPHABET_SIZE = 26;
    private Node root;
    private int size = 0;

    /**
     * Creates a new Tree object that stores Nodes containing a letter and an array of child nodes.
     */
    public Tree()
    {
        root = new Node(null);
    }

    /**
     * This method adds a word to the Tree structure one letter at a time.
     * @param word Word to be added to the tree.
     * @return Boolean true if word was successfully added, false otherwise.
     */
    public boolean add(String word)
    {
        //If word is valid, add word, else return false
        if(validateWord(word)){
            //Store current Tree size, if savedSize changes return true, else return false
            int savedSize = size;
            add(root, word.toLowerCase());
            return size > savedSize;
        }
        else{
            return false;
        }
    }

    /**
     * This method adds Nodes for the letters in the word parameter to the Tree recursively.
     * @param current The current Node.
     * @param word The word being added to the tree.
     */
    private void add(Node current, String word)
    {
        //If there is a letter to add
        if(!word.isEmpty()){
            //Convert the new letter's ASCII value to an index position in the Node[], store it
            int childIndex = charToIndex(word.charAt(0));

            //If a child node does not exist in the index position
            if(current.children[childIndex] == null){
                //Create a child for the letter and set empty flag to false
                current.children[childIndex] = new Node(word.charAt(0));
                current.flagEmpty = false;

                //If the current node was a leaf node, set to an end node
                if(current.flagLeaf){
                    current.flagLeaf = false;
                    current.flagEnd = true;
                }
            }
            //Add next letter
            add(current.children[childIndex], word.substring(1));
        }
        //Else, there are no more letters to add
        else{
            //If the current Node[] is empty, set to a leaf node, else it is an end node
            if(current.flagEmpty){
                current.flagLeaf = true;
            }
            else{
                current.flagEnd = true;
            }
        }
    }

    /**
     * This method will return a String[] containing matches found for a user supplied String
     * @param word Word or partial word to search for.
     * @return String containing all matches found.
     */
    public String[] getMatches(String word)
    {
        //Create ArrayList for matches, get the node at the end of the word
        ArrayList<String> matches = new ArrayList<>();

        //If word is valid, return matches, otherwise return empty String[]
        return validateWord(word) ? getMatches(root, word, "", matches).toArray(new String[0])
                : new String[0];

    }

    /**
     * This method will traverse the tree through all available child nodes recursively adding
     * a node's letter to a cumulative String. If a node is either a leaf node or end of word
     * node, indicating the current state of the cumulative String is a complete word.
     * @param current Current node
     * @param word String user provided word or partial word
     * @param cumulative String that is appended with the current node's data
     * @param matches ArrayList storing String word matches
     * @return ArrayList<String> containing String word matches
     */
    private ArrayList<String> getMatches(Node current, String word, String cumulative, ArrayList<String> matches)
    {
        //Continue traversing the Tree following the word until word length > cumulative length
        if(word.length() > cumulative.length()){
            char letter = word.charAt(cumulative.length());
            int index = charToIndex(letter);

            //If child node exists for letter, add letter to cumulative, recursively call getMatches
            if(current.children[index] != null){
                matches = getMatches(current.children[index], word, cumulative+ letter, matches);
            }
        }
        //Else, end node of word reached, search child nodes for matches
        else{
            //If current flagEnd or flagLeaf = true, add to matches
            if(current.flagEnd || current.flagLeaf){
                matches.add(cumulative);
            }

            //If current node has children nodes, search them for matches
            if(!current.flagLeaf){
                //Loop over each child in children Node[]
                for(Node child : current.children){
                    if(child != null){
                        //Call getMatches() on child node appending cumulative with its data
                        matches = getMatches(child, word, cumulative + child.data, matches);
                    }
                }
            }
        }
        return matches;
    }

    /**
     * This method returns a char's int index position in an array of alphabetical letters.
     * @param letter The char letter to be converted
     * @return The int index position in children Node[] (char ASCII value - 97)
     */
    private int charToIndex(char letter)
    {
        return letter - INDEX_OFFSET;
    }

    /**
     * This method checks that the String parameter word is not empty and contains no
     * non-alphabetic characters, if valid it returns true, otherwise it returns false.
     * @param word User supplied word
     * @return True if the word contains only alphabetic characters, false otherwise
     */
    private boolean validateWord(String word){
        //If word is not empty
        if(!word.isEmpty()){
            int index;
            //Check that each character is a lowercase alphabet character, else return false
            for(int i=0; i < word.length(); i++){
                index = charToIndex(word.charAt(i));
                if(index > ALPHABET_SIZE - 1 || index < 0){
                    return false;
                }
            }
            //All characters are lowercase letters
            return true;
        }
        //Word is empty
        return false;
    }

    @Override
    public String toString()
    {
        return "Tree{" +
                "root=" + root +
                '}';
    }

    /**
     * Creates a Node object used to store a lowercase alphabetical character and store links to
     * 26 additional child Nodes (one for each letter in the alphabet).
     */
    private class Node
    {
        //Fields
        private Character data;
        private Node[] children;
        private boolean flagEnd; //Node is a end of word node
        private boolean flagLeaf; //Node is a leaf node
        private boolean flagEmpty; //Node is a leaf node


        /**
         * Creates a Node object storing a Character and an array of child Nodes
         * @param data Character - to be added to Node data
         */
        private Node(Character data)
        {
            this.data = data;
            this.children = new Node[ALPHABET_SIZE];
            this.flagEnd = false;
            this.flagLeaf = false;
            this.flagEmpty = true;
            size++; //Increase the Tree size
        }

        @Override
        public String toString()
        {
            return "Node{" +
                    "data=" + data +
                    ", children=" + Arrays.toString(children) +
                    ", flagEnd=" + flagEnd +
                    ", flagLeaf=" + flagLeaf +
                    '}';
        }
    }

}