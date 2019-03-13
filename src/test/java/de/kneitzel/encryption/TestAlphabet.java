package de.kneitzel.encryption;

/**
 * Test Alphabet just with lowercase chaarcters and underscore.
 */
public class TestAlphabet extends BaseAlphabet {
    /**
     * All characters we want to use.
     */
    private static final char[] ALL_CHARS = new char[]{
            // lowercase characters
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
            's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '_'};

    /**
     * Gets the available characters.
     *
     * @return
     */
    @Override
    public char[] availableCharacters() {
        return ALL_CHARS;
    }

    /**
     * Gets the radix of the alphabet.
     *
     * @return
     */
    @Override
    public Integer radix() {
        return ALL_CHARS.length;
    }
}
