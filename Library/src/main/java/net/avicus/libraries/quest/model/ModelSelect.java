package net.avicus.libraries.quest.model;

import net.avicus.libraries.quest.database.DatabaseException;
import net.avicus.libraries.quest.query.Filter;
import net.avicus.libraries.quest.query.Filterable;
import net.avicus.libraries.quest.query.Operator;
import net.avicus.libraries.quest.query.Select;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModelSelect<M extends Model> implements Filterable {

    private final Table<M> table;
    private final Select select;

    public ModelSelect(Table<M> table) {
        this.table = table;
        this.select = new Select(table.getDatabase(), table.getName());
        this.select.columns(this.table.getColumns());
    }

    public ModelSelect<M> limit(int count) {
        this.select.limit(count);
        return this;
    }

    public ModelSelect<M> limit(int from, int to) {
        this.select.limit(from, to);
        return this;
    }

    public ModelSelect<M> order(String field) {
        this.select.order(field);
        return this;
    }

    public ModelSelect<M> order(String field, String direction) {
        this.select.order(field, direction);
        return this;
    }

    @Override
    public ModelSelect<M> where(String field, Object value) {
        return this.where(field, value, Operator.EQUALS);
    }

    @Override
    public ModelSelect<M> where(String field, Object value, Operator operator) {
        return this.where(new Filter(field, value, operator));
    }

    @Override
    public ModelSelect<M> where(Filter filter) {
        this.select.where(filter);
        return this;
    }

    public String build() {
        return this.select.build();
    }

    public ModelList<M> execute() throws DatabaseException {
        try (PreparedStatement statement = this.table.getDatabase()
                .createQueryStatement(build(), true)) {
            ModelList list = new ModelList<>(this.table);
            try (ResultSet set = statement.executeQuery()) {
                list.addRows(set);
            }
            return list;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    public ModelIterator<M> executeIterator() throws DatabaseException {
        PreparedStatement statement = this.table.getDatabase().createQueryStatement(build(), false);
        try {
            return new ModelIterator<>(this.table, statement.executeQuery());
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
