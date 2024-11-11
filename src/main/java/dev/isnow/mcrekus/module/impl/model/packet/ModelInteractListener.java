package dev.isnow.mcrekus.module.impl.model.packet;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketType.Play.Client;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import dev.isnow.mcrekus.module.impl.model.parser.impl.Model;

public class ModelInteractListener implements PacketListener {

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (event.getPacketType() == Client.INTERACT_ENTITY) {
            WrapperPlayClientInteractEntity wrapper = new WrapperPlayClientInteractEntity(event);

        }
    }
}
