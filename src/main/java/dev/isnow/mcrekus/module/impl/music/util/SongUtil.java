package dev.isnow.mcrekus.module.impl.music.util;

import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

@UtilityClass
public class SongUtil {
    public CustomRadioPlayer playSong(final Song song, final Player player) {
        final CustomRadioPlayer rsp = new CustomRadioPlayer(song);
        rsp.setRepeatMode(RepeatMode.NO);
        rsp.addPlayer(player);
        rsp.setPlaying(true);

        return rsp;
    }

}
