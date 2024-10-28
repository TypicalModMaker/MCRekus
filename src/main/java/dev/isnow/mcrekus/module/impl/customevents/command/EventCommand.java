package dev.isnow.mcrekus.module.impl.customevents.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.customevents.CustomEventsModule;
import dev.isnow.mcrekus.module.impl.customevents.config.CustomEventsConfig;
import dev.isnow.mcrekus.module.impl.customevents.event.CustomEvent;
import dev.isnow.mcrekus.module.impl.customevents.event.CustomEventManager;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import org.bukkit.entity.Player;

@CommandAlias("event")
@Description("Command to manage events")
@CommandPermission("mcrekus.event")
@SuppressWarnings("unused")
public class EventCommand extends BaseCommand {

    private final ModuleAccessor<CustomEventsModule> moduleAccessor = new ModuleAccessor<>(CustomEventsModule.class);

    @Default
    @CommandCompletion("start|stop <event>")
    public void execute(Player player, String[] args) {
        final CustomEventsConfig config = moduleAccessor.getModule().getConfig();


        if (args.length < 1) {
            player.sendMessage(ComponentUtil.deserialize(config.getEventCommandUsage()));
            return;
        }

        final CustomEventManager eventManager = moduleAccessor.getModule().getCustomEventManager();

        switch (args[0].toLowerCase()) {
            case "start":
                if (eventManager.getCurrentEvent() != null) {
                    player.sendMessage(ComponentUtil.deserialize(config.getEventAlreadyRunningMessage()));
                    return;
                }

                final CustomEvent event = eventManager.getEventByName(args[1]);

                if (event == null) {
                    player.sendMessage(ComponentUtil.deserialize(config.getEventNotFoundMessage()));
                    return;
                }

                eventManager.startEvent(event);
                break;
            case "stop":
                if (eventManager.getCurrentEvent() == null) {
                    player.sendMessage(ComponentUtil.deserialize(config.getEventNotRunningMessage()));
                    return;
                }

                eventManager.stopEvent();
                break;
            default:
                player.sendMessage(ComponentUtil.deserialize(config.getEventCommandUsage()));
                break;
        }
    }
}
