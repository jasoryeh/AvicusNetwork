package net.avicus.hook.prestige;
import com.google.common.collect.Lists;
import net.avicus.magma.module.prestige.PrestigeModule;
import org.bukkit.event.Listener;

import java.util.List;

public abstract class ExperienceRewardListener implements Listener {

    public static List<Class<? extends ExperienceRewardListener>> EXPERIENCE_LISTENERS = Lists.newArrayList();

    protected final PrestigeModule module;

    public ExperienceRewardListener(PrestigeModule module) {
        this.module = module;
    }

}
