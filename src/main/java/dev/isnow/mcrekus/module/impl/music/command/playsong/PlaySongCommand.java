package dev.isnow.mcrekus.module.impl.music.command.playsong;

import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.music.MusicModule;
import dev.isnow.mcrekus.module.impl.music.ParsedSong;
import dev.isnow.mcrekus.module.impl.music.config.MusicConfig;
import dev.isnow.mcrekus.module.impl.music.tracker.MusicTracker;
import dev.isnow.mcrekus.module.impl.music.util.CustomRadioPlayer;
import dev.isnow.mcrekus.module.impl.music.util.SongUtil;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Named;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.SuggestionProvider;
import dev.velix.imperat.annotations.Usage;
import org.bukkit.entity.Player;

@Command("playsong")
@Description("Command to play a song")
@Permission("mcrekus.playsong")
@SuppressWarnings("unused")
public class PlaySongCommand extends ModuleAccessor<MusicModule> {

    @Usage
    @Async
    public void executeDefault(final BukkitSource source) {
        final MusicConfig config = getModule().getConfig();

        source.reply(ComponentUtil.deserialize(config.getCommandUsageMessage()));
    }

    @Usage
    @Async
    public void execute(final BukkitSource source, @Named("song") @SuggestionProvider("song") final String song) {
        final Player player = source.asPlayer();

        final MusicConfig config = getModule().getConfig();
        final MusicTracker tracker = getModule().getMusicTracker();

        if (tracker.isPlaying(player)) {
            source.reply(ComponentUtil.deserialize(config.getSongAlreadyPlayingMessage()));
            return;
        }

        final ParsedSong foundSong = getModule().getSongs().get(song);

        if(foundSong == null) {
            source.reply(ComponentUtil.deserialize(config.getSongNotFoundMessage()));
            return;
        }


        final CustomRadioPlayer songPlayer = SongUtil.playSong(foundSong.getSong(), source.asPlayer());

        tracker.trackMusic(player, songPlayer, true);

        source.reply(ComponentUtil.deserialize("[P] Playing song " + foundSong.getDisplayName() + ", Author: " + foundSong.getAuthor()));
    }
}

