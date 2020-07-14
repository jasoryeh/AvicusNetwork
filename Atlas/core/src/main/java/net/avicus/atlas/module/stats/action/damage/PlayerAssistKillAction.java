package net.avicus.atlas.module.stats.action.damage;

import lombok.Getter;
import net.avicus.atlas.module.stats.action.ScoreUtils;
import net.avicus.atlas.util.external.tracker.DamageInfo;
import net.avicus.atlas.util.external.tracker.Lifetimes;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Player;

import java.time.Instant;

public class PlayerAssistKillAction extends PlayerDamageAction {

    @Getter
    private final Player killer;
    @Getter
    private final Player victim;
    @Getter
    private final double score;
    @Getter
    private final String debugMessage;

    public PlayerAssistKillAction(Player actor, Instant when, DamageInfo info, Player killer,
                                  Player victim) {
        super(actor, when, info);
        this.killer = killer;
        this.victim = victim;

        Pair<StringBuilder, Double> calc = ScoreUtils
                .getDamageInfoScore(Lifetimes.getLifetime(victim), info, victim.getLocation());
        this.score = calc.getValue() / 2.4;
        this.debugMessage = "Kill Assist: " + calc.getKey().toString();

    }
}
