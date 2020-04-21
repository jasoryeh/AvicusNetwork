package net.avicus.magma.database.table.impl;

import net.avicus.libraries.quest.database.Database;
import net.avicus.libraries.quest.model.Table;
import net.avicus.magma.database.model.impl.ExperienceLeaderboardEntry;

/**
 * Experience Leaderboards!!!
 * <p>
 * Note: This is not name ExperienceLeaderboardEntryTable, because that's too long,
 * and ExperienceLeaderboardTable just makes sense.
 */
public class ExperienceLeaderboardTable extends Table<ExperienceLeaderboardEntry> {

    public ExperienceLeaderboardTable(Database database, String name,
                                      Class<ExperienceLeaderboardEntry> model) {
        super(database, name, model);
    }
}
