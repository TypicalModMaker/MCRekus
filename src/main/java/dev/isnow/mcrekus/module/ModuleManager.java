package dev.isnow.mcrekus.module;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.util.ReflectionUtil;
import dev.isnow.mcrekus.util.RekusLogger;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.entity.Player;

public class ModuleManager {
    private final MCRekus plugin;
    private final Set<Module> modules = new HashSet<>();

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

    public Module<?> getModuleByName(final String moduleName) {
        return modules.stream().filter(module -> module.getName().equals(moduleName)).findFirst().orElseThrow(NullPointerException::new);
    }

    public Module<?> getModuleByClass(final Class<?> clazz) {
        return modules.stream().filter(module -> module.getClass().getName().equals(clazz.getName())).findFirst().orElseThrow(NullPointerException::new);
    }
}
