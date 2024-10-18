package dev.isnow.mcrekus.module.impl.casino.machine;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.casino.CasinoModule;
import dev.isnow.mcrekus.module.impl.casino.config.CasinoConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.FaceAttachable.AttachedFace;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.data.type.Switch.Face;
import org.bukkit.craftbukkit.v1_20_R3.block.impl.CraftButtonAbstract;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.material.Attachable;
import org.bukkit.material.Directional;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.units.qual.A;

@EqualsAndHashCode(callSuper = false)
@Data
public class CasinoMachine extends ModuleAccessor<CasinoModule> {
    private final RekusLocation baseLocation;
    private final List<Block> spinningBlocks;

    private boolean onZAxis;

    private Player spinner;

    public void spin(final Player player) {
        spinner = player;

        final CasinoConfig config = getModule().getConfig();

        final Title.Times times = Title.Times.times(Duration.ofMillis(config.getTitleFadeIn()), Duration.ofMillis(config.getTitleStay()), Duration.ofMillis(config.getTitleFadeOut()));

        new BukkitRunnable() {
            int spinCount = 0;

            @Override
            public void run() {
                if(!player.isOnline()) {
                    spinner = null;
                    cancel();
                    return;
                }

                spinCount++;
                player.playSound(player, Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1, 1.3f);

                final int modulo = spinCount % 10;
                final int test = spinCount / 10;

                for(int i = 0; i < spinningBlocks.size(); i++) {
                    if(test >= i + 1) {
                        continue;
                    }

                    final Block block = spinningBlocks.get(i);
                    block.setType(config.getBlockTypes().get(new Random().nextInt(config.getBlockTypes().size())));
                }

                if(modulo == 0) {
                    final int index = spinCount / 10;

                    player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1 + index * 0.2f);

                    final Random random = new Random();
                    spinningBlocks.get(index - 1).setType(config.getBlockTypes().get(random.nextInt(config.getBlockTypes().size())));

                    if(spinCount == 30) {
                        if(spinningBlocks.get(0).getType() == spinningBlocks.get(1).getType() && spinningBlocks.get(1).getType() == spinningBlocks.get(2).getType()) {
                            final Title title = Title.title(ComponentUtil.deserialize(config.getTitleWinningText()), ComponentUtil.deserialize(config.getTitleWinningSubtext()), times);
                            player.showTitle(title);

                            config.getWinCommands().forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName())));
                            spawnFireworks(baseLocation.toBukkitLocation().clone().add(0.5, 1, 0.5), 3);
                        } else {
                            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.5f);
                            Bukkit.getScheduler().runTaskLater(MCRekus.getInstance(), () -> {
                                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.6f);
                            }, 10L);

                            Bukkit.getScheduler().runTaskLater(MCRekus.getInstance(), () -> {
                                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.7f);
                            }, 20L);

                            Bukkit.getScheduler().runTaskLater(MCRekus.getInstance(), () -> {
                                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 2.0f);
                            }, 30L);

                            final Title title = Title.title(ComponentUtil.deserialize(config.getTitleLosingText()), ComponentUtil.deserialize(config.getTitleLosingSubtext()), times);
                            player.showTitle(title);
                        }

                        spinner = null;
                        cancel();
                    }
                }
            }
        }.runTaskTimer(MCRekus.getInstance(), 0, 5);
    }

    public boolean isInUse() {
        return spinner != null && spinner.isOnline();
    }

    private void spawnFireworks(final Location location, final int amount) {
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if(i == amount) {
                    cancel();
                    return;
                }

                i++;

                final FireworkMeta fireworkMeta = (FireworkMeta) Bukkit.getItemFactory().getItemMeta(Material.FIREWORK_ROCKET);
                fireworkMeta.setPower(2);

                final Color randomColor = Color.fromRGB(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
                fireworkMeta.addEffect(FireworkEffect.builder().withColor(randomColor).flicker(true).build());

                final Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
                firework.setFireworkMeta(fireworkMeta);
            }
        }.runTaskTimer(MCRekus.getInstance(), 0, 10);
    }

    public static CasinoMachine setupMachine(final CasinoModule module, final Location location) {
        location.getBlock().setType(Material.GRAY_CONCRETE);

        final Location buttonLocation = location.clone().add(1, 0, 0);
        buttonLocation.getBlock().setType(Material.STONE_BUTTON);

        final CraftButtonAbstract buttonAbstract = (CraftButtonAbstract) buttonLocation.getBlock().getBlockData();
        buttonAbstract.setFacing(BlockFace.EAST);
        buttonAbstract.setAttachedFace(AttachedFace.WALL);

        buttonLocation.getBlock().setBlockData(buttonAbstract);

        final Location baseLoc = location.clone().subtract(1, 0, 0).add(0, 1, 0);

        final List<Block> casinoBlocks = new ArrayList<>();

        casinoBlocks.add(baseLoc.clone().add(0, 0, 1).getBlock());
        casinoBlocks.add(baseLoc.getBlock());
        casinoBlocks.add(baseLoc.clone().subtract(0, 0, 1).getBlock());

        for(Block block : casinoBlocks) {
            block.setType(module.getConfig().getBlockTypes().get(new Random().nextInt(module.getConfig().getBlockTypes().size())));
        }

        final RekusLocation baseLocation = RekusLocation.fromBukkitLocationTrimmed(location);

        return new CasinoMachine(baseLocation, casinoBlocks);
    }
}
