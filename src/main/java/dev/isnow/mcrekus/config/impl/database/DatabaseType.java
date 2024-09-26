package dev.isnow.mcrekus.config.impl.database;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@ToString
@Getter
public enum DatabaseType {
    MYSQL("jdbc:mysql://"),
    MARIADB("jdbc:mariadb://", "?useLegacyDatetimeCode=false"),
    H2("jdbc:h2:file:");

    String prefix;
    String suffix;
    DatabaseType(final String prefix, final String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    DatabaseType(final String prefix) {
        this.prefix = prefix;
        this.suffix = "";
    }
}
