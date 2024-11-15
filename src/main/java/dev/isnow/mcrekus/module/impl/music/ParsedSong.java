package dev.isnow.mcrekus.module.impl.music;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import lombok.Data;

@Data
public class ParsedSong {
    private final Song song;
    private final String name;
    private final String displayName;
    private final String author;
}
