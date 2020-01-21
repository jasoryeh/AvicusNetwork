package net.avicus.compendium.locale.text;

import net.avicus.compendium.TextStyle;

/**
 * Represents anything that can be translated and sent to players.
 */
public abstract class Localizable extends TranslatableComponent {

    public static Localizable[] EMPTY = new Localizable[0];

    /**
     * Get the style of this.
     */
    public abstract TextStyle style();

    /**
     * Copy this and its styles.
     */
    public abstract Localizable duplicate();
}
