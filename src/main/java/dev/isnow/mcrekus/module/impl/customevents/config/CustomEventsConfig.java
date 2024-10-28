package dev.isnow.mcrekus.module.impl.customevents.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import dev.isnow.mcrekus.module.ModuleConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
public class CustomEventsConfig extends ModuleConfig {
    public CustomEventsConfig() {
        super("config", "CustomEvents");
    }

    @Comment({"", "Events min players to start"})
    private int minPlayers = 20;

    @Comment({"", "Events min delay between events (in minutes)"})
    private int minDelay = 60;

    @Comment({"", "Events max delay between events (in minutes)"})
    private int maxDelay = 120;

    @Comment({"", "Events time length (in minutes)"})
    private int timeLength = 10;

    @Comment("Event start broadcast")
    private String[] eventStartBroadcast = new String[] {
        "[P] &aRozpoczęto wydarzenie! &7[%event%]",
        "[P] &aSzczegóły: &7%description%",
        "[P] &aWydarzenie potrwa &e%time% &aminut!",
        "[P] &aPowodzenia!"
    };

    @Comment("Event end broadcast")
    private String[] eventEndBroadcast = new String[] {
        "[P] &aWydarzenie zakończone! &7[%event%]",
        "[P] &aZwycięzcy:",
        "[P] &a1. - &e%first%",
        "[P] &a2. - &e%second%",
        "[P] &a3. - &e%third%",
    };

    @Comment({"", "Base value for event rewards"})
    private int baseReward = 5000;

    @Comment({"", "Base divider player counter for event rewards"})
    private int baseDivider = 100;

    @Comment({"", "Event command usage"})
    private String eventCommandUsage = "&cUżycie: /event <start|stop> <event>";

    @Comment({"", "Event not found message"})
    private String eventNotFoundMessage = "[P] &cNie znaleziono wydarzenia";

    @Comment({"", "Event already running message"})
    private String eventAlreadyRunningMessage = "[P] &cInne wydarzenie właśnie trwa!";

    @Comment({"", "Event started message"})
    private String eventStartedMessage = "[P] &aWydarzenie rozpoczęte";

    @Comment({"", "Event stopped message cause not enough players"})
    private String eventStoppedNotEnoughPlayersMessage = "[P] &cWydarzenie zatrzymane z powodu zbyt małej ilości graczy online";

    @Comment({"", "Event not running message"})
    private String eventNotRunningMessage = "[P] &cNie ma aktywnego wydarzenia";

    @Comment({"", "Player not found placeholder"})
    private String playerNotFound = "Brak gracza";

    @Comment({"", "Score not found placeholder"})
    private String scoreNotFound = "Brak";
}
