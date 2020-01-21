package net.avicus.quest.model;

import lombok.Getter;
import net.avicus.quest.database.DatabaseException;
import net.avicus.quest.query.Row;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ModelList<M extends Model> extends ArrayList<M> {

    @Getter
    private final Table<M> table;

    public ModelList(Table<M> table) {
        this.table = table;
    }

    public M first() {
        if (size() == 0) {
            return null;
        }
        return get(0);
    }

    public M last() {
        if (size() == 0) {
            return null;
        }
        return get(size() - 1);
    }

    void addRows(ResultSet resultSet) {
        try {
            while (resultSet.next()) {
                add(this.table.newInstance(new Row(resultSet)));
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
