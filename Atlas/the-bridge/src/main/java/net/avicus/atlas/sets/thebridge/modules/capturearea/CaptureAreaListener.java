package net.avicus.atlas.sets.thebridge.modules.capturearea;

import net.avicus.atlas.module.groups.Competitor;
import net.avicus.atlas.module.groups.Group;
import net.avicus.atlas.module.groups.GroupsModule;
import net.avicus.atlas.module.objectives.ObjectivesModule;
import net.avicus.atlas.module.shop.PlayerEarnPointEvent;
import net.avicus.atlas.module.spawns.SpawnsModule;
import net.avicus.atlas.sets.thebridge.modules.capturearea.events.CaptureAreaCaptureEvent;
import net.avicus.atlas.sets.thebridge.modules.capturearea.events.CaptureAreaScoreCompleteEvent;
import net.avicus.atlas.util.Events;
import net.avicus.compendium.number.NumberAction;
import net.avicus.atlas.event.player.PlayerCoarseMoveEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;
import java.util.Optional;

public class CaptureAreaListener implements Listener {

    private final ObjectivesModule module;
    private final List<CaptureAreaObjective> objectives;

    public CaptureAreaListener(ObjectivesModule module, List<CaptureAreaObjective> objectives) {
        this.module = module;
        this.objectives = objectives;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            if(event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                // no fall damage
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onCoarseMove(PlayerCoarseMoveEvent event) {
        Player player = event.getPlayer();
        Optional<Competitor> competitorOf = this.module.getMatch().getRequiredModule(GroupsModule.class)
                .getCompetitorOf(player);

        if(!competitorOf.isPresent()) {
            return;
        }
        Competitor competitor = competitorOf.get();

        // fall into void
        SpawnsModule spawnsModule = this.module.getMatch().getRequiredModule(SpawnsModule.class);
        if(event.getTo().getY() <= -3) {
            spawnsModule.spawn(player);
            spawnsModule.setAutoRespawn(true);
        }

        // objectives stuff
        for (CaptureAreaObjective objective : objectives) {
            if(objective.canComplete(competitor) && objective.isInside(event.getTo().getBlock())) {
                Events.call(new CaptureAreaCaptureEvent(objective, player));
                Events.call(new PlayerEarnPointEvent(player, "capture-area-capture")); // string matches with the pointearnconfig and execution dispatch in thebridgemain
            } else if(!objective.canComplete(competitor) && objective.isInside(event.getTo().getBlock())) {
                IllegalCaptureBehavior ifWrongPlayer = objective.getIfWrongPlayer();
                switch(ifWrongPlayer) {
                    case KILL:
                        spawnsModule.setDead(player, true);
                        //player.damage(player.getMaxHealth() * 2); // just in case
                        break;
                    case SPAWN:
                        spawnsModule.spawn(player);
                        break;
                    case NOTHING:
                        break;
                }
            }
        }
    }

    @EventHandler
    public void captureAreaEvent(CaptureAreaCaptureEvent event) {
        CaptureAreaObjective objective = event.getCaptureArea();
        Player capturer = event.getCapturer();
        GroupsModule groupsModule = this.module.getMatch().getRequiredModule(GroupsModule.class);
        Optional<Competitor> competitor = groupsModule.getCompetitorOf(capturer);
        if(competitor.isPresent()) {
            objective.modify(competitor.get(), 1, NumberAction.ADD, capturer);
            this.respawnAll();

            if(objective.getCompletion(competitor.get()) >= 1.0) {
                Events.call(new CaptureAreaScoreCompleteEvent(event.getCaptureArea(), event.getCapturer()));
                Events.call(new PlayerEarnPointEvent(capturer, "capture-area-score-complete")); // string matches with the pointearnconfig and execution dispatch in thebridgemain
            }
        }
    }

    private void respawnAll() {
        SpawnsModule spawnsModule = this.module.getMatch().getRequiredModule(SpawnsModule.class);
        GroupsModule groupsModule = this.module.getMatch().getRequiredModule(GroupsModule.class);
        for (Player player : this.module.getMatch().getPlayers()) {
            Group group = groupsModule.getGroup(player);
            spawnsModule.setDead(player, false);
            spawnsModule.spawn(group, player, true, true);
        }
    }

}
