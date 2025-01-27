package dev.isnow.mcrekus.module;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.command.CommandManager;
import dev.isnow.mcrekus.util.ReflectionUtil;
import dev.isnow.mcrekus.util.RekusLogger;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
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
        RekusLogger.info("Initializing command manager");
        MCRekus.getInstance().setCommandManager(new CommandManager(MCRekus.getInstance()));

        List<Module<?>> initializedModules = new ArrayList<>();

        try {
            List<Class<?>> moduleClasses = ReflectionUtil.getClasses(packageName);
            for (Class<?> clazz : moduleClasses) {
                if (Module.class.isAssignableFrom(clazz) && !clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers())) {
                    try {
                        final Module<?> module = (Module<?>) clazz.getDeclaredConstructor().newInstance();
                        try {
                            initializedModules.add(module);
                        } catch (Exception e) {
                            RekusLogger.error("Failed to find module " + module.getName() + "!");
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        RekusLogger.error("Failed to load module " + clazz.getSimpleName());
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            RekusLogger.error("Failed to load modules!");
            e.printStackTrace();
        }

        initializedModules = sortModulesByDependencies(initializedModules);

        for(final Module<?> module : initializedModules) {
            if(!plugin.getConfigManager().getGeneralConfig().getEnabledModules().contains(module.getName())) {
                continue;
            }
            try {
                modules.add(module);
                RekusLogger.info("Loading module " + module.getName());
                module.onEnable(plugin);
                RekusLogger.info("Loaded module " + module.getName());
            } catch (Exception e) {
                modules.remove(module);
                RekusLogger.error("Failed to load module " + module.getName());
                e.printStackTrace();
            }
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

    public List<Module<?>> sortModulesByDependencies(final List<Module<?>> modules) {
        final Map<Class<?>, Integer> inDegree = new HashMap<>();
        final Map<Class<?>, Module<?>> moduleMap = new HashMap<>();
        final Queue<Class<?>> zeroDegreeQueue = new LinkedList<>();

        List<Module<?>> sortedList = new ArrayList<>();

        for (final Module<?> module : modules) {
            final Class<?> moduleClass = module.getClass();
            inDegree.put(moduleClass, 0);
            moduleMap.put(moduleClass, module);
        }

        for (final Module<?> module : modules) {
            for (final Class<?> dependency : module.getDependencies()) {
                inDegree.put(dependency, inDegree.getOrDefault(dependency, 0) + 1);
            }
        }

        for (final Class<?> moduleClass : inDegree.keySet()) {
            if (inDegree.get(moduleClass) == 0) {
                zeroDegreeQueue.add(moduleClass);
            }
        }

        while (!zeroDegreeQueue.isEmpty()) {
            final Class<?> currentClass = zeroDegreeQueue.poll();
            final Module<?> currentModule = moduleMap.get(currentClass);

            sortedList.add(currentModule);

            for (final Class<?> dependency : currentModule.getDependencies()) {
                final int updatedInDegree = inDegree.get(dependency) - 1;
                inDegree.put(dependency, updatedInDegree);

                if (updatedInDegree == 0) {
                    zeroDegreeQueue.add(dependency);
                }
            }
        }

        sortedList = sortedList.reversed();

        if (sortedList.size() != modules.size()) {
            RekusLogger.error("There is a cycle in the dependencies!");
            return null;
        }

        // Return the sorted list of modules
        return sortedList;
    }


}
