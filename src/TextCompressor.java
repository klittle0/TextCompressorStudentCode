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
 *  @author Zach Blick, Kate Little
 */
public class TextCompressor {
    private static final int ALLCODES = 4096;
    private static final int EOF = 80;
    private static final int BITS = 12;


    private static void compress() {
        // Initialize TST with 256 codes
        TST values = new TST();
        for (int i = 0; i < 256; i++){
            values.insert(String.valueOf((char)i), i);
        }
        // Represents how many codes have been created
        int numCodes = 256;
        // Represents the code of the next String/prefix found
        int startCode = 81;

        int currentIndex = 0;
        String text = BinaryStdIn.readString();

        // LZW algorithm here
        while (currentIndex < text.length()){
            String prefix = values.getLongestPrefix(text, currentIndex);
            int code = values.lookup(prefix);
            // Slide further down text
            currentIndex += prefix.length();
            // Write 12-bit code associated with prefix
            BinaryStdOut.write(code, BITS);
            // Scan one character ahead only if within bounds
            if (currentIndex < text.length() && numCodes < ALLCODES) {
                String ahead = prefix + text.charAt(currentIndex);
                values.insert(ahead, startCode);
                // Increment to represent new # of codes
                numCodes++;
                // Increment so next code is different
                startCode++;
            }
            else{
                break;
            }
        }
        // Write EOF character
        BinaryStdOut.write(EOF, BITS);
        BinaryStdOut.close();
    }

    // DON'T USE SUBSTRING ANYWHERE! KEEP TRACK OF THE START INDEX TO SAVE A LOT OF SPACE
    private static void expand() {
        // Initialize array with all Ascii values
        int currentCode = 81;
        String[] prefixes = new String[ALLCODES];
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
