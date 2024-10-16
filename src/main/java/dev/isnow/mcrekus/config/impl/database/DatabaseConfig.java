package dev.isnow.mcrekus.config.impl.database;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import dev.isnow.mcrekus.config.MasterConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
public class DatabaseConfig extends MasterConfig {
    @Comment({"", "The host of the database."})
    private String host = "127.0.0.1";
    @Comment({"", "The username of the database."})
    private String username = "root";
    @Comment({"", "The password of the database."})
    private String password = "";
    @Comment({"", "The name of the database."})
    private String database = "mcrekus";

    @Comment({"", "The type of the database.", "Available types: MYSQL, MARIADB, H2"})
    private DatabaseTypeConfig databaseType = DatabaseTypeConfig.H2;

    @Comment({"", "Migrate old json files to the database."})
    private boolean migrate = false;

    public DatabaseConfig() {
        super("database");
    }
}