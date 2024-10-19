package dev.isnow.mcrekus.module.impl.spawners.event;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawners.SpawnersModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class ChunkEvent extends ModuleAccessor<SpawnersModule> implements Listener {

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
//        MCRekus.getInstance().getThreadPool().execute(() -> {
//            PaperLib.getChunkAtAsync(event.getChunk().getBlock(1, 1, 1).getLocation()).thenAccept(chunk -> {
//                getModule().loadChunk(chunk);
//            });
//        });
    }

}
