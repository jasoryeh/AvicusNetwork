package net.avicus.compendium;

import net.avicus.compendium.locale.LocaleBundle;
import net.avicus.compendium.locale.LocaleStrings;
import net.avicus.compendium.locale.text.LocalizedFormat;
import net.avicus.compendium.locale.text.LocalizedText;
import net.avicus.compendium.locale.text.UnlocalizedText;
import org.bukkit.ChatColor;
import org.jdom2.JDOMException;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class LocaleTests {

    @Test
    public void xml() throws JDOMException, IOException {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("./test.xml");
        LocaleStrings strings = LocaleStrings.fromXml(stream);
        System.out.println(strings);
    }

    @Test
    public void bundles() {
        System.out.println("=== BUNDLES ===");
        LocaleStrings en = new LocaleStrings(new Locale("en"));
        en.add("hello", "Hello {0}!");

        LocaleBundle bundle = new LocaleBundle();
        bundle.add(en);

        System.out.println(bundle.get(Locale.ENGLISH, "hello"));

        UnlocalizedText keenan = new UnlocalizedText("Keenan");
        keenan.style().color(ChatColor.RED);

        LocalizedText text = new LocalizedText(bundle, "hello", keenan, keenan);
        text.style().color(ChatColor.DARK_RED).bold(true);

        System.out.println(text.translate(Locale.TRADITIONAL_CHINESE).toLegacyText());
    }

    @Test
    public void constants() {
        System.out.println("=== CONSTANTS ===");
        LocalizedFormat format = ChatConstant.HELLO;

        LocalizedText name = ChatConstant.NAME.with(TextStyle.ofColor(ChatColor.RED).bold(false));

        LocalizedText hello = format.with(TextStyle.ofBold(), name);

        System.out.println(hello.translate(new Locale("es")).toLegacyText());
    }
}
