package net.avicus.quest.query;

import lombok.ToString;
import net.avicus.quest.database.DatabaseException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.NoSuchElementException;

@ToString
public class RowIterator implements Iterator<Row> {

    private final ResultSet resultSet;
    private Row current;

    public RowIterator(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    @Override
    public boolean hasNext() {
        try {
            if (this.current != null) {
                return true;
            }
            boolean next = this.resultSet.next();
            if (next) {
                this.current = new Row(this.resultSet);
            }
            return next;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Row next() throws DatabaseException {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        Row row = this.current;
        this.current = null;

        return row;
    }
}
