package dev.satyrn.lunamoth.util.v1;

import org.javatuples.Pair;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the {@link ExperienceUtil} class.
 *
 * @author Isabel Maskrey
 * @since  1.0-SNAPSHOT
 * @see    ExperienceUtil
 */
public class ExperienceUtilTest {
    /**
     * Creates a new instance of this test class.
     *
     * @since 1.0-SNAPSHOT
     */
    public ExperienceUtilTest() { }

    /**
     * Test case for {@link ExperienceUtil#convertTotalXPToLevelAndPercentProgress(BigInteger)} method.
     * <p>
     * Tests various scenarios:
     * <ul>
     *     <li>Converts total XP value 7. Expected level 1 and 0.0% progress.</li>
     *     <li>Converts total XP value 100. Expected level 7 and approximately 44-45% progress.</li>
     *     <li>Converts total XP value 352. Expected level 16 and 0.0% progress.</li>
     *     <li>Converts total XP value 1507. Expected level 31 and 0.0% progress.</li>
     *     <li>Converts total XP value greater than 1507. Expected level 31 and progress between 0% and 100%.</li>
     * </ul>
     *
     * @since 1.0-SNAPSHOT
     * @see   ExperienceUtil#convertTotalXPToLevelAndPercentProgress(BigInteger)
     */
    @Test
    public void testConvertTotalXPToLevelAndPercentProgress() {
        // Test cases for convertTotalXPToLevelAndPercentProgress method
        BigInteger xp7 = BigInteger.valueOf(7);
        BigInteger xp100 = BigInteger.valueOf(100);
        BigInteger xp352 = BigInteger.valueOf(352);
        BigInteger xp1507 = BigInteger.valueOf(1507);
        BigInteger xpMoreThan1507 = xp1507.add(BigInteger.ONE);

        Pair<Integer, Float> result1 = ExperienceUtil.convertTotalXPToLevelAndPercentProgress(xp7);
        assertEquals(1, result1.getValue0());
        assertEquals(0.0f, result1.getValue1());

        Pair<Integer, Float> result2 = ExperienceUtil.convertTotalXPToLevelAndPercentProgress(xp100);
        assertEquals(7, result2.getValue0());
        assertTrue(0.44F < result2.getValue1() && result2.getValue1() < 0.45F, "Expected between 0.42 and 0.43, actual " +
                result2.getValue1());

        Pair<Integer, Float> result3 = ExperienceUtil.convertTotalXPToLevelAndPercentProgress(xp352);
        assertEquals(16, result3.getValue0());
        assertEquals(0.0f, result3.getValue1());

        Pair<Integer, Float> result4 = ExperienceUtil.convertTotalXPToLevelAndPercentProgress(xp1507);
        assertEquals(31, result4.getValue0());
        assertEquals(0.0f, result4.getValue1());

        Pair<Integer, Float> result5 = ExperienceUtil.convertTotalXPToLevelAndPercentProgress(xpMoreThan1507);
        assertEquals(31, result5.getValue0());
        assertTrue(result5.getValue1() > 0.0f && result5.getValue1() < 1.0f);
    }

    /**
     * Test cases for {@link ExperienceUtil#convertLevelAndPercentProgressToTotalXP(BigDecimal)} method.
     * <ul>
     *     <li>Test case for level 1.5 with floored 50% progress. Expected XP: 11 (7 base XP + 4 floored progress XP)</li>
     *     <li>Test case for level 16.5 with floored 50% progress. Expected XP: 373 (352 base XP + 21 floored progress XP)</li>
     *     <li>Test case for level 31.5 with floored 50% progress Expected XP: 1567 (1507 base XP + 60 floored progress XP)</li>
     *     <li>Test case for level 32.5 with floored 50% progress: Expected XP: 1693 (1628 base XP + 65 floored progress XP)</li>
     * </ul>
     *
     * @since 1.0-SNAPSHOT
     * @see   ExperienceUtil#convertLevelAndPercentProgressToTotalXP(BigDecimal)
     * @see   ExperienceUtil#convertLevelAndPercentProgressToTotalXP(int, float)
     */
    @Test
    public void testConvertLevelAndPercentProgressToTotalXP() {
        // Test cases for convertLevelAndPercentProgressToTotalXP methods
        BigDecimal levelAndProgress1 = new BigDecimal("1.5");
        BigDecimal levelAndProgress2 = new BigDecimal("16.5");
        BigDecimal levelAndProgress3 = new BigDecimal("31.5");
        BigDecimal levelAndProgress4 = new BigDecimal("32.5");

        BigInteger xp1 = ExperienceUtil.convertLevelAndPercentProgressToTotalXP(levelAndProgress1);
        // XP at lvl 1 with added floored 50% progress
        assertEquals(BigInteger.valueOf(7 + 4), xp1);

        // XP at lvl 16 with added floored 50% progress
        BigInteger xp2 = ExperienceUtil.convertLevelAndPercentProgressToTotalXP(levelAndProgress2);
        assertEquals(BigInteger.valueOf(352 + 21), xp2);

        // XP at lvl 31 with added floored 50% progress
        BigInteger xp3 = ExperienceUtil.convertLevelAndPercentProgressToTotalXP(levelAndProgress3);
        assertEquals(BigInteger.valueOf(1507 + 60), xp3);

        // XP at lvl 32 with added floored 50% progress
        BigInteger xp4 = ExperienceUtil.convertLevelAndPercentProgressToTotalXP(levelAndProgress4);
        assertEquals(BigInteger.valueOf(1628 + 65), xp4);
    }

    /**
     * Test cases for {@link ExperienceUtil#getCurrentLevelPercentProgressAsPoints(int, float)} method.
     * <ul>
     *     <li>Test case for level 10 with 50% progress. Expected points: 13 (Halfway progress is 13.5, floored to 13)</li>
     *     <li>Test case for level 25 with 50% progress. Expected points: 43 (Halfway progress is 43.5, floored to 43)</li>
     *     <li>Test case for level 35 with 50% progress. Expected points: 78 (Halfway progress is 78.5, floored to 78)</li>
     * </ul>
     * 
     * @since 1.0-SNAPSHOT
     * @see   ExperienceUtil#getCurrentLevelPercentProgressAsPoints(int, float)
     */
    @Test
    public void testGetCurrentLevelPercentProgressAsPoints() {
        // Test cases for getCurrentLevelPercentProgressAsPoints methods
        BigInteger points1 = ExperienceUtil.getCurrentLevelPercentProgressAsPoints(10, 0.5f);
        // Halfway progress is 13.5, floored
        assertEquals(BigInteger.valueOf(13), points1);

        BigInteger points2 = ExperienceUtil.getCurrentLevelPercentProgressAsPoints(25, 0.5f);
        // Halfway progress is 43.5, floored
        assertEquals(BigInteger.valueOf(43), points2);

        BigInteger points3 = ExperienceUtil.getCurrentLevelPercentProgressAsPoints(35, 0.5f);
        // Halfway progress is 78.5, floored
        assertEquals(BigInteger.valueOf(78), points3);
    }

    /**
     * Test case for instantiating {@link ExperienceUtil}, which should throw {@link UnsupportedOperationException}.
     *
     * @since 1.0-SNAPSHOT
     * @see   ExperienceUtil#ExperienceUtil() 
     */
    @Test
    public void testInstantiationShouldThrowUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ExperienceUtil::new);
    }

    /**
     * Test case for {@link ExperienceUtil#getTotalXPPointsAtLevel(int)} method.
     * <ul>
     *     <li>Tests XP calculation at level 1. Expected XP: 7.</li>
     *     <li>Tests XP calculation at level 16. Expected XP: 352.</li>
     *     <li>Tests XP calculation at level 31. Expected XP: 1507.</li>
     *     <li>Tests XP calculation at level 32. Expected XP: 1628.</li>
     * </ul>
     *
     * @since 1.0-SNAPSHOT
     * @see   ExperienceUtil#getTotalXPPointsAtLevel(int)
     */
    @Test
    public void testGetTotalXPPointsAtLevel() {
        int level1 = 1;
        int level16 = 16;
        int level31 = 31;
        int level32 = 32;

        BigInteger xp1 = ExperienceUtil.getTotalXPPointsAtLevel(level1);
        // XP at lvl 1 with added floored 50% progress
        assertEquals(BigInteger.valueOf(7), xp1);

        // XP at lvl 16 with added floored 50% progress
        BigInteger xp2 = ExperienceUtil.getTotalXPPointsAtLevel(level16);
        assertEquals(BigInteger.valueOf(352), xp2);

        // XP at lvl 31 with added floored 50% progress
        BigInteger xp3 = ExperienceUtil.getTotalXPPointsAtLevel(level31);
        assertEquals(BigInteger.valueOf(1507), xp3);

        // XP at lvl 32 with added floored 50% progress
        BigInteger xp4 = ExperienceUtil.getTotalXPPointsAtLevel(level32);
        assertEquals(BigInteger.valueOf(1628), xp4);
    }
}
