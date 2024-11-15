package dev.isnow.mcrekus.module.impl.model.tracker;

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import dev.isnow.mcrekus.module.impl.model.parser.impl.Model;
import dev.isnow.mcrekus.module.impl.model.util.ModelUtil;
import dev.isnow.mcrekus.module.impl.model.util.MountableWrappedEntity;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import java.util.HashMap;
import java.util.List;
import me.tofaa.entitylib.meta.display.BlockDisplayMeta;
import me.tofaa.entitylib.meta.display.TextDisplayMeta;
import me.tofaa.entitylib.meta.types.DisplayMeta;
import me.tofaa.entitylib.ve.ViewerEngine;
import me.tofaa.entitylib.wrapper.WrapperEntity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ModelTracker {

    final HashMap<Integer, TrackedModel> trackedModels = new HashMap<>();

    public TrackedModel getModel(final int entityId) {
        return trackedModels.get(entityId);
    }

    public TrackedModel spawnModel(final Location location, final Model model, final Player player) {
        final MountableWrappedEntity base = new MountableWrappedEntity(EntityTypes.BLOCK_DISPLAY, player);

        final BlockDisplayMeta meta = base.getEntityMeta(BlockDisplayMeta.class);

        meta.setScale(model.getDefaultTransform().getScale());
        meta.setTranslation(model.getDefaultTransform().getTranslation());
        meta.setLeftRotation(model.getDefaultTransform().getLeftRotation());
        meta.setRightRotation(model.getDefaultTransform().getRightRotation());

        base.spawn(location, player);

        final List<WrapperEntity> objects = ModelUtil.setupModel(base, model);

        final TrackedModel trackedModel = new TrackedModel(model, base, objects);

        trackedModels.put(base.getEntityId(), trackedModel);

        return trackedModel;
    }


    public TrackedModel spawnModel(final org.bukkit.Location bukkitLocation, final Model model) {

//        trackedModel.getBase().addViewer(player.getUniqueId());
//        for(final WrapperEntity object : trackedModel.getObjects()) {
//            object.addViewer(player.getUniqueId());
//        }

        return spawnModel(bukkitLocation, model, null);
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

    public void despawnModel(final TrackedModel model) {
        despawnModel(model.getBase().getEntityId());
    }
}
