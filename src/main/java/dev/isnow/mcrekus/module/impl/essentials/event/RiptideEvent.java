package dev.isnow.mcrekus.module.impl.essentials.event;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.RekusLogger;
import dev.isnow.mcrekus.util.cooldown.Cooldown;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRiptideEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

public class RiptideEvent extends ModuleAccessor<EssentialsModule> implements Listener {
    private final Cooldown<UUID> riptideCooldowns = new Cooldown<>(getModule().getConfig().getRiptideCooldown() * 1000L);

    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();

        if (!player.isRiptiding()) return;

        final String cooldown = riptideCooldowns.isOnCooldown(player.getUniqueId());

        if(!cooldown.equals("-1")) return;
        riptideCooldowns.addCooldown(player.getUniqueId());
    }
    @EventHandler
    public void onRiptide(final PlayerInteractEvent event) {
        final EquipmentSlot hand = event.getHand();
        if (hand == null) return;

        final Player player = event.getPlayer();
        if (player.hasPermission("mcrekus.riptide.bypass")) return;
        if (!player.isInWater()) return;

        if (!event.getAction().name().contains("RIGHT")) return;

        final ItemStack item = event.getItem();

        if(item == null) return;
        if(item.getType() != Material.TRIDENT || !item.containsEnchantment(Enchantment.RIPTIDE)) return;

        final String cooldown = riptideCooldowns.isOnCooldown(player.getUniqueId());
        if(cooldown.equals("-1")) return;

        player.sendMessage(ComponentUtil.deserialize(getModule().getConfig().getRiptideCooldownMessage().replace("%time%", cooldown)));

        final PlayerInventory playerInventory = player.getInventory();
        switch (hand) {
            case HAND:
                playerInventory.setItemInMainHand(null);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (playerInventory.getItemInMainHand().getType().equals(Material.AIR)) {
                            playerInventory.setItemInMainHand(item);
                        } else {
                            playerInventory.setItemInOffHand(item);
                        }
                    }
                }.runTaskLater(MCRekus.getInstance(), 3);
                break;
            case OFF_HAND:
                playerInventory.setItemInOffHand(null);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (playerInventory.getItemInOffHand().getType().equals(Material.AIR)) {
                            playerInventory.setItemInOffHand(item);
                        } else {
                            playerInventory.setItemInMainHand(item);
                        }
                    }
                }.runTaskLater(MCRekus.getInstance(), 3);
        }
    }
}
