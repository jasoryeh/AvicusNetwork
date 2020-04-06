package net.avicus.magma.database.table.impl;

import net.avicus.libraries.quest.database.Database;
import net.avicus.libraries.quest.model.Table;
import net.avicus.magma.database.model.impl.Server;
import net.avicus.magma.database.model.impl.ServerCategory;

import java.util.Optional;

public class ServerCategoryTable extends Table<ServerCategory> {

    public ServerCategoryTable(Database database, String name, Class<ServerCategory> model) {
        super(database, name, model);
    }

    public Optional<ServerCategory> fromServer(Server server) {
        if (server == null || server.getServerCategoryId() == 0) {
            return Optional.empty();
        }

        return Optional
                .ofNullable(select().where("id", server.getServerCategoryId()).execute().first());
    }
}
