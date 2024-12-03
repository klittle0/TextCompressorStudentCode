import java.util.Dictionary;
import java.util.Hashtable;
/******************************************************************************
 *  Compilation:  javac TextCompressor.java
 *  Execution:    java TextCompressor - < input.txt   (compress)
 *  Execution:    java TextCompressor + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *  Data files:   abra.txt
 *                jabberwocky.txt
 *                shakespeare.txt
 *                virus.txt
 *
 *  % java DumpBinary 0 < abra.txt
 *  136 bits
 *
 *  % java TextCompressor - < abra.txt | java DumpBinary 0
 *  104 bits    (when using 8-bit codes)
 *
 *  % java DumpBinary 0 < alice.txt
 *  1104064 bits
 *  % java TextCompressor - < alice.txt | java DumpBinary 0
 *  480760 bits
 *  = 43.54% compression ratio!
 ******************************************************************************/

/**
 *  The {@code TextCompressor} class provides static methods for compressing
 *  and expanding natural language through textfile input.
 *
 *  @author Zach Blick, YOUR NAME HERE
 */
public class TextCompressor {
    final static int CHARS = 60;

    private static void compress() {
        int occurrences = 0;
        Dictionary<String, Integer> repeats = new Hashtable<>();

        int index = 0;

        // I'm checking windows of 3, 4, and 5 — 60 is divisible by all of these
        // Should start by looking at chunks of 60 chars from file
        // This will make it way faster, but it might miss some occurrences. I'm fine with that.

        // Slide through file & keep track of a window. increment the # of occurrences

        // NEED A HEADER -- pass in the length of dictionary, aka the # of key-value pairs being passed in
        // Also pass in a key, followed by its value
        // IDs window

        while (!BinaryStdIn.isEmpty()){
            // Reads in 60 chars from text
            StringBuilder segment = new StringBuilder();
            for (int i = 0; i < CHARS; i++){
                segment.append(BinaryStdIn.readChar());
            }

            // Keep sliding the window over repeatedly
            while (index + 5 < CHARS){
                // Makes windows of size 3,4, and 5, starting @ index
                String window3 = window(index, 3);
                String window4 = window(index, 4);
                String window5 = window(index, 5);

                // Checks the number of occurrences of all 3 windows in the segment
                int occur3 = checkOccur(String.valueOf(segment), window3);
                int occur4 = checkOccur(String.valueOf(segment), window4);
                int occur5 = checkOccur(String.valueOf(segment), window5);

                // Save the # of occurrences in a dictionary of repeats
                repeats.put(window3, occur3);
                repeats.put(window4, occur4);
                repeats.put(window5, occur5);

                index++;
            }
        }
        // Now I need to analyze the repeats dictionary!!
        // Go through every value in the dictionary. For any that have a key of 3 or above, keep it. Otherwise, remove from dictionary
        //

        for (int i = 0; i < repeats.size(); i++){
            if (repeats.get)

        }
        // Make an array of all words that belong, as well as a unique code that corresponds to each
            // Should I use ascii codes? or maybe binary.
            // Maybe I should just use a for loop & assign all of the values a binary code between 128 and 256.

        // write an escape character after the dictionary to indicate that the real data is starting

        // Go through the file again this time. If it IDs a certain sequence, write its code

        BinaryStdOut.close();
    }

    // Makes a window starting at index, of length n
    private static String window(int index, int n){
        StringBuilder window = new StringBuilder();
        // Creates window of length n
        for (int i = index; i < n; i++){
            window.append(BinaryStdIn.readChar());
        }
        return window.toString();
    }

    // IDs the # of times each window occurs in segment
    private static int checkOccur(String segment, String window){
        int occurrences = 0;
        for (int i = 0; i < CHARS; i++){
            String sub = segment.substring(i, i + 3);
            // Increments after a match
            if (window.equals(sub)){
                occurrences++;
            }
        }
        return occurrences;
    }

    private static void expand() {

        // TODO: Complete the expand() method

        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
