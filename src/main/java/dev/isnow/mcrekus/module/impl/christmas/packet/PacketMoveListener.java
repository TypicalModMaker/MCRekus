package dev.isnow.mcrekus.module.impl.christmas.packet;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;

public class PacketMoveListener extends PacketListenerAbstract {

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
//        if(event.getPacketType() == Client.PLAYER_POSITION) {
//            final WrapperPlayClientPlayerPosition wrapper = new WrapperPlayClientPlayerPosition(event);
//
//            final ChristmasModule christmasModule = MCRekus.getInstance().getModuleManager().getModuleByClass(ChristmasModule.class);
//
//            final Player player = event.getPlayer();
//
//            final TrackedModel trackedModel = christmasModule.getTrackedHats().get(player);
//            if (trackedModel != null) {
//                final Location location = wrapper.getLocation().clone();
//
//                final Vector3d oldPosition = location.getPosition();
//
//                location.setPosition(new Vector3d(oldPosition.getX(), oldPosition.getY() + 1.9, oldPosition.getZ()));
//                location.setYaw(trackedModel.getBase().getYaw());
//                location.setPitch(trackedModel.getBase().getPitch());
//
//                trackedModel.getBase().teleport(location);
//
//                trackedModel.getObjects().forEach(object -> {
//                    object.teleport(location);
//                });
//            }
//        } else if(event.getPacketType() == Client.PLAYER_ROTATION) {
//            final WrapperPlayClientPlayerRotation wrapper = new WrapperPlayClientPlayerRotation(event);
//
//            final ChristmasModule christmasModule = MCRekus.getInstance().getModuleManager().getModuleByClass(ChristmasModule.class);
//
//            final Player player = event.getPlayer();
//
//            final TrackedModel trackedModel = christmasModule.getTrackedHats().get(player);
//
//            if (trackedModel != null) {
//                trackedModel.getBase().rotateHead(wrapper.getLocation());
//
//                trackedModel.getObjects().forEach(object -> {
//                    object.rotateHead(wrapper.getLocation());
//                });
//            }
//        } else if(event.getPacketType() == Client.PLAYER_POSITION_AND_ROTATION) {
//            final WrapperPlayClientPlayerRotation wrapper = new WrapperPlayClientPlayerRotation(event);
//
//            final ChristmasModule christmasModule = MCRekus.getInstance().getModuleManager().getModuleByClass(ChristmasModule.class);
//
//            final Player player = event.getPlayer();
//
//            final TrackedModel trackedModel = christmasModule.getTrackedHats().get(player);
//
//            if (trackedModel != null) {
//                final Location location = wrapper.getLocation().clone();
//                final Vector3d oldPosition = location.getPosition();
//                location.setPosition(new Vector3d(oldPosition.getX(), oldPosition.getY() + 1.9, oldPosition.getZ()));
//
//                trackedModel.getBase().teleport(location);
//                trackedModel.getObjects().forEach(object -> {
//
//                    object.teleport(location);
//                });
//            }
//        }

    }
}
