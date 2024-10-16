package dev.isnow.mcrekus.module.impl.essentials.command.home;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.data.HomeData;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import org.bukkit.entity.Player;

@CommandAlias("delhome|usundom")
@Description("Command to delete a home")
@CommandPermission("mcrekus.delhome")
@SuppressWarnings("unused")
public class DelHomeCommand extends BaseCommand {

    private final ModuleAccessor<EssentialsModule> moduleAccessor = new ModuleAccessor<>(EssentialsModule.class);

    @Default
    @CommandCompletion("[nazwa]")
    public void execute(Player player, String[] args) {
        final EssentialsConfig config = moduleAccessor.getModule().getConfig();

        if(args.length == 0) {
            player.sendMessage(ComponentUtil.deserialize(config.getDelHomeMessage()));
            return;
        }

        MCRekus.getInstance().getDatabaseManager().getUserAsync(player, (session, data) -> {
            if(data == null) {
                player.sendMessage(ComponentUtil.deserialize("&cWystąpił błąd podczas ładowania danych gracza. Spróbuj ponownie później."));
                return;
            }

            final String homeName = String.join(" ", args);

            final HomeData home = data.getHomeLocations().get(homeName);

            if(home == null) {
                player.sendMessage(ComponentUtil.deserialize(config.getDelHomeNotFoundMessage(), null, "%home%", homeName));
                return;
            }

            data.getHomeLocations().remove(homeName);
            player.sendMessage(ComponentUtil.deserialize(config.getDelHomeMessage(), null, "%home%", homeName));

            data.save(session);
        });
    }
}
