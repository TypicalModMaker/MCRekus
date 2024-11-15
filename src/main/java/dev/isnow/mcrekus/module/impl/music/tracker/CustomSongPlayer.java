package dev.isnow.mcrekus.module.impl.music.tracker;

import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import dev.isnow.mcrekus.module.impl.music.util.CustomRadioPlayer;
import lombok.Data;

@Data
public class CustomSongPlayer {
    private final CustomRadioPlayer player;
    private final boolean destroyOnNoPlayers;
}
