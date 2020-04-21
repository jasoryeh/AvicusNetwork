package net.avicus.magma.database.table.impl;

import net.avicus.libraries.quest.database.Database;
import net.avicus.libraries.quest.model.ModelList;
import net.avicus.libraries.quest.model.Table;
import net.avicus.magma.database.model.impl.Setting;

import java.util.List;

public class SettingTable extends Table<Setting> {

    public SettingTable(Database database, String name, Class<Setting> model) {
        super(database, name, model);
    }

    public List<Setting> findByUser(int userId) {
        return select().where("user_id", userId).execute();
    }

    public void updateOrSet(int userId, String key, String value) {
        ModelList<Setting> list = this.select().where("user_id", userId).where("key", key).execute();
        if (list.isEmpty()) {
            this.insert(new Setting(userId, key, value)).execute();
        } else {
            this.update().set("value", value).where("user_id", userId).where("key", key).execute();
        }
    }
}
