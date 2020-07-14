package net.avicus.atlas.module.stats.action.damage;

import lombok.Getter;
import lombok.ToString;
import net.avicus.atlas.module.stats.action.base.PlayerAction;
import net.avicus.atlas.util.external.tracker.DamageInfo;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.time.Instant;

@ToString
public abstract class PlayerDamageAction implements PlayerAction {

    @Getter
    private final Player actor;
    @Getter
    private final Instant when;
    @Getter
    @Nullable
    private final DamageInfo info;

    public PlayerDamageAction(Player actor, Instant when, DamageInfo info) {
        this.actor = actor;
        this.when = when;
        this.info = info;
    }
}
