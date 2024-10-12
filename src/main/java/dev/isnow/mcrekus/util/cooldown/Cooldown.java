package dev.isnow.mcrekus.util.cooldown;

import dev.isnow.mcrekus.util.DateUtil;
import java.util.HashMap;
import lombok.Data;

@Data
public class Cooldown<T> {
    private final long startingCooldown;
    private final HashMap<T, Long> cooldowns = new HashMap<>();

    public Cooldown(final long startingCooldown) {
        this.startingCooldown = startingCooldown;
    }

    public final String isOnCooldown(final T item) {
        final boolean containsItem = cooldowns.containsKey(item);

        long time = System.currentTimeMillis();
        if(containsItem) {
            time = System.currentTimeMillis() - cooldowns.get(item);
        }

        if(time < startingCooldown) {
            return DateUtil.formatElapsedTime(startingCooldown - time);
        } else {
            removeCooldown(item);
        }

        return "-1";
    }

    public final void addCooldown(final T item) {
        cooldowns.put(item, System.currentTimeMillis());
    }

    public final void removeCooldown(final T item) {
        cooldowns.remove(item);
    }
}
