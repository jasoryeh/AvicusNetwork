package net.avicus;

import lombok.Getter;
import net.avicus.libraries.grave.GravePlugin;
import net.avicus.libraries.tabbed.TabbedPlugin;
import net.avicus.libraries.tracker.plugin.TrackerPlugin;
import net.avicus.libraries.tutorial.plugin.TutorialPlugin;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class Library {
    @Getter
    private final JavaPlugin parent;

    public Library(JavaPlugin parent) {
        this.parent = parent;
    }

    public abstract void onEnable();

    public abstract void onDisable();

    public void onSafeDisable() {
        parent.getLogger().info("Unloading library " + this.getClass().getName());
        try {
            this.onDisable();
            parent.getLogger().info("Disabled library " + this.getClass().getName());
        } catch(Exception e) {
            parent.getLogger().warning("Library was unable to successfully disable the library " + this.getClass().getName());
            e.printStackTrace();
        }
    }

    public void onSafeEnable() {
        parent.getLogger().info("Loading library " + this.getClass().getName());
        try {
            this.onEnable();
            parent.getLogger().info("Enabled library " + this.getClass().getName());
        } catch(Exception e) {
            parent.getLogger().warning("Library was unable to successfully disable the library " + this.getClass().getName());
            e.printStackTrace();
        }
    }

    private static TutorialPlugin tutorialPlugin;
    private static TrackerPlugin trackerPlugin;
    private static TabbedPlugin tabbedPlugin;
    private static GravePlugin gravePlugin;

    @Getter
    private static boolean initialized = false;
    @Getter
    private static boolean deinitialized = false;

    public static void loadLibraries(JavaPlugin plugin) {
        tutorialPlugin = new TutorialPlugin(plugin);
        tutorialPlugin.onSafeEnable();

        trackerPlugin = new TrackerPlugin(plugin);
        trackerPlugin.onSafeEnable();

        tabbedPlugin = new TabbedPlugin(plugin);
        tabbedPlugin.onSafeEnable();

        // quest

        gravePlugin = new GravePlugin(plugin);
        gravePlugin.onSafeEnable();

        // bossy

        initialized = true;
    }

    public static void unloadLibraries(JavaPlugin plugin) {
        // bossy
        gravePlugin.onSafeDisable();
        // quest
        tabbedPlugin.onSafeDisable();
        trackerPlugin.onSafeDisable();
        tutorialPlugin.onSafeDisable();

        initialized = false;
        deinitialized = true;
    }
}
