package net.avicus.atlas.util.external.tracker.damage.resolvers;

import net.avicus.atlas.util.external.tracker.DamageInfo;
import net.avicus.atlas.util.external.tracker.DamageResolver;
import net.avicus.atlas.util.external.tracker.Lifetime;
import net.avicus.atlas.util.external.tracker.damage.OwnedMobDamageInfo;
import net.avicus.atlas.util.external.tracker.trackers.OwnedMobTracker;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class OwnedMobDamageResolver implements DamageResolver {

    private final OwnedMobTracker ownedMobTracker;

    public OwnedMobDamageResolver(OwnedMobTracker ownedMobTracker) {
        this.ownedMobTracker = ownedMobTracker;
    }

    public
    @Nullable
    DamageInfo resolve(@Nonnull LivingEntity entity, @Nonnull Lifetime lifetime,
                       @Nonnull EntityDamageEvent damageEvent) {
        if (damageEvent instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) damageEvent;

            if (event.getDamager() instanceof Projectile) {
                if (((Projectile) event.getDamager()).getShooter() == null) {
                    return null;
                }
                if (!(((Projectile) event.getDamager()).getShooter() instanceof Player)
                        && ((Projectile) event.getDamager()).getShooter() instanceof LivingEntity) {
                    LivingEntity mob = (LivingEntity) ((Projectile) event.getDamager()).getShooter();
                    Player mobOwner = this.ownedMobTracker.getOwner(mob);

                    if (mobOwner != null) {
                        return new OwnedMobDamageInfo(mob, mobOwner, (Projectile) event.getDamager());
                    }
                }
            } else if (!(event.getDamager() instanceof Player) && event
                    .getDamager() instanceof LivingEntity) {
                LivingEntity mob = (LivingEntity) event.getDamager();
                Player mobOwner = this.ownedMobTracker.getOwner(mob);

                if (mobOwner != null) {
                    return new OwnedMobDamageInfo(mob, mobOwner, null);
                }
            }
        }
        return null;
    }
}
