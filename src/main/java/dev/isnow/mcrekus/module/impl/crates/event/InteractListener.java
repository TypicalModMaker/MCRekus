package dev.isnow.mcrekus.module.impl.crates.event;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.util.Quaternion4f;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockAction;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.crates.CratesModule;
import dev.isnow.mcrekus.util.MathUtil;
import dev.isnow.mcrekus.util.RekusLogger;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import me.tofaa.entitylib.meta.display.ItemDisplayMeta;
import me.tofaa.entitylib.wrapper.WrapperEntity;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class InteractListener extends ModuleAccessor<CratesModule> implements Listener {
    private final Random random = new Random();
    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getClickedBlock() == null) return;
        if (event.getClickedBlock().getType() != Material.CHEST) return;

        event.setCancelled(true);

        final Location chestLocation = event.getClickedBlock().getLocation();
        final Location center = chestLocation.clone().add(0.5, 0, 0.5);

        event.getPlayer().playSound(event.getPlayer(), Sound.BLOCK_CHEST_OPEN, 1.0f, 0.7f);
        final WrapperPlayServerBlockAction wrapper = new WrapperPlayServerBlockAction(new Vector3i(chestLocation.getBlockX(), chestLocation.getBlockY(), chestLocation.getBlockZ()), 1, 1, 0);
        wrapper.setBlockType(SpigotConversionUtil.fromBukkitBlockData(chestLocation.getBlock().getBlockData()));
        PacketEvents.getAPI().getPlayerManager().sendPacket(event.getPlayer(), wrapper);
        RekusLogger.debug("Chest location: " + chestLocation);
        initializeItems(event.getPlayer(), center);
    }

    private void initializeItems(final Player player, final Location center) {
        final int itemCount = 12;
        final double initialRadius = 0.2;
        final int totalStages = 90;
        final float angleIncrement = (float) Math.toRadians(2);
        final List<WrapperEntity> displays = new ArrayList<>();

        for (int i = 0; i < itemCount; i++) {
            final double angle = i * (2 * Math.PI / itemCount);
            final Location spawnLocation = center.clone().add(initialRadius * Math.cos(angle), 0.1, initialRadius * Math.sin(angle));

            final ItemStack itemStack = new ItemStack(Material.values()[random.nextInt(Material.values().length)]);

            final WrapperEntity entity = new WrapperEntity(EntityTypes.ITEM_DISPLAY);

            final ItemDisplayMeta meta = (ItemDisplayMeta) entity.getEntityMeta();

            meta.setItem(SpigotConversionUtil.fromBukkitItemStack(itemStack));
            meta.setScale(new Vector3f(0.2f, 0.2f, 0.2f));

            entity.addViewerSilently(player.getUniqueId());
            entity.spawn(SpigotConversionUtil.fromBukkitLocation(spawnLocation));

            displays.add(entity);
        }

        final RadioSongPlayer rsp = new RadioSongPlayer(getModule().getOpen());
        rsp.addPlayer(player);
        rsp.setPlaying(true);

        new BukkitRunnable() {
            private int stage = 0;
            private boolean descending = false;

            @Override
            public void run() {
                if (stage >= totalStages && !descending) {
                    descending = true;
                    stage = 0;
                } else if (descending && stage >= 10) {

                    final WrapperEntity randomDisplay = displays.get(random.nextInt(displays.size()));

                    final ItemDisplayMeta meta = (ItemDisplayMeta) randomDisplay.getEntityMeta();

                    meta.setTransformationInterpolationDuration(10);
                    meta.setInterpolationDelay(-1);

                    meta.setTranslation(meta.getTranslation().add(0, 1, 0));
                    meta.setScale(new Vector3f(0.5f, 0.5f, 0.5f));

                    new BukkitRunnable() {
                        int spinStage = 0;
                        @Override
                        public void run() {
                            if(spinStage == 41) {
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        final ItemDisplayMeta spinTransformation = (ItemDisplayMeta) randomDisplay.getEntityMeta();

                                        spinTransformation.setTransformationInterpolationDuration(10);
                                        spinTransformation.setInterpolationDelay(-1);

                                        spinTransformation.setTranslation(spinTransformation.getTranslation().subtract(0, 1, 0));
                                        spinTransformation.setRightRotation(new Quaternion4f(0,0, 0, 1));

                                        new BukkitRunnable() {
                                            @Override
                                            public void run() {
                                                player.getInventory().addItem(SpigotConversionUtil.toBukkitItemStack(spinTransformation.getItem()));
                                                displays.forEach(WrapperEntity::remove);

                                                final WrapperPlayServerBlockAction wrapper = new WrapperPlayServerBlockAction(new Vector3i(center.getBlockX(), center.getBlockY(), center.getBlockZ()), 1, 0, 0);
                                                wrapper.setBlockType(SpigotConversionUtil.fromBukkitBlockData(center.getBlock().getBlockData()));
                                                PacketEvents.getAPI().getPlayerManager().sendPacket(player, wrapper);
                                                player.playSound(player, Sound.BLOCK_CHEST_CLOSE, 1, 0.65f);
                                            }
                                        }.runTaskLaterAsynchronously(MCRekus.getInstance(), 20L);
                                    }
                                }.runTaskLaterAsynchronously(MCRekus.getInstance(), 10L);
                                cancel();
                                return;
                            }
                            final Quaternion4f spinRotation = new Quaternion4f(0, 0, 0, 1);

                            MathUtil.rotateY(((float) Math.toRadians(spinStage * 9)), spinRotation);

                            meta.setLeftRotation(spinRotation);

                            meta.setTransformationInterpolationDuration(1);
                            spinStage++;
                        }
                    }.runTaskTimerAsynchronously(MCRekus.getInstance(), 10, 1);
                    cancel();
                    return;
                }

                for (int i = 0; i < itemCount; i++) {
                    double angle = i * (2 * Math.PI / itemCount);
                    final WrapperEntity display = displays.get(i);

                    final ItemDisplayMeta meta = (ItemDisplayMeta) display.getEntityMeta();

                    if (!descending) {
                        final Pair<Vector3f, Vector3f> updatedTransformation = getRotation(angle, meta);

                        meta.setTransformationInterpolationDuration(5);
                        meta.setInterpolationDelay(-1);

                        meta.setTranslation(updatedTransformation.getLeft());
                        meta.setScale(updatedTransformation.getRight());
                    } else {
                        final Vector3f oldTrans = meta.getTranslation();

                        final Vector3f descentTranslation = new Vector3f(
                                oldTrans.getX(),
                                oldTrans.getY() - 0.1f,
                                oldTrans.getZ()
                        );

                        meta.setTransformationInterpolationDuration(3);
                        meta.setInterpolationDelay(-1);

                        meta.setTranslation(descentTranslation);
                        meta.setScale(new Vector3f(0.2f, 0.2f, 0.2f));
                    }
                }
                stage++;
            }

            private Pair<Vector3f, Vector3f> getRotation(final double angle, final ItemDisplayMeta meta) {
                final float globalAngle = angleIncrement * stage;
                final double rotatedX = initialRadius * Math.cos(globalAngle + angle);
                final double rotatedZ = initialRadius * Math.sin(globalAngle + angle);

                final Vector3f translation = new Vector3f((float) rotatedX, 1.0f, (float) rotatedZ);

                return Pair.of(translation, new Vector3f(0.2f, 0.2f, 0.2f));
            }
        }.runTaskTimerAsynchronously(MCRekus.getInstance(), 0L, 1);
    }

}
