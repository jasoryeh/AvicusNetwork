package net.avicus.atlas.util.tablist.item;

import lombok.ToString;
import net.avicus.atlas.util.tablist.util.Skin;
import net.avicus.atlas.util.tablist.util.Skins;

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
