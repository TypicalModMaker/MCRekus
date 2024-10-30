package dev.isnow.mcrekus.module.impl.crates.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import dev.isnow.mcrekus.module.ModuleConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
public class CratesConfig extends ModuleConfig {
    public CratesConfig() {
        super("config", "Crates");
    }

    @Comment({"", "Crates command usage message"})
    private String commandUsageMessage = "[P] /crates create|delete|givekey <name> <amount> <player|all>";
}
