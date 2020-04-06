package net.avicus.libraries.tracker.damage.resolvers;

import com.google.common.base.Preconditions;
import net.avicus.libraries.tracker.DamageInfo;
import net.avicus.libraries.tracker.DamageResolver;
import net.avicus.libraries.tracker.Lifetime;
import net.avicus.libraries.tracker.damage.GravityDamageInfo;
import net.avicus.libraries.tracker.trackers.base.gravity.Fall;
import net.avicus.libraries.tracker.trackers.base.gravity.SimpleGravityKillTracker;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GravityDamageResolver implements DamageResolver {

    private final
    @Nonnull
    SimpleGravityKillTracker tracker;

    public GravityDamageResolver(@Nonnull SimpleGravityKillTracker tracker) {
        Preconditions.checkNotNull(tracker, "tracker");
        this.tracker = tracker;
    }

    public
    @Nullable
    DamageInfo resolve(@Nonnull LivingEntity entity, @Nonnull Lifetime lifetime,
                       @Nonnull EntityDamageEvent damageEvent) {
        if (!(entity instanceof Player)) {
            return null;
        }
        Player victim = (Player) entity;
        Fall fall = this.tracker.getCausingFall(victim, damageEvent.getCause());
        if (fall != null) {
            return new GravityDamageInfo(fall.attacker, fall.cause, fall.from, fall.whereOnGround);
        } else {
            return null;
        }
    }
}
