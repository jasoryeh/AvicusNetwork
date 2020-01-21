package net.avicus.quest.query;

import net.avicus.quest.QuestUtils;
import net.avicus.quest.database.Database;
import net.avicus.quest.database.DatabaseException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Insert {

    private final Database database;
    private final String table;
    private final Map<String, Object> values;

    public Insert(Database database, String table) {
        this.database = database;
        this.table = table;
        this.values = new LinkedHashMap<>();
    }

    public Insert set(String field, Object value) {
        this.values.put(field, value);
        return this;
    }

    public Insert set(Map<String, Object> values) {
        this.values.putAll(values);
        return this;
    }

    public String build() {
        List<String> fields = new ArrayList<>(this.values.size());
        for (String field : this.values.keySet()) {
            fields.add(QuestUtils.getField(field));
        }

        List<String> values = new ArrayList<>(this.values.size());
        for (Object value : this.values.values()) {
            values.add(QuestUtils.getValue(value));
        }

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ");
        sql.append(QuestUtils.getField(this.table));
        sql.append(" (");
        sql.append(String.join(", ", fields));
        sql.append(") VALUES (");
        sql.append(String.join(", ", values));
        sql.append(");");
        return sql.toString();
    }

    /**
     * Executes the insert.
     *
     * @return The auto generated key if present, otherwise Optional.empty().
     */
    public Optional<Integer> execute() throws DatabaseException {
        String sql = this.build();
        try (PreparedStatement statement = this.database.createUpdateStatement(sql)) {
            statement.executeUpdate();

            // generated keys
            try (ResultSet set = statement.getGeneratedKeys()) {
                if (set.next()) {
                    return Optional.of(set.getInt(1));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DatabaseException(String.format("Failed statement: %s", sql), e);
        }
    }
}
