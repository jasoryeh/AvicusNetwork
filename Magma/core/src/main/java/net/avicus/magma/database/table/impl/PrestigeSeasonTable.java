package net.avicus.magma.database.table.impl;

import net.avicus.libraries.quest.database.Database;
import net.avicus.libraries.quest.model.ModelList;
import net.avicus.libraries.quest.model.Table;
import net.avicus.libraries.quest.query.Filter;
import net.avicus.libraries.quest.query.Operator;
import net.avicus.magma.database.model.impl.PrestigeSeason;

import java.util.Date;
import java.util.Optional;

public class PrestigeSeasonTable extends Table<PrestigeSeason> {

    public PrestigeSeasonTable(Database database, String name, Class<PrestigeSeason> model) {
        super(database, name, model);
    }

    public Optional<PrestigeSeason> findCurrentSeason() {
        Date now = new Date();
        Filter date = new Filter().where("start_at", now, Operator.LESS_OR_EQUAL)
                .and("end_at", now, Operator.GREATER_OR_EQUAL);
        ModelList<PrestigeSeason> slots = select().where(date).limit(1).execute();

        if (slots.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(slots.first());
    }

    public Optional<PrestigeSeason> findById(int id) {
        ModelList<PrestigeSeason> list = select().where("id", id).limit(1).execute();
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.first());
    }
}
