package net.avicus.atlas.listener;

import net.avicus.atlas.Atlas;
import net.avicus.atlas.AtlasConfig;
import net.avicus.atlas.countdown.AutoStartingCountdown;
import net.avicus.atlas.countdown.CyclingCountdown;
import net.avicus.atlas.countdown.StartingCountdown;
import net.avicus.atlas.event.RotationEndEvent;
import net.avicus.atlas.event.match.MatchOpenEvent;
import net.avicus.atlas.event.match.MatchStateChangeEvent;
import net.avicus.atlas.event.player.PlayerJoinDelayedEvent;
import net.avicus.atlas.map.rotation.Rotation;
import net.avicus.atlas.match.Match;
import net.avicus.atlas.module.loadouts.type.VehicleLoadout;
import net.avicus.atlas.module.spawns.SpawnsModule;
import net.avicus.atlas.module.states.StatesModule;
import net.avicus.atlas.module.vote.VoteModule;
import net.avicus.atlas.util.AtlasTask;
import net.avicus.atlas.util.Events;
import net.avicus.compendium.countdown.RestartingCountdown;
import net.avicus.atlas.event.player.PlayerCoarseMoveEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.util.Vector;
import org.joda.time.Duration;
import org.spigotmc.event.entity.EntityDismountEvent;

public class AtlasListener implements Listener {

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        event.setJoinMessage(null);

        new AtlasTask() {
            @Override
            public void run() {
                if (event.getPlayer().isOnline()) {
                    PlayerJoinDelayedEvent call = new PlayerJoinDelayedEvent(event.getPlayer());
                    Events.call(call);
                }
            }
        }.later(1);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        event.setLeaveMessage(null);
    }

    @EventHandler
    public void onRotationEnd(RotationEndEvent event) {
        if (!AtlasConfig.isRotationRestart()) {
            return;
        }

        event.getRotation().startRestartCountdown(new RestartingCountdown());
    }

    @EventHandler
    public void handleQueuedRestart(MatchStateChangeEvent event) {
        if (event.isChangeToNotPlaying() && Atlas.get().getMatchManager().getRotation()
                .isRestartQueued()) {
            Atlas.get().getMatchManager().getRotation()
                    .startRestartCountdown(new RestartingCountdown(Duration.standardSeconds(30)));
        }
    }

    @EventHandler
    public void onCoarseMove(PlayerCoarseMoveEvent event) {
        if (event.getTo().getY() < -72 && event.getPlayer().getGameMode() != GameMode.SURVIVAL) {
            event.getPlayer().setVelocity(new Vector(0, 4, 0));
        }
    }

    @EventHandler
    public void onMatchEnd(MatchStateChangeEvent event) {
        if (!event.getTo().isPresent()) {
            return;
        }

        Rotation rotation = Atlas.get().getMatchManager().getRotation();
        if (!event.getTo().get().getNextState().isPresent()) {
            if (rotation.getNextMatch().isPresent()) {
                if (rotation.isRestartQueued()) {
                    rotation.startRestartCountdown(new RestartingCountdown());
                    return;
                }

                if (rotation.isVoteQueued()) {
                    AtlasTask.of(() -> event.getMatch().getModule(VoteModule.class)
                            .ifPresent(VoteModule::delayedStart)).later(20);
                    rotation.setVoteQueued(false);
                    return;
                }

                CyclingCountdown countdown = new CyclingCountdown(event.getMatch(),
                        rotation.getNextMatch().get());
                rotation.cycleMatch(countdown);
            } else {
                RotationEndEvent call = new RotationEndEvent(rotation);
                Events.call(call);
            }
        }
    }

    @EventHandler
    public void multiTrade(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() == EntityType.VILLAGER) {
            event.setCancelled(true);
            event.getPlayer().openMerchantCopy((Villager) event.getRightClicked());
        }
    }

    @EventHandler
    public void onMatchOpen(MatchOpenEvent event) {
        if (event.getMatch().getRequiredModule(StatesModule.class).isStarting() && AtlasConfig
                .isRotationAutoStart()) {
            StartingCountdown starting = new AutoStartingCountdown(event.getMatch());
            Atlas.get().getMatchManager().getRotation().startMatch(starting);
        }
    }

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        if (Atlas.getMatch() != null) {
            event.setMotd(ChatColor.WHITE + Atlas.getMatch().getMap().getName());
        }
    }

    // TODO: Maybe put this somewhere else.

    @EventHandler
    public void onDismount(EntityDismountEvent event) {
        if (event.getDismounted().hasMetadata(VehicleLoadout.STICKY_TAG) &&
                event.getDismounted().getMetadata(VehicleLoadout.STICKY_TAG).get(0).asBoolean()) {
            event.setCancelled(true);
        }

        if (event.getDismounted().hasMetadata(VehicleLoadout.REMOVE_TAG) &&
                event.getDismounted().getMetadata(VehicleLoadout.REMOVE_TAG).get(0).asBoolean()) {
            event.getDismounted().remove();
        }
    }

    /*@EventHandler
    public void onAnyEvent(Event event) {
        Bukkit.getLogger().info("Event fired: " + event.getClass().getName() + " -> " + event.toString());
    }*/

    // Fallback respawn listener (so players don't get into abandoned regular world)
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Bukkit.getLogger().info("Spawning " + event.getPlayer().getName());
        Match match = Atlas.getMatch();
        if(match == null) {
            Bukkit.getLogger().warning("Atlas match is unavailable!");
            return;
        }
        SpawnsModule requiredModule = match.getRequiredModule(SpawnsModule.class);
        if(requiredModule == null) {
            Bukkit.getLogger().warning("No spawns module! Players may fail and spawn in the normal survival world!");
            return;
        }
        requiredModule.spawn(event.getPlayer());
    }


}
