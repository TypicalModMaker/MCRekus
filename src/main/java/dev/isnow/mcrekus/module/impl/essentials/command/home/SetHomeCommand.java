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
import dev.isnow.mcrekus.util.PermissionUtil;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import org.bukkit.entity.Player;

@CommandAlias("sethome|ustawdom")
@Description("Command to set a home")
@CommandPermission("mcrekus.sethome")
@SuppressWarnings("unused")
public class SetHomeCommand extends BaseCommand {

    private final ModuleAccessor<EssentialsModule> moduleAccessor = new ModuleAccessor<>(EssentialsModule.class);

    @Default
    @CommandCompletion("[nazwa]")
    public void execute(Player player, String[] args) {
        final EssentialsConfig config = moduleAccessor.getModule().getConfig();

        if(args.length == 0) {
            player.sendMessage(ComponentUtil.deserialize(config.getSetHomeUsageMessage()));
            return;
        }

        MCRekus.getInstance().getDatabaseManager().getUserAsync(player, (session, data) -> {
            if(data == null) {
                player.sendMessage(ComponentUtil.deserialize("&cWystąpił błąd podczas ładowania danych gracza. Spróbuj ponownie później."));
                return;
            }

            final int maxHomes = PermissionUtil.getMaxAllowedHomes(moduleAccessor.getModule().getConfig().getMaxAllowedHomesByDefault(), player);

            if (data.getHomeLocations().size() >= maxHomes) {
                player.sendMessage(ComponentUtil.deserialize(config.getSetHomeAtLimitMessage(), null, "%max%", String.valueOf(maxHomes)));
                return;
            }

            final String homeName = String.join(" ", args);

            final HomeData home = data.getHomeLocations().get(homeName);

            if (home != null) {
                home.setLocation(new RekusLocation(player.getLocation()));

                player.sendMessage(ComponentUtil.deserialize(config.getSetHomeUpdatedMessage(), null, "%home%", homeName));
                player.playSound(player.getLocation(), config.getSetHomeSound(), 1.0F, 1.0F);
            } else {
                final HomeData homeData = new HomeData(homeName, new RekusLocation(player.getLocation()), data);

                data.getHomeLocations().put(homeName, homeData);
                player.sendMessage(ComponentUtil.deserialize(config.getSetHomeCreatedMessage(), null, "%home%", homeName));
                player.playSound(player.getLocation(), config.getSetHomeSound(), 1.0F, 1.0F);
            }

            MCRekus.getInstance().getDatabaseManager().saveUser(data, session);
        });
    }
}
