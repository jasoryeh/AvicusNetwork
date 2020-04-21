package net.avicus.libraries.tracker;

import net.avicus.libraries.tracker.base.SimpleResolverManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class DamageResolvers {

    private static
    @Nullable
    DamageResolverManager manager = null;

    private DamageResolvers() {
    }

    public static
    @Nonnull
    DamageResolverManager getManager() {
        if (manager == null) {
            manager = new SimpleResolverManager();
        }
        return manager;
    }
}
