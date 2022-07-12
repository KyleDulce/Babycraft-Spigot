package me.someoneawesome.babycraft.model.comparator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class AlphabeticalComparatorTest {

    @ParameterizedTest
    @CsvSource({"abc,bcd", "aab,abb", "a,ab", "a,bc"})
    public void testCompare_ascending(String s1, String s2) {
        AlphabeticalComparator comparator = new AlphabeticalComparator();
        int actual = comparator.compare(s1, s2);

        assertEquals(-1, actual);
    }

    @ParameterizedTest
    @CsvSource({"bcd,abc", "abb,aab", "ab,a", "ba,a"})
    public void testCompare_descending(String s1, String s2) {
        AlphabeticalComparator comparator = new AlphabeticalComparator();
        int actual = comparator.compare(s1, s2);

        assertEquals(1, actual);
    }

    @ParameterizedTest
    @CsvSource({"abc,abc", "aaa,aaa", "a,a", "bro,bro"})
    public void testCompare_equal(String s1, String s2) {
        AlphabeticalComparator comparator = new AlphabeticalComparator();
        int actual = comparator.compare(s1, s2);

        assertEquals(0, actual);
    }
}
