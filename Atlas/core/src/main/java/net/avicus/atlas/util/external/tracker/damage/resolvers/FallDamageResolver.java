package net.avicus.atlas.util.external.tracker.damage.resolvers;

import net.avicus.atlas.util.external.tracker.DamageInfo;
import net.avicus.atlas.util.external.tracker.DamageResolver;
import net.avicus.atlas.util.external.tracker.Lifetime;
import net.avicus.atlas.util.external.tracker.damage.base.SimpleFallDamageInfo;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FallDamageResolver implements DamageResolver {

    public
    @Nullable
    DamageInfo resolve(@Nonnull LivingEntity entity, @Nonnull Lifetime lifetime,
                       @Nonnull EntityDamageEvent damageEvent) {
        if (damageEvent.getCause() == DamageCause.FALL) {
            float fallDistance = Math.max(0, entity.getFallDistance());

            return new SimpleFallDamageInfo(null, fallDistance);
        }

        return null;
    }
}
