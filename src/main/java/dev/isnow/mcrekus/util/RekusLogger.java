package dev.isnow.mcrekus.util;


import dev.isnow.mcrekus.MCRekus;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

@UtilityClass
public class RekusLogger {

    public final String bigPrefix = "\n  __  __  ___ ___     _           \n" +
            " |  \\/  |/ __| _ \\___| |___  _ ___\n" +
            " | |\\/| | (__|   / -_) / / || (_-<\n" +
            " |_|  |_|\\___|_|_\\___|_\\_\\\\_,_/__/\n" +
            "                                  ";
    private final String prefix = "&7[MCRekus] >>";

    public void error(final String log) {
        Bukkit.getConsoleSender().sendMessage(ComponentUtil.deserialize(prefix + " &c[ERROR] " + log));
    }

    public void info(final String log) {
        Bukkit.getConsoleSender().sendMessage(ComponentUtil.deserialize(prefix + " &f[INFO] " + log));
    }

    public void warn(final String log) {
        Bukkit.getConsoleSender().sendMessage(ComponentUtil.deserialize(prefix + " &e[WARN] " + log));
    }

    public void debug(final String log) {
        if(!MCRekus.getInstance().getConfigManager().getGeneralConfig().isDebugMode()) return;

        Bukkit.getConsoleSender().sendMessage(ComponentUtil.deserialize(prefix + " &a[DEBUG] " + log));
    }

    public void big(final String log) {
        Bukkit.getConsoleSender().sendMessage(ComponentUtil.deserialize(bigPrefix + "\n" + log));
    }

    public void watermark() {
        big("© 5170 ↝ 2024");
    }
}