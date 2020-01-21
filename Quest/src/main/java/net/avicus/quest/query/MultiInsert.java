package net.avicus.quest.query;

import net.avicus.quest.QuestUtils;
import net.avicus.quest.database.Database;
import net.avicus.quest.database.DatabaseException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * An insert with multiple rows.
 */
public class MultiInsert {

    private final Database database;
    private final String table;
    private final Collection<String> columns;
    private final List<List<Object>> rows;

    public MultiInsert(Database database, String table, Collection<String> columns) {
        this.database = database;
        this.table = table;
        this.columns = columns;
        this.rows = new ArrayList<>();
    }

    public MultiInsert add(List<Object> row) {
        this.rows.add(row);
        return this;
    }

    public MultiInsert addAll(List<List<Object>> rows) {
        this.rows.addAll(rows);
        return this;
    }

    public String build() {
        List<String> fields = new ArrayList<>(this.columns.size());
        for (String column : this.columns) {
            fields.add(QuestUtils.getField(column));
        }

        List<String> rows = new ArrayList<>(this.rows.size());
        for (List<Object> row : this.rows) {
            List<String> values = new ArrayList<>();
            for (Object value : row) {
                values.add(QuestUtils.getValue(value));
            }
            rows.add("(" + String.join(", ", values) + ")");
        }

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ");
        sql.append(QuestUtils.getField(this.table));
        sql.append(" (");
        sql.append(String.join(", ", fields));
        sql.append(") VALUES ");
        sql.append(String.join(", ", rows));
        sql.append(";");
        return sql.toString();
    }

    /**
     * Executes the insert.
     */
    public void execute() throws DatabaseException {
        String sql = this.build();
        try (PreparedStatement statement = this.database.createUpdateStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(String.format("Failed statement: %s", sql), e);
        }
    }
}
