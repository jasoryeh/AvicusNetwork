package net.avicus.atlas.util.external.tracker.damage.resolvers;

import net.avicus.atlas.util.external.tracker.DamageInfo;
import net.avicus.atlas.util.external.tracker.DamageResolver;
import net.avicus.atlas.util.external.tracker.Lifetime;
import net.avicus.atlas.util.external.tracker.damage.base.SimpleVoidDamageInfo;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class VoidDamageResolver implements DamageResolver {

    public
    @Nullable
    DamageInfo resolve(@Nonnull LivingEntity entity, @Nonnull Lifetime lifetime,
                       @Nonnull EntityDamageEvent damageEvent) {
        if (damageEvent.getCause() == DamageCause.VOID) {
            return new SimpleVoidDamageInfo(null);
        }

        return null;
    }
}
