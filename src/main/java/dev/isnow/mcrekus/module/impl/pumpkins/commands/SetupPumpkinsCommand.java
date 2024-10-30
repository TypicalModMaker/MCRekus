package dev.isnow.mcrekus.module.impl.pumpkins.commands;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.database.DatabaseManager;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.pumpkins.PumpkinsModule;
import dev.isnow.mcrekus.module.impl.pumpkins.config.PumpkinsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Named;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.Suggest;
import dev.velix.imperat.annotations.Usage;
import java.util.List;
import org.bukkit.entity.Player;

@Command("pumpkin")
@Description("Command to setup pumpkins")
@Permission("mcrekus.pumpkin")
@SuppressWarnings("unused")
public class SetupPumpkinsCommand extends ModuleAccessor<PumpkinsModule> {

    @Usage
    @Async
    public void execute(final BukkitSource source, @Named("action") @Suggest({"setup", "resetProgress"}) final String action) {
        final PumpkinsConfig config = getModule().getConfig();

        if(action.equalsIgnoreCase("resetProgress")) {
            final DatabaseManager databaseManager = MCRekus.getInstance().getDatabaseManager();
            databaseManager.getUserAsync(source.asPlayer(), (session, user) -> {
                user.getPumpkins().clear();

                user.save(session);

                source.reply(ComponentUtil.deserialize("&aProgress reset!"));
            });

        } else if(action.equalsIgnoreCase("setup")) {
            final Player player = source.asPlayer();
            final List<Player> setupPlayers = getModule().getSetupPlayers();

            if (setupPlayers.contains(player)) {
                setupPlayers.remove(player);
                source.reply(ComponentUtil.deserialize(config.getSetupPumpkinsOFF()));
            } else {
                setupPlayers.add(player);
                source.reply(ComponentUtil.deserialize(config.getSetupPumpkinsON()));
            }
        }
    }
}
