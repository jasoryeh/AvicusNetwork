package tc.oc.tracker;

import org.bukkit.entity.LivingEntity;
import tc.oc.tracker.base.SimpleLifetimeManager;

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
