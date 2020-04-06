package net.avicus.libraries.quest.model;

import lombok.ToString;
import net.avicus.libraries.quest.database.DatabaseException;
import net.avicus.libraries.quest.query.RowIterator;

import java.sql.ResultSet;
import java.util.Iterator;

@ToString
public class ModelIterator<M extends Model> implements Iterator<M> {

    private final Table<M> table;
    private final RowIterator rowIterator;

    public ModelIterator(Table<M> table, ResultSet resultSet) {
        this.table = table;
        this.rowIterator = new RowIterator(resultSet);
    }

    @Override
    public boolean hasNext() {
        return this.rowIterator.hasNext();
    }

    @Override
    public M next() throws DatabaseException {
        return this.table.newInstance(this.rowIterator.next());
    }
}
