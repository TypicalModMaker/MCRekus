package dev.isnow.mcrekus.module.impl.essentials.command;

import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Named;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.SuggestionProvider;
import dev.velix.imperat.annotations.Usage;
import dev.velix.imperat.command.parameters.CommandParameter;
import dev.velix.imperat.context.SuggestionContext;
import dev.velix.imperat.resolvers.SuggestionResolver;
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.A;

@Command("playsong")
@Description("Command to play a song")
@Permission("mcrekus.playsong")
@SuppressWarnings("unused")
public class PlaySongCommand extends ModuleAccessor<EssentialsModule> {

    public PlaySongCommand() {
        super(EssentialsModule.class);

        MCRekus.getInstance().getCommandManager().registerCompletion("song", new SongResolver());
    }

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

class SongResolver implements SuggestionResolver<BukkitSource> {

    @Override
    public Collection<String> autoComplete(SuggestionContext<BukkitSource> context,
            CommandParameter<BukkitSource> parameter) {
        return List.of(new File("plugins/MCRekus/songs/").listFiles()).stream()
                .map(file -> file.getName().replace(".nbs", ""))
                .toList();
    }
}