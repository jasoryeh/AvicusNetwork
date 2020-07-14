package net.avicus.atlas.util.external.tracker;

import net.avicus.atlas.util.external.tracker.base.SimpleLifetimeManager;
import org.bukkit.entity.LivingEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class Lifetimes {

    private static
    @Nullable
    LifetimeManager manager;

    private Lifetimes() {
    }

    public static
    @Nonnull
    LifetimeManager getManager() {
        if (manager == null) {
            manager = new SimpleLifetimeManager();
        }
        return manager;
    }

    public static
    @Nonnull
    Lifetime getLifetime(@Nonnull LivingEntity entity) {
        return getManager().getLifetime(entity);
    }
}
