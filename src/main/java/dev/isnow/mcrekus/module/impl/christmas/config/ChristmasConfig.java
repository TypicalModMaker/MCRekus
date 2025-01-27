package dev.isnow.mcrekus.module.impl.christmas.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import dev.isnow.mcrekus.module.ModuleConfig;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
public class ChristmasConfig extends ModuleConfig {
    public ChristmasConfig() {
        super("config", "Christmas");
    }

    @Comment({"", "List of songs to play"})
    private List<String> songs = List.of("feliznavidad", "lastchristmas", "look");

    @Comment({"", "Santa hat model name"})
    private String santaHatModel = "Czapa";
}