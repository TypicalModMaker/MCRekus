package dev.isnow.mcrekus.module.impl.christmas;

import com.github.retrooper.packetevents.PacketEvents;
import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.model.SoundCategory;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.Module;
import dev.isnow.mcrekus.module.impl.christmas.config.ChristmasConfig;
import dev.isnow.mcrekus.module.impl.christmas.packet.PacketMoveListener;
import dev.isnow.mcrekus.module.impl.model.ModelModule;
import dev.isnow.mcrekus.module.impl.model.tracker.TrackedModel;
import dev.isnow.mcrekus.module.impl.music.MusicModule;
import dev.isnow.mcrekus.module.impl.music.ParsedSong;
import dev.isnow.mcrekus.module.impl.music.tracker.CustomSongPlayer;
import dev.isnow.mcrekus.module.impl.music.util.CustomRadioPlayer;
import dev.isnow.mcrekus.module.impl.pumpkins.config.PumpkinsConfig;
import dev.isnow.mcrekus.module.impl.spawnprotection.SpawnProtectionModule;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class ChristmasModule extends Module<ChristmasConfig> {

    private CustomSongPlayer christmasSongPlayer;
    private final HashMap<Player, TrackedModel> trackedHats = new HashMap<>();

    public ChristmasModule() {
        super("Christmas", ModelModule.class, MusicModule.class, SpawnProtectionModule.class);
    }

    @Override
    public void onEnable(final MCRekus plugin) {
        registerListeners("event");
        registerCommands("commands");

        final ChristmasConfig config = getConfig();

        final HashMap<String, ParsedSong> parsedSongs = MCRekus.getInstance().getModuleManager().getModuleByClass(MusicModule.class).getSongs();

        final List<Song> songs = new ArrayList<>(
                parsedSongs.values().stream()
                        .filter(parsedSong -> config.getSongs().contains(parsedSong.getName()))
                        .map(ParsedSong::getSong).toList());

        final Playlist spawnPlaylist = new Playlist(songs.toArray(new Song[0]));

        final CustomRadioPlayer radioSongPlayer = new CustomRadioPlayer(parsedSongs.values().stream().toList().getFirst(), spawnPlaylist);

        radioSongPlayer.setRepeatMode(RepeatMode.ALL);
        radioSongPlayer.setPlaying(true);
        radioSongPlayer.playSong(0);
        christmasSongPlayer = new CustomSongPlayer(radioSongPlayer, false);

        PacketEvents.getAPI().getEventManager().registerListener(new PacketMoveListener());
    }

    @Override
    public void onDisable(final MCRekus plugin) {
        unRegisterListeners();
        unRegisterCommands();
    }
}
