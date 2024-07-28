package dev.satyrn.lunamoth.util.v1;

import org.junit.jupiter.api.Test;

import java.util.Formatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the {@link Cast} utility.
 *
 * @author Isabel Maskrey
 * @since  1.0-SNAPSHOT
 * @see    Cast
 */
public class CastTest {
    /**
     * Instantiates this test class.
     *
     * @since 1.0-SNAPSHOT
     */
    public CastTest() { }

    /**
     * Tests the cast constructor, which should always throw an {@link UnsupportedOperationException}.
     *
     * @since 1.0-SNAPSHOT
     * @see   Cast#Cast()
     */
    @Test
    public void testCastConstructor() {
        // Attempt to instantiate Cast, which should throw UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, Cast::new);
    }

    /**
     * Tests the {@link Cast#to(Class, Object)} method.
     * <ul>
     *     <li>If a null class is cast, the result should be an empty optional</li>
     *     <li>If the cast class is a valid supertype, the result is not empty, and is equivalent to the input</li>
     *     <li>If the cast class in String and the input is a String, returns the input in an optional</li>
     *     <li>If the cast class is String and the input is not a String, return the toString value of the object</li>
     *     <li>If the cast class cannot be assinged to the target class, return an empty optional.</li>
     * </ul>
     * 
     * @since 1.0-SNAPSHOT
     * @see   Cast#to(Class, Object)
     */
    @Test
    public void testTo() {
        // Null object cast
        Optional<Object> nullResult = Cast.to(Object.class, null);
        assertTrue(nullResult.isEmpty());

        // Cast to valid superclass
        Formatter formatter = new Formatter();
        Optional<Object> success = Cast.to(Object.class, formatter);
        assertFalse(success.isEmpty());
        assertEquals(formatter, success.get());

        // Cast string to string (a very silly goose thing to do)
        String testString = "fee fie fo fum i smell the blood of an englishmun";
        Optional<String> stringToString = Cast.to(String.class, testString);
        assertFalse(stringToString.isEmpty());
        assertEquals(testString, stringToString.get());

        // Cast any class to a string
        Object testObj = new Object();
        Optional<String> toString = Cast.to(String.class, testObj);
        assertFalse(toString.isEmpty());
        assertEquals(testObj.toString(), toString.get());

        // Cast unassignable class to other
        Optional<Formatter> formatterFail = Cast.to(Formatter.class, testObj);
        assertTrue(formatterFail.isEmpty());
    }

    /**
     * Tests the {@link Cast#to(Class, Object)} method.
     * <ul>
     *     <li>If a null class is cast, the result should be the supplier result</li>
     *     <li>If the cast class is a valid supertype, the result is not empty, and is equivalent to the input</li>
     *     <li>If the cast class in String and the input is a String, returns the input</li>
     *     <li>If the cast class is String and the input is not a String, return the toString value of the object</li>
     *     <li>If the cast class cannot be assigned to the target class, return an empty optional.</li>
     * </ul>
     *
     * @since 1.0-SNAPSHOT
     * @see   Cast#to(Class, Object)
     */
    @Test
    public void testOrElseGet() {
        // Null object cast should return default
        Object defaultObj = new Object();
        Object orElseGetObj = Cast.orElseGet(Object.class, null, () -> defaultObj);
        assertNotNull(orElseGetObj);
        assertEquals(defaultObj, orElseGetObj);

        // Cast to valid superclass should not return default
        Formatter formatter = new Formatter();
        Object formatterAsObject = Cast.orElseGet(Object.class, formatter, () -> defaultObj);
        assertNotNull(formatterAsObject);
        assertNotEquals(defaultObj, formatterAsObject);
        assertEquals(formatter, formatterAsObject);

        // Cast string to string also a valid option and should not return default
        String testString = "test";
        String defaultString = "default";
        String stringToString = Cast.orElseGet(String.class, testString, () -> defaultString);
        assertNotNull(stringToString);
        assertNotEquals(defaultString, stringToString);
        assertEquals(testString, stringToString);

        // Cast any class to a string should succeed.
        // We will use Formatter and defaultString
        String formatterToString = formatter.toString();
        String formatterCastAsString = Cast.orElseGet(String.class, formatter, () -> defaultString);
        assertNotNull(formatterCastAsString);
        assertNotEquals(defaultString, formatterCastAsString);
        assertEquals(formatterToString, formatterCastAsString);

        //Unassignable cast falls back to default
        Object castObjectToFormatter = new Object();
        Formatter objectOrDefaultFormatter = Cast.orElseGet(Formatter.class, castObjectToFormatter, () -> formatter);
        assertNotNull(objectOrDefaultFormatter);
        assertNotEquals(castObjectToFormatter, objectOrDefaultFormatter);
        assertEquals(formatter, objectOrDefaultFormatter);
    }

    @Test
    public void testOrElse() {
        // Null object cast should return default
        Object defaultObj = new Object();
        Object orElseGetObj = Cast.orElse(Object.class, null, defaultObj);
        assertNotNull(orElseGetObj);
        assertEquals(defaultObj, orElseGetObj);

        // Cast to valid superclass should not return default
        Formatter formatter = new Formatter();
        Object formatterAsObject = Cast.orElse(Object.class, formatter, defaultObj);
        assertNotNull(formatterAsObject);
        assertNotEquals(defaultObj, formatterAsObject);
        assertEquals(formatter, formatterAsObject);

        // Cast string to string also a valid option and should not return default
        String testString = "test";
        String defaultString = "default";
        String stringToString = Cast.orElse(String.class, testString, defaultString);
        assertNotNull(stringToString);
        assertNotEquals(defaultString, stringToString);
        assertEquals(testString, stringToString);

        // Cast any class to a string should succeed.
        // We will use Formatter and defaultString
        String formatterToString = formatter.toString();
        String formatterCastAsString = Cast.orElse(String.class, formatter, defaultString);
        assertNotNull(formatterCastAsString);
        assertNotEquals(defaultString, formatterCastAsString);
        assertEquals(formatterToString, formatterCastAsString);

        //Unassignable cast falls back to default
        Object castObjectToFormatter = new Object();
        Formatter objectOrDefaultFormatter = Cast.orElse(Formatter.class, castObjectToFormatter, formatter);
        assertNotNull(objectOrDefaultFormatter);
        assertNotEquals(castObjectToFormatter, objectOrDefaultFormatter);
        assertEquals(formatter, objectOrDefaultFormatter);
    }

    @Test
    public void testOrNull() {
        // Null object cast should return null
        Object orElseGetObj = Cast.orNull(Object.class, null);
        assertNull(orElseGetObj);

        // Cast to valid superclass should not return default
        Formatter formatter = new Formatter();
        Object formatterAsObject = Cast.orNull(Object.class, formatter);
        assertNotNull(formatterAsObject);
        assertEquals(formatter, formatterAsObject);

        // Cast string to string also a valid option and should not return default
        String testString = "test";
        String stringToString = Cast.orNull(String.class, testString);
        assertNotNull(stringToString);
        assertEquals(testString, stringToString);

        // Cast any class to a string should succeed.
        // We will use Formatter and defaultString
        String formatterToString = formatter.toString();
        String formatterCastAsString = Cast.orNull(String.class, formatter);
        assertNotNull(formatterCastAsString);
        assertEquals(formatterToString, formatterCastAsString);

        // Unassignable cast falls back to null
        Object castObjectToFormatter = new Object();
        Formatter objectOrDefaultFormatter = Cast.orNull(Formatter.class, castObjectToFormatter);
        assertNull(objectOrDefaultFormatter);
    }

    @Test
    public void testToString() {
        int testValue = 54897;
        Object testObject = new Object();
        String testString = "testString";
        assertEquals(Integer.toString(testValue), Cast.toString(testValue));
        assertEquals(testString, Cast.toString(testString));
        assertEquals(testObject.toString(), Cast.toString(testObject));
        assertNull(null, Cast.toString(null));
    }
}
