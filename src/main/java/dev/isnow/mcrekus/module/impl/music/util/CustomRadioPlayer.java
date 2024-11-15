package dev.isnow.mcrekus.module.impl.music.util;

import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import com.xxmicloxx.NoteBlockAPI.event.SongDestroyingEvent;
import com.xxmicloxx.NoteBlockAPI.event.SongEndEvent;
import com.xxmicloxx.NoteBlockAPI.model.Layer;
import com.xxmicloxx.NoteBlockAPI.model.Note;
import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.impl.music.MusicModule;
import dev.isnow.mcrekus.module.impl.music.ParsedSong;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.RekusLogger;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class CustomRadioPlayer extends RadioSongPlayer implements Listener {
    private ParsedSong currentSong;

    public CustomRadioPlayer(final ParsedSong firstSong, final Playlist playlist) {
        super(playlist);

        Bukkit.getPluginManager().registerEvents(this, MCRekus.getInstance());
        currentSong = firstSong;
    }

    public CustomRadioPlayer(final Song song) {
        super(song);
    }

    @Override
    public void playTick(Player player, int tick) {
        byte playerVolume = NoteBlockAPI.getPlayerVolume(player);

        for (Layer layer : song.getLayerHashMap().values()) {
            Note note = layer.getNote(tick);
            if (note == null) {
                continue;
            }

            float volume = (layer.getVolume() * (int) this.volume * (int) playerVolume * note.getVelocity()) / 100_00_00_00F;

            channelMode.play(player, player.getEyeLocation(), song, layer, note, soundCategory, volume, !enable10Octave);
        }

        if (tick % 20 == 0 && currentSong != null) {
            player.sendActionBar(ComponentUtil.deserialize("<b>&#2D2D2D🎵</b> <i><gradient:#6B6B6B:#666666>" + currentSong.getDisplayName() + " - " + currentSong.getAuthor() + "</gradient></i> [" + formatTime(tick) + "/" + formatTime(song.getLength()) + "]"));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSongEnd(final SongEndEvent event) {
        RekusLogger.debug("Song end event for " + currentSong.getDisplayName());
        if (event.getSongPlayer() != this) {
            return;
        }

        RekusLogger.debug("Song end event for zzzzz" + currentSong.getDisplayName());

        final HashMap<String, ParsedSong> songs = MCRekus.getInstance().getModuleManager().getModuleByClass(MusicModule.class).getSongs();

        final List<ParsedSong> list = songs.values().stream().toList();

        final int currentIndex = songs.values().stream().toList().indexOf(currentSong);
        if (currentIndex == songs.size() - 1) {
            currentSong = list.getFirst();
            return;
        }

        currentSong = list.get(currentIndex + 1);
    }

    @EventHandler(ignoreCancelled = true)
    public void onDestroy(final SongDestroyingEvent event) {
        RekusLogger.debug("Destroying radio player for ZZZZ" + currentSong.getDisplayName());
        if (event.getSongPlayer() != this) {
            return;
        }

        RekusLogger.debug("Destroying radio player for " + currentSong.getDisplayName());

        HandlerList.unregisterAll(this);
    }

    public static String formatTime(int ticks) {
        int totalSeconds = ticks / 20;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;

        return String.format("%d:%02d", minutes, seconds);
    }

}
