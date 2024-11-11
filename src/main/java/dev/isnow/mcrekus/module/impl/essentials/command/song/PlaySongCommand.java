package dev.isnow.mcrekus.module.impl.essentials.command.song;

import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Named;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.SuggestionProvider;
import dev.velix.imperat.annotations.Usage;
import java.io.File;

@Command("playsong")
@Description("Command to play a song")
@Permission("mcrekus.playsong")
@SuppressWarnings("unused")
public class PlaySongCommand extends ModuleAccessor<EssentialsModule> {

    @Usage
    @Async
    public void executeDefault(final BukkitSource source) {
        source.reply(ComponentUtil.deserialize("&cUsage: /playsong <song>"));
    }

    @Usage
    @Async
    public void execute(final BukkitSource source, @Named("song") @SuggestionProvider("song") final String song) {

        final Song foundSong = NBSDecoder.parse(new File("plugins/MCRekus/songs/" + song + ".nbs"));

        if(foundSong == null) {
            source.reply(ComponentUtil.deserialize("&cSong not found"));
            return;
        }

        final RadioSongPlayer rsp = new RadioSongPlayer(foundSong);
        rsp.setRepeatMode(RepeatMode.NO);
        rsp.addPlayer(source.asPlayer());
        rsp.setPlaying(true);
    }
}

