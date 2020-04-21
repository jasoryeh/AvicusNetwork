package net.avicus.magma.database.table.impl;

import net.avicus.libraries.quest.database.Database;
import net.avicus.libraries.quest.model.Table;
import net.avicus.magma.database.model.impl.LeaderboardEntry;

/**
 * Leaderboards!
 * <p>
 * Note: This is not name LeaderboardEntryTable, because that's too long,
 * and LeaderboardTable just makes sense.
 */
public class LeaderboardTable extends Table<LeaderboardEntry> {

    public LeaderboardTable(Database database, String name, Class<LeaderboardEntry> model) {
        super(database, name, model);
    }
}
