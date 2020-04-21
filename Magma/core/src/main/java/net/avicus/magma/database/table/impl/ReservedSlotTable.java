package net.avicus.magma.database.table.impl;

import net.avicus.libraries.quest.database.Database;
import net.avicus.libraries.quest.model.ModelList;
import net.avicus.libraries.quest.model.Table;
import net.avicus.libraries.quest.query.Filter;
import net.avicus.libraries.quest.query.Operator;
import net.avicus.magma.database.model.impl.ReservedSlot;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ReservedSlotTable extends Table<ReservedSlot> {

    public ReservedSlotTable(Database database, String name, Class<ReservedSlot> model) {
        super(database, name, model);
    }

    public Optional<ReservedSlot> findCurrentReservation(String server) {
        Date now = new Date();
        Filter date = new Filter().where("server", server)
                .where("start_at", now, Operator.LESS_OR_EQUAL)
                .and("end_at", now, Operator.GREATER_OR_EQUAL);
        ModelList<ReservedSlot> reservations = select().where(date).limit(1).execute();

        if (reservations.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(reservations.first());
    }

    /**
     * Find reservations where their start_at time is between specific times.
     */
    public List<ReservedSlot> findByStart(Date from, Date to) {
        return select().where("start_at", from, Operator.GREATER_OR_EQUAL)
                .where("start_at", to, Operator.LESS).execute();
    }
}
