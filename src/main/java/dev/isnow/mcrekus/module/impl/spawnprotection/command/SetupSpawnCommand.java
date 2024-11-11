package dev.isnow.mcrekus.module.impl.spawnprotection.command;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawnprotection.SpawnProtectionModule;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.Usage;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Command("setupspawn")
@Description("Command to setup spawn cuboid")
@Permission("mcrekus.setupspawn")
@SuppressWarnings("unused")
public class SetupSpawnCommand extends ModuleAccessor<SpawnProtectionModule> {

    public static final Component SELECTION_WAND = ComponentUtil.deserialize("&c&lSelect Spawn Cuboid");

    @Usage
    @Async
    public void execute(final BukkitSource source) {
        final Player player = source.asPlayer();

        final ItemStack selectionWand = new ItemStack(Material.GOLDEN_AXE);
        final ItemMeta selectionWandMeta = selectionWand.getItemMeta();

        selectionWand.addEnchantment(Enchantment.DURABILITY, 1);
        selectionWandMeta.displayName(SELECTION_WAND);
        selectionWandMeta.setUnbreakable(true);
        selectionWandMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        selectionWand.setItemMeta(selectionWandMeta);

        player.getInventory().addItem(selectionWand);
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
        player.sendMessage(ComponentUtil.deserialize("&aSelect spawn cuboid with this wand."));
        player.sendMessage(ComponentUtil.deserialize("&aLeft click to set first location."));
        player.sendMessage(ComponentUtil.deserialize("&aRight click to set second location."));
    }
}
