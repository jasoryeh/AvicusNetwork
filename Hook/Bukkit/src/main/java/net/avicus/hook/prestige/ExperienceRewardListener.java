package net.avicus.hook.prestige;

import com.google.common.util.concurrent.AtomicDouble;
import net.avicus.atlas.Atlas;
import net.avicus.atlas.event.competitor.CompetitorWinEvent;
import net.avicus.atlas.event.competitor.PlayerChangeCompetitorEvent;
import net.avicus.atlas.event.group.PlayerChangedGroupEvent;
import net.avicus.atlas.event.match.MatchCompleteEvent;
import net.avicus.atlas.module.damagetrack.DamageTrackModule;
import net.avicus.grave.event.PlayerDeathEvent;
import net.avicus.hook.HookConfig;
import net.avicus.hook.utils.Events;
import net.avicus.hook.utils.Messages;
import net.avicus.magma.Magma;
import net.avicus.magma.event.user.AsyncHookLoginEvent;
import net.avicus.magma.module.prestige.PrestigeModule;
import net.avicus.magma.network.user.Users;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.joda.time.Duration;
import org.joda.time.Instant;

import java.util.*;
import java.util.stream.Collectors;

public class ExperienceRewardListener implements Listener {

    private final Map<UUID, Instant> teamJoinTimes;
    private final PrestigeModule module;

    public ExperienceRewardListener() {
        this.teamJoinTimes = new HashMap<>();
        this.module = Magma.get().getMm().get(PrestigeModule.class);

        if (Atlas.get().getLoader().hasModule("competitive-objectives")) {
            Events.register(new CompetitveRewardListener(this.module));
        }
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

    @EventHandler(ignoreCancelled = true)
    public void rewardInitial(AsyncHookLoginEvent event) {
        Player player = Users.player(event.getUser()).orElse(null);

        if (player == null) {
            return;
        }

        if (Magma.get().database().getXpTransations()
                .sumXP(event.getUser().getId(), Magma.get().getCurrentSeason()) <= 0
                && HookConfig.Experience.getInitialBalance() > 0) {
            this.module.give(player, HookConfig.Experience.getInitialBalance(),
                    Atlas.getMatch().getMap().getGenre().name());
        }
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

            int reward = HookConfig.Experience.Rewards.getKillPlayer();
            if (reward > 0) {
                this.module.reward((Player) damager, reward, Messages.UI_REWARD_KILL_PLAYER,
                        Atlas.getMatch().getMap().getGenre().name());
                exclude = ((Player) damager).getUniqueId();
            }
        }

        // Assists
        Player killed = event.getPlayer();
        DamageTrackModule dtm = Atlas.getMatch().getRequiredModule(DamageTrackModule.class);
        int rewardAssist = HookConfig.Experience.Rewards.getKillPlayerAssist();

        List<DamageTrackModule.DamageExchange> damageExchanges = dtm.getDamageExchanges();

        Map<UUID, AtomicDouble> assisters = new HashMap<>();

        for (DamageTrackModule.DamageExchange exc : damageExchanges) {
            if(exc.getDirection() == DamageTrackModule.DamageDirection.GIVE && exc.getYou() == killed.getUniqueId()) {
                if(exc.getMe() == exclude || exc.getMe() == killed.getUniqueId()) {
                    continue;
                }

                if(assisters.containsKey(exc.getMe())) {
                    assisters.get(exc.getMe()).addAndGet(exc.getAmount());
                } else {
                    assisters.put(exc.getMe(), new AtomicDouble(exc.getAmount()));
                }
            }
        }

        for (Map.Entry<UUID, AtomicDouble> assist : assisters.entrySet()) {
            Player assister = Bukkit.getPlayer(assist.getKey());

            if(assister != null) {
                if(assist.getValue().get() > 5) {
                    this.module.reward(assister, rewardAssist, Messages.UI_REWARD_KILL_PLAYER_ASSIST,
                                    Atlas.getMatch().getMap().getGenre().name());
                }
                // people who do less than 2 damage(1 heart(s)) are not given experience.
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

            int rewardBase = HookConfig.Experience.Rewards.getLosePerMinute();
            int rewardMin = HookConfig.Experience.Rewards.getLoseMinimum();

            int minutes = (int) timeInMatch.getStandardMinutes();
            int reward = Math.max(rewardBase * minutes, rewardMin);
            if (reward > 0) {
                this.module.reward(participant, reward, Messages.UI_REWARD_PARTICIPATION,
                        Atlas.getMatch().getMap().getGenre().name());
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

            int rewardBase = HookConfig.Experience.Rewards.getWinPerMinute();
            int rewardMin = HookConfig.Experience.Rewards.getWinMinimum();

            int minutes = (int) timeInMatch.getStandardMinutes();
            int reward = Math.max(rewardBase * minutes, rewardMin);
            if (reward > 0) {
                this.module.reward(winner, reward, Messages.UI_REWARD_WIN,
                        Atlas.getMatch().getMap().getGenre().name());
            }
        }
    }
}
