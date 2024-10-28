package dev.isnow.mcrekus.module.impl.crates;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.Module;
import dev.isnow.mcrekus.module.impl.crates.config.CratesConfig;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.module.impl.essentials.menu.InvseeMenu;
import dev.isnow.mcrekus.module.impl.essentials.message.MessageManager;
import dev.isnow.mcrekus.module.impl.essentials.teleport.TeleportManager;
import java.io.File;
import java.util.HashMap;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class CratesModule extends Module<CratesConfig> {
    public final Song open = NBSDecoder.parse(new File("plugins/MCRekus/songs/crate-open.nbs"));

    public CratesModule() {
        super("Crates");
    }

    @Override
    public void onEnable(final MCRekus plugin) {
        registerListeners("event");
//        registerCommands("command");
    }

    @Override
    public void onDisable(final MCRekus plugin) {
//        unRegisterCommands();
        unRegisterListeners();
    }
}
