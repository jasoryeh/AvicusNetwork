package net.avicus.quest.query;

import lombok.ToString;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ToString
public class Row {

    private final Map<String, Object> values;

    public Row(ResultSet resultSet) throws SQLException {
        int columns = resultSet.getMetaData().getColumnCount();
        Map<String, Object> values = new LinkedHashMap<>();
        for (int i = 0; i < columns; i++) {
            String name = resultSet.getMetaData().getColumnName(i + 1);
            Object value = resultSet.getObject(i + 1);
            values.put(name, value);
        }
        this.values = values;
    }

    public Row(Map<String, Object> values) {
        this.values = values;
    }

    public Map<String, Object> getMap() {
        return this.values;
    }

    public List<String> getColumns() {
        List<String> columns = new ArrayList<>();
        for (String column : this.values.keySet()) {
            columns.add(column);
        }
        return columns;
    }

    public List<Object> getValues() {
        List<Object> values = new ArrayList<>();
        for (Object column : this.values.values()) {
            values.add(column);
        }
        return values;
    }

    public String getString(String field) throws ClassCastException {
        return get(String.class, field);
    }

    public int getInteger(String field) throws ClassCastException {
        return get(Integer.class, field);
    }

    public boolean getBoolean(String field) throws ClassCastException {
        return get(boolean.class, field);
    }

    public Object get(String field) {
        return this.values.get(field);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> type, String field) {
        return (T) this.values.get(field);
    }
}
