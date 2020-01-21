package net.avicus.compendium.locale.text;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;

import java.util.Locale;

/**
 * Pseudo translatable component to replace the one we found in Magnet
 */
public abstract class TranslatableComponent extends BaseComponent {

    public abstract BaseComponent translate(Locale toLocale);

    public BaseComponent translate(CommandSender viewer) {
        return this.translate(viewer.getLocale());
    }

}
