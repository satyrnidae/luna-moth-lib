package dev.satyrn.lunamoth.util.v1;

import org.javatuples.Pair;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Provides utilities for converting Minecraft player XP levels and percentage progress to total XP values and back.
 *
 * @author Isabel Maskrey
 * @since 1.0-SNAPSHOT
 */
public final class ExperienceUtil {
    /**
     * The math context used for all calculations.
     *
     * @since 1.0-SNAPSHOT
     */
    private static final @NotNull MathContext MATH_CONTEXT = new MathContext(34, RoundingMode.HALF_UP);

    /**
     * The threshold at which to switch from using {@code Func1} to {@code Func2} to calculate the level and percentage
     *  progress to next level from the player's total XP value.
     * <p>
     * Set equal to 352, the amount of XP that the player will have at level 16 with 0% progress to the next level.
     * 
     * @since 1.0-SNAPSHOT
     * @see   #convertTotalXPToLevelAndPercentProgress(BigInteger)
     */
    private static final @NotNull BigInteger TOTAL_XP_THRESHOLD_1 = BigInteger.valueOf(352);

    /**
     * The threshold at which to switch from using {@link #convertTotalXPToLevelAndPercentProgress$Func2(BigInteger)}
     *  to {@link #convertTotalXPToLevelAndPercentProgress$Func3(BigInteger)} to calculate the level and percentage
     *  progress to next level from the player's total XP value.
     * <p>
     * Set equal to 1507, the amount of XP that the player will have at level 31 with 0% progress to the next level.
     *
     * @since 1.0-SNAPSHOT
     * @see   #convertTotalXPToLevelAndPercentProgress(BigInteger)
     */
    private static final @NotNull BigInteger TOTAL_XP_THRESHOLD_2 = BigInteger.valueOf(1507);

    /**
     * The threshold for switching from {@code Func1} to {@code Func2} for level progress to total XP functions.
     * <p>
     * Set to 16 for ease of use in both {@link #convertLevelAndPercentProgressToTotalXP(BigDecimal)} and
     * {@link #getCurrentLevelPercentProgressAsPoints(int, float)}
     *
     * @since 1.0-SNAPSHOT
     * @see   #convertLevelAndPercentProgressToTotalXP(int, float)
     * @see   #getCurrentLevelPercentProgressAsPoints(int, float)
     */
    private static final int LEVEL_THRESHOLD_1 = 16;

    /**
     * The threshold for switching from {@code Func2} to {@code Func3} for level progress to total XP functions.
     * <p>
     * Set to 31 for ease of use in both {@link #convertLevelAndPercentProgressToTotalXP(BigDecimal)} and
     * {@link #getCurrentLevelPercentProgressAsPoints(int, float)}
     *
     * @since 1.0-SNAPSHOT
     * @see   #convertLevelAndPercentProgressToTotalXP(int, float)
     * @see   #getCurrentLevelPercentProgressAsPoints(int, float)
     */
    private static final int LEVEL_THRESHOLD_2 = 31;

    /**
     * Constructs a new instance of {@code ExperienceUtil}.
     * <p>
     * Always throws {@code UnsupportedOperationExcception}, see API note.
     *
     * @throws  UnsupportedOperationException if an attempt is made to instantiate {@code ExperienceUtil}.
     * @apiNote This constructor is private to prevent instantiation of {@code ExperienceUtil}, as it is a static
     *          utility class. Attempts to instantiate this class will result in a {@link UnsupportedOperationException}
     *          being thrown.
     * @since   1.0-SNAPSHOT
     */
    @Contract(value = "-> fail", pure = true)
    ExperienceUtil() {
        throw new UnsupportedOperationException("ExperienceUtil cannot be instantiated.");
    }

    /**
     * Splits a player's total XP count to an integer level and a float value representing the percentage progress to
     * the next level.
     * <p>
     * Note that some precision loss is unavoidable at higher levels due to the exponentially increasing amount of XP
     * needed to progress at higher levels and the single-precision number used to store the percentage progress in
     * the game.
     * <p>
     * The following functions are used to convert the total XP number to a level and progress value, where \(x\) is
     * equal to the total XP value.
     * <ul>
     *     <li>For levels 16 and below, uses the function \(\sqrt{x + 9} - 3\)</li>
     *     <li>For levels below level 32, uses the function
     *     \(\frac{81}{10} + \sqrt{\frac{2\left(x - \frac{7839}{40}\right)}{5}}\)</li>
     *     <li>For levels 32 and above, uses the function
     *     \(\frac{325}{18} + \sqrt{\frac{2(x - \frac{54215}{72})}{9}}\)</li>
     * </ul>
     * See {@link #convertLevelAndPercentProgressToTotalXP(int, float)} for information on how a level value is
     * converted to an XP value.
     *
     * @param  total The total XP value as an integer.
     * @return The level and progress values as a Pair.
     * @throws NullPointerException if {@code total} is {@code null}.
     * @since  1.0-SNAPSHOT
     */
    @Contract(value = "_ -> !null", pure = true)
    public static @NotNull Pair<@NotNull Integer, @NotNull Float> convertTotalXPToLevelAndPercentProgress(final @NotNull BigInteger total) {
        final @NotNull BigDecimal levelAndPercentProgress;
        // 352 is the XP value at 16 and 0%
        if (total.compareTo(TOTAL_XP_THRESHOLD_1) <= 0) {
            levelAndPercentProgress = convertTotalXPToLevelAndPercentProgress$Func1(total);
        // 1507 is the XP value at 31 and 0%
        } else if (total.compareTo(TOTAL_XP_THRESHOLD_2) <= 0) {
            levelAndPercentProgress = convertTotalXPToLevelAndPercentProgress$Func2(total);
        } else {
            levelAndPercentProgress = convertTotalXPToLevelAndPercentProgress$Func3(total);
        }
        final int level = levelAndPercentProgress.setScale(0, RoundingMode.FLOOR).toBigIntegerExact().intValue();
        final float percentProgress = levelAndPercentProgress.remainder(BigDecimal.ONE, MATH_CONTEXT).floatValue();
        return new Pair<>(level, percentProgress);
    }

    /**
     * For levels 16 and below, use the function \(\sqrt{x + 9} - 3\)
     *
     * @param  total The player's total XP value
     * @return The combined level progress as a {@link BigDecimal}, with level in the whole number value and progress in
     *         the decimal.
     * @throws NullPointerException if {@code total} is {@code null}
     * @since  1.0-SNAPSHOT
     */
    @Contract(value = "_ -> !null", pure = true)
    private static @NotNull BigDecimal convertTotalXPToLevelAndPercentProgress$Func1(final @NotNull BigInteger total) {
        Objects.requireNonNull(total);
        return new BigDecimal(total, MATH_CONTEXT)
                .add(BigDecimal.valueOf(9), MATH_CONTEXT)
                .sqrt(MATH_CONTEXT)
                .subtract(BigDecimal.valueOf(3), MATH_CONTEXT);
    }

    /**
     * For levels below 32, use the function \(\frac{81}{10} + \sqrt{\frac{2\left(x - \frac{7839}{40}\right)}{5}}\)
     *
     * @param  total The player's total XP value
     * @return The combined level progress as a {@link BigDecimal}, with level in the whole number value and progress in
     *         the decimal.
     * @throws NullPointerException if {@code total} is {@code null}
     * @since  1.0-SNAPSHOT
     */
    @Contract(value = "_ -> !null", pure = true)
    private static @NotNull BigDecimal convertTotalXPToLevelAndPercentProgress$Func2(final BigInteger total) {
        Objects.requireNonNull(total);
        return new BigDecimal(total, MATH_CONTEXT)
                .subtract(BigDecimal.valueOf(195975, 3), MATH_CONTEXT)
                .multiply(BigDecimal.valueOf(4, 1), MATH_CONTEXT)
                .sqrt(MATH_CONTEXT)
                .add(BigDecimal.valueOf(81, 1), MATH_CONTEXT);
    }

    /**
     * For level 32 and above, use the function \(\frac{325}{18} + \sqrt{\frac{2(x - \frac{54215}{72})}{9}}\)
     *
     * @param  total The player's total XP value.
     * @return The combined level progress as a {@link BigDecimal}, with level in the whole number value and progress in
     *         the decimal.
     * @throws NullPointerException if {@code total} is {@code null}.
     * @since  1.0-SNAPSHOT
     */
    @Contract(value = "_ -> !null", pure = true)
    private static @NotNull BigDecimal convertTotalXPToLevelAndPercentProgress$Func3(final BigInteger total) {
        Objects.requireNonNull(total);
        return new BigDecimal(total, MATH_CONTEXT)
                // Can't simplify this because this value is repeating :P
                .subtract(BigDecimal.valueOf(54215).divide(BigDecimal.valueOf(72), MATH_CONTEXT), MATH_CONTEXT)
                // Actually this one too. They really like their repeating numbers here.
                .multiply(BigDecimal.valueOf(2).divide(BigDecimal.valueOf(9), MATH_CONTEXT), MATH_CONTEXT)
                .sqrt(MATH_CONTEXT)
                // Wow, again???
                .add(BigDecimal.valueOf(325).divide(BigDecimal.valueOf(18), MATH_CONTEXT), MATH_CONTEXT);
    }

    /**
     * Converts the level and percentage of progress to the next level into a total XP value.
     * <ul>
     *     <li>For levels 0-15, uses the function \(x^2+2xy+6x+7y\).</li>
     *     <li>At level 16, the function changes slightly to \(x^2+5xy+6x-38y\).</li>
     *     <li>For levels 17-30, uses the function \(\frac{5}{2}x^2+5xy-\frac{81}{2}x-38y+360\).</li>
     *     <li>At level 31, this function changes slightly to \(\frac{5}{2}x^2+9xy-\frac{81}{2}x-158y+360\).</li>
     *     <li>For level 32 and above, uses the function \(\frac{9}{2}x^2+9xy-\frac{325}{2}x-158y+2220\).</li>
     * </ul>
     *
     * @param  levelAndPercentProgress A combined value representing the current level in the whole number value and the
     *                                 percentage progress to the next level in the decimal value.
     * @return The total XP that the player has at the level combined with the amount of XP the player has towards the
     *         next level.
     * @throws NullPointerException if {@code levelAndPercentProgress} is {@code null}
     * @since  1.0-SNAPSHOT
     */
    @Contract(value = "_ -> !null", pure = true)
    public static @NotNull BigInteger convertLevelAndPercentProgressToTotalXP(final @NotNull BigDecimal levelAndPercentProgress) {
        Objects.requireNonNull(levelAndPercentProgress);
        final int level = levelAndPercentProgress.setScale(0, RoundingMode.FLOOR).toBigIntegerExact().intValue();
        final float percentage = levelAndPercentProgress.remainder(BigDecimal.ONE, MATH_CONTEXT).floatValue();
        return convertLevelAndPercentProgressToTotalXP(level, percentage);
    }

    /**
     * Converts the level and percentage of progress to the next level into a total XP value.
     * <ul>
     *     <li>For levels 0-15, uses the function \(x^2+2xy+6x+7y\).</li>
     *     <li>At level 16, the function changes slightly to \(x^2+5xy+6x-38y\).</li>
     *     <li>For levels 17-30, uses the function \(\frac{5}{2}x^2+5xy-\frac{81}{2}x-38y+360\).</li>
     *     <li>At level 31, this function changes slightly to \(\frac{5}{2}x^2+9xy-\frac{81}{2}x-158y+360\).</li>
     *     <li>For level 32 and above, uses the function \(\frac{9}{2}x^2+9xy-\frac{325}{2}x-158y+2220\).</li>
     * </ul>
     *
     * @param  level           The XP level value.
     * @param  percentProgress The percentage progress towards the next level.
     * @return The total XP that the player has at the level combined with the amount of XP the player has towards the
     *         next level.
     * @since  1.0-SNAPSHOT
     */
    public static @NotNull BigInteger convertLevelAndPercentProgressToTotalXP(final int level,
                                                                              final float percentProgress) {
        if (level <= LEVEL_THRESHOLD_1) {
            return convertLevelAndPercentProgressToTotalXP$Func1(level, percentProgress);
        } else if (level <= LEVEL_THRESHOLD_2) {
            return convertLevelAndPercentProgressToTotalXP$Func2(level, percentProgress);
        } else {
            return convertLevelAndPercentProgressToTotalXP$Func3(level, percentProgress);
        }
    }

    /**
     * For levels 0-15, uses the function \(x^2+2xy+6x+7y\). At level 16, the function changes slightly to
     * \(x^2+5xy+6x-38y\).
     *
     * @param  level           The XP level value.
     * @param  percentProgress The percentage progress towards the next level.
     * @return The total XP that the player has at the level and progress to the next level.
     * @since  1.0-SNAPSHOT
     */
    private static @NotNull BigInteger convertLevelAndPercentProgressToTotalXP$Func1(final int level,
                                                                                     final float percentProgress) {
        return BigDecimal.valueOf(level)
                .pow(2, MATH_CONTEXT)
                .add(BigDecimal.valueOf(6).multiply(BigDecimal.valueOf(level), MATH_CONTEXT), MATH_CONTEXT)
                .add(new BigDecimal(getCurrentLevelPercentProgressAsPoints(level, percentProgress), MATH_CONTEXT), MATH_CONTEXT)
                .setScale(0, RoundingMode.FLOOR)
                .toBigIntegerExact();
    }

    /**
     * For levels 17-30, uses the function \(\frac{5}{2}x^2+5xy-\frac{81}{2}x-38y+360\). At level 31, this function
     * changes slightly to \(\frac{5}{2}x^2+9xy-\frac{81}{2}x-158y+360\).
     *
     * @param  level           The current level.
     * @param  percentProgress The percentage progress to the next level.
     * @return The total XP that the player has at the current level and progress to the next level.
     * @since  1.0-SNAPSHOT
     */
    private static @NotNull BigInteger convertLevelAndPercentProgressToTotalXP$Func2(final int level,
                                                                                     final float percentProgress) {
        return BigDecimal.valueOf(25, 1)
                .multiply(BigDecimal.valueOf(level).pow(2, MATH_CONTEXT), MATH_CONTEXT)
                .subtract(BigDecimal.valueOf(405, 1).multiply(BigDecimal.valueOf(level), MATH_CONTEXT), MATH_CONTEXT)
                .add(BigDecimal.valueOf(360), MATH_CONTEXT)
                .add(new BigDecimal(getCurrentLevelPercentProgressAsPoints(level, percentProgress), MATH_CONTEXT), MATH_CONTEXT)
                .setScale(0, RoundingMode.FLOOR)
                .toBigIntegerExact();
    }

    /**
     * For level 32 and above, uses the function \(\frac{9}{2}x^2+9xy-\frac{325}{2}x-158y+2220\).
     *
     * @param  level           The current level.
     * @param  percentProgress The percentage progress to the next level.
     * @return The total XP that the player has at the current level and progress to the next level.
     * @since  1.0-SNAPSHOT
     */
    private static @NotNull BigInteger convertLevelAndPercentProgressToTotalXP$Func3(final int level,
                                                                                     final float percentProgress) {
        return BigDecimal.valueOf(45, 1)
                .multiply(BigDecimal.valueOf(level).pow(2, MATH_CONTEXT), MATH_CONTEXT)
                .subtract(BigDecimal.valueOf(1625, 1).multiply(BigDecimal.valueOf(level), MATH_CONTEXT), MATH_CONTEXT)
                .add(BigDecimal.valueOf(2220))
                .add(new BigDecimal(getCurrentLevelPercentProgressAsPoints(level, percentProgress), MATH_CONTEXT), MATH_CONTEXT)
                .setScale(0, RoundingMode.FLOOR)
                .toBigIntegerExact();
    }

    /**
     * Gets the total experience points at a given level with 0% progress.
     * <ul>
     *     <li>At levels 0-16, uses the function \(x^2+6x\)</li>
     *     <li>At levels 17-31, uses the function \(\frac{5}{2}x^2-\frac{81}{2}x+360\)</li>
     *     <li>At level 32 and above, uses the function \(\frac{9}{2}x^2-\frac{325}{2}x+2220\)</li>
     * </ul>
     *
     * @param  level The level to get the total XP from.
     * @return The amount of XP the player has at the given level.
     * @since  1.0-SNAPSHOT
     * @see    #convertLevelAndPercentProgressToTotalXP(int, float)
     */
    public static @NotNull BigInteger getTotalXPPointsAtLevel(final int level) {
        return convertLevelAndPercentProgressToTotalXP(level, 0F);
    }

    /**
     * Converts the progress percentage at a specific level to the number of points the player has accumulated for that
     * level.
     * <ul>
     *     <li>
     *         For levels up to 16, use the function \(2xy+7y\), where \(x\) is the level and \(y\) is the progress to
     *         the next level.
     *     </li>
     *     <li>
     *         For levels up to 31, use the function \(5xy-38y\), where \(x\) is the level and \(y\) is the progress to
     *         the next level.
     *     </li>
     *     <li>
     *         For progress above level 31, use the function \(9xy-158y\), where \(x\) is the level and \(y\) is the
     *         progress to the next level.
     *     </li>
     * </ul>
     *
     * @param  level           The current level.
     * @param  percentProgress The percentage progress to the next level.
     * @return The current progress to the next level as a number of experience points.
     * @since  1.0-SNAPSHOT
     */
    public static @NotNull BigInteger getCurrentLevelPercentProgressAsPoints(final int level,
                                                                             final float percentProgress) {
        if (level < LEVEL_THRESHOLD_1) {
            return getCurrentLevelPercentProgressAsPoints$Func1(level, percentProgress);
        } else if (level < LEVEL_THRESHOLD_2) {
            return getCurrentLevelPercentProgressAsPoints$Func2(level, percentProgress);
        } else {
            return getCurrentLevelPercentProgressAsPoints$Func3(level, percentProgress);
        }
    }

    /**
     * For levels up to 16, use the function \(y\times(2x+7)\), where \(x\) is the level and \(y\) is the progress to
     * the next level.
     *
     * @param  level           The current level.
     * @param  percentProgress The percentage progress to the next level.
     * @return The total XP the player currently has at this level.
     * @since  1.0-SNAPSHOT
     */
    private static @NotNull BigInteger getCurrentLevelPercentProgressAsPoints$Func1(final int level,
                                                                                    final float percentProgress) {
        return BigDecimal.valueOf(level)
                .multiply(BigDecimal.valueOf(2), MATH_CONTEXT)
                .add(BigDecimal.valueOf(7), MATH_CONTEXT)
                .multiply(BigDecimal.valueOf(percentProgress), MATH_CONTEXT)
                .setScale(0, RoundingMode.FLOOR)
                .toBigIntegerExact();
    }

    /**
     * For levels up to 31, use the function \(5xy - 38y\), where \(x\) is the level and \(y\) is the progress to
     * the next level.
     *
     * @param  level           The current level.
     * @param  percentProgress The percentage progress to the next level.
     * @return The total XP the player currently has at this level.
     * @since  1.0-SNAPSHOT
     */
    private static @NotNull BigInteger getCurrentLevelPercentProgressAsPoints$Func2(final int level,
                                                                                    final float percentProgress) {
        return BigDecimal.valueOf(level)
                .multiply(BigDecimal.valueOf(5), MATH_CONTEXT)
                .subtract(BigDecimal.valueOf(38), MATH_CONTEXT)
                .multiply(BigDecimal.valueOf(percentProgress), MATH_CONTEXT)
                .setScale(0, RoundingMode.FLOOR)
                .toBigIntegerExact();
    }

    /**
     * For progress above level 31, use the function \(y\times(9x-158)\), where \(x\) is the level and \(y\) is the
     * progress to the next level.
     *
     * @param  level The current level.
     * @param  percentProgress The percentage progress to the next level.
     * @return The total XP the player currently has at this level.
     * @since  1.0-SNAPSHOT
     */
    private static @NotNull BigInteger getCurrentLevelPercentProgressAsPoints$Func3(final int level,
                                                                                    final float percentProgress) {
        return BigDecimal.valueOf(level)
                .multiply(BigDecimal.valueOf(9), MATH_CONTEXT)
                .subtract(BigDecimal.valueOf(158), MATH_CONTEXT)
                .multiply(BigDecimal.valueOf(percentProgress), MATH_CONTEXT)
                .setScale(0, RoundingMode.FLOOR)
                .toBigIntegerExact();
    }
}
