package dev.isnow.mcrekus.module.impl.music.tracker;

import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import dev.isnow.mcrekus.module.impl.music.util.CustomRadioPlayer;
import java.util.HashMap;
import org.bukkit.entity.Player;

public class MusicTracker {

    private final HashMap<Player, CustomSongPlayer> currentlyPlaying = new HashMap<>();

    public boolean isPlaying(final Player player) {
        final CustomSongPlayer songPlayer = currentlyPlaying.get(player);

        if (songPlayer == null) {
            return false;
        }

        return songPlayer.getPlayer().isPlaying();
    }

    public void stopMusic(final Player player) {
        final CustomSongPlayer customSongPlayer = currentlyPlaying.get(player);

        if (customSongPlayer == null) {
            return;
        }

        final SongPlayer songPlayer = customSongPlayer.getPlayer();

        songPlayer.removePlayer(player);
        if (customSongPlayer.isDestroyOnNoPlayers() && songPlayer.getPlayerUUIDs().isEmpty()) {
            songPlayer.destroy();
        }

        currentlyPlaying.remove(player);
    }

    public void trackMusic(final Player player, final CustomRadioPlayer songPlayer, final boolean destroyOnNoPlayers) {
        currentlyPlaying.put(player, new CustomSongPlayer(songPlayer, destroyOnNoPlayers));
    }

    public void trackMusic(final Player player, final CustomSongPlayer songPlayer) {
        currentlyPlaying.put(player, songPlayer);
    }

    public CustomSongPlayer getSongPlayer(final Player player) {
        return currentlyPlaying.get(player);
    }

}
