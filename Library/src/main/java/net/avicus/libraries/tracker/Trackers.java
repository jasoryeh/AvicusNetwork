package net.avicus.libraries.tracker;

import net.avicus.libraries.tracker.base.SimpleTrackerManager;

import javax.annotation.Nonnull;


public final class Trackers {

    private static TrackerManager manager;

    private Trackers() {
    }

    public static
    @Nonnull
    TrackerManager getManager() {
        if (manager == null) {
            manager = new SimpleTrackerManager();
        }
        return manager;
    }

    /**
     * Utility method for {@link TrackerManager#getTracker}
     */
    public static <T extends Tracker> T getTracker(@Nonnull Class<T> trackerClass) {
        return getManager().getTracker(trackerClass);
    }
}
