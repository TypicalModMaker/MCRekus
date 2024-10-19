package dev.isnow.mcrekus.module.impl.casino.event;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.casino.CasinoModule;
import dev.isnow.mcrekus.module.impl.casino.config.CasinoConfig;
import dev.isnow.mcrekus.module.impl.casino.machine.CasinoMachine;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import org.bukkit.Tag;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class ButtonEvent extends ModuleAccessor<CasinoModule> implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onButtonClick(final PlayerInteractEvent event) {
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (event.getClickedBlock() == null) return;

        if(!Tag.BUTTONS.isTagged(event.getClickedBlock().getType())) return;

        final CasinoMachine casinoMachine = getModule().getCasinoMachines().get(RekusLocation.fromBukkitLocationTrimmed(event.getClickedBlock().getLocation().clone().subtract(1, 0, 0)));

        if (casinoMachine == null) {
            return;
        }

        final Player player = event.getPlayer();

        final CasinoConfig config = getModule().getConfig();

        if (casinoMachine.isInUse()) {
            player.sendMessage(ComponentUtil.deserialize(config.getAlreadyBeingUsedMessage()));
            return;
        }

        if (MCRekus.getInstance().getEconomy().has(player, config.getRequiredMoney())) {
            MCRekus.getInstance().getEconomy().withdrawPlayer(player, config.getRequiredMoney());
            casinoMachine.spin(player);
            player.sendMessage(ComponentUtil.deserialize("&cKręcimy skurwiela"));
        } else {
            player.sendMessage(ComponentUtil.deserialize(config.getNotEnoughMoneyMessage()));
        }
    }
}
