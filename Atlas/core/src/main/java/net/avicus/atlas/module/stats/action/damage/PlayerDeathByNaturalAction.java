package net.avicus.atlas.module.stats.action.damage;

import lombok.ToString;
import net.avicus.atlas.util.external.tracker.DamageInfo;
import org.bukkit.entity.Player;

import java.time.Instant;

@ToString
public class PlayerDeathByNaturalAction extends PlayerDamageAction {

    public PlayerDeathByNaturalAction(Player actor, Instant when, DamageInfo info) {
        super(actor, when, info);
    }

    @Override
    public double getScore() {
        return -0.5;
    }

    @Override
    public String getDebugMessage() {
        return "DEATH: natural";
    }
}
