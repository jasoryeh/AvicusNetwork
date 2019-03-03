package net.avicus.atlas.sets.example;

import lombok.Getter;
import lombok.Setter;
import net.avicus.atlas.Atlas;
import net.avicus.atlas.GameType;
import net.avicus.atlas.external.ModuleSet;
import net.avicus.atlas.map.AtlasMap;
import net.avicus.atlas.map.AtlasMapFactory;
import net.avicus.atlas.match.Match;
import net.avicus.magma.util.MapGenre;

import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

public class BedwarsMain extends ModuleSet {
    @Getter
    private static BedwarsMain instance;

    @Setter
    private Atlas atlas;
    @Setter
    private Logger logger;

    @Override
    public void onEnable() {
        instance = this;

        this.logger.info("Enabled bedwars set.");

        AtlasMapFactory.TYPE_DETECTORS.add(new AtlasMap.TypeDetector() {

            @Override
            public Optional<MapGenre> detectGenre(Match match) {
                return match.hasModule()
            }

            @Override
            public Set<GameType> detectGameTypes(Match match) {
                return null;
            }
        });
    }

    @Override
    public void onDisable() {

    }
}
