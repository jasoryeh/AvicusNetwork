package net.avicus.magma.api;

import lombok.Getter;
import lombok.SneakyThrows;
import net.avicus.magma.api.service.AlertsService;
import net.avicus.magma.api.service.FakeAlertsService;
import net.avicus.magma.api.service.FakePresentsService;
import net.avicus.magma.api.service.PresentsService;

import java.io.IOException;

@Getter
public class API {

    private final APIClient client;

    private final AlertsService alerts;
    private final PresentsService presents;
    private final boolean isFake;

    @Deprecated
    public API(APIClient client) {
        this.isFake = false;
        this.client = client;

        this.alerts = new AlertsService(this.client);
        this.presents = new PresentsService(this.client);
    }

    public API(String url, String token, boolean isFake) throws IOException {
        this.isFake = isFake;

        if(isFake) {
            this.alerts = new FakeAlertsService();
            this.presents = new FakePresentsService();
            this.client = null;
        } else {
            this.client = new APIClient(url, token);

            this.alerts = new AlertsService(this.client);
            this.presents = new PresentsService(this.client);
        }
    }
}
