package dev.isnow.mcrekus.module.impl.music.config;

import de.exlll.configlib.Configuration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Configuration
public class SongConfig {
   private String file;
   private String name;
   private String author;
}
