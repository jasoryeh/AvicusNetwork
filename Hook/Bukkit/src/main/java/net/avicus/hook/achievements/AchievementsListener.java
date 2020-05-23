package net.avicus.hook.achievements;

import com.google.common.collect.Lists;
import org.bukkit.event.Listener;

import java.util.List;

public abstract class AchievementsListener implements Listener {

    public static List<Class<? extends AchievementsListener>> ACHIEVEMENTS_LISTENERS = Lists.newArrayList();

    protected final Achievements achievements;

    public AchievementsListener(Achievements achievements) {
        this.achievements = achievements;
    }

}
