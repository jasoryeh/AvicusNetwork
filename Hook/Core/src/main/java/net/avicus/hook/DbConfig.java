package net.avicus.hook;

import lombok.Getter;
import net.avicus.compendium.config.inject.ConfigKey;
import net.avicus.compendium.config.inject.ConfigPath;
import net.avicus.libraries.quest.database.DatabaseConfig;
import net.avicus.libraries.quest.database.DatabaseConfig.Builder;
import net.avicus.magma.redis.Redis;

import java.util.Optional;

public class DbConfig {

    @ConfigPath("mysql")
    public static class MySQLConfig {

        @ConfigKey
        private static String host;

        @ConfigKey
        private static String username;

        @ConfigKey
        private static String password;

        @ConfigKey
        private static String database;

        @ConfigKey
        private static boolean embedded;

        public static DatabaseConfig create() {
            return DatabaseConfig.builder(host, database, username, password).embedded(embedded).reconnect(true).build();
        }
    }

    @ConfigPath("redis")
    public static class RedisConfig {

        @ConfigKey
        private static String host;

        @ConfigKey
        private static int database;

        @ConfigPath("auth")
        @ConfigKey
        private static Optional<Boolean> enabled;

        @Getter
        @ConfigPath("auth")
        @ConfigKey
        private static Optional<String> password;

        public static Redis.Builder create() {
            Redis.Builder builder = Redis.builder(host);
            builder.database(database);
            if (enabled.isPresent() && enabled.get() && password.isPresent()) {
                builder.password(password.get());
            }
            return builder;
        }
    }
}
