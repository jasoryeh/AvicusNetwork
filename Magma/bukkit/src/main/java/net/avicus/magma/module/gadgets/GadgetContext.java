package net.avicus.magma.module.gadgets;

import com.google.gson.JsonObject;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public interface GadgetContext<G extends Gadget> {

    G getGadget();

    TransactibilityContext getTransactability();

    ItemStack icon(Locale locale);

    JsonObject serialize();

    /**
     * Convenience method to get the manager of this gadget context.
     */
    default GadgetManager getManager() {
        return getGadget().getManager();
    }
}
