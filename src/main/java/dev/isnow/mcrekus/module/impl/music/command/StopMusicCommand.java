package dev.isnow.mcrekus.module.impl.music.command;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.music.MusicModule;
import dev.isnow.mcrekus.module.impl.music.config.MusicConfig;
import dev.isnow.mcrekus.module.impl.music.tracker.MusicTracker;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.Usage;
import org.bukkit.entity.Player;

@Command("stopmusic")
@Description("Command to stop the music")
@Permission("mcrekus.stopmusic")
@SuppressWarnings("unused")
public class StopMusicCommand extends ModuleAccessor<MusicModule> {

    @Usage
    public void execute(final BukkitSource source) {
        final Player player = source.asPlayer();

        final MusicTracker tracker = getModule().getMusicTracker();
        final MusicConfig config = getModule().getConfig();

        if(!tracker.isPlaying(player)) {
            source.reply(ComponentUtil.deserialize(config.getNoSongPlayingMessage()));
            return;
        }

        tracker.stopMusic(player);
        source.reply(ComponentUtil.deserialize(config.getSongStoppedMessage()));
    }
}
