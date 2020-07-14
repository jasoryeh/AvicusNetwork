package net.avicus.atlas.util.external.tracker.damage.resolvers;

import com.google.common.base.Preconditions;
import net.avicus.atlas.util.external.tracker.DamageInfo;
import net.avicus.atlas.util.external.tracker.DamageResolver;
import net.avicus.atlas.util.external.tracker.Lifetime;
import net.avicus.atlas.util.external.tracker.damage.GravityDamageInfo;
import net.avicus.atlas.util.external.tracker.trackers.base.gravity.Fall;
import net.avicus.atlas.util.external.tracker.trackers.base.gravity.SimpleGravityKillTracker;
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
