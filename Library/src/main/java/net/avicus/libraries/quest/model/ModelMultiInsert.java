package net.avicus.libraries.quest.model;

import net.avicus.libraries.quest.query.MultiInsert;
import net.avicus.libraries.quest.query.Row;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ModelMultiInsert<M extends Model> {

    private final MultiInsert insert;

    public ModelMultiInsert(Table<M> table, Collection<M> instances) {
        List<String> columnNames = new ArrayList<>();
        List<Row> rows = new ArrayList<>();
        for (M instance : instances) {
            Row row = table.fromInstance(instance);
            for (String column : row.getColumns()) {
                if (!columnNames.contains(column)) {
                    columnNames.add(column);
                }
            }
            rows.add(row);
        }

        MultiInsert insert = new MultiInsert(table.getDatabase(), table.getName(), columnNames);

        for (Row row : rows) {
            Map<String, Object> map = row.getMap();
            List<Object> values = new ArrayList<>();
            for (String column : columnNames) {
                Object value = map.get(column);
                values.add(value);
            }
            insert.add(values);
        }

        this.insert = insert;
    }

    public void execute() {
        this.insert.execute();
    }
}
