package net.avicus.libraries.quest.query;

import lombok.ToString;
import net.avicus.libraries.quest.QuestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ToString
public class Filter implements Filterable {

    private List<Filter> ands;
    private List<Filter> ors;

    private Optional<String> field;
    private Optional<Object> value;
    private Optional<Operator> operator;

    public Filter() {
        this.ands = new ArrayList<>(1);
        this.ors = new ArrayList<>(1);
        this.field = Optional.empty();
        this.value = Optional.empty();
    }

    public Filter(String field, Object value, Operator operator) {
        this();
        this.field = Optional.of(field);
        this.value = Optional.ofNullable(value);
        this.operator = Optional.of(operator);
    }

    public Filter(Filter filter) {
        this();
        this.ors = new ArrayList<>(filter.ors);
        this.ands = new ArrayList<>(filter.ands);
        this.field = filter.field;
        this.value = filter.value;
        this.operator = filter.operator;
    }

    public Filter(String field, Object value) {
        this(field, value, Operator.EQUALS);
    }

    @Override
    public Filter where(String field, Object value) {
        return this.where(field, value, Operator.EQUALS);
    }

    @Override
    public Filter where(String field, Object value, Operator operator) {
        return this.where(new Filter(field, value, operator));
    }

    @Override
    public Filter where(Filter filter) {
        if (this.field.isPresent() && this.value.isPresent()) {
            this.ands.add(filter);
            return this;
        }

        this.field = Optional.of(filter.field.get());
        this.value = Optional.of(filter.value.get());
        this.operator = Optional.of(filter.operator.get());
        return this;
    }

    public Filter and(String field, Object value) {
        return and(field, value, Operator.EQUALS);
    }

    public Filter and(String field, Object value, Operator operator) {
        return where(field, value, operator);
    }

    public Filter and(Filter filter) {
        return where(filter);
    }

    public Filter or(String field, Object value) {
        return or(field, value, Operator.EQUALS);
    }

    public Filter or(String field, Object value, Operator operator) {
        return or(new Filter(field, value, operator));
    }

    public Filter or(Filter filter) {
        this.ors.add(filter);
        return this;
    }

    public Optional<String> build() {
        if (!this.field.isPresent() && !this.value.isPresent() && this.ands.size() == 0
                && this.ors.size() == 0) {
            return Optional.empty();
        }

        List<Filter> ands = new ArrayList<>(this.ands);
        List<Filter> ors = new ArrayList<>(this.ors);

        String sql = "";

        if (this.field.isPresent()) {
            StringBuilder builder = new StringBuilder();
            builder.append(QuestUtils.getField(this.field.get()));
            builder.append(" ");
            builder.append(this.operator.get().getValue());
            builder.append(" ");
            builder.append(this.value.isPresent() ? QuestUtils.getValue(this.value.get()) : "NULL");
            sql += builder.toString();
        }

        for (Filter filter : ands) {
            Optional<String> str = filter.build();
            if (str.isPresent()) {
                sql += sql.length() == 0 ? str.get() : " AND " + str.get();
            }
        }

        for (Filter filter : ors) {
            Optional<String> str = filter.build();
            if (str.isPresent()) {
                sql += sql.length() == 0 ? str.get() : " OR " + str.get();
            }
        }

        return Optional.of("(" + sql + ")");
    }
}
