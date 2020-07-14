package net.avicus.atlas.module.stats.action.damage;

import lombok.ToString;
import net.avicus.atlas.util.external.tracker.DamageInfo;
import org.bukkit.entity.Player;

import java.time.Instant;

@ToString
public class PlayerDeathBySelfAction extends PlayerDamageAction {

    public PlayerDeathBySelfAction(Player actor, Instant when, DamageInfo info) {
        super(actor, when, info);
    }

    @Override
    public double getScore() {
        return -1.5;
    }

    @Override
    public String getDebugMessage() {
        return "DEATH: self";
    }
}
