package net.avicus.quest.model;

import net.avicus.quest.database.DatabaseException;
import net.avicus.quest.query.Filter;
import net.avicus.quest.query.Filterable;
import net.avicus.quest.query.Operator;
import net.avicus.quest.query.Update;

public class ModelUpdate<M extends Model> implements Filterable {

    private final Table<M> table;
    private final Update update;

    public ModelUpdate(Table<M> table) {
        this.table = table;
        this.update = new Update(table.getDatabase(), table.getName());
    }

    public ModelUpdate<M> set(String field, Object value) {
        this.update.set(field, value);
        return this;
    }

    @Override
    public ModelUpdate<M> where(String field, Object value) {
        return this.where(field, value, Operator.EQUALS);
    }

    @Override
    public ModelUpdate<M> where(String field, Object value, Operator operator) {
        return this.where(new Filter(field, value, operator));
    }

    @Override
    public ModelUpdate<M> where(Filter filter) {
        this.update.where(filter);
        return this;
    }

    public void execute() throws DatabaseException {
        this.update.execute();
    }
}
