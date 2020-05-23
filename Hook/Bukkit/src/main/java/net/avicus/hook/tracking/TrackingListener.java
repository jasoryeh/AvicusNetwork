package net.avicus.hook.tracking;

import com.google.common.collect.Lists;
import org.bukkit.event.Listener;

import java.util.List;

public abstract class TrackingListener implements Listener {

    public static List<Class<? extends TrackingListener>> TRACKERS = Lists.newArrayList();

    protected final Tracking tracking;

    public TrackingListener(Tracking tracking) {
        this.tracking = tracking;
    }

}
