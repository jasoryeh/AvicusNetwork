package net.avicus.atlas.sets.competitive.objectives.flag;

public enum FlagPickupMethod {
    ANY,
    MOVE,
    INTERACT;

    public boolean allow(final FlagPickupMethod that) {
        return this == that || this == ANY;
    }
}
