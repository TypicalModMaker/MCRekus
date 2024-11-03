package dev.isnow.mcrekus.command;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.command.impl.RekusCommand;
import dev.isnow.mcrekus.module.impl.customevents.command.EventResolver;
import dev.isnow.mcrekus.module.impl.essentials.command.gamemode.GameModeResolver;
import dev.isnow.mcrekus.module.impl.essentials.command.broadcast.BroadcastTypeResolver;
import dev.isnow.mcrekus.module.impl.essentials.command.song.SongResolver;
import dev.isnow.mcrekus.module.impl.essentials.command.speed.SpeedTypeResolver;
import dev.velix.imperat.BukkitConfigBuilder;
import dev.velix.imperat.BukkitImperat;

public class CommandManager {

    private final BukkitImperat commandManager;

    public CommandManager(final MCRekus plugin) {
        commandManager = BukkitConfigBuilder.builder(plugin)
                .namedSuggestionResolver("event", new EventResolver())
                .namedSuggestionResolver("broadcastType", new BroadcastTypeResolver())
                .namedSuggestionResolver("speedType", new SpeedTypeResolver())
                .namedSuggestionResolver("song", new SongResolver())
                .namedSuggestionResolver("gamemode", new GameModeResolver())
                .build();

        commandManager.registerCommand(new RekusCommand());
    }

    public void registerCommand(final Object command) {
        commandManager.registerCommand(command);
    }

    public void unRegisterCommand(final String command) {
        commandManager.unregisterCommand(command);
    }
}
