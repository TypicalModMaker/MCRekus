package dev.isnow.mcrekus.module.impl.essentials.command.broadcast;

import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.command.parameters.CommandParameter;
import dev.velix.imperat.context.SuggestionContext;
import dev.velix.imperat.resolvers.SuggestionResolver;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class BroadcastTypeResolver implements SuggestionResolver<BukkitSource> {

    @Override
    public Collection<String> autoComplete(SuggestionContext<BukkitSource> context,
            CommandParameter<BukkitSource> parameter) {
        return Arrays.stream(BroadcastType.values())
                .map(BroadcastType::name)
                .collect(Collectors.toList());
    }
}
