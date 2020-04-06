package net.avicus.libraries.tabbed.item;

import lombok.ToString;
import net.avicus.libraries.tabbed.util.Skin;
import net.avicus.libraries.tabbed.util.Skins;

/**
 * A blank TextTabItem
 */
@ToString
public class BlankTabItem extends TextTabItem {
    public BlankTabItem(Skin skin) {
        super("", 1000, skin);
    }

    public BlankTabItem() {
        this(Skins.DEFAULT_SKIN);
    }
}
