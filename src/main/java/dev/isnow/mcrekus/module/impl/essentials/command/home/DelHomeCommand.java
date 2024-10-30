package dev.isnow.mcrekus.module.impl.essentials.command.home;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.data.HomeData;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Greedy;
import dev.velix.imperat.annotations.Named;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.Suggest;
import dev.velix.imperat.annotations.Usage;
import org.bukkit.entity.Player;

@Command({"delhome", "usundom"})
@Description("Command to delete a home")
@Permission("mcrekus.delhome")
@SuppressWarnings("unused")
public class DelHomeCommand extends ModuleAccessor<EssentialsModule> {

    @Async
    @Usage
    public void executeDefault(final BukkitSource source) {
        final EssentialsConfig config = getModule().getConfig();

        source.reply(ComponentUtil.deserialize(config.getDelHomeUsageMessage()));
    }

    @Async
    @Usage
    public void execute(final BukkitSource source, @Named("name") @Suggest("nazwa") @Greedy final String name) {
        final EssentialsConfig config = getModule().getConfig();
        final Player player = source.asPlayer();

        MCRekus.getInstance().getDatabaseManager().getUserAsync(player, (session, data) -> {
            if(data == null) {
                player.sendMessage(ComponentUtil.deserialize("&cWystąpił błąd podczas ładowania danych gracza. Spróbuj ponownie później."));
                return;
            }

            final HomeData home = data.getHomeLocations().get(name);

            if(home == null) {
                player.sendMessage(ComponentUtil.deserialize(config.getDelHomeNotFoundMessage(), null, "%home%", name));
                return;
            }

            data.getHomeLocations().remove(name);
            player.sendMessage(ComponentUtil.deserialize(config.getDelHomeMessage(), null, "%home%", name));

            data.save(session);
        });
    }
}
