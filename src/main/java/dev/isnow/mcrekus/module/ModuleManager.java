package dev.isnow.mcrekus.module;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.util.ReflectionUtil;
import dev.isnow.mcrekus.util.RekusLogger;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModuleManager {
    private final MCRekus plugin;
    private final Set<Module<?>> modules = new HashSet<>();

    public ModuleManager(final MCRekus plugin) {
        this.plugin = plugin;
    }

    public void loadModules() {
        loadModules(plugin, "dev.isnow.mcrekus.module.impl");
    }

    private void loadModules(final MCRekus plugin, final String packageName) {
        try {
            List<Class<?>> moduleClasses = ReflectionUtil.getClasses(packageName);
            for (Class<?> clazz : moduleClasses) {
                if (Module.class.isAssignableFrom(clazz) && !clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers())) {
                    final Module<?> module = (Module<?>) clazz.getDeclaredConstructor().newInstance();

                    try {
                        if(plugin.getConfigManager().getGeneralConfig().getEnabledModules().contains(module.getName())) {
                            modules.add(module);

                            RekusLogger.info("Loading module " + module.getName());
                            module.onEnable(plugin);
                            RekusLogger.info("Loaded module " + module.getName());
                        }
                    } catch (Exception e) {
                        modules.remove(module);
                        RekusLogger.error("Failed to load module " + module.getName() + "!");
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            RekusLogger.error("Failed to load modules!");
            e.printStackTrace();
        }
    }

    public void unloadModules() {
        modules.removeIf(module -> {
            RekusLogger.info("Disabling module " + module.getName());
            module.onDisable(plugin);

            if(module.getConfig() != null && !module.getConfig().getPath().toFile().exists()) {
                RekusLogger.debug("Saving default config for module " + module.getName());
                module.getConfig().save();
            }

            return true;
        });
    }

    public <T> T getModuleByName(final String moduleName) {
        final Module<?> foundModule = modules.stream().filter(module -> module.getName().equals(moduleName)).findFirst().orElse(null);;

        return foundModule != null ? (T) foundModule : null;
    }

    public <T> T getModuleByClass(final Class<T> clazz) {
        final Module<?> foundModule = modules.stream().filter(module -> module.getClass().equals(clazz)).findFirst().orElse(null);

        return foundModule != null ? (T) foundModule : null;
    }
}
