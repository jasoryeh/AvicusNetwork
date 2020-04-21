package net.avicus.libraries.quest.query;

public interface Filterable {

    Filterable where(String field, Object value);

    Filterable where(String field, Object value, Operator operator);

    Filterable where(Filter filter);
}
