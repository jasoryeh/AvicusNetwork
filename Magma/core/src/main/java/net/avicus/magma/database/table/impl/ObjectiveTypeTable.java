package net.avicus.magma.database.table.impl;

import net.avicus.libraries.quest.database.Database;
import net.avicus.libraries.quest.model.Table;
import net.avicus.magma.database.model.impl.ObjectiveType;

import java.util.Optional;

public class ObjectiveTypeTable extends Table<ObjectiveType> {

    public ObjectiveTypeTable(Database database, String name, Class<ObjectiveType> model) {
        super(database, name, model);
    }

    public ObjectiveType findOrCreate(String name) {
        Optional<ObjectiveType> search = select().where("name", name).execute().stream().findAny();
        if (search.isPresent()) {
            return search.get();
        }

        ObjectiveType type = new ObjectiveType(name);
        return insert(type).execute();
    }
}
