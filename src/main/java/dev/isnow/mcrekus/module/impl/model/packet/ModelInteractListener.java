package dev.isnow.mcrekus.module.impl.model.packet;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketType.Play.Client;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.model.ModelModule;
import dev.isnow.mcrekus.module.impl.model.parser.impl.Model;
import dev.isnow.mcrekus.module.impl.model.tracker.ModelTracker;
import dev.isnow.mcrekus.module.impl.model.tracker.TrackedModel;
import dev.isnow.mcrekus.util.RekusLogger;

public class ModelInteractListener extends ModuleAccessor<ModelModule> implements PacketListener {

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (event.getPacketType() == Client.INTERACT_ENTITY) {
            final WrapperPlayClientInteractEntity wrapper = new WrapperPlayClientInteractEntity(event);

            final ModelTracker modelTracker = getModule().getModelTracker();

            final TrackedModel model = modelTracker.getModel(wrapper.getEntityId());

            if(model != null) {
                RekusLogger.debug("Interacted with model: " + model.getModel().getName());
            }
        }
    }
}
