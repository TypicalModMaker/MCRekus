package dev.isnow.mcrekus.module.impl.essentials.command.gamemode;

import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.command.parameters.CommandParameter;
import dev.velix.imperat.context.SuggestionContext;
import dev.velix.imperat.resolvers.SuggestionResolver;
import java.util.Collection;

public class GameModeResolver implements SuggestionResolver<BukkitSource> {

    @Override
    public Collection<String> autoComplete(SuggestionContext<BukkitSource> context,
            CommandParameter<BukkitSource> parameter) {
        return GamemodeCommand.GAME_MODES.keySet();
    }
}
