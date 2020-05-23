package net.avicus.atlas.sets.thebridge;

import com.google.common.collect.Lists;
import lombok.Getter;
import net.avicus.atlas.Atlas;
import net.avicus.atlas.component.AtlasComponentManager;
import net.avicus.atlas.component.visual.SidebarComponent;
import net.avicus.atlas.countdown.CyclingCountdown;
import net.avicus.atlas.countdown.StartingCountdown;
import net.avicus.atlas.external.ModuleSet;
import net.avicus.atlas.module.executors.ExecutionDispatch;
import net.avicus.atlas.module.groups.ffa.FFAModule;
import net.avicus.atlas.module.groups.teams.TeamsModule;
import net.avicus.atlas.module.map.CountdownConfig;
import net.avicus.atlas.module.objectives.ObjectivesFactory;
import net.avicus.atlas.module.objectives.ObjectivesModule;
import net.avicus.atlas.module.results.ResultsModule;
import net.avicus.atlas.module.shop.PointEarnConfig;
import net.avicus.atlas.module.stats.StatsModule;
import net.avicus.atlas.sets.thebridge.atlas.TheBridgeFFABridge;
import net.avicus.atlas.sets.thebridge.atlas.TheBridgeObjectivesBridge;
import net.avicus.atlas.sets.thebridge.atlas.TheBridgeResultsBridge;
import net.avicus.atlas.sets.thebridge.atlas.TheBridgeStatsBridge;
import net.avicus.atlas.sets.thebridge.modules.capturearea.CaptureAreaFactory;
import net.avicus.atlas.sets.thebridge.modules.capturearea.events.CaptureAreaCaptureEvent;
import net.avicus.atlas.sets.thebridge.modules.capturearea.events.CaptureAreaScoreCompleteEvent;
import net.avicus.atlas.sets.thebridge.atlas.TheBridgeSidebarHook;
import org.bukkit.Bukkit;
import org.joda.time.Seconds;

import java.util.logging.Logger;

public class TheBridgeMain extends ModuleSet {

    @Getter
    private static TheBridgeMain instance;

    @Getter
    private Logger logger;

    @Override
    public void onEnable() {
        instance = this;
        this.logger = Atlas.get().getLogger();

        this.logger.info("Enabling the bridge set.");
        // 1) factories
        ObjectivesFactory.FACTORY_MAP.put("capture-area", new CaptureAreaFactory());

        // 2) execution listeners
        // capture area was entered/captured
        ExecutionDispatch.registerListener("capture-area-capture", CaptureAreaCaptureEvent.class, e -> {
            CaptureAreaCaptureEvent event = (CaptureAreaCaptureEvent) e;
            ExecutionDispatch.whenDispatcherExists(dispatcher -> dispatcher
                    .handleEvent(event, event.getPlayers().get(0), event.getPlayers().get(0).getLocation()));
        });
        // capture area points all captured
        ExecutionDispatch.registerListener("capture-area-score-complete", CaptureAreaScoreCompleteEvent.class, e -> {
            CaptureAreaScoreCompleteEvent event = (CaptureAreaScoreCompleteEvent) e;
            ExecutionDispatch.whenDispatcherExists(dispatcher -> dispatcher
                    .handleEvent(event, event.getPlayers().get(0), event.getPlayers().get(0).getLocation()));
        });

        // 3) bridges
        // 3.1) results bridges
        ResultsModule.BRIDGES.putIfAbsent(ResultsModule.class, Lists.newArrayList());
        ResultsModule.BRIDGES.get(ResultsModule.class).add(TheBridgeResultsBridge.class);

        // 3.2) objectives bridges (register objectives)
        ObjectivesModule.BRIDGES.putIfAbsent(ObjectivesModule.class, Lists.newArrayList());
        ObjectivesModule.BRIDGES.get(ObjectivesModule.class).add(TheBridgeObjectivesBridge.class);

        // 3.3) stats bridges
        StatsModule.BRIDGES.putIfAbsent(StatsModule.class, Lists.newArrayList());
        StatsModule.BRIDGES.get(StatsModule.class).add(TheBridgeStatsBridge.class);

        // 3.4) teams bridges
        TeamsModule.BRIDGES.putIfAbsent(TeamsModule.class, Lists.newArrayList());
        TeamsModule.BRIDGES.get(TeamsModule.class).add(TheBridgeStatsBridge.class);

        // 3.5) ffa bridges
        FFAModule.BRIDGES.putIfAbsent(FFAModule.class, Lists.newArrayList());
        FFAModule.BRIDGES.get(FFAModule.class).add(TheBridgeFFABridge.class);

        // 4) configure points?
        // listen for this event to give points
        PointEarnConfig.CONFIGURABLES.add("capture-area-capture"); // string corresponds with an execution listener above
        PointEarnConfig.CONFIGURABLES.add("capture-area-score-complete"); // string corresponds with an execution listener above

        // 5) default countdowns
        CountdownConfig.DEFAULT_VALUES.clear();
        CountdownConfig.DEFAULT_VALUES.put(StartingCountdown.class, Seconds.seconds(15).toStandardDuration());
        CountdownConfig.DEFAULT_VALUES.put(CyclingCountdown.class, Seconds.seconds(15).toStandardDuration());

        this.logger.info("The Bridge set ready.");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("Goodbye Cruel World!");
    }

    @Override
    public void onComponentsEnable(AtlasComponentManager componentManager) {
        if(componentManager.hasModule(SidebarComponent.class)) {
            SidebarComponent.HOOKS.add(new TheBridgeSidebarHook());
            this.logger.info("Registered the brige sidebar hook");
        }
    }
}
