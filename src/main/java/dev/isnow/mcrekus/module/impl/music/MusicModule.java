package dev.isnow.mcrekus.module.impl.music;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.Module;
import dev.isnow.mcrekus.module.impl.music.config.MusicConfig;
import dev.isnow.mcrekus.module.impl.music.config.SongConfig;
import dev.isnow.mcrekus.module.impl.music.tracker.MusicTracker;
import dev.isnow.mcrekus.util.RekusLogger;
import java.io.File;
import java.util.HashMap;
import lombok.Getter;

@Getter
public class MusicModule extends Module<MusicConfig> {
    private final HashMap<String, ParsedSong> songs = new HashMap<>();

    private final MusicTracker musicTracker = new MusicTracker();

    public MusicModule() {
        super("Music");
    }

    @Override
    public void onEnable(final MCRekus plugin) {
        RekusLogger.info("Loading songs...");
        final File songsFolder = new File("plugins/MCRekus/songs/");

        if (!songsFolder.exists()) {
            final boolean ignored = songsFolder.mkdirs();
        }

        final MusicConfig config = getConfig();

        for (final File file : songsFolder.listFiles()) {
            if (file.getName().endsWith(".nbs")) {
                final Song song = NBSDecoder.parse(file);

                final String name = file.getName().replace(".nbs", "");
                RekusLogger.info("Loaded song " + name);

                final SongConfig songConfig = config.getSongs().get(name);
                if (songConfig == null) {
                    songs.put(name, new ParsedSong(song, name, "Unknown", "Unknown"));
                    continue;
                }

                songs.put(name, new ParsedSong(song, name, songConfig.getName(), songConfig.getAuthor()));
            }
        }

        registerCommands("command");
    }

    @Override
    public void onDisable(final MCRekus plugin) {
        unRegisterCommands();
    }
}
