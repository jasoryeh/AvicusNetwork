package net.avicus.atlas.sets.thebridge.atlas;

import net.avicus.atlas.command.GameCommands;
import net.avicus.atlas.component.visual.SidebarHook;
import net.avicus.atlas.module.objectives.ObjectivesModule;
import net.avicus.atlas.sets.thebridge.modules.capturearea.events.CaptureAreaCaptureEvent;
import net.avicus.atlas.sets.thebridge.modules.capturearea.events.CaptureAreaScoreCompleteEvent;
import net.avicus.atlas.util.Messages;
import net.avicus.atlas.util.ObjectiveRenderer;
import net.avicus.compendium.locale.text.Localizable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.Optional;

public class TheBridgeSidebarHook extends SidebarHook {

    private static final ObjectiveRenderer RENDERER = new TheBridgeRenderer();

    static {
        // replace the render in the command that renders the sidebar into the chat
        GameCommands.RENDERER = RENDERER;
    }

    @Override
    public ObjectiveRenderer getRenderer() {
        return RENDERER;
    }

    @Override
    public Optional<Localizable> getTitle(ObjectivesModule module) {
        TheBridgeObjectivesBridge bridge = module.getMatch().getRequiredModule(ObjectivesModule.class).getBridge(TheBridgeObjectivesBridge.class);

        if(bridge.objectives.isEmpty()) {
            return Optional.empty();
        }

        if(bridge.objectives.size() == bridge.captureAreas.size()) {
            return Optional.of(Messages.UI_THE_BRIDGE.with());
        }

        return Optional.empty();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void captureAreaCapture(CaptureAreaCaptureEvent event) {
        getComponent().delayedUpdate();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void captureAreaScoreComplete(CaptureAreaScoreCompleteEvent event) {
        getComponent().delayedUpdate();
    }
}
