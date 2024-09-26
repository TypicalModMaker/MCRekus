package dev.isnow.mcrekus.config.impl.database;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import dev.isnow.mcrekus.config.MasterConfig;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.YamlConfiguration;

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
    private DatabaseType databaseType = DatabaseType.H2;

    public DatabaseConfig() {
        super("database");
    }
}