package net.avicus.magma.database.table.impl;

import net.avicus.libraries.quest.database.Database;
import net.avicus.libraries.quest.model.Table;
import net.avicus.magma.database.model.impl.Announcement;
import net.avicus.magma.database.model.impl.Announcement.Type;

import java.util.List;

public class AnnouncementTable extends Table<Announcement> {

    public AnnouncementTable(Database database, String name, Class<Announcement> model) {
        super(database, name, model);
    }

    public List<Announcement> findByType(Type type) {
        return select().where(type.columnName(), true).where("enabled", true).execute();
    }
}
