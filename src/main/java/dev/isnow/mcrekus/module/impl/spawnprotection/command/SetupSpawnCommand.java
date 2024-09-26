package dev.isnow.mcrekus.module.impl.spawnprotection.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawnprotection.SpawnProtectionModule;
import dev.isnow.mcrekus.util.ComponentUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@CommandAlias("setupspawn")
@Description("Command to setup spawn cuboid")
@CommandPermission("mcrekus.setupspawn")
@SuppressWarnings("unused")
public class SetupSpawnCommand extends BaseCommand {

    public static final Component SELECTION_WAND = ComponentUtil.deserialize("&c&lSelect Spawn Cuboid");

    private final ModuleAccessor<SpawnProtectionModule> moduleAccessor = new ModuleAccessor<>(SpawnProtectionModule.class);

    @Default
    public void execute(Player player, String[] args) {
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
