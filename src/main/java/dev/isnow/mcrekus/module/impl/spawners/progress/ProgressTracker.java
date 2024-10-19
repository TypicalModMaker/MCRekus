package dev.isnow.mcrekus.module.impl.spawners.progress;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawners.SpawnersModule;
import dev.isnow.mcrekus.module.impl.spawners.config.SpawnersConfig;
import dev.isnow.mcrekus.module.impl.spawners.spawners.RekusSpawner;
import dev.isnow.mcrekus.util.ComponentUtil;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Random;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

@EqualsAndHashCode(callSuper = false)
@Data
public class ProgressTracker extends ModuleAccessor<SpawnersModule> {
    public static final String BASE_LINE = "━━━━━━━━━";
    public static final ArrayList<String> SPAWNER_MESSAGES = new ArrayList<>();

    static {
        SPAWNER_MESSAGES.add("Oblałeś spawner olejem, myśląc, że to smar!");
        SPAWNER_MESSAGES.add("Złamałeś spawner, bo za mocno uderzyłeś!");
        SPAWNER_MESSAGES.add("Zamiast śrubokręta, użyłeś widelca do naprawy spawneru!");
        SPAWNER_MESSAGES.add("Niechcący przybiłeś spawner gwoździem do ściany!");
        SPAWNER_MESSAGES.add("Przypadkowo oblałeś spawner farbą podczas malowania!");
        SPAWNER_MESSAGES.add("Użyłeś strzały do otworzenia spawneru, zamiast klucza!");
        SPAWNER_MESSAGES.add("Zamiast rączki, użyłeś szczypiec do podniesienia spawneru!");
        SPAWNER_MESSAGES.add("Przypadkowo przyciągnąłeś spawner magnesem!");
        SPAWNER_MESSAGES.add("Pomyliłeś spawner z blokiem redstone'u i spróbowałeś go aktywować!");
        SPAWNER_MESSAGES.add("Pomyliłeś spawner z kominkiem i spróbowałeś go rozpalić!");
        SPAWNER_MESSAGES.add("Niechcący podłączyłeś spawner do generatora prądu!");
        SPAWNER_MESSAGES.add("Niechcący wsypałeś dirta do spawneru!");
    }

    private final Player player;

    private BukkitTask task;

    private int progress = 0;
    private int targetPosition = new Random().nextInt(1, BASE_LINE.length());
    private int position = -1;
    private boolean reverse;
    private String lastFailedMessage = "";

    public String getHighlightedLine() {
        final StringBuilder highlightedLine = new StringBuilder(BASE_LINE);

        if (position == targetPosition) {
            return highlightedLine.substring(0, position)
                    + "&#4888DD&m&l━&#305C96&m&l"
                    + highlightedLine.substring(position + 1);
        }

        String result = highlightedLine.substring(0, targetPosition)
                + "&#FFFFFF&m&l━&#305C96&m&l"
                + highlightedLine.substring(targetPosition + 1);

        int adjustedPosition = position;
        if (position > targetPosition) {
            adjustedPosition += "&#FFFFFF&m&l━&#305C96&m&l".length() - 1;
        }

        result = result.substring(0, adjustedPosition)
                + "&#4888DD&m&l━&#305C96&m&l"
                + result.substring(adjustedPosition + 1);

        return result;
    }

    public void updatePosition() {
        if (!reverse) {
            position++;
            if (position == BASE_LINE.length() - 1) {
                reverse = true;
            }
        } else {
            position--;
            if (position == 0) {
                reverse = false;
            }
        }
    }


    public void showTitle() {
        final String animatedLine = getHighlightedLine();
        final SpawnersConfig config = getModule().getConfig();

        player.showTitle(Title.title(
                ComponentUtil.deserialize("&#125CBE🔧 &#305C96&m&l" + animatedLine + "<reset> &#125CBE🔧"),
                ComponentUtil.deserialize(config.getFixingSpawnerSubtitle()),
                Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ZERO)
        ));
    }

    public void showBar() {
        final String actionBar = "&#4888DD&m&l━".repeat(Math.max(0, progress) * 5)
                + "&#305C96&m&l━".repeat(Math.max(0, 4 - progress) * 5);
        player.sendActionBar(ComponentUtil.deserialize(actionBar));
    }

    public void setup() {
        task = new BukkitRunnable() {
            @Override
            public void run() {
                if(!player.isOnline()) {
                    cancel();
                    final RekusSpawner spawner = getModule().getSpawners().values().stream().filter(rekusSpawner -> rekusSpawner.getRepairingPlayer() == player).findFirst().orElse(null);

                    if(spawner != null) {
                        spawner.addCooldown(player.getUniqueId());
                        spawner.setRepairingPlayer(null);
                    }

                    getModule().removeProgressTracker(player);
                    return;
                }
                updatePosition();

                showTitle();
                showBar();
            }
        }.runTaskTimerAsynchronously(MCRekus.getInstance(), 0L, 10L);

    }
}
