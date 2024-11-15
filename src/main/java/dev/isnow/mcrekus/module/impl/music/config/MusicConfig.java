package dev.isnow.mcrekus.module.impl.music.config;

import com.google.common.collect.ImmutableMap;
import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import dev.isnow.mcrekus.module.ModuleConfig;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
public class MusicConfig extends ModuleConfig {
    public MusicConfig() {
        super("config", "Music");
    }

    @Comment({"", "Song command usage message"})
    private String commandUsageMessage = "[P] Usage: /playsong <song>";

    @Comment({"", "Song not found message"})
    private String songNotFoundMessage = "[P] Song not found";

    @Comment({"", "Song already playing message"})
    private String songAlreadyPlayingMessage = "[P] You already have a song playing!";

    @Comment({"", "No Song playing message"})
    private String noSongPlayingMessage = "[P] You don't have a song playing!";

    @Comment({"", "Song stopped message"})
    private String songStoppedMessage = "[P] Song stopped!";

    private Map<String, SongConfig> songs = ImmutableMap.of(
        "feliznavidad", new SongConfig("feliznavidad", "Feliz Navidad", "José Feliciano"),
        "lastchristmas", new SongConfig("lastchristmas", "Last Christmas", "Wham!"),
        "looklike", new SongConfig("looklike", "It's Beginning to Look a Lot Like Christmas", "Michael Bublé")
    );
}
