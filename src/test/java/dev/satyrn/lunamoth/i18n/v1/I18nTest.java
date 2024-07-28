package dev.satyrn.lunamoth.i18n.v1;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the {@code I18n} class.
 *
 * @author Isabel Maskrey
 * @since  1.0-SNAPSHOT
 */
public class I18nTest {
    /**
     * Creates a new instance of this test class.
     *
     * @since 1.0-SNAPSHOT
     */
    public I18nTest() { }

    /**
     * Tests the {@code translate} method.
     *
     * @since 1.0-SNAPSHOT
     */
    @Test
    public void testTranslate() {
        final @NotNull I18n i18n = new I18n("lang", "testFiles");

        assertEquals("successful test", i18n.translate("test.result"));
        assertEquals("test.nonexistant", i18n.translate("test.nonexistant"));
        assertEquals("there is no translation", i18n.translate("test.nonexistant", "there is no translation"));
        assertEquals("this is loaded from an external file", i18n.translate("test.externalTranslation"));
        assertEquals("successful format test true", i18n.translate("test.format", new Object[]{true}));

        final @NotNull I18n jsonI18n = new I18n("lang").setResourceType(I18n.ResourceType.JSON);

        assertEquals("this comes from a JSON file", jsonI18n.translate("test.fromJson"));

        final @NotNull I18n italianI18n = new I18n("lang").setCurrentLocale("it_it");

        assertEquals("prova di successo", italianI18n.translate("test.result"));
        assertEquals("fell back from a different language", italianI18n.translate("test.fallback"));
    }

    /**
     * Tests the {@code format} method.
     *
     * @since 1.0-SNAPSHOT
     */
    @Test
    public void testFormat() {
        final @NotNull I18n i18n = new I18n("lang").setCurrentLocale(Locale.US);

        assertEquals("successful format test true", i18n.format("test.format", "test.format", true));
        assertEquals("failed test [0] [1]", i18n.format("test.currencyFormat.fail", "test.currencyFormat.fail", true));
        //assertEquals("successful test $1.06 $2.15", i18n.format("test.currencyFormat.success", "test.currencyFormat.success", 1.06F, 2.15F));
        assertEquals("translation for this key failed", i18n.format("test.nonexistant", "translation for this key failed", "one", "two"));
        assertEquals("fallback should be formatted", i18n.format("test.nonexistant", "fallback should be {0}", "formatted"));
    }

    /**
     * Tests the {@code getBaseName} method.
     *
     * @since 1.0-SNAPSHOT
     */
    @Test
    public void testGetBaseName() {
        final @NotNull I18n testInstance = new I18n("lang");

        assertEquals("lang", testInstance.getBaseName());
    }

    /**
     * Tests the {@code getCurrentLocale} method
     *
     * @since 1.0-SNAPSHOT
     */
    @Test
    public void testGetCurrentLocale() {
        final @NotNull I18n i18n = new I18n("lang").setCurrentLocale(Locale.CANADA);

        assertEquals(Locale.CANADA,  i18n.getCurrentLocale());
    }

    /**
     * Tests the {@code getLocaleString} method
     *
     * @since 1.0-SNAPSHOT
     */
    @Test
    void testGetLocaleString() {
        assertEquals("en_us", I18n.getLocaleString(Locale.US));
        assertEquals("en_ca", I18n.getLocaleString(Locale.CANADA));
        assertEquals("ja_jp", I18n.getLocaleString(Locale.JAPAN));
        assertEquals("it", I18n.getLocaleString(Locale.ITALIAN));
    }

    /**
     * Tests the {@code setCurrentLocale} method
     *
     * @since 1.0-SNAPSHOT
     */
    @Test
    public void testSetCurrentLocale() {
        final @NotNull I18n i18n = new I18n("lang");

        assertNotEquals(Locale.JAPAN, i18n.getCurrentLocale());

        i18n.setCurrentLocale(Locale.JAPAN);

        assertEquals(Locale.JAPAN, i18n.getCurrentLocale());

        i18n.setCurrentLocale("it_it");

        assertEquals(Locale.ITALY, i18n.getCurrentLocale());

        i18n.setCurrentLocale("en_pt"); // pirate speak
        final @NotNull Locale currentLocale = i18n.getCurrentLocale();
        assertEquals(currentLocale.getLanguage(), "en");
        assertEquals(currentLocale.getCountry(), "PT");
    }

    /**
     * Tests the {@code setBaseDirectory} method
     *
     * @since 1.0-SNAPSHOT
     */
    @Test
    public void testSetBaseDirectory() {
        final @NotNull I18n i18n = new I18n("lang", "testFiles");

        assertEquals("testFiles", i18n.getBaseDirectory());

        i18n.setBaseDirectory("testFiles2");

        assertEquals("testFiles2", i18n.getBaseDirectory());

        i18n.setBaseDirectory(null);

        assertNull(i18n.getBaseDirectory());
    }

    /**
     * Tests the {@code setResourceType} method.
     *
     * @since 1.0-SNAPSHOT
     */
    @Test
    void testSetResourceType() {

        final @NotNull I18n i18n = new I18n("lang");

        assertEquals("this translation does not come from an json file", i18n.translate("test.fromJson"));

        i18n.setResourceType(I18n.ResourceType.JSON);

        assertEquals("this comes from a JSON file", i18n.translate("test.fromJson"));

        i18n.setResourceType(I18n.ResourceType.LANG);

        assertEquals("this translation does not come from an json file", i18n.translate("test.fromJson"));
    }
}