package dev.isnow.mcrekus.module.impl.customevents.command;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.customevents.CustomEventsModule;
import dev.isnow.mcrekus.module.impl.customevents.config.CustomEventsConfig;
import dev.isnow.mcrekus.module.impl.customevents.event.CustomEvent;
import dev.isnow.mcrekus.module.impl.customevents.event.CustomEventManager;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Named;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.Suggest;
import dev.velix.imperat.annotations.SuggestionProvider;
import dev.velix.imperat.annotations.Usage;
import dev.velix.imperat.command.parameters.CommandParameter;
import dev.velix.imperat.context.SuggestionContext;
import dev.velix.imperat.resolvers.SuggestionResolver;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.bukkit.entity.Player;

@Command("event")
@Description("Command to manage events")
@Permission("mcrekus.event")
@SuppressWarnings("unused")
public class EventCommand extends ModuleAccessor<CustomEventsModule> {

    @Async
    @Usage
    public void usage(final BukkitSource source) {
        final CustomEventsConfig config = getModule().getConfig();

        source.reply(ComponentUtil.deserialize(config.getEventCommandUsage()));
    }

    @Usage
    @Async
    public void execute(final BukkitSource source, @Named("toggle") @Suggest({"start", "stop"}) final String toggle, @Named("event") @SuggestionProvider("event") final String event) {
        final CustomEventsConfig config = getModule().getConfig();

        final CustomEventManager eventManager = getModule().getCustomEventManager();

        switch (toggle.toLowerCase()) {
            case "start":
                if (eventManager.getCurrentEvent() != null) {
                    source.reply(ComponentUtil.deserialize(config.getEventAlreadyRunningMessage()));
                    return;
                }

                final CustomEvent foundEvent = eventManager.getEventByName(event);

                if (foundEvent == null) {
                    source.reply(ComponentUtil.deserialize(config.getEventNotFoundMessage()));
                    return;
                }

                eventManager.startEvent(foundEvent);
                break;
            case "stop":
                if (eventManager.getCurrentEvent() == null) {
                    source.reply(ComponentUtil.deserialize(config.getEventNotRunningMessage()));
                    return;
                }

                eventManager.stopEvent();
                break;
            default:
                source.reply(ComponentUtil.deserialize(config.getEventCommandUsage()));
                break;
        }
    }
}

