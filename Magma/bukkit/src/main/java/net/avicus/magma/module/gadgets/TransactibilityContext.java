package net.avicus.magma.module.gadgets;

public interface TransactibilityContext<C extends GadgetContext> {
    C getGadgetContext();

    long getPurchaseCost();

    long getSellCost();

    boolean isPurchasable();

    boolean isSellable();

    boolean isTransactable();
}
