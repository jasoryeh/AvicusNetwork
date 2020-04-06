package net.avicus.libraries.quest.database;

import lombok.Getter;
import net.avicus.libraries.quest.QuestUtils;
import net.avicus.libraries.quest.query.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Database {

    @Getter
    private final DatabaseConfig config;
    @Getter
    private final DatabaseConnection connection;

    public Database(DatabaseConfig config) {
        this.config = config;
        this.connection = new DatabaseConnection(this);
    }

    public Update update(String table) {
        return new Update(this, table);
    }

    public Select select(String table) {
        return new Select(this, table);
    }

    public Delete delete(String table) {
        return new Delete(this, table);
    }

    public Insert insert(String table) {
        return new Insert(this, table);
    }

    public MultiInsert multiInsert(String table, Collection<String> columns) {
        return new MultiInsert(this, table, columns);
    }

    public MultiInsert multiInsert(String table, String... columns) {
        return new MultiInsert(this, table, Arrays.asList(columns));
    }

    public void createTable(String table, Map<String, String> columns) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE ");
        sql.append(QuestUtils.getField(table));
        sql.append(" (");

        List<String> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : columns.entrySet()) {
            list.add(QuestUtils.getField(entry.getKey()) + " " + entry.getValue());
        }
        sql.append(String.join(", ", list));

        sql.append(")");

        try (PreparedStatement statement = createUpdateStatement(sql.toString())) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to create table.", e);
        }
    }

    public PreparedStatement createUpdateStatement(String sql) {
        try {
            return this.connection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public PreparedStatement createQueryStatement(String sql, boolean memory)
            throws DatabaseException {
        try {
            if (memory) {
                return this.connection.getConnection().prepareStatement(sql);
            }
            PreparedStatement statement = this.connection.getConnection()
                    .prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            statement.setFetchSize(Integer.MIN_VALUE);
            return statement;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public DatabaseConnection connect() throws DatabaseException {
        this.connection.open();
        return this.connection;
    }

    public DatabaseConnection disconnect() throws DatabaseException {
        this.connection.close();
        return this.connection;
    }
}
