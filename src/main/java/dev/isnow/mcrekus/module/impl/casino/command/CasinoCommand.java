package dev.isnow.mcrekus.module.impl.casino.command;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.casino.CasinoModule;
import dev.isnow.mcrekus.module.impl.casino.machine.CasinoMachine;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.Usage;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Command("setupcasino")
@Description("Command to setup casino")
@Permission("mcrekus.setupcasino")
@SuppressWarnings("unused")
public class CasinoCommand extends ModuleAccessor<CasinoModule> {

    @Usage
    @Async
    public void execute(final BukkitSource source) {
        final Player player = source.asPlayer();

        final Location location = player.getLocation().clone();

        final RekusLocation baseLocation = RekusLocation.fromBukkitLocationTrimmed(location);

        getModule().getConfig().getLocations().add(baseLocation);
        getModule().getConfig().save();

        getModule().getCasinoMachines().put(baseLocation, CasinoMachine.setupMachine(getModule(), baseLocation.toBukkitLocation()));

        source.reply(ComponentUtil.deserialize("&aCasino machine has been setup at your location!"));
    }
}
