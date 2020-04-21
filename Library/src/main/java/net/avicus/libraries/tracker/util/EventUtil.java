package net.avicus.libraries.tracker.util;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.AuthorNagException;
import org.bukkit.plugin.RegisteredListener;

import javax.annotation.Nonnull;
import java.util.logging.Level;

public final class EventUtil {

    private EventUtil() {
    }

    public static void callEvent(@Nonnull Event event, @Nonnull HandlerList handlers,
                                 @Nonnull EventPriority priority) {
        Preconditions.checkNotNull(event, "event");
        Preconditions.checkNotNull(handlers, "handlers");
        Preconditions.checkNotNull(priority, "priority");

        // CraftBukkit does not expose the event calling logic in a flexible
        // enough way, so we have to do a bit of copy and paste.
        //
        // The following is copied from SimplePluginManager#fireEvent with
        // modifications
        for (RegisteredListener registration : handlers.getRegisteredListeners()) {
            //if (!registration.isEnabled())
            //  continue;

            // skip over registrations that are not in the correct priority
            if (registration.getPriority() != priority) {
                continue;
            }

            try {
                registration.callEvent(event);
            } catch (AuthorNagException ex) {
                //
            } catch (Throwable ex) {
                Bukkit.getLogger().log(Level.SEVERE,
                        "Could not pass event " + event.getEventName() + " to " + registration.toString(), ex);
            }
        }
    }
}
