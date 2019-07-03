package net.avicus.atlas.module.gadget.map;

import com.google.gson.JsonObject;
import net.avicus.compendium.TextStyle;
import net.avicus.compendium.locale.text.Localizable;
import net.avicus.compendium.locale.text.UnlocalizedText;
import net.avicus.magma.module.gadgets.AbstractGadget;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Locale;

public class MapSetNextToken extends AbstractGadget<MapTokenContext> {
    public MapSetNextToken() {
        super(MapTokenManager.getInstance());
    }

    @Override
    public Localizable getName() {
        return new UnlocalizedText("Map Token", TextStyle.ofColor(ChatColor.AQUA));
    }

    @Override
    public ItemStack icon(Locale locale) {
        ItemStack is = new ItemStack(Material.PRISMARINE_SHARD);
        ItemMeta im = is.getItemMeta();

        im.setDisplayName(getName().translate(locale).toLegacyText());

        is.setItemMeta(im);
        return is;
    }

    @Override
    public JsonObject serialize() {
        JsonObject json = new JsonObject();
        json.addProperty("type", MapTokenManager.getInstance().getType());
        return json;
    }

    @Override
    public MapTokenContext defaultContext() {
        return new MapTokenContext(this);
    }

    @Override
    public MapTokenContext deserializeContext(JsonObject json) {
        return new MapTokenContext(this);
    }

    @Override
    public boolean isAllowedInMatches() {
        return true;
    }
}
