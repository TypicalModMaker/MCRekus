package dev.isnow.mcrekus.module.impl.christmas.event;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.christmas.ChristmasModule;
import dev.isnow.mcrekus.module.impl.music.MusicModule;
import dev.isnow.mcrekus.module.impl.music.tracker.CustomSongPlayer;
import dev.isnow.mcrekus.module.impl.music.tracker.MusicTracker;
import dev.isnow.mcrekus.module.impl.spawn.SpawnModule;
import dev.isnow.mcrekus.module.impl.spawnprotection.SpawnProtectionModule;
import dev.isnow.mcrekus.util.RekusLogger;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove extends ModuleAccessor<ChristmasModule> implements Listener {

    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
        final Location from = event.getFrom();
        final Location to = event.getTo();

        if(from.getBlockX() != to.getBlockX() || from.getBlockZ() != to.getBlockZ() || from.getBlockY() != to.getBlockY()) {
            final SpawnProtectionModule spawnModule = MCRekus.getInstance().getModuleManager().getModuleByClass(SpawnProtectionModule.class);

            final Player player = event.getPlayer();

            final MusicModule musicModule = MCRekus.getInstance().getModuleManager().getModuleByClass(MusicModule.class);

            final MusicTracker musicTracker = musicModule.getMusicTracker();

            final CustomSongPlayer christmasSongPlayer = getModule().getChristmasSongPlayer();

            if (spawnModule.getSpawnCuboid().isIn(player)) {
                if (!musicTracker.isPlaying(player)) {
                    christmasSongPlayer.getPlayer().addPlayer(player);
                    RekusLogger.debug("Playing Christmas song for " + player.getName());

                    musicModule.getMusicTracker().trackMusic(player, christmasSongPlayer);
                }
            } else {
                if (musicTracker.isPlaying(player)) {
                    final CustomSongPlayer songPlayer = musicTracker.getSongPlayer(player);

                    RekusLogger.debug("Stopping Christmas song for " + player.getName());
                    if (christmasSongPlayer == songPlayer) {
                        RekusLogger.debug("Stopping Christmas song for 212  23123123" + player.getName());
                        musicTracker.stopMusic(player);
                    }
                }
            }
        }
    }
}
