package net.avicus.libraries.quest.query;

import lombok.Getter;

public enum Operator {
    EQUALS("="),
    NOT_EQUAL("!="),
    LESS("<"),
    LESS_OR_EQUAL("<="),
    GREATER(">"),
    GREATER_OR_EQUAL(">="),
    IN("IN"),
    LIKE("LIKE"),
    IS("IS");

    @Getter
    private final String value;

    Operator(String value) {
        this.value = value;
    }
}
