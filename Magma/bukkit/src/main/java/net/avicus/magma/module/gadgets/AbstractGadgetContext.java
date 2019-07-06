package net.avicus.magma.module.gadgets;

import net.avicus.magma.Magma;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public abstract class AbstractGadgetContext<G extends Gadget> implements GadgetContext<G> {

    private TransactibilityContext transactability;

    private final G gadget;

    private GadgetContext context;

    public AbstractGadgetContext(G gadget) {
        this.gadget = gadget;
        this.context = this;

        this.setBuyable(new DefaultTransactabilityContext(context));
    }

    public AbstractGadgetContext(G gadget, TransactibilityContext transactability) {
        this.gadget = gadget;
        this.context = this;

        this.setBuyable(transactability);
    }

    public void setBuyable(TransactibilityContext transactable) {
        this.transactability = transactable;


        if (this.transactability.isTransactable()) {
            Magma.get().getMm().get(Gadgets.class).addTransactable(this);
        } else {
            Magma.get().getMm().get(Gadgets.class).removeTransactable(this);
        }
    }

    @Override
    public ItemStack icon(Locale locale) {
        return this.gadget.icon(locale);
    }

    @Override
    public G getGadget() {
        return this.gadget;
    }

    @Override
    public TransactibilityContext getTransactability() {
        return this.transactability;
    }
}
