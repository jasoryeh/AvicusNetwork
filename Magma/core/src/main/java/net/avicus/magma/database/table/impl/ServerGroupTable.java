package net.avicus.magma.database.table.impl;

import net.avicus.libraries.quest.database.Database;
import net.avicus.libraries.quest.model.Table;
import net.avicus.magma.database.model.impl.ServerGroup;

import java.util.Optional;

public class ServerGroupTable extends Table<ServerGroup> {

    public ServerGroupTable(Database database, String name, Class<ServerGroup> model) {
        super(database, name, model);
    }

    public Optional<ServerGroup> findBySlug(String slug) {
        return select().where("slug", slug).execute().stream().findFirst();
    }

    public Optional<ServerGroup> findById(int id) {
        return select().where("id", id).execute().stream().findFirst();
    }
}
