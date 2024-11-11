package dev.isnow.mcrekus.module.impl.essentials.command.home;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.data.HomeData;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.module.impl.essentials.menu.HomeMenu;
import dev.isnow.mcrekus.module.impl.essentials.teleport.TeleportManager;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Greedy;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.SuggestionProvider;
import dev.velix.imperat.annotations.Usage;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

@Command({"home", "dom"})
@Description("Command to open home menu")
@Permission("mcrekus.home")
@SuppressWarnings("unused")
public class HomeCommand extends ModuleAccessor<EssentialsModule> {

    @Usage
    @Async
    public void execute(final BukkitSource source) {
        final Player player = source.asPlayer();
        final EssentialsConfig config = getModule().getConfig();

        source.reply(ComponentUtil.deserialize(config.getOpenHomeMessage()));

        MCRekus.getInstance().getDatabaseManager().getUserAsync(player, (session, data) -> MCRekus.getInstance().getMenuAPI().openMenu(player, new HomeMenu(data, session)));
    }

    @Usage
    @Async
    public void execute(final BukkitSource source, @SuggestionProvider("home") @Greedy final String name) {
        final Player player = source.asPlayer();
        final EssentialsConfig config = getModule().getConfig();

        MCRekus.getInstance().getDatabaseManager().getUserAsync(player, (session, data) -> {
            if (data == null) {
                player.sendMessage(ComponentUtil.deserialize("&cWystąpił błąd podczas ładowania danych gracza. Spróbuj ponownie później."));
                return;
            }

            final HomeData home = data.getHomeLocations().get(name);
            if (home == null) {
                player.sendMessage(
                        ComponentUtil.deserialize(getModule().getConfig().getDelHomeNotFoundMessage(),
                                null, "%home%", name));
                return;
            }

            final TeleportManager teleportManager = getModule().getTeleportManager();

            if(player.hasPermission("mcrekus.home.bypass")) {
                HomeMenu.teleportPlayer(config, home, player);
            } else {
                if(teleportManager.isPlayerTeleporting(player.getUniqueId())) {
                    player.sendMessage(ComponentUtil.deserialize(config.getHomeTeleportingMessage()));
                    return;
                }

                final BukkitTask bukkitRunnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        teleportManager.removePlayerTeleporting(player.getUniqueId());
                        HomeMenu.teleportPlayer(config, home, player);
                    }
                }.runTaskLater(MCRekus.getInstance(), config.getHomeDelayTime() * 20L);

                teleportManager.addPlayerTeleporting(player.getUniqueId(), bukkitRunnable);
                player.sendMessage(ComponentUtil.deserialize(config.getHomeTeleportingRightNowMessage(), null, "%time%", String.valueOf(config.getHomeDelayTime())));
            }
        });

    }
}
