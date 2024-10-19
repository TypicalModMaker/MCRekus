package dev.isnow.mcrekus.module.impl.casino.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.casino.CasinoModule;
import dev.isnow.mcrekus.module.impl.casino.machine.CasinoMachine;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@CommandAlias("setupcasino")
@Description("Command to setup casino")
@CommandPermission("mcrekus.setupcasino")
@SuppressWarnings("unused")
public class CasinoCommand extends BaseCommand {

    private final ModuleAccessor<CasinoModule> moduleAccessor = new ModuleAccessor<>(CasinoModule.class);

    @Default
    public void execute(Player player, String[] args) {
        final Location location = player.getLocation().clone();

        final RekusLocation baseLocation = RekusLocation.fromBukkitLocationTrimmed(location);

        moduleAccessor.getModule().getConfig().getLocations().add(baseLocation);
        moduleAccessor.getModule().getConfig().save();

        moduleAccessor.getModule().getCasinoMachines().put(baseLocation, CasinoMachine.setupMachine(moduleAccessor.getModule(), baseLocation.toBukkitLocation()));

        player.sendMessage(ComponentUtil.deserialize("&aCasino machine has been setup at your location!"));
    }
}
