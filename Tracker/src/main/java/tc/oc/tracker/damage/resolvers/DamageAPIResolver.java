package tc.oc.tracker.damage.resolvers;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import tc.oc.tracker.*;
import tc.oc.tracker.plugin.DamageAPIHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Resolves the damage stored in the {@link DamageAPI}.
 * <p>
 * When a plugin uses the API to inflict damage on an entity, it specifies its
 * own {@link Damage} object to use. However, plugins listening on the Bukkit
 * event will use the regular channels to fetch the object for the event.
 * Therefore, this resolver is necessary to feed the proper object to those
 * event listeners.
 */
public class DamageAPIResolver implements DamageResolver {

    static {
        DamageResolvers.getManager().register(new DamageAPIResolver());
    }

    /**
     * @see DamageResolvers#resolve
     */
    public
    @Nullable
    DamageInfo resolve(@Nonnull LivingEntity entity, @Nonnull Lifetime lifetime,
                       @Nonnull EntityDamageEvent damageEvent) {
        return DamageAPIHelper.get().getEventDamageInfo(damageEvent);
    }
}
