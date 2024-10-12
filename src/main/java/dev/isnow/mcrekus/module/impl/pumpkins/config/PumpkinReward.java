package dev.isnow.mcrekus.module.impl.pumpkins.config;

import de.exlll.configlib.Configuration;
import dev.isnow.mcrekus.util.Range;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Configuration
public class PumpkinReward {
    private Range range;
    private List<String> commands;
}
