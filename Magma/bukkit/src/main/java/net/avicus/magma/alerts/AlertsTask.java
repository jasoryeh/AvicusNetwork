package net.avicus.magma.alerts;

import net.avicus.magma.Magma;
import net.avicus.magma.MagmaConfig;
import net.avicus.magma.api.graph.types.alert.Alert;
import net.avicus.magma.network.user.Users;
import net.avicus.magma.util.MagmaTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Optional;

public class AlertsTask extends MagmaTask {

    private DateTime lastUpdate = new DateTime();

    public void start() {
        repeatAsync(0, 20 * MagmaConfig.Alerts.getPoll());
    }

    @Override
    public void run() throws Exception {
        // Don't waste our time running alert updates if people aren't even online! When they are online, then we start updating!
        if(Bukkit.getOnlinePlayers().size() > 0) {
            // All alerts
            List<Alert> alerts = Magma.get().getApiClient().getAlerts().getAlertsAfter(this.lastUpdate);
            this.lastUpdate = new DateTime();

            for (Alert alert : alerts) {
                Optional<Player> player = Users.player(alert.getUserId());

                if (player.isPresent()) {
                    // Save and notify our alerts if the guy is online
                    Alerts.add(alert);
                    Alerts.notify(player.get());
                }
            }
        }
    }
}
