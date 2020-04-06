package net.avicus;

import lombok.Getter;
import net.avicus.libraries.tabbed.TabbedPlugin;
import net.avicus.libraries.tracker.plugin.TrackerPlugin;
import net.avicus.libraries.tutorial.plugin.TutorialPlugin;

public abstract class Library {
    @Getter
    private final LibraryPlugin parent;

    public Library(LibraryPlugin parent) {
        this.parent = parent;
    }

    public abstract void onEnable();

    public abstract void onDisable();

    private static TutorialPlugin tutorialPlugin;
    private static TrackerPlugin trackerPlugin;
    private static TabbedPlugin tabbedPlugin;

    public static void loadLibraries(LibraryPlugin plugin) {
        tutorialPlugin = new TutorialPlugin(plugin);
        tutorialPlugin.onEnable();

        trackerPlugin = new TrackerPlugin(plugin);
        trackerPlugin.onEnable();

        tabbedPlugin = new TabbedPlugin(plugin);
        tabbedPlugin.onEnable();

        // quest

        // grave

        // bossy
    }

    public static void unloadLibraries(LibraryPlugin plugin) {
        tutorialPlugin.onDisable();
        trackerPlugin.onDisable();
        tabbedPlugin.onDisable();
        // quest
        // grave
        // bossy
    }
}
