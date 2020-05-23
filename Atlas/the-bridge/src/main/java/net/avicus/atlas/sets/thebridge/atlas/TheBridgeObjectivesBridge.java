package net.avicus.atlas.sets.thebridge.atlas;

import com.google.common.collect.Lists;
import net.avicus.atlas.match.Match;
import net.avicus.atlas.module.bridge.ObjectivesModuleBridge;
import net.avicus.atlas.module.objectives.Objective;
import net.avicus.atlas.module.objectives.ObjectivesModule;
import net.avicus.atlas.sets.thebridge.modules.capturearea.CaptureAreaListener;
import net.avicus.atlas.sets.thebridge.modules.capturearea.CaptureAreaObjective;
import org.bukkit.event.Listener;

import java.util.List;

public class TheBridgeObjectivesBridge extends ObjectivesModuleBridge implements Listener {

    private Match match;
    private ObjectivesModule module;

    final List<Listener> listeners;

    final List<CaptureAreaObjective> captureAreas;

    List<Objective> objectives;

    public TheBridgeObjectivesBridge(ObjectivesModule module) {
        this.module = module;
        this.match = module.getMatch();

        this.listeners = module.getListeners();

        this.captureAreas = module.getObjectivesByType(CaptureAreaObjective.class);

        this.objectives = Lists.newArrayList();
        this.objectives.addAll(this.captureAreas);
    }

    @Override
    public void onOpen(ObjectivesModule module) {
        if(this.captureAreas.size() > 0) {
            this.listeners.add(new CaptureAreaListener(module, this.captureAreas));
        }

        this.listeners.add(this);
    }

    @Override
    public void onClose(ObjectivesModule module) {

    }
}
