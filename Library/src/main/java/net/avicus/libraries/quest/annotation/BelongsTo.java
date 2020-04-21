package net.avicus.libraries.quest.annotation;

import net.avicus.libraries.quest.model.Model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BelongsTo {

    Class<? extends Model> model();

    String foreignKey();
}
