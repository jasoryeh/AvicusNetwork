package net.avicus.quest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    String name() default "";

    boolean text() default false;

    int length() default -1;

    boolean nullable() default true;

    boolean primaryKey() default false;

    boolean unique() default false;

    String def() default "";

    String type() default "";
}
