package net.avicus.atlas.sets.thebridge.modules.capturearea.events;

import lombok.Getter;
import net.avicus.atlas.event.objective.ObjectiveCompleteEvent;
import net.avicus.atlas.module.groups.Competitor;
import net.avicus.atlas.sets.thebridge.modules.capturearea.CaptureAreaObjective;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class CaptureAreaCaptureEvent extends ObjectiveCompleteEvent {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private final Player capturer;
    @Getter
    private final CaptureAreaObjective captureArea;

    public CaptureAreaCaptureEvent(CaptureAreaObjective captureArea, Player player) {
        super(captureArea, player);
        this.capturer = player;
        this.captureArea = captureArea;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
