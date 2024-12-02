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

    private static void compress() {
        int n = 2;
        int occurences = 0;
        StringBuilder window = new StringBuilder();

        // Make a window of size n
        // Slide through file & keep track of a window. increment the # of occurences

        // NEED A HEADER -- pass in the length of dictionary, aka the # of key-value pairs being passed in
        // Also pass in a key, followed by its value
        // IDs window
        while (!BinaryStdIn.isEmpty()){
            // Reads in whole text
            String text = BinaryStdIn.readString();
            for (int i = 0; i < n; i++){
                window.append(BinaryStdIn.readChar());
            }
            occurences += 1;
            // ID the # of times this window occurs in String
            // Goes through text
            for (int i = 0; i < text.length(); i++){
                String sub = text.substring(i, i + n);
                if (window.ToStringequals(sub)){

                }

            }

        }

        BinaryStdOut.close();
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
