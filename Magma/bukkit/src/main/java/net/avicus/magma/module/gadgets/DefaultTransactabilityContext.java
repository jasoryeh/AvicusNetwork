package net.avicus.magma.module.gadgets;

import org.apache.commons.lang3.tuple.Pair;

public class DefaultTransactabilityContext<C extends GadgetContext> implements TransactibilityContext<C> {

    private C gadgetContext;

    private boolean purchasable;
    private boolean sellable;

    private long purchaseCost;
    private long sellCost;

    public DefaultTransactabilityContext(C gadgetContext) {
        this(gadgetContext, Pair.of(Boolean.FALSE, 0L), Pair.of(Boolean.FALSE, 0L));
    }

    public DefaultTransactabilityContext(C gadgetContext, Pair<Boolean, Long> purchase, Pair<Boolean, Long> sell) {
        this.gadgetContext = gadgetContext;

        this.purchasable = purchase.getKey();
        this.purchaseCost = purchase.getValue();

        this.sellable = sell.getKey();
        this.sellCost = sell.getValue();
    }

    @Override
    public C getGadgetContext() {
        return this.gadgetContext;
    }

    public void setPurchase(Pair<Boolean, Long> purchase) {
        this.purchasable = purchase.getKey();
        this.purchaseCost = purchase.getValue();
    }

    public void setSell(Pair<Boolean, Long> sell) {
        this.sellable = sell.getKey();
        this.sellCost = sell.getValue();
    }


    @Override
    public boolean isPurchasable() {
        return this.purchasable;
    }

    @Override
    public boolean isSellable() {
        return this.sellable;
    }

    @Override
    public long getPurchaseCost() {
        return this.purchaseCost;
    }

    @Override
    public long getSellCost() {
        return this.sellCost;
    }

    @Override
    public boolean isTransactable() {
        return this.sellable || this.purchasable;
    }
}
