package dev.isnow.mcrekus.module;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.data.PlayerData;
import dev.isnow.mcrekus.util.ReflectionUtil;
import dev.isnow.mcrekus.util.RekusLogger;
import dev.velix.imperat.annotations.Command;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

@Getter
public abstract class Module<T extends ModuleConfig> {
    private final String name;
    private final Set<String[]> registeredCommands = new HashSet<>();
    private final Set<Listener> registeredListeners = new HashSet<>();

    @Setter
    private T config;

    public Module(final String name) {
        this.name = name;

        this.config = createConfig();
    }

    @SuppressWarnings("unchecked")
    public T createConfig() {
        try {
            final Class<T> configClass = ReflectionUtil.getGenericTypeClass(this, 0);
            if(configClass == null) {
                RekusLogger.debug("No config has been found for module " + getName());
                return null;
            }

            return (T) configClass.getDeclaredConstructor().newInstance().load();
        } catch (Exception e) {
            RekusLogger.error("Failed to load config for module " + name);
            e.printStackTrace();
            return null;
        }
    }

    public final void registerListener(final Class<? extends Listener> clazz) {
        RekusLogger.debug("Registering listener " + clazz.getSimpleName());
        try {
            final Listener listener = clazz.getDeclaredConstructor().newInstance();

            Bukkit.getPluginManager().registerEvents(listener, MCRekus.getInstance());

            registeredListeners.add(listener);
        } catch (Exception e) {
            RekusLogger.info("Failed to register listener " + clazz.getSimpleName() + " for module " + name);
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public final void registerListeners(String packageName) {
        try {
            final List<Class<?>> eventClasses = ReflectionUtil.getClasses(getClass().getPackageName() + "." + packageName);
            for (final Class<?> clazz : eventClasses) {
                registerListener((Class<? extends Listener>) clazz);
            }
        } catch (final Exception e) {
            RekusLogger.info("Failed to register listeners for module " + name);
            e.printStackTrace();
        }
    }

    public final void unRegisterCommands() {
        registeredCommands.removeIf(command -> {
            for (final String s : command) {
                MCRekus.getInstance().getCommandManager().unRegisterCommand(s);
            }
            return true;
        });
    }

    public final void unRegisterListeners() {
        for (final Listener listener : registeredListeners) {
            HandlerList.unregisterAll(listener);
        }
    }

    private void registerCommand(final String[] names, final Class<?> clazz) {
        RekusLogger.debug("Registering command " + clazz.getSimpleName());
        try {
            final Object baseCommand = clazz.getDeclaredConstructor().newInstance();
            registeredCommands.add(names);

            MCRekus.getInstance().getCommandManager().registerCommand(baseCommand);
        } catch (Exception e) {
            RekusLogger.info("Failed to register command " + clazz.getSimpleName() + " for module " + name);
            e.printStackTrace();
        }
    }

    public final void registerCommands(String packageName) {
        try {
            final List<Class<?>> eventClasses = ReflectionUtil.getClasses(getClass().getPackageName() + "." + packageName);
            for (final Class<?> clazz : eventClasses) {
                if(clazz.isAnnotationPresent(Command.class)) {
                    final Command command = clazz.getAnnotation(Command.class);

                    registerCommand(command.value(), clazz);
                }
            }
        } catch (final Exception e) {
            RekusLogger.info("Failed to register commands for module " + name);
            e.printStackTrace();
        }
    }

    public abstract void onEnable(final MCRekus plugin);

    public abstract void onDisable(final MCRekus plugin);
}
