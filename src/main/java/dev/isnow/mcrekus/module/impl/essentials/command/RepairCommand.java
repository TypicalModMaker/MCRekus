package dev.isnow.mcrekus.module.impl.essentials.command;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.cooldown.Cooldown;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Named;
import dev.velix.imperat.annotations.Optional;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.Suggest;
import dev.velix.imperat.annotations.Usage;
import java.util.List;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

@Command("repair|napraw|rep")
@Description("Command to repair items")
@Permission("mcrekus.repair")
@SuppressWarnings("unused")
public class RepairCommand extends ModuleAccessor<EssentialsModule> {

    private final Cooldown<UUID> cooldown = new Cooldown<>(getModule().getConfig().getRepairCooldown() * 1000L);

    @Usage
    @Async
    public void execute(final BukkitSource source, @Named("action") @Suggest({"hand", "all"}) @Optional final String action) {
        final EssentialsConfig config = getModule().getConfig();

        final Player player = source.asPlayer();

        final String cooldownTime = cooldown.isOnCooldown(player.getUniqueId());
        if(!player.hasPermission("mcrekus.repair.bypass") && !cooldownTime.equals("-1")) {
            source.reply(ComponentUtil.deserialize(config.getRepairCooldownMessage(), null, "%time%", cooldownTime));
            return;
        }

        if(action == null || action.isEmpty()) {
            repairInHand(player, config);
            return;
        }

        if(action.equalsIgnoreCase("hand")) {
            repairInHand(player, config);
            return;
        }

        if(!player.hasPermission("mcrekus.repair.all")) {
            source.reply(ComponentUtil.deserialize(config.getRepairNoPermissionMessage()));
            return;
        }

        for(final ItemStack item : player.getInventory().getContents()) {
            if(item == null) continue;
            if(!(item.getItemMeta() instanceof Damageable damageable)) continue;

            damageable.setDamage(0);
            item.setItemMeta(damageable);
        }

        source.reply(ComponentUtil.deserialize(config.getRepairAllMessage()));
        player.playSound(player.getLocation(), config.getRepairSound(), 1.0F, 1.0F);

        cooldown.addCooldown(player.getUniqueId());
    }

    private void repairInHand(Player player, EssentialsConfig config) {
        final ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if(!(itemInHand.getItemMeta() instanceof Damageable damageable)) {
            player.sendMessage(ComponentUtil.deserialize(config.getRepairItemNotHoldingMessage()));
            return;
        }

        player.sendMessage(ComponentUtil.deserialize(config.getRepairItemMessage()));
        player.playSound(player.getLocation(), config.getRepairSound(), 1.0F, 1.0F);

        damageable.setDamage(0);
        itemInHand.setItemMeta(damageable);
        cooldown.addCooldown(player.getUniqueId());
    };
}
