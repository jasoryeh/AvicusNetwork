package net.avicus.atlas.sets.bedwars;

import lombok.Getter;
import lombok.Setter;
import net.avicus.atlas.Atlas;
import net.avicus.atlas.external.ModuleSet;

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
    }

    @Override
    public void onDisable() {

    }
}
