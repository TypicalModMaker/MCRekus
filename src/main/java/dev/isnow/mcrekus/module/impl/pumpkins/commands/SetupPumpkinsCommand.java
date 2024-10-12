package dev.isnow.mcrekus.module.impl.pumpkins.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.database.DatabaseManager;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.pumpkins.PumpkinsModule;
import dev.isnow.mcrekus.module.impl.pumpkins.config.PumpkinsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import org.bukkit.entity.Player;

@CommandAlias("pumpkin")
@Description("Command to setup pumpkins")
@CommandPermission("mcrekus.pumpkin")
@SuppressWarnings("unused")
public class SetupPumpkinsCommand extends BaseCommand {

    private final ModuleAccessor<PumpkinsModule> moduleAccessor = new ModuleAccessor<>(PumpkinsModule.class);

    @Default
    @CommandCompletion("setup|resetProgress")
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(ComponentUtil.deserialize("&cUsage: /pumpkins <setup/resetProgress>"));
            return;
        }

        final PumpkinsConfig config = moduleAccessor.getModule().getConfig();

        if(args[0].equalsIgnoreCase("resetProgress")) {
            final DatabaseManager databaseManager = MCRekus.getInstance().getDatabaseManager();
            databaseManager.getUserAsync(player, (session, user) -> {
                user.getPumpkins().clear();

                databaseManager.saveUser(user, session);
                player.sendMessage(ComponentUtil.deserialize("&aProgress reset!"));
            });
        } else if(args[0].equalsIgnoreCase("setup")) {
            if (moduleAccessor.getModule().getSetupPlayers().contains(player)) {
                moduleAccessor.getModule().getSetupPlayers().remove(player);
                player.sendMessage(ComponentUtil.deserialize(config.getSetupPumpkinsOFF()));
            } else {
                moduleAccessor.getModule().getSetupPlayers().add(player);
                player.sendMessage(ComponentUtil.deserialize(config.getSetupPumpkinsON()));
            }
        }
    }
}
