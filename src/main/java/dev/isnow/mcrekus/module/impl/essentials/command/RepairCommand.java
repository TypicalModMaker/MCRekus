package dev.isnow.mcrekus.module.impl.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.cooldown.Cooldown;
import java.util.List;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

@CommandAlias("repair|napraw|rep")
@Description("Command to repair items")
@CommandPermission("mcrekus.repair")
@SuppressWarnings("unused")
public class RepairCommand extends BaseCommand {

    private final ModuleAccessor<EssentialsModule> moduleAccessor = new ModuleAccessor<>(EssentialsModule.class);
    private final Cooldown<UUID> cooldown = new Cooldown<>(moduleAccessor.getModule().getConfig().getRepairCooldown() * 1000L);

    public RepairCommand() {
        MCRekus.getInstance().getCommandManager().registerCompletion("repair", context -> List.of("all", "hand"));
    }

    @Default
    @CommandCompletion("@repair")
    public void execute(Player player, String[] args) {
        final EssentialsConfig config = moduleAccessor.getModule().getConfig();

        final String cooldownTime = cooldown.isOnCooldown(player.getUniqueId());
        if(!player.hasPermission("mcrekus.repair.bypass") && !cooldownTime.equals("-1")) {
            player.sendMessage(ComponentUtil.deserialize(config.getRepairCooldownMessage(), null, "%time%", cooldownTime));
            return;
        }

        if(args.length == 0) {
            repairInHand(player, config);
            return;
        }

        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("hand")) {
                repairInHand(player, config);
                return;
            }

            if(!player.hasPermission("mcrekus.repair.all")) {
                player.sendMessage(ComponentUtil.deserialize(config.getRepairNoPermissionMessage()));
                return;
            }

            for(ItemStack item : player.getInventory().getContents()) {
                if(item == null) continue;
                if(!(item.getItemMeta() instanceof Damageable damageable)) continue;

                damageable.setDamage(0);
                item.setItemMeta(damageable);
            }

            player.sendMessage(ComponentUtil.deserialize(config.getRepairAllMessage()));
            player.playSound(player.getLocation(), config.getRepairSound(), 1.0F, 1.0F);

            cooldown.addCooldown(player.getUniqueId());

            return;
        }

        player.sendMessage(ComponentUtil.deserialize(config.getRepairUsageMessage()));
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
