package net.avicus.magma.api.service;

import com.lambdaworks.com.google.common.collect.Lists;
import net.avicus.magma.api.graph.QLBuilder;
import net.avicus.magma.api.graph.inputs.AlertDeleteInput;
import net.avicus.magma.api.graph.inputs.AlertSendInput;
import net.avicus.magma.api.graph.mutations.alert_send.AlertSendQuery;
import net.avicus.magma.api.graph.types.alert.Alert;
import net.avicus.magma.api.graph.types.base.BaseQueryQueryDefinition;
import net.avicus.magma.database.model.impl.Friend;
import net.avicus.magma.database.model.impl.User;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FakeAlertsService extends AlertsService {
    public FakeAlertsService() {
        super(null);
    }

    @Override
    public List<Alert> getAlerts(User user) {
        return new ArrayList<>();
    }

    @Override
    public List<Alert> getAlertsAfter(DateTime after) {
        return new ArrayList<>();
    }

    @Override
    public boolean delete(Alert alert) {
        return false;
    }

    @Override
    public boolean sendAlert(User to, String name, String url, String message) {
        return false;
    }

    @Override
    public boolean createFriendRequest(User user, User friend, Friend association) {
        return false;
    }

    @Override
    public void destroyFriendRequest(Friend association) { }

    @Override
    public boolean createFriendAccept(User user, User friend, Friend association) {
        return false;
    }

    @Override
    public Optional<Alert> findOne(BaseQueryQueryDefinition definition) {
        return Optional.empty();
    }

    @Override
    public List<Alert> findMany(BaseQueryQueryDefinition definition) {
        return new ArrayList<>();
    }
}
