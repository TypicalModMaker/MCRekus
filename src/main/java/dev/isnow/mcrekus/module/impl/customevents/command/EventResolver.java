package dev.isnow.mcrekus.module.impl.customevents.command;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.customevents.CustomEventsModule;
import dev.isnow.mcrekus.module.impl.customevents.event.CustomEvent;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.command.parameters.CommandParameter;
import dev.velix.imperat.context.SuggestionContext;
import dev.velix.imperat.resolvers.SuggestionResolver;
import java.util.Collection;

public class EventResolver extends ModuleAccessor<CustomEventsModule> implements SuggestionResolver<BukkitSource> {

    @Override
    public Collection<String> autoComplete(SuggestionContext<BukkitSource> context,
            CommandParameter<BukkitSource> parameter) {
        return getModule().getCustomEventManager().getEvents().stream().map(CustomEvent::getName).toList();
    }
}
