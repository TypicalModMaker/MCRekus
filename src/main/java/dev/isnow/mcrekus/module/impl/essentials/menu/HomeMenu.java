package dev.isnow.mcrekus.module.impl.essentials.menu;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.data.HomeData;
import dev.isnow.mcrekus.data.PlayerData;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.module.impl.essentials.teleport.TeleportManager;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.ExpiringSession;
import dev.isnow.mcrekus.util.PermissionUtil;
import io.github.mqzen.menus.base.Content;
import io.github.mqzen.menus.base.Menu;
import io.github.mqzen.menus.base.iterator.Direction;
import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.DataRegistry;
import io.github.mqzen.menus.misc.Slot;
import io.github.mqzen.menus.misc.button.Button;
import io.github.mqzen.menus.misc.button.actions.ButtonClickAction;
import io.github.mqzen.menus.misc.itembuilder.LegacyItemBuilder;
import io.github.mqzen.menus.titles.MenuTitle;
import io.github.mqzen.menus.titles.MenuTitles;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class HomeMenu extends ModuleAccessor<EssentialsModule> implements Menu {

    private final PlayerData playerData;
    private final ExpiringSession session;

    @Override
    public String getName() {
        return "Home";
    }

    @Override
    public InventoryType getMenuType() {
        return InventoryType.CHEST;
    }

    @Override
    public @NotNull MenuTitle getTitle(DataRegistry dataRegistry, Player player) {
        return MenuTitles.createModern(ComponentUtil.deserialize(getModule().getConfig().getHomeMenuTitle()));
    }

    @Override
    public @NotNull Capacity getCapacity(DataRegistry dataRegistry, Player player) {
        return Capacity.ofRows(5);
    }

    @Override
    public @NotNull Content getContent(DataRegistry dataRegistry, Player player,
            Capacity capacity) {

        ArrayList<Button> buttons = new ArrayList<>();

        for (final HomeData home : playerData.getHomeLocations().values()) {
            final ItemStack homeStack = LegacyItemBuilder.legacy(Material.LIME_BED, 1)
                    .setDisplay("&a&l" + home.getName())
                    .setLore("&fLokalizacja domu: " + ComponentUtil.formatLocation(home.getLocation(), true)).build();

            buttons.add(Button.clickable(homeStack, ButtonClickAction.plain((menuView, inventoryClickEvent) -> {
                final TeleportManager teleportManager = getModule().getTeleportManager();
                final EssentialsConfig config = getModule().getConfig();

                if(player.hasPermission("mcrekus.home.bypass")) {
                    teleportPlayer(config, home, player);
                } else {
                    inventoryClickEvent.setCancelled(true);

                    if(teleportManager.isPlayerTeleporting(player.getUniqueId())) {
                        player.sendMessage(ComponentUtil.deserialize(config.getHomeTeleportingMessage()));
                        return;
                    }

                    final BukkitTask bukkitRunnable = new BukkitRunnable() {
                        @Override
                        public void run() {
                            teleportManager.removePlayerTeleporting(player.getUniqueId());
                            teleportPlayer(config, home, player);
                        }
                    }.runTaskLater(MCRekus.getInstance(), config.getHomeDelayTime() * 20L);

                    teleportManager.addPlayerTeleporting(player.getUniqueId(), bukkitRunnable);
                    player.sendMessage(ComponentUtil.deserialize(config.getHomeTeleportingRightNowMessage(), null, "%time%", String.valueOf(config.getHomeDelayTime())));
                }
                player.closeInventory();
            })));
        }
        session.closeSession();


        final int maxHomes = PermissionUtil.getMaxAllowedHomes(getModule().getConfig().getMaxAllowedHomesByDefault(), player);

        AtomicInteger i = new AtomicInteger();
        return Content.builder(capacity)
                .iterate(Slot.of(10), Slot.of(16), Direction.RIGHT, ((content, slot) -> handleContent(buttons, maxHomes, i.incrementAndGet(), content, slot)))
                .iterate(Slot.of(18), Slot.of(18), Direction.RIGHT, ((content, slot) -> handleContent(buttons, maxHomes, i.incrementAndGet(), content, slot)))
                .iterate(Slot.of(26), Slot.of(26), Direction.RIGHT, ((content, slot) -> handleContent(buttons, maxHomes, i.incrementAndGet(), content, slot)))
                .iterate(Slot.of(28), Slot.of(34), Direction.RIGHT, ((content, slot) -> handleContent(buttons, maxHomes, i.incrementAndGet(), content, slot)))
                .build();

    }

    private void handleContent(final List<Button> buttons, final int maxHomes, final int currentIndex, final Content content, final Slot slot) {
        if(currentIndex > maxHomes) {
            content.setButton(slot, Button.clickable(LegacyItemBuilder.legacy(Material.RED_BED, 1)
                    .setDisplay("&4&lOSIĄGNIĘTO LIMIT DOMÓW")
                    .setLore("&fPotrzebujesz lepszą rangę aby uzyskać dostęp do tego domu.")
                    .build(), ButtonClickAction.plain((menuView, inventoryClickEvent) -> inventoryClickEvent.setCancelled(true))));
            return;
        }

        if (buttons.isEmpty()) {
            content.setButton(slot, Button.clickable(LegacyItemBuilder.legacy(Material.GRAY_BED, 1)
                    .setDisplay("&7&lBRAK USTAWIONEGO DOMU")
                    .setLore("&fNie posiadasz więcej domów.")
                    .build(), ButtonClickAction.plain((menuView, inventoryClickEvent) -> inventoryClickEvent.setCancelled(true))));
        } else {
            content.setButton(slot, buttons.removeFirst());
        }
    }

    private void teleportPlayer(final EssentialsConfig config, final HomeData home, final Player player) {
        player.teleport(home.getLocation().toBukkitLocation());
        player.playSound(player.getLocation(), config.getHomeTeleportSound(), 1.0F, 1.0F);
        player.sendMessage(ComponentUtil.deserialize(config.getHomeTeleportMessage(), null, "%home%", home.getName()));
    }
}
