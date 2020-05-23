package net.avicus.atlas.sets.thebridge.atlas;

import com.google.common.collect.Sets;
import net.avicus.atlas.GameType;
import net.avicus.atlas.map.AtlasMap;
import net.avicus.atlas.match.Match;
import net.avicus.atlas.module.bridge.ObjectivesModuleBridge;
import net.avicus.atlas.module.objectives.Objective;
import net.avicus.atlas.module.objectives.ObjectivesModule;
import net.avicus.atlas.sets.thebridge.modules.capturearea.CaptureAreaObjective;
import net.avicus.magma.util.MapGenre;

import java.util.Optional;
import java.util.Set;

public class TheBridgeTypeDetector implements AtlasMap.TypeDetector {
    @Override
    public Optional<MapGenre> detectGenre(Match match) {
        Optional<ObjectivesModule> module = match.getModule(ObjectivesModule.class);

        if(module.isPresent()) {
            ObjectivesModule objectivesModule = module.get();
            TheBridgeObjectivesBridge bridge = objectivesModule.getBridge(TheBridgeObjectivesBridge.class);

            if(bridge.captureAreas.size() == bridge.objectives.size()) {
                return Optional.of(MapGenre.THE_BRIDGE);
            }
        }

        return Optional.empty();
    }

    @Override
    public Set<GameType> detectGameTypes(Match match) {
        Set<GameType> types = Sets.newHashSet();

        match.getModule(ObjectivesModule.class).ifPresent(objectives -> {
            for (final Objective objective : objectives.getObjectives()) {
                if (objective instanceof CaptureAreaObjective) {
                    types.add(GameType.THE_BRIDGE);
                }
            }
        });

        return types;
    }
}
