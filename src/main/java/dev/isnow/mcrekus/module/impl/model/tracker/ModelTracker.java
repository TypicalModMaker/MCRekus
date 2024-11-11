package dev.isnow.mcrekus.module.impl.model.tracker;

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.util.Vector3d;
import dev.isnow.mcrekus.module.impl.model.parser.impl.Model;
import dev.isnow.mcrekus.module.impl.model.util.ModelUtil;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import java.util.HashMap;
import java.util.List;
import me.tofaa.entitylib.meta.display.BlockDisplayMeta;
import me.tofaa.entitylib.ve.ViewerEngine;
import me.tofaa.entitylib.wrapper.WrapperEntity;

public class ModelTracker {

    final HashMap<Integer, TrackedModel> trackedModels = new HashMap<>();

    public TrackedModel getModel(final int entityId) {
        return trackedModels.get(entityId);
    }

    public TrackedModel spawnModel(final org.bukkit.Location bukkitLocation, final Model model) {
        final WrapperEntity base = new WrapperEntity(EntityTypes.BLOCK_DISPLAY);

        final BlockDisplayMeta meta = base.getEntityMeta(BlockDisplayMeta.class);

        meta.setScale(model.getDefaultTransform().getScale());
        meta.setTranslation(model.getDefaultTransform().getTranslation());
        meta.setLeftRotation(model.getDefaultTransform().getLeftRotation());
        meta.setRightRotation(model.getDefaultTransform().getRightRotation());

        final Location location = SpigotConversionUtil.fromBukkitLocation(bukkitLocation);

        base.spawn(location);

        final List<WrapperEntity> objects = ModelUtil.setupModel(base, model);

        final TrackedModel trackedModel = new TrackedModel(model, base, objects);

        trackedModels.put(base.getEntityId(), trackedModel);

        return trackedModel;
    }

    public void despawnModel(final int entityId) {
        final TrackedModel model = getModel(entityId);

        if (model != null) {
            for(final WrapperEntity object : model.getObjects()) {
                object.despawn();
            }
            model.getBase().despawn();
        }

        trackedModels.remove(entityId);
    }
}
