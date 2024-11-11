package dev.isnow.mcrekus.module.impl.model.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import dev.isnow.mcrekus.module.ModuleConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
public class ModelConfig extends ModuleConfig {
    public ModelConfig() {
        super("config", "Model");
    }

    @Comment({"", "Model command usage message"})
    private String commandUsageMessage = "[P] /model <list|place|remove|parseall> <name>";
}
