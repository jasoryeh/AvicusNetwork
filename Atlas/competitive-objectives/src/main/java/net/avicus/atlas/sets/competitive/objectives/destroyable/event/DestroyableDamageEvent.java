package net.avicus.atlas.sets.competitive.objectives.destroyable.event;

import lombok.Getter;
import net.avicus.atlas.event.objective.ObjectiveStateChangeEvent;
import net.avicus.atlas.sets.competitive.objectives.destroyable.DestroyableEventInfo;
import net.avicus.atlas.sets.competitive.objectives.destroyable.DestroyableObjective;
import org.bukkit.event.HandlerList;

public class DestroyableDamageEvent extends ObjectiveStateChangeEvent {

    private static final HandlerList handlers = new HandlerList();
    @Getter
    private final DestroyableEventInfo info;

    public DestroyableDamageEvent(DestroyableObjective objective, DestroyableEventInfo info) {
        super(objective);
        this.info = info;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
