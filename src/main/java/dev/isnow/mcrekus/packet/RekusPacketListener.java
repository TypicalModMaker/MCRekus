package dev.isnow.mcrekus.packet;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType.Play.Client;
import com.github.retrooper.packetevents.protocol.packettype.PacketType.Play.Server;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import dev.isnow.mcrekus.event.custom.PlayerDropItemSlotEvent;
import dev.isnow.mcrekus.util.RekusLogger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RekusPacketListener implements PacketListener {

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (event.getPacketType() == Client.PLAYER_DIGGING) {
            final WrapperPlayClientPlayerDigging wrapper = new WrapperPlayClientPlayerDigging(event);

            if(wrapper.getAction() == DiggingAction.DROP_ITEM || wrapper.getAction() == DiggingAction.DROP_ITEM_STACK) {
                final Player player = event.getPlayer();

                final PlayerDropItemSlotEvent customEvent = new PlayerDropItemSlotEvent(
                        player,
                        player.getInventory().getItemInMainHand(),
                        player.getInventory(),
                        wrapper.getAction(),
                        player.getInventory().getHeldItemSlot());

                Bukkit.getPluginManager().callEvent(customEvent);
                if (customEvent.isCancelled()) event.setCancelled(true);
            }
        }
    }

    @Override
    public void onPacketSend(final PacketSendEvent event) {
        final Player player = event.getPlayer();

        if (player == null) return;

        if (event.getPacketType() == Server.SPAWN_ENTITY) {
            final WrapperPlayServerSpawnEntity wrapper = new WrapperPlayServerSpawnEntity(event);

            if (wrapper.getClientVersion() == ClientVersion.V_1_20_3) return;

            if (wrapper.getEntityType() != EntityTypes.GOAT) return;

            if (wrapper.getData() != 0 || wrapper.getHeadYaw() != 0 || wrapper.getYaw() != 0 || wrapper.getPitch() != 0) return;

            RekusLogger.debug("Cancelling crash packet");
            event.setCancelled(true);
        }
    }

}
