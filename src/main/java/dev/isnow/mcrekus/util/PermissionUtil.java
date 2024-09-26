package dev.isnow.mcrekus.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

@UtilityClass
public class PermissionUtil {

    public int getMaxAllowedHomes(final int defaultMaximumHomes, final Player player) {
        final Pattern maxHomesPerm = Pattern.compile("mcrekus\\.home\\.(\\d+)");

        return player.getEffectivePermissions().stream().map(i -> {
            Matcher matcher = maxHomesPerm.matcher(i.getPermission());
            if (matcher.matches()) {
                return Integer.parseInt(matcher.group(1));
            }
            return defaultMaximumHomes;
        }).max(Integer::compareTo).orElse(defaultMaximumHomes);
    }
}
