package dev.isnow.mcrekus.module.impl.spawn.command;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawn.SpawnModule;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.Usage;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@Command("setspawn")
@Description("Command to set spawn")
@Permission("mcrekus.setspawn")
@SuppressWarnings("unused")
public class SetSpawnCommand extends ModuleAccessor<SpawnModule> {

    @Usage
    @Async
    public void execute(final BukkitSource source) {
        final Player player = source.asPlayer();

        getModule().getConfig().setSpawnLocation(RekusLocation.fromBukkitLocation(player.getLocation()));

        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        source.reply(ComponentUtil.deserialize(getModule().getConfig().getSpawnSetSuccessfullyMessage()));

        getModule().getConfig().save();
    }
}
