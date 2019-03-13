package de.kneitzel.encryption;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the BaseAlphabet class
 */
public class BaseAlphabetTests {

    /**
     * Tests the isValidCharacter of the BaseAlphabet class.
     */
    @Test
    public void isInvalidCharacterTests() {
        BaseAlphabet alphabet = new TestAlphabet();

        Assert.assertTrue(alphabet.isValidCharacter('a'));
        Assert.assertTrue(alphabet.isValidCharacter('_'));
        Assert.assertFalse(alphabet.isValidCharacter('A'));
    }

    /**
     * Tests the replaceIllegalCharacters of the BaseAlphabet class.
     */
    @Test
    public void replaceInvalidCharactersTests() {
        BaseAlphabet alphabet = new TestAlphabet();

        Assert.assertEquals("abcd", alphabet.replaceIllegalCharacters("abcd", '_'));
        Assert.assertEquals("_abcd", alphabet.replaceIllegalCharacters("Aabcd", '_'));
        Assert.assertEquals("a_bcd", alphabet.replaceIllegalCharacters("aBbcd", '_'));
        Assert.assertEquals("abcd_", alphabet.replaceIllegalCharacters("abcdD", '_'));
        Assert.assertEquals("_ab_cd_", alphabet.replaceIllegalCharacters("AabCcdD", '_'));

        // Check Exception
        try {
            Assert.assertEquals("abcd", alphabet.replaceIllegalCharacters("abcd", 'A'));
            Assert.fail();
        } catch (IllegalArgumentException ex) {
            // Expected exception
        }
    }
}
