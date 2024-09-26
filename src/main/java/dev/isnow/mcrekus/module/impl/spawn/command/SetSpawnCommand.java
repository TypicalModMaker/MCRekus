package dev.isnow.mcrekus.module.impl.spawn.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawn.SpawnModule;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@CommandAlias("setspawn")
@Description("Command to set spawn")
@CommandPermission("mcrekus.setspawn")
@SuppressWarnings("unused")
public class SetSpawnCommand extends BaseCommand {

    private final ModuleAccessor<SpawnModule> moduleAccessor = new ModuleAccessor<>(SpawnModule.class);

    @Default
    public void execute(Player player, String[] args) {
        moduleAccessor.getModule().getConfig().setSpawnLocation(RekusLocation.fromBukkitLocation(player.getLocation()));

        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        player.sendMessage(ComponentUtil.deserialize(moduleAccessor.getModule().getConfig().getSpawnSetSuccessfullyMessage()));

        moduleAccessor.getModule().getConfig().save();
    }
}
