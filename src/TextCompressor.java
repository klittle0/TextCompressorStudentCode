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

    //change this
    private static final int NUMCODES = 500;
    private static int lastCode = 0;
    private static void compress() {
        // Initialize TST with 256 codes
        TST prefixes = new TST();
        for (int i = 0; i < 256; i++){
            // Is this correct?
            prefixes.insert(Integer.toString(i), i);
            lastCode = i;
        }

        // Does this read in the whole thing?? I'm confused abt what's happening here & if my while loop is correct as a result
        String text = BinaryStdIn.readString();

        while (text.length() != 0){
            int
            String prefix = prefixes.getLongestPrefix(text);
            // Write 8-bit code associated with X
            int code = prefixes.lookup(prefix);
            BinaryStdOut.write(code, 8);
            // Scans one character past — do I need the plus one?
            int startIndex = text.indexOf(prefix) + prefix.length() + 1;
            // Check to see if I have reached # codes
            if ()
            String ahead = prefix + text.charAt(startIndex);
            prefixes.insert(ahead, );


        }


        while (!BinaryStdIn.isEmpty()){




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
