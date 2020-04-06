package net.avicus.magma.api.service;

import net.avicus.magma.api.graph.types.base.BaseQueryQueryDefinition;
import net.avicus.magma.api.graph.types.present.Present;
import net.avicus.magma.database.model.impl.User;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FakePresentsService extends PresentsService {
    public FakePresentsService() {
        super(null);
    }

    @Override
    public Pair<Boolean, String> find(User who, String family, String slug) {
        return Pair.of(false, "Presents are currently unavailable.");
    }

    @Override
    public Optional<Present> findOne(BaseQueryQueryDefinition definition) {
        return Optional.empty();
    }

    @Override
    public List<Present> findMany(BaseQueryQueryDefinition definition) {
        return new ArrayList<>();
    }
}
