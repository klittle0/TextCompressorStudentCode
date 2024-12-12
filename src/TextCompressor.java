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
    private static final int MAXCODES = 4096;
    private static final int EOF = 256;
    private static final int BITS = 12;


    private static void compress() {
        // Initialize TST with 256 (EOF) codes
        TST values = new TST();
        for (int i = 0; i < EOF; i++){
            values.insert(String.valueOf((char)i), i);
        }
        // Represents how many codes have been created
        int currentCodes = EOF + 1;
        
        String text = BinaryStdIn.readString();

        // LZW algorithm here
        for (int i = 0; i < text.length(); i++){
            String prefix = values.getLongestPrefix(text, i);
            int code = values.lookup(prefix);
            // Write out 12-bit code associated with prefix
            BinaryStdOut.write(code, BITS);

            // If there's
            if (i + prefix.length() < text.length()){
                // Look one character ahead & add another code if there's room
                if (currentCodes < MAXCODES){
                    String ahead = prefix + text.charAt(i + prefix.length());
                    values.insert(ahead, currentCodes);
                    currentCodes++;
                }
            }
            // Increment, but only if within bounds
            i += prefix.length() - 1;
        }
        // Write EOF character
        BinaryStdOut.write(EOF, BITS);
        BinaryStdOut.close();
    }

    private static void expand() {
        // Initialize array with all Ascii values
        int numCodes = EOF + 1;
        String[] prefixes = new String[MAXCODES];
        for (int i = 0; i < EOF; i++){
            prefixes[i] = String.valueOf((char)i);
        }
        // Remains empty for end of file character
        prefixes[EOF] = " ";

        // Read 12 bits at a time
        int code = BinaryStdIn.readInt(BITS);
        String value = prefixes[code];
        // Continue until reaching the EOF code word.
        while (code != EOF){
            BinaryStdOut.write(value);
            // Look ahead at next code
            code = BinaryStdIn.readInt(BITS);
            // If the code doesn't exist yet in prefixes, I need to construct the string myself & add the code
            String ahead = prefixes[code];
            if (code == numCodes){
                ahead = value + value.charAt(0);
                prefixes[numCodes] = ahead;
            }
            // Add string to array only if code # is less than 4096
            if (numCodes < MAXCODES){
                prefixes[numCodes] = value + prefixes[code].charAt(0);
            }
            numCodes += 1;
            value = ahead;
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}