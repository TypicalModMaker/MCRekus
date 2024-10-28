package dev.isnow.mcrekus.module.impl.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import java.io.File;
import org.bukkit.entity.Player;

@CommandAlias("playsong")
@Description("Command to play a song")
@CommandPermission("mcrekus.playsong")
@SuppressWarnings("unused")
public class PlaySongCommand extends BaseCommand {

    private final ModuleAccessor<EssentialsModule> moduleAccessor = new ModuleAccessor<>(EssentialsModule.class);

    @Default
    @CommandCompletion("<song>")
    public void execute(Player player, String[] args) {
        if(args.length < 0) {
            player.sendMessage(ComponentUtil.deserialize("&cUsage: /playsong <song>"));
            return;
        }

        final Song song = NBSDecoder.parse(new File("plugins/MCRekus/songs/" + args[0] + ".nbs"));

        if(song == null) {
            player.sendMessage(ComponentUtil.deserialize("&cSong not found"));
            return;
        }

        final RadioSongPlayer rsp = new RadioSongPlayer(song);
        rsp.setRepeatMode(RepeatMode.NO);
        rsp.addPlayer(player);
        rsp.setPlaying(true);
    }
}
