package dev.isnow.mcrekus.module.impl.music.command.playsong;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.music.MusicModule;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.command.parameters.CommandParameter;
import dev.velix.imperat.context.SuggestionContext;
import dev.velix.imperat.resolvers.SuggestionResolver;
import java.util.Collection;

public class SongResolver extends ModuleAccessor<MusicModule> implements SuggestionResolver<BukkitSource> {

    @Override
    public Collection<String> autoComplete(SuggestionContext<BukkitSource> context,
            CommandParameter<BukkitSource> parameter) {
        return getModule().getSongs().keySet();
    }
}
