package dev.isnow.mcrekus.module.impl.essentials.command.song;

import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.command.parameters.CommandParameter;
import dev.velix.imperat.context.SuggestionContext;
import dev.velix.imperat.resolvers.SuggestionResolver;
import java.io.File;
import java.util.Collection;
import java.util.List;

public class SongResolver implements SuggestionResolver<BukkitSource> {

    @Override
    public Collection<String> autoComplete(SuggestionContext<BukkitSource> context,
            CommandParameter<BukkitSource> parameter) {
        return List.of(new File("plugins/MCRekus/songs/").listFiles()).stream()
                .map(file -> file.getName().replace(".nbs", ""))
                .toList();
    }
}
