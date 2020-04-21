package net.avicus.magma.database.table.impl;

import net.avicus.libraries.quest.database.Database;
import net.avicus.libraries.quest.model.Table;
import net.avicus.magma.database.model.impl.IPBan;

import java.util.Optional;

public class IPBanTable extends Table<IPBan> {

    public IPBanTable(Database database, String name, Class<IPBan> model) {
        super(database, name, model);
    }

    public Optional<IPBan> getByIp(String ip) {
        return select().where("ip", ip).execute().stream().findFirst();
    }
}
