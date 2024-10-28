package dev.isnow.mcrekus.module.impl.kingdoms.event;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.kingdoms.KingdomsModule;
import dev.isnow.mcrekus.module.impl.kingdoms.champion.Champion;
import dev.isnow.mcrekus.module.impl.kingdoms.config.KingdomsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.RekusLogger;
import io.papermc.paper.event.entity.EntityMoveEvent;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.kingdoms.constants.player.KingdomPlayer;
import org.kingdoms.events.invasion.KingdomInvadeEndEvent;
import org.kingdoms.events.invasion.KingdomInvadeEvent;
import org.kingdoms.managers.entity.KingdomEntityRegistry;
import org.kingdoms.managers.entity.types.KingdomChampionEntity;
import org.kingdoms.managers.invasions.Invasion;

public class ChampionEvent extends ModuleAccessor<KingdomsModule> implements Listener {

    @EventHandler
    public void onMove(final EntityMoveEvent event) {
        final LivingEntity entity = event.getEntity();
        if(!getModule().getChampions().containsKey(entity.getEntityId())) {
            return;
        }

        getModule().getEntityThreadPool().execute(() -> {
            final Location from = event.getFrom();
            final Location to = event.getTo();


            final Champion champion = getModule().getChampions().get(entity.getEntityId());

            final KingdomsConfig config = getModule().getConfig();

            final boolean hasTeleportationTask = champion.getTeleportationTask() != null;
            if (to.getChunk().getChunkKey() != champion.getStartingLocation().getChunk().getChunkKey() && ((entity.getLocation().getY() > champion.getStartingLocation().getY() + config.getChampionTeleportYOffset()) || entity.getLocation().getY() < champion.getStartingLocation().getY() - config.getChampionTeleportYOffset())) {
                if (hasTeleportationTask) {
                    RekusLogger.debug("Cancelling tp task");
                    champion.getTeleportationTask().cancel();
                    champion.setTeleportationTask(null);
                }
                RekusLogger.debug("Teleporting champion due to y offset");
                champion.teleportChampion();

                return;
            }

            if ((from.getBlockX() >> 4) != (to.getBlockX() >> 4) || (from.getBlockZ() >> 4) != (to.getBlockZ() >> 4)) {
                if (to.getChunk().getChunkKey() == champion.getStartingLocation().getChunk().getChunkKey()) {
                    if (champion.getTeleportationTask() != null) {
                        RekusLogger.debug("Cancelling tp task | chunk re-enter");
                        champion.getTeleportationTask().cancel();
                        champion.setTeleportationTask(null);
                    }
                    return;
                }

                if (hasTeleportationTask) {
                    return;
                }

                final BukkitTask bukkitTask = new BukkitRunnable() {
                    @Override
                    public void run() {
                        RekusLogger.debug("Teleporting champion");
                        champion.setTeleportationTask(null);
                        champion.teleportChampion();
                    }
                }.runTaskLater(MCRekus.getInstance(), 20L * config.getChampionTeleportDelay());
                RekusLogger.debug("Teleport task created");
                champion.setTeleportationTask(bukkitTask);
            }
        });
    }

    @EventHandler
    public void onInvasionStart(final KingdomInvadeEvent event) {
        final Invasion invasion = event.getInvasion();

        final Creature entity = invasion.getChampion();

        if (entity == null) {
            RekusLogger.warn("Champion is null | start");
            return;
        }

        final KingdomChampionEntity championEntity = (KingdomChampionEntity) KingdomEntityRegistry.getKingdomEntity(entity);

        final BukkitTask targetTask = new BukkitRunnable() {
            @Override
            public void run() {
                final Champion champion = getModule().getChampions().get(entity.getEntityId());

                if (champion == null) {
                    return;
                }

                final List<Player> entities = champion.getAttackingPlayers().stream()
                        .sorted(Comparator.comparingDouble(o -> entity.getLocation().distance(o.getLocation())))
                        .toList();

                if(entities.isEmpty()) {
                    return;
                }

                for(final Player player : entities) {
                    if(player.getLocation().distance(entity.getLocation()) > 10)  {
                        player.teleport(entity.getLocation());
                        return;
                    }
                }

                final Player nearestPlayer = entities.getFirst();

                entity.setTarget(nearestPlayer);
                championEntity.setTarget(nearestPlayer);
            }
        }.runTaskTimer(MCRekus.getInstance(), 0, 5);

        final List<Player> attackers = Arrays.stream(entity.getChunk().getEntities()).filter(entity1 -> {
            if(!(entity1 instanceof Player player)) {
                return false;
            }

            KingdomPlayer kp = KingdomPlayer.getKingdomPlayer(player);

            return kp.getKingdom() != null && kp.getKingdom().getName().equals(invasion.getAttacker().getKingdom().getName());
        }).map(entity1 -> (Player) entity1).toList();

        final KingdomsConfig config = getModule().getConfig();

        final List<Player> allies = attackers.stream().filter(player1 -> player1.getUniqueId() != invasion.getInvader().getPlayer().getUniqueId()).toList();

        final String alliesString = String.join(", ", allies.stream().map(Player::getName).toArray(String[]::new));

        final Player invader = invasion.getInvader().getPlayer();

        final Component attackersMessage;
        if(!allies.isEmpty()) {
            attackersMessage = ComponentUtil.deserialize(config.getInvasionPlayerListMessage().replaceAll("%allies%", alliesString).replaceAll("%player%", invader.getName()));
        } else {
            attackersMessage = ComponentUtil.deserialize(config.getInvasionPlayerListMessage().replaceAll("%allies%", "BRAK!").replaceAll("%player%", invader.getName()));
        }

        invader.sendMessage(ComponentUtil.deserialize(config.getInvasionPlayerListMessageDefender().replaceAll("%allies%", alliesString).replaceAll("%kingdom_name%", invasion.getDefender().getName())));

        for(final Player player : invasion.getDefender().getOnlineMembers()) {
            player.sendMessage(attackersMessage);
        }

        for(final Player player : allies) {
            player.sendMessage(ComponentUtil.deserialize(config.getInvasionPlayerListMessageAlly().replaceAll("%player%", invader.getName()).replaceAll("%kingdom_name%", invasion.getDefender().getName())));
        }

        RekusLogger.debug("Adding champion to map");
        getModule().getChampions().put(entity.getEntityId(), new Champion(attackers, entity, entity.getLocation().clone(), targetTask));
    }

    @EventHandler
    public void onInvastionEnd(final KingdomInvadeEndEvent event) {
        final Invasion invasion = event.getInvasion();

        final LivingEntity entity = invasion.getChampion();

        if (entity == null) {
            RekusLogger.warn("Champion is null | end");
            return;
        }

        final Champion champion = getModule().getChampions().get(entity.getEntityId());

        RekusLogger.debug("Champion died");
        if (champion.getTeleportationTask() != null) {
            RekusLogger.debug("Cancelling tp task");
            champion.getTeleportationTask().cancel();
        }

        champion.getChampionTargetTask().cancel();

        RekusLogger.debug("Removing champion from map");
        getModule().getChampions().remove(entity.getEntityId());
    }

    @EventHandler
    public void onDeath(final PlayerDeathEvent event) {
        final Player player = event.getEntity();

        getModule().getChampions().values().stream()
                .filter(champion -> champion.getAttackingPlayers().contains(player))
                .forEach(champion -> champion.getAttackingPlayers().remove(player));
    }
}
