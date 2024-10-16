package dev.isnow.mcrekus.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.BukkitCommandCompletionContext;
import co.aikar.commands.CommandCompletions;
import co.aikar.commands.PaperCommandManager;
import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.command.impl.RekusCommand;
import dev.velix.imperat.BukkitImperat;

public class CommandManager {

    private final PaperCommandManager internalCommandManager;

    public CommandManager(final MCRekus plugin) {
        BukkitImperat commandManager = BukkitImperat.create(plugin);

        internalCommandManager = new PaperCommandManager(plugin);
        internalCommandManager.getLocales().setDefaultLocale(plugin.getConfigManager().getGeneralConfig().getCommandsLocale().getJavaLocale());

        internalCommandManager.registerCommand(new RekusCommand());
    }

    public void registerCommand(final BaseCommand baseCommand) {
        internalCommandManager.registerCommand(baseCommand);
    }

    public boolean unRegisterCommand(final BaseCommand baseCommand) {
        internalCommandManager.unregisterCommand(baseCommand);

        return true;
    }

    public void registerCompletion(final String completion, CommandCompletions.AsyncCommandCompletionHandler<BukkitCommandCompletionContext> handler) {
        internalCommandManager.getCommandCompletions().registerAsyncCompletion(completion, handler);
    }
}
