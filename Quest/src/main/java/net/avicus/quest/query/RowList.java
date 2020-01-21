package net.avicus.quest.query;

import lombok.ToString;
import net.avicus.quest.database.DatabaseException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@ToString
public class RowList extends ArrayList<Row> {

    public Row first() {
        return get(0);
    }

    public Row last() {
        return get(size() - 1);
    }

    void addRows(ResultSet resultSet) {
        try {
            while (resultSet.next()) {
                add(new Row(resultSet));
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
