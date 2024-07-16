package dev.satyrn.lunamoth.i18n.v1;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class I18nTest {

    private final @NotNull I18n instance;

    public I18nTest() {
        this.instance = new I18n("lang");
    }

    @Test
    public void testGetBaseName() {
    }

    @Test
    public void testGetCurrentLocale() {
    }

    @Test
    public void testSetCurrentLocale() {
    }

    @Test
    public void testGetLocaleString() {
    }

    @Test
    public void testSetResourceType() {
    }

    @Test
    public void translate() {
        // Holy shit this worked first try :D
        // 1:53 AM on a Monday Isabel out y'all
        assertEquals("successful test", this.instance.translate("test.result"));
        assertEquals("successful format test true", this.instance.translate("test.format", List.of(true).toArray()));
        assertEquals("failed test [0] [1]", this.instance.translate("test.currencyFormat.fail", List.of(true).toArray()));
        assertEquals("successful test $1.06 $2.15", this.instance.translate("test.currencyFormat.success", List.of(1.06F, 2.15F).toArray()));
        assertEquals("test.nonexistant", this.instance.translate("test.nonexistant"));
        assertEquals("there is no translation", this.instance.translate("test.nonexistant", "there is no translation"));
        assertEquals("translation for this key failed", this.instance.translate("test.nonexistant", "translation for this key failed", List.of("one", "two").toArray()));
        assertEquals("fallback should be formatted", this.instance.translate("test.nonexistant", "fallback should be {0}", List.of("formatted").toArray()));

        this.instance.setResourceType(I18n.ResourceType.JSON);

        assertEquals("this comes from a JSON file", this.instance.translate("test.fromJson"));
    }
}