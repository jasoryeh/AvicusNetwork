package net.avicus.libraries.quest.model;

import net.avicus.libraries.quest.database.DatabaseException;
import net.avicus.libraries.quest.query.Delete;
import net.avicus.libraries.quest.query.Filter;
import net.avicus.libraries.quest.query.Filterable;
import net.avicus.libraries.quest.query.Operator;

public class ModelDelete<M extends Model> implements Filterable {

    private final Table<M> table;
    private final Delete delete;

    public ModelDelete(Table<M> table) {
        this.table = table;
        this.delete = new Delete(table.getDatabase(), table.getName());
    }

    @Override
    public ModelDelete<M> where(String field, Object value) {
        return this.where(field, value, Operator.EQUALS);
    }

    @Override
    public ModelDelete<M> where(String field, Object value, Operator operator) {
        return this.where(new Filter(field, value, operator));
    }

    @Override
    public ModelDelete<M> where(Filter filter) {
        this.delete.where(filter);
        return this;
    }

    public void execute() throws DatabaseException {
        this.delete.execute();
    }
}
