package net.avicus.atlas.module.stats.action.damage;

import lombok.Getter;
import lombok.ToString;
import net.avicus.atlas.module.stats.action.ScoreUtils;
import net.avicus.atlas.util.external.tracker.DamageInfo;
import net.avicus.atlas.util.external.tracker.Lifetimes;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Player;

import java.time.Instant;

@ToString
public class PlayerKillAction extends PlayerDamageAction {

    @Getter
    private final Player victim;
    @Getter
    private final double score;
    @Getter
    private final String debugMessage;

    public PlayerKillAction(Player actor, Instant when, DamageInfo info, Player victim) {
        super(actor, when, info);
        this.victim = victim;
        Pair<StringBuilder, Double> calc = ScoreUtils
                .getDamageInfoScore(Lifetimes.getLifetime(victim), info, victim.getLocation());
        this.score = calc.getValue();
        this.debugMessage = "Kill: " + calc.getKey().toString();
    }
}
