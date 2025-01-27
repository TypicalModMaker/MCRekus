package dev.isnow.mcrekus.module.impl.christmas.event;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.christmas.ChristmasModule;
import dev.isnow.mcrekus.module.impl.christmas.config.ChristmasConfig;
import dev.isnow.mcrekus.module.impl.model.ModelModule;
import dev.isnow.mcrekus.module.impl.model.tracker.TrackedModel;
import me.tofaa.entitylib.wrapper.WrapperEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class HatEvent extends ModuleAccessor<ChristmasModule> implements Listener {

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        final ModelModule modelModule = MCRekus.getInstance().getModuleManager().getModuleByClass(ModelModule.class);

        final ChristmasConfig config = getModule().getConfig();

        final TrackedModel trackedModel = modelModule.getModelTracker().spawnModel(player.getLocation(), modelModule.getParsedModels().get(config.getSantaHatModel()), player);

        for(final WrapperEntity object : trackedModel.getObjects()) {
            object.addViewer(player.getUniqueId());
        }

        getModule().getTrackedHats().put(player, trackedModel);
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        final TrackedModel trackedModel = getModule().getTrackedHats().remove(player);

        if(trackedModel != null) {
            final ModelModule modelModule = MCRekus.getInstance().getModuleManager().getModuleByClass(ModelModule.class);

            modelModule.getModelTracker().despawnModel(trackedModel);
        }
    }
}
