package net.avicus.hook.credits.reward;

import com.google.common.util.concurrent.AtomicDouble;
import net.avicus.atlas.Atlas;
import net.avicus.atlas.event.competitor.CompetitorWinEvent;
import net.avicus.atlas.event.competitor.PlayerChangeCompetitorEvent;
import net.avicus.atlas.event.group.PlayerChangedGroupEvent;
import net.avicus.atlas.event.match.MatchCompleteEvent;
import net.avicus.atlas.module.damagetrack.DamageTrackModule;
import net.avicus.hook.HookConfig;
import net.avicus.hook.HookPlugin;
import net.avicus.hook.credits.Credits;
import net.avicus.hook.utils.Events;
import net.avicus.hook.utils.Messages;
import net.avicus.libraries.grave.event.PlayerDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.joda.time.Duration;
import org.joda.time.Instant;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import static net.avicus.hook.credits.reward.CreditRewardListener.CREDIT_LISTENERS;

public class CreditRewarder implements Listener {

    private final Map<UUID, Instant> teamJoinTimes;

    public CreditRewarder() {
        this.teamJoinTimes = new HashMap<>();

        CREDIT_LISTENERS.forEach(listener -> {
            try {
                CreditRewardListener creditRewardListener = listener.getDeclaredConstructor(CreditRewarder.class).newInstance(CreditRewarder.this);

                Events.register(creditRewardListener);
            } catch (InstantiationException|NoSuchMethodException|IllegalAccessException|InvocationTargetException exception) {
                HookPlugin.getInstance().getLogger().warning("Unable to instantiate a credit reward listener:");
                exception.printStackTrace();
            }
        });
    }

    private Optional<Duration> timeInMatch(Player player) {
        Instant from = this.teamJoinTimes.get(player.getUniqueId());
        if (from == null) {
            return Optional.empty();
        }

        Instant to = Instant.now();
        return Optional.of(new Duration(from, to));
    }

    private void resetTimeInMatch(Player player) {
        this.teamJoinTimes.remove(player.getUniqueId());
    }

    @EventHandler
    public void onGroupChange(PlayerChangedGroupEvent event) {
        this.teamJoinTimes.put(event.getPlayer().getUniqueId(), Instant.now());

        if (event.getGroup().isSpectator()) {
            resetTimeInMatch(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getLifetime().getLastDamage() == null) {
            return;
        }

        UUID exclude = null;
        LivingEntity damager = event.getLifetime().getLastDamage().getInfo().getResolvedDamager();

        if (damager instanceof Player) {
            if (damager.equals(event.getPlayer())) {
                return;
            }

            int reward = HookConfig.Credits.Rewards.getKillPlayer();
            if (reward > 0) {
                Credits.reward((Player) damager, reward, Messages.UI_REWARD_KILL_PLAYER);
                exclude = ((Player) damager).getUniqueId();
            }
        }

        // Assists
        Player killed = event.getPlayer();
        DamageTrackModule dtm = Atlas.getMatch().getRequiredModule(DamageTrackModule.class);
        int rewardAssist = HookConfig.Credits.Rewards.getKillPlayerAssist();

        List<DamageTrackModule.DamageExchange> damageExchanges = dtm.getDamageExchanges();

        Map<UUID, AtomicDouble> assisters = new HashMap<>();

        for (DamageTrackModule.DamageExchange exc : damageExchanges) {

            if(exc.getDirection() == DamageTrackModule.DamageDirection.GIVE && exc.getYou() == killed.getUniqueId()) {
                if(exc.getMe() == exclude || exc.getMe() == killed.getUniqueId() || exc.isCreditRewarded()) {
                    continue;
                }

                if(assisters.containsKey(exc.getMe())) {
                    assisters.get(exc.getMe()).addAndGet(exc.getAmount());
                } else {
                    assisters.put(exc.getMe(), new AtomicDouble(exc.getAmount()));
                }

                // set as rewarded :)
                exc.setCreditRewarded(true);
            }
        }

        for (Map.Entry<UUID, AtomicDouble> assist : assisters.entrySet()) {
            Player assister = Bukkit.getPlayer(assist.getKey());

            if(assister != null) {
                if(assist.getValue().get() > 5) {
                    Credits.reward(assister, rewardAssist, Messages.UI_REWARD_KILL_PLAYER_ASSIST);
                }
                // people who do less than 5 damage(2 1/2 hearts) are given experience only, as we don't want this abused.
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        resetTimeInMatch(event.getPlayer());
    }

    @EventHandler
    public void onPlayerChangeGroup(PlayerChangeCompetitorEvent event) {
        resetTimeInMatch(event.getPlayer());
    }

    @EventHandler
    public void onComplete(MatchCompleteEvent event) {
        List<Player> winners = event.getWinners().stream()
                .flatMap(competitor -> competitor.getPlayers().stream()).collect(Collectors.toList());
        List<Player> participants = event.getCompetitors().stream()
                .flatMap(competitor -> competitor.getPlayers().stream())
                .filter(player -> !winners.contains(player)).collect(Collectors.toList());

        for (Player participant : participants) {
            Duration timeInMatch = timeInMatch(participant).orElse(null);
            if (timeInMatch == null) {
                continue;
            }
            resetTimeInMatch(participant);

            int rewardBase = HookConfig.Credits.Rewards.getLosePerMinute();
            int rewardMin = HookConfig.Credits.Rewards.getLoseMinimum();

            int minutes = (int) timeInMatch.getStandardMinutes();
            int reward = Math.max(rewardBase * minutes, rewardMin);
            if (reward > 0) {
                Credits.reward(participant, reward, Messages.UI_REWARD_PARTICIPATION);
            }
        }
    }

    @EventHandler
    public void onCompetitorWin(CompetitorWinEvent event) {
        List<Player> winners = event.getWinner().getPlayers();

        for (Player winner : winners) {
            Duration timeInMatch = timeInMatch(winner).orElse(null);
            if (timeInMatch == null) {
                continue;
            }
            resetTimeInMatch(winner);

            int rewardBase = HookConfig.Credits.Rewards.getWinPerMinute();
            int rewardMin = HookConfig.Credits.Rewards.getWinMinimum();

            int minutes = (int) timeInMatch.getStandardMinutes();
            int reward = Math.max(rewardBase * minutes, rewardMin);
            if (reward > 0) {
                Credits.reward(winner, reward, Messages.UI_REWARD_WIN);
            }
        }
    }
}
