package dev.isnow.mcrekus.module.impl.essentials;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.Module;
import dev.isnow.mcrekus.module.impl.essentials.command.broadcast.BroadcastTypeResolver;
import dev.isnow.mcrekus.module.impl.essentials.command.song.SongResolver;
import dev.isnow.mcrekus.module.impl.essentials.command.speed.SpeedTypeResolver;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.module.impl.essentials.menu.InvseeMenu;
import dev.isnow.mcrekus.module.impl.essentials.message.MessageManager;
import dev.isnow.mcrekus.module.impl.essentials.teleport.TeleportManager;
import java.util.HashMap;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class EssentialsModule extends Module<EssentialsConfig> {

    private final TeleportManager teleportManager = new TeleportManager();
    private final MessageManager messageManager = new MessageManager();

    private final HashMap<Player, InvseeMenu> invseeMenus = new HashMap<>();

    public EssentialsModule() {
        super("Essentials");
    }

    @Override
    public void onEnable(final MCRekus plugin) {
        registerListeners("event");
        registerCommands("command");
    }

    @Override
    public void onDisable(final MCRekus plugin) {
        unRegisterCommands();
    }
}
