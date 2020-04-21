package net.avicus.magma.database.table.impl;

import net.avicus.libraries.quest.database.Database;
import net.avicus.libraries.quest.model.Table;
import net.avicus.magma.database.model.impl.User;
import net.avicus.magma.database.model.impl.UserDetail;

public class UserDetailTable extends Table<UserDetail> {

    public UserDetailTable(Database database, String name, Class<UserDetail> model) {
        super(database, name, model);
    }

    public UserDetail findByUser(User user) {
        return select().where("id", user.getId()).execute().first();
    }
}
