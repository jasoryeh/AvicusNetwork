package net.avicus.magma.database.table.impl;

import net.avicus.libraries.quest.database.Database;
import net.avicus.libraries.quest.model.Table;
import net.avicus.magma.database.model.impl.User;
import net.avicus.magma.database.model.impl.Username;

import java.util.List;

public class UsernameTable extends Table<Username> {

    public UsernameTable(Database database, String name, Class<Username> model) {
        super(database, name, model);
    }

    public List<Username> findByUser(User user) {
        return select().where("user_id", user.getId()).order("created_at", "DESC").execute();
    }
}
