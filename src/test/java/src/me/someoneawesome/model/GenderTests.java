package src.me.someoneawesome.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GenderTests {

    @Test
    public void getOppositeGender_getsOpposite() {
        assertEquals(Gender.MALE, Gender.FEMALE.getOppositeGender());
        assertEquals(Gender.FEMALE, Gender.MALE.getOppositeGender());
        assertEquals(Gender.OTHER, Gender.OTHER.getOppositeGender());
    }

    @Test
    public void areEqualGenders_success() {
        assertTrue(Gender.areEqualGenders(Gender.MALE, Gender.MALE));
        assertTrue(Gender.areEqualGenders(Gender.FEMALE, Gender.FEMALE));
    }

    @Test
    public void areEqualGender_fail() {
        assertFalse(Gender.areEqualGenders(Gender.MALE, Gender.FEMALE));
        assertFalse(Gender.areEqualGenders(Gender.FEMALE, Gender.MALE));
        assertFalse(Gender.areEqualGenders(Gender.MALE, Gender.OTHER));
        assertFalse(Gender.areEqualGenders(Gender.OTHER, Gender.MALE));
        assertFalse(Gender.areEqualGenders(Gender.FEMALE, Gender.OTHER));
        assertFalse(Gender.areEqualGenders(Gender.OTHER, Gender.FEMALE));
        assertFalse(Gender.areEqualGenders(Gender.OTHER, Gender.OTHER));
    }

    @Test
    public void areEqualGender_null_exception() {
        assertThrows(IllegalArgumentException.class, () -> {
            Gender.areEqualGenders(Gender.NULL, Gender.MALE);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Gender.areEqualGenders(Gender.FEMALE, Gender.NULL);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Gender.areEqualGenders(Gender.NULL, Gender.NULL);
        });
    }
}
