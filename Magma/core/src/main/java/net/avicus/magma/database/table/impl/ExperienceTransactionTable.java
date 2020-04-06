package net.avicus.magma.database.table.impl;

import net.avicus.libraries.quest.database.Database;
import net.avicus.libraries.quest.model.Table;
import net.avicus.libraries.quest.query.RowList;
import net.avicus.magma.database.model.impl.ExperienceTransaction;
import net.avicus.magma.database.model.impl.PrestigeSeason;

import java.math.BigDecimal;

public class ExperienceTransactionTable extends Table<ExperienceTransaction> {

    public ExperienceTransactionTable(Database database, String name,
                                      Class<ExperienceTransaction> model) {
        super(database, name, model);
    }

    public int sumXP(int userId, PrestigeSeason season) {
        RowList list = getDatabase().select(getName())
                .columns("SUM(`amount`)")
                .where("user_id", userId)
                .where("season_id", season.getId())
                .execute();
        try {
            BigDecimal big = (BigDecimal) list.first().getMap().values().stream().findAny()
                    .orElse(new BigDecimal(0));
            return big.intValue();
        } catch (Exception e) {
            return 0;
        }
    }
}
