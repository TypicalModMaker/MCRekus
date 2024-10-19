package dev.isnow.mcrekus.packet;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType.Play.Client;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import dev.isnow.mcrekus.event.custom.PlayerDropItemSlotEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RekusPacketListener implements PacketListener {
    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (event.getPacketType() == Client.PLAYER_DIGGING) {
            WrapperPlayClientPlayerDigging wrapper = new WrapperPlayClientPlayerDigging(event);

            if(wrapper.getAction() == DiggingAction.DROP_ITEM || wrapper.getAction() == DiggingAction.DROP_ITEM_STACK) {
                final Player player = event.getPlayer();

                final PlayerDropItemSlotEvent customEvent = new PlayerDropItemSlotEvent(
                        player,
                        player.getInventory().getItemInMainHand(),
                        player.getInventory(),
                        player.getInventory().getHeldItemSlot());

                Bukkit.getPluginManager().callEvent(customEvent);
                if (customEvent.isCancelled()) event.setCancelled(true);
            }
        }
    }

}
