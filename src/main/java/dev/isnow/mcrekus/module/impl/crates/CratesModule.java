package dev.isnow.mcrekus.module.impl.crates;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.Module;
import dev.isnow.mcrekus.module.impl.crates.config.CratesConfig;
import java.io.File;
import lombok.Getter;

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
