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
    private static final int NUMCODES = 4096;
    private static final int EOF = 80;
    private static final int BITS = 12;


    private static void compress() {
        // Initialize TST with 256 codes
        // Is it necessary to do this at the beginning??
        TST prefixes = new TST();
        for (int i = 0; i < 256; i++){
            // Is this correct? Does the to-string
            prefixes.insert(Integer.toString(i), i);
        }
        // Represents how many codes have been created
        int numCodes = 256;
        // Represents the code of the next String/prefix found
        int startCode = 81;

        String text = BinaryStdIn.readString();

        while (!text.isEmpty() && numCodes <= NUMCODES){
            String prefix = prefixes.getLongestPrefix(text);
            // Cut prefix out of text
            text = text.substring(0, prefix.length());
            // Write 12-bit code associated with X
            int code = prefixes.lookup(prefix);
            BinaryStdOut.write(code, BITS);
            // Scans one character ahead
            String ahead = prefix + text.charAt(0);
            prefixes.insert(ahead, startCode);
            // Increment to show new # of codes
            numCodes++;
            // Increment so next code is different
            startCode++;
        }
        // Write EOF character
        BinaryStdOut.write(EOF);
        BinaryStdOut.close();
    }

    // DON'T USE SUBSTRING ANYWHERE! KEEP TRACK OF THE START INDEX TO SAVE A LOT OF SPACE
    private static void expand() {
        // Initialize array with all Ascii values
        int currentCode = 81;
        String[] prefixes = new String[NUMCODES];
        for (int i = 0; i < 256; i++){
            prefixes[i] = Integer.toString(i);
        }

        // Reads 12 bits at a time
        int code = BinaryStdIn.readInt(BITS);
        String val = prefixes[code];
        // Continue until reaching the EOF code word.
        while (code != EOF){
            BinaryStdOut.write(val);
            int ahead = BinaryStdIn.readInt(BITS);
            String aheadS = prefixes[ahead];
            // Add string to array
            prefixes[currentCode] = val + prefixes[ahead].charAt(0);
            code = ahead;
            val = aheadS;
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
