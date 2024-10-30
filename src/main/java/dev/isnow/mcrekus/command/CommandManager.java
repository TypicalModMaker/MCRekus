package dev.isnow.mcrekus.command;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.command.impl.RekusCommand;
import dev.velix.imperat.BukkitImperat;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.command.CommandUsage;
import dev.velix.imperat.resolvers.SuggestionResolver;

public class CommandManager {

    private final BukkitImperat commandManager;

    public CommandManager(final MCRekus plugin) {
        commandManager = BukkitImperat.create(plugin);

        commandManager.registerCommand(new RekusCommand());
    }

    public void registerCommand(final Object command) {
        commandManager.registerCommand(command);
    }

    public void unRegisterCommand(final String command) {
        commandManager.unregisterCommand(command);
    }

    public void registerCompletion(final String completion, final SuggestionResolver<BukkitSource> handler) {
        commandManager.registerNamedSuggestionResolver(completion, handler);
    }
}
