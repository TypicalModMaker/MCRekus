package dev.isnow.mcrekus.module.impl.model;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.Module;
import dev.isnow.mcrekus.module.impl.model.config.ModelConfig;
import dev.isnow.mcrekus.module.impl.model.parser.ProjectParser;
import dev.isnow.mcrekus.module.impl.model.parser.impl.Model;
import dev.isnow.mcrekus.module.impl.model.tracker.ModelTracker;
import dev.isnow.mcrekus.util.RekusLogger;
import java.io.File;
import java.util.HashMap;
import lombok.Getter;
import me.tofaa.entitylib.ve.ViewerEngine;

@Getter
public class ModelModule extends Module<ModelConfig> {

    private final HashMap<String, Model> parsedModels = new HashMap<>();
    private final ViewerEngine viewerEngine = new ViewerEngine();
    private final ModelTracker modelTracker = new ModelTracker();

    public ModelModule() {
        super("Model");
    }

    @Override
    public void onEnable(final MCRekus plugin) {
        RekusLogger.info("Loading models...");
        parseModels();

        registerCommands("command");
        viewerEngine.enable();
    }

    @Override
    public void onDisable(final MCRekus plugin) {
        unRegisterCommands();
    }

    public void parseModels() {
        parsedModels.clear();

        final File modelFolder = new File("plugins/MCRekus/modules/Model/models/");
        modelFolder.mkdirs();

        for(final File file : modelFolder.listFiles()) {
            if(file.getName().endsWith(".bdengine")) {
                RekusLogger.debug("Parsing model: " + file.getName());

                final Model model = ProjectParser.parseFile(file);

                if(model != null) {
                    RekusLogger.debug("Parsed model: " + model.getName());

                    parsedModels.put(model.getName(), model);
                }
            }
        }
    }
}
