package net.avicus.magma.database.table.impl;

import net.avicus.libraries.quest.database.Database;
import net.avicus.libraries.quest.model.ModelList;
import net.avicus.libraries.quest.model.Table;
import net.avicus.libraries.quest.query.Filter;
import net.avicus.magma.database.model.impl.Team;

import java.util.Optional;

public class TeamTable extends Table<Team> {

    public TeamTable(Database database, String name, Class<Team> model) {
        super(database, name, model);
    }

    public Optional<Team> findById(int id) {
        return select().where("id", id).execute().stream().findAny();
    }

    public Optional<Team> findByTagOrTitle(String query) {
        Filter filter = new Filter().where("title", query).or("tag", query);
        ModelList<Team> list = select().where(filter).limit(1).execute();
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.first());
    }
}
