package dev.satyrn.lunamoth.util.v1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the {@link MathUtil} class.
 *
 * @author Isabel Maskrey
 * @since  1.0-SNAPSHOT
 * @see    MathUtil
 */
public class MathUtilTest {
    /**
     * Creates a new instance of this test class.
     *
     * @since 1.0-SNAPSHOT
     */
    public MathUtilTest() { }

    /**
     * Tests the double clamp method {@link MathUtil#clamp(double, double, double)}
     * <p>
     * The test executes the following checks:
     * <ul>
     *     <li>Makes sure a value within the clamp range is maintained</li>
     *     <li>Makes sure a value lower than the clamp range is raised to the minimum value</li>
     *     <li>Makes sure a value higher than the clamp range is lowered to the maximum value</li>
     *     <li>Makes sure that the value is set to the min/max values if the range has 0 size</li>
     *     <li>Makes sure an {@link IllegalArgumentException} is thrown when the clamp min value is higher than the max.</li>
     * </ul>
     *
     * @since 1.0-SNAPSHOT
     * @see   MathUtil#clamp(double, double, double)
     */
    @Test
    public void testClampDouble() {
        double actual = MathUtil.clamp(5.0D, 0.0D, 10.0D);
        assertEquals(5.0D, actual);

        actual = MathUtil.clamp(0.0D, 5.0D, 10.0D);
        assertEquals(5.0D, actual);

        actual = MathUtil.clamp(10.0D, 0.0D, 5.0D);
        assertEquals(5.0D, actual);

        actual = MathUtil.clamp(10.0D, 5.0D, 5.0D);
        assertEquals(5.0D, actual);

        assertThrows(IllegalArgumentException.class, () -> {
            MathUtil.clamp(5.0D, 10.0D, 5.0D);
        });
    }

    /**
     * Tests the float clamp method {@link MathUtil#clamp(float, float, float)}
     * <p>
     * The test executes the following checks:
     * <ul>
     *     <li>Makes sure a value within the clamp range is maintained</li>
     *     <li>Makes sure a value lower than the clamp range is raised to the minimum value</li>
     *     <li>Makes sure a value higher than the clamp range is lowered to the maximum value</li>
     *     <li>Makes sure that the value is set to the min/max values if the range has 0 size</li>
     *     <li>Makes sure an {@link IllegalArgumentException} is thrown when the clamp min value is higher than the max.</li>
     * </ul>
     *
     * @since 1.0-SNAPSHOT
     * @see   MathUtil#clamp(float, float, float)
     */
    @Test
    public void testClampFloat() {
        double actual = MathUtil.clamp(5.0F, 0.0F, 10.0F);
        assertEquals(5.0F, actual);

        actual = MathUtil.clamp(0.0F, 5.0F, 10.0F);
        assertEquals(5.0F, actual);

        actual = MathUtil.clamp(10.0F, 0.0F, 5.0F);
        assertEquals(5.0F, actual);

        actual = MathUtil.clamp(10.0F, 5.0F, 5.0F);
        assertEquals(5.0F, actual);

        assertThrows(IllegalArgumentException.class, () -> {
            MathUtil.clamp(5.0F, 10.0F, 5.0F);
        });
    }

    /**
     * Tests the integer clamp method {@link MathUtil#clamp(int, int, int)}
     * <p>
     * The test executes the following checks:
     * <ul>
     *     <li>Makes sure a value within the clamp range is maintained</li>
     *     <li>Makes sure a value lower than the clamp range is raised to the minimum value</li>
     *     <li>Makes sure a value higher than the clamp range is lowered to the maximum value</li>
     *     <li>Makes sure that the value is set to the min/max values if the range has 0 size</li>
     *     <li>Makes sure an {@link IllegalArgumentException} is thrown when the clamp min value is higher than the max.</li>
     * </ul>
     *
     * @since 1.0-SNAPSHOT
     * @see   MathUtil#clamp(int, int, int)
     */
    @Test
    public void testClampInt() {
        double actual = MathUtil.clamp(5, 0, 10);
        assertEquals(5, actual);

        actual = MathUtil.clamp(0, 5, 10);
        assertEquals(5, actual);

        actual = MathUtil.clamp(10, 0, 5);
        assertEquals(5, actual);

        actual = MathUtil.clamp(10, 5, 5);
        assertEquals(5, actual);

        assertThrows(IllegalArgumentException.class, () -> {
            MathUtil.clamp(5, 10, 5);
        });
    }

    /**
     * Test case for {@link MathUtil#log(double, double)} method.
     * <p>
     * Tests various scenarios:
     * <ul>
     *     <li>Calculates the logarithm of 8 with base 2. Expected result is 3.0.</li>
     *     <li>Throws {@link IllegalArgumentException} when base is 0.</li>
     *     <li>Throws {@link IllegalArgumentException} when base is 1.</li>
     *     <li>Throws {@link IllegalArgumentException} when value is 0.</li>
     *     <li>Throws {@link UnsupportedOperationException} when attempting to instantiate {@link MathUtil}.</li>
     * </ul>
     *
     * @since 1.0-SNAPSHOT
     * @see   MathUtil#log(double, double)
     */
    @Test
    public void log() {
        final double expected = 3.0D;
        final double actual = MathUtil.log(8, 2);
        assertEquals(expected, actual);

        assertThrows(IllegalArgumentException.class, () -> MathUtil.log(8, 0));
        assertThrows(IllegalArgumentException.class, () -> MathUtil.log(8, 1));
        assertThrows(IllegalArgumentException.class, () -> MathUtil.log(0, 2));
        assertThrows(UnsupportedOperationException.class, MathUtil::new);
    }
}