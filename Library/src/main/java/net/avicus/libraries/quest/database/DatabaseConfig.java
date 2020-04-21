package net.avicus.libraries.quest.database;

import lombok.Getter;

public class DatabaseConfig {

    @Getter
    private final String host;
    @Getter
    private final String database;
    @Getter
    private final String username;
    @Getter
    private final String password;
    @Getter
    private final boolean reconnectEnabled;
    @Getter
    private final boolean unicode;
    @Getter
    private final boolean embedded;

    private DatabaseConfig(String host, String database, String username, String password,
                           boolean reconnectEnabled, boolean unicode, boolean embedded) {
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
        this.reconnectEnabled = reconnectEnabled;
        this.unicode = unicode;
        this.embedded = embedded;
    }

    public static Builder builder(String host, String database, String username, String password) {
        return new Builder(host, database, username, password);
    }

    public String getUrl() {
        StringBuilder url = new StringBuilder();
        if(!isEmbedded()) {
            // Remote database
            url.append(String.format("jdbc:mysql://%s/%s", this.host, this.database));
            url.append("?");

            if (this.reconnectEnabled) {
                url.append("autoReconnect=true&");
            }

            if (this.unicode) {
                url.append("useUnicode=yes&characterEncoding=UTF-8&");
            }
        } else {
            // local db
            url.append(String.format("jdbc:sqlite:%s.db", this.database));
        }
        return url.toString();
    }

    public static class Builder {

        private String host;
        private String database;
        private String username;
        private String password;
        private boolean reconnectEnabled;
        private boolean unicode;
        private boolean embedded;

        Builder(String host, String database, String username, String password) {
            this.host = host;
            this.database = database;
            this.username = username;
            this.password = password;
            this.reconnectEnabled = false;
            this.unicode = true;
            this.embedded = false;
        }

        public DatabaseConfig build() {
            return new DatabaseConfig(this.host, this.database, this.username, this.password,
                    this.reconnectEnabled, this.unicode, this.embedded);
        }

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder database(String database) {
            this.database = database;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder reconnect(boolean reconnectEnabled) {
            this.reconnectEnabled = reconnectEnabled;
            return this;
        }

        public Builder unicode(boolean unicode) {
            this.unicode = unicode;
            return this;
        }

        public Builder embedded(boolean embedded) {
            this.embedded = embedded;
            return this;
        }
    }
}
