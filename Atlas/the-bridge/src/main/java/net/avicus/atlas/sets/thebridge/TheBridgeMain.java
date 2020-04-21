package net.avicus.atlas.sets.bedwars;

import lombok.Getter;
import net.avicus.atlas.Atlas;
import net.avicus.atlas.countdown.CyclingCountdown;
import net.avicus.atlas.countdown.StartingCountdown;
import net.avicus.atlas.external.ModuleSet;
import net.avicus.atlas.module.map.CountdownConfig;
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

        CountdownConfig.DEFAULT_VALUES.clear();
        CountdownConfig.DEFAULT_VALUES.put(StartingCountdown.class, Seconds.seconds(15).toStandardDuration());
        CountdownConfig.DEFAULT_VALUES.put(CyclingCountdown.class, Seconds.seconds(15).toStandardDuration());


        this.logger.info("The Bridge set ready.");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("Goodbye Cruel World!");
    }
}
