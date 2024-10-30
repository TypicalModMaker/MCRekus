package dev.isnow.mcrekus.module.impl.essentials.command.home;


import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.data.HomeData;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.PermissionUtil;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
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

@Command({"sethome", "ustawdom"})
@Description("Command to set a home")
@Permission("mcrekus.sethome")
@SuppressWarnings("unused")
public class SetHomeCommand extends ModuleAccessor<EssentialsModule> {

    @Usage
    @Async
    public void executeDefault(final BukkitSource source) {
        final EssentialsConfig config = getModule().getConfig();

        source.reply(ComponentUtil.deserialize(config.getSetHomeUsageMessage()));
    }

    @Usage
    @Async
    public void execute(final BukkitSource source, @Named("name") @Suggest("nazwa") @Greedy final String name) {
        final EssentialsConfig config = getModule().getConfig();

        final Player player = source.asPlayer();

        MCRekus.getInstance().getDatabaseManager().getUserAsync(player, (session, data) -> {
            if(data == null) {
                source.reply(ComponentUtil.deserialize("&cWystąpił błąd podczas ładowania danych gracza. Spróbuj ponownie później."));
                return;
            }

            final int maxHomes = PermissionUtil.getMaxAllowedHomes(getModule().getConfig().getMaxAllowedHomesByDefault(), player);

            if (data.getHomeLocations().size() >= maxHomes) {
                source.reply(ComponentUtil.deserialize(config.getSetHomeAtLimitMessage(), null, "%max%", String.valueOf(maxHomes)));
                return;
            }

            final HomeData home = data.getHomeLocations().get(name);

            if (home != null) {
                home.setLocation(new RekusLocation(player.getLocation()));

                source.reply(ComponentUtil.deserialize(config.getSetHomeUpdatedMessage(), null, "%home%", name));
                player.playSound(player.getLocation(), config.getSetHomeSound(), 1.0F, 1.0F);
            } else {
                final HomeData homeData = new HomeData(name, new RekusLocation(player.getLocation()), data);

                data.getHomeLocations().put(name, homeData);
                source.reply(ComponentUtil.deserialize(config.getSetHomeCreatedMessage(), null, "%home%", name));
                player.playSound(player.getLocation(), config.getSetHomeSound(), 1.0F, 1.0F);
            }

            data.save(session);
        });
    }
}
