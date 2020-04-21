package net.avicus.magma.database.table.impl;

import net.avicus.libraries.quest.database.Database;
import net.avicus.libraries.quest.model.Table;
import net.avicus.libraries.quest.query.RowList;
import net.avicus.magma.database.model.impl.CreditTransaction;

import java.math.BigDecimal;

public class CreditTransactionTable extends Table<CreditTransaction> {

    public CreditTransactionTable(Database database, String name, Class<CreditTransaction> model) {
        super(database, name, model);
    }

    public int sumCredits(int userId) {
        RowList list = getDatabase().select(getName()).columns("SUM(`amount`)").where("user_id", userId)
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
