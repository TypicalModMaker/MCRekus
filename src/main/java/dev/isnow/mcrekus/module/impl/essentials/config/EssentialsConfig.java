package dev.isnow.mcrekus.module.impl.essentials.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import dev.isnow.mcrekus.module.ModuleConfig;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Sound;

@Getter
@Setter
@Configuration
public class EssentialsConfig extends ModuleConfig {
    public EssentialsConfig() {
        super("config", "Essentials");
    }

    @Comment({"Essentials module configuration (Essentials Alternative)", "", "Repair command cooldown (in seconds)"})
    private int repairCooldown = 60;

    @Comment({"", "Broadcast title duration (in seconds)"})
    private int broadcastTitleDuration = 5;

    @Comment({"", "Broadcast title fade in (in seconds)"})
    private int broadcastTitleFadeIn = 1;

    @Comment({"", "Broadcast title fade out (in seconds)"})
    private int broadcastTitleFadeOut = 1;

    @Comment({"", "TPA expiry time (in seconds)"})
    private int tpaExpiryTime = 60;

    @Comment({"", "TPA cooldown time (in seconds)"})
    private int tpaCooldownTime = 60;

    @Comment({"", "TPA delay time (in seconds)"})
    private int tpaDelayTime = 5;

    @Comment({"", "Home delay time (in seconds)"})
    private int homeDelayTime = 5;

    @Comment({"", "Helpop use cooldown time (in seconds)"})
    private int helpopCooldownTime = 60;

    @Comment({"", "Max allowed homes by default"})
    private int maxAllowedHomesByDefault = 3;

    @Comment({"", " ", "-------------------------", "Commands", "-------------------------", " ", "", "Opened crafting table message"})
    private String openWorkbenchMessage = "[P] &aOtwarto crafting.";

    @Comment({"", "Opened workbench sound"})
    private Sound openWorkbenchSound = Sound.BLOCK_WOODEN_BUTTON_CLICK_ON;

    @Comment({"", "Opened anvil message"})
    private String openAnvilMessage = "[P] &aOtwarto kowadło.";

    @Comment({"", "Opened anvil sound"})
    private Sound openAnvilSound = Sound.BLOCK_WOODEN_BUTTON_CLICK_ON;

    @Comment({"", "Opened ender chest message"})
    private String openEnderChestMessage = "[P] &aOtwarto ender chest.";

    @Comment({"", "Opened ender chest sound"})
    private Sound openEnderChestSound = Sound.BLOCK_ENDER_CHEST_OPEN;

    @Comment({"", "Gamemode no args message"})
    private String gamemodeNoArgsMessage = "[P] &cUżycie: /gm <0|1|2|3>";

    @Comment({"", "Gamemode changed message"})
    private String gamemodeChangedMessage = "[P] &aZmieniono tryb gry na &e%gamemode%&a.";

    @Comment({"", "Gamemode changed sound"})
    private Sound gamemodeChangedSound = Sound.BLOCK_NOTE_BLOCK_PLING;

    @Comment({"", "Gamemode creative translation"})
    private String gamemodeCreative = "Kreatywny";

    @Comment({"", "Gamemode adventure translation"})
    private String gamemodeAdventure = "Przygodowy";

    @Comment({"", "Gamemode spectator translation"})
    private String gamemodeSpectator = "Obserwator";

    @Comment({"", "Gamemode survival translation"})
    private String gamemodeSurvival = "Przetrwanie";

    @Comment({"", "Gamemode invalid message"})
    private String gamemodeInvalidMessage = "[P] &cNieprawidłowy tryb gry.";

    @Comment({"", "Gamemode player not found message"})
    private String gamemodePlayerNotFoundMessage = "[P] &cGracz %player% nie został znaleziony.";

    @Comment({"", "Gamemode changed other message"})
    private String gamemodeChangedOtherMessage = "[P] &aZmieniono tryb gry gracza &e%player%&a na &e%gamemode%&a.";

    @Comment({"", "Gamemode usage message"})
    private String gamemodeUsageMessage = "[P] &cUżycie: /gm <0|1|2|3> [gracz]";

    @Comment({"", "Repair item in hand message"})
    private String repairItemMessage = "[P] &aNaprawiono przedmiot w ręce.";

    @Comment({"", "Repair all message"})
    private String repairAllMessage = "[P] &aNaprawiono wszystkie przedmioty w ekwipunku.";

    @Comment({"", "Repair sound"})
    private Sound repairSound = Sound.BLOCK_ANVIL_USE;

    @Comment({"", "Cannot repair holding item messsage"})
    private String repairItemNotHoldingMessage = "[P] &cNie można naprawić tego przedmiotu.";

    @Comment({"", "Repair usage message"})
    private String repairUsageMessage = "[P] &cUżycie: /repair [all]";

    @Comment({"", "Repair cooldown message"})
    private String repairCooldownMessage = "[P] &cMusisz odczekać %time% sekund przed kolejnym naprawieniem.";

    @Comment({"", "Repair no permission message"})
    private String repairNoPermissionMessage = "[P] &cNie masz uprawnień do naprawiania wszystkich przedmiotów.";

    @Comment({"", "Broadcast no args message"})
    private String broadcastNoArgsMessage = "[P] &cUżycie: /broadcast <title/chat/actionbar> <wiadomość>";

    @Comment({"", "Broadcast chat message"})
    private String broadcastChatMessage = "[P] &c[OGŁOSZENIE] &f%message%";

    @Comment({"", "Broadcast title message"})
    private String broadcastTitleMessage = "&c[OGŁOSZENIE]";

    @Comment({"", "Broadcast actionbar message"})
    private String broadcastActionbarMessage = "&c[OGŁOSZENIE] &f%message%";

    @Comment({"", "Broadcast invalid type message"})
    private String broadcastInvalidTypeMessage = "[P] &cNieprawidłowy typ ogłoszenia, dostępne: chat, title, actionbar.";

    @Comment({"", "Teleport no args message"})
    private String teleportNoArgsMessage = "[P] &cUżycie: /tp <gracz> <gracz2> lub /tp <x> <y> <z>";

    @Comment({"", "Teleport success sound"})
    private Sound teleportSuccessSound = Sound.ENTITY_ENDERMAN_TELEPORT;

    @Comment({"", "Teleport to player message"})
    private String teleportToPlayerMessage = "[P] &aPrzeteleportowano do gracza &e%player%&a.";

    @Comment({"", "Teleport player to player message"})
    private String teleportPlayerToPlayerMessage = "[P] &aPrzeteleportowano gracza &e%player%&a do gracza &e%destination%&a.";

    @Comment({"", "Teleporthere no args message"})
    private String teleportHereNoArgsMessage = "[P] &cUżycie: /tphere <gracz>";

    @Comment({"", "Teleport player not found"})
    private String teleportPlayerNotFoundMessage = "[P] &cGracz %player% nie został znaleziony.";

    @Comment({"", "Teleport to location message"})
    private String teleportToLocationMessage = "[P] &aPrzeteleportowano do lokacji %coordinates%.";
    @Comment({"", "TPA no args message"})
    private String tpaNoArgsMessage = "[P] &cUżycie: /tpa <gracz>";

    @Comment({"", "TPA request sent message"})
    private String tpaRequestSentMessage = "[P] &aWysłano prośbę o teleportację do gracza &e%player%&a.";

    @Comment({"", "TPA request received message"})
    private String tpaRequestReceivedMessage = "[P] &aOtrzymano prośbę o teleportację od gracza &e%player%&a. Czas na odpowiedź: %time% sekund.";

    @Comment({"", "TPA cooldown message"})
    private String tpaCooldownMessage = "[P] &cMusisz odczekać %time% sekund przed kolejnym zapytaniem.";

    @Comment({"", "TPA request expired message"})
    private String tpaRequestExpiredSenderMessage = "[P] &cProśba o teleportację do %player% wygasła.";

    @Comment({"", "TPA request expired message"})
    private String tpaRequestExpiredReceivedMessage = "[P] &cProśba o teleportację od %player% wygasła.";

    @Comment({"", "TPA request accepted message"})
    private String tpaRequestAcceptedMessage = "[P] &aZaakceptowano prośbę o teleportację od gracza &e%player%&a.";

    @Comment({"", "TPA request denied message"})
    private String tpaRequestDeniedMessage = "[P] &cOdrzucono prośbę o teleportację od gracza &e%player%&c.";

    @Comment({"", "TPA player not found message"})
    private String tpaPlayerNotFoundMessage = "[P] &cGracz %player% nie został znaleziony.";

    @Comment({"", "TPA player already requested message"})
    private String tpaPlayerAlreadyRequestedMessage = "[P] &cGracz %player% już posiada aktywną prośbę o teleportację.";

    @Comment({"", "TPA request no request message"})
    private String tpacceptNoRequestMessage = "[P] &cNie masz żadnych aktywnych prośb o teleportację.";

    @Comment({"", "TPA request requester offline message"})
    private String tpacceptRequesterOfflineMessage = "[P] &cGracz, który wysłał prośbę o teleportację jest offline.";

    @Comment({"", "TPA your request got accepted message"})
    private String tpaRequestAcceptedRequesterMessage = "[P] &aTwoja prośba o teleportację do gracza %player% została zaakceptowana. Nie ruszaj sie przez %time% sekund.";

    @Comment({"", "TPA moved (cancelled) message"})
    private String tpaMovedMessage = "[P] &cRuszyłeś się, teleportacja anulowana.";

    @Comment({"", "TPA denied sender message"})
    private String tpaDeniedSenderMessage = "[P] &cTwoja prośba o teleportację do gracza %player% została odrzucona.";

    @Comment({"", "TPA cant teleport to yourself message"})
    private String tpaCantTeleportToYourselfMessage = "[P] &cNie możesz teleportować się do siebie.";

    @Comment({"", "Helpop no args message"})
    private String helpopNoArgsMessage = "[P] &cUżycie: /helpop <wiadomość>";

    @Comment({"", "Helpop message sent message"})
    private String helpopMessageSentMessage = "[P] &aWiadomość została wysłana do administracji.";

    @Comment({"", "Helpop cooldown message"})
    private String helpopCooldownMessage = "[P] &cMusisz odczekać %time% sekund przed kolejnym wysłaniem wiadomości.";

    @Comment({"", "Helpop message"})
    private String helpopMessage = "[P] &c[HELP] &f%player%: %message%";

    @Comment({"", "GodMode no args message"})
    private String godModeNoArgsMessage = "[P] &cUżycie: /god <gracz>";

    @Comment({"", "GodMode enabled message"})
    private String godModeEnabledMessage = "[P] &aWłączono tryb boga.";

    @Comment({"", "GodMode disabled message"})
    private String godModeDisabledMessage = "[P] &cWyłączono tryb boga.";

    @Comment({"", "GodMode enabled other message"})
    private String godModeEnabledOtherMessage = "[P] &aWłączono tryb boga dla gracza %player%.";

    @Comment({"", "GodMode disabled other message"})
    private String godModeDisabledOtherMessage = "[P] &cWyłączono tryb boga dla gracza %player%.";

    @Comment({"", "GodMode player not found message"})
    private String godModePlayerNotFoundMessage = "[P] &cGracz %player% nie został znaleziony.";

    @Comment({"", "Day message"})
    private String dayMessage = "[P] &aZmieniono czas na dzień.";

    @Comment({"", "Night message"})
    private String nightMessage = "[P] &aZmieniono czas na noc.";

    @Comment({"", "Speed command usage message"})
    private String speedUsageMessage = "[P] &cUżycie: /speed <walk|fly> <0-10>";

    @Comment({"", "Speed command invalid type message"})
    private String speedInvalidTypeMessage = "[P] &cNieprawidłowy typ prędkości, dostępne: walk, fly.";

    @Comment({"", "Speed command invalid speed message"})
    private String speedInvalidSpeedMessage = "[P] &cNieprawidłowa prędkość, dostępne: od -1 do 1.";

    @Comment({"", "Speed command changed other message"})
    private String speedChangedOtherMessage = "[P] &aZmieniono prędkość %type% dla gracza %player% na %speed%.";

    @Comment({"", "Speed command player not found message"})
    private String speedPlayerNotFoundMessage = "[P] &cGracz %player% nie został znaleziony.";

    @Comment({"", "Fligth Speed command changed message"})
    private String flightSpeedChangedMessage = "[P] &aZmieniono prędkość latania na %speed%.";

    @Comment({"", "Walk Speed command changed message"})
    private String walkSpeedChangedMessage = "[P] &aZmieniono prędkość chodzenia na %speed%.";

    @Comment({"", "Flight Speed reset message"})
    private String flightSpeedResetMessage = "[P] &aZresetowano prędkość latania.";

    @Comment({"", "Walk Speed reset message"})
    private String walkSpeedResetMessage = "[P] &aZresetowano prędkość chodzenia.";

    @Comment({"", "Message command usage message"})
    private String messageUsageMessage = "[P] &cUżycie: /msg <gracz> <wiadomość>";

    @Comment({"", "Message command player not found message"})
    private String messagePlayerNotFoundMessage = "[P] &cGracz %player% nie został znaleziony.";

    @Comment({"", "Message receiver format"})
    private String messageReceiverFormat = "[P] &7[&6Ja -> %player%&7] &f%message%";

    @Comment({"", "Message sender format"})
    private String messageSenderFormat = "[P] &7[&6%player% -> Ja&7] &f%message%";

    @Comment({"", "Message no last message"})
    private String messageNoLastMessage = "[P] &cNie masz komu odpisać.";

    @Comment({"", "Message received sound"})
    private Sound messageReceivedSound = Sound.BLOCK_NOTE_BLOCK_PLING;

    @Comment({"", "Reply command usage message"})
    private String replyUsageMessage = "[P] &cUżycie: /r <wiadomość>";

    @Comment({"", "Message cant message yourself"})
    private String messageYourselfMessage = "[P] &cNie możesz wysłać wiadomości do siebie.";

    @Comment({"", "Healed yourself message"})
    private String healSelfMessage = "[P] &aUleczyłeś się.";

    @Comment({"", "Healed other message"})
    private String healSenderFormat = "[P] &aUleczyłeś gracza %player%.";

    @Comment({"", "Heal Player not found message"})
    private String healPlayerNotFoundMessage = "[P] &cGracz %player% nie został znaleziony.";

    @Comment({"", "Fed yourself message"})
    private String feedSelfMessage = "[P] &aNakarmiłeś się.";

    @Comment({"", "Fed other message"})
    private String feedSenderFormat = "[P] &aNakarmiłeś gracza %player%.";

    @Comment({"", "Feed Player not found message"})
    private String feedPlayerNotFoundMessage = "[P] &cGracz %player% nie został znaleziony.";

    @Comment({"", "Rename no args message"})
    private String renameNoArgsMessage = "[P] &cUżycie: /rename <nazwa>";

    @Comment({"", "Rename message"})
    private String renameMessage = "[P] &aZmieniono nazwę przedmiotu na &e%name%<reset>&a.";

    @Comment({"", "Rename sound"})
    private Sound renameSound = Sound.BLOCK_ANVIL_USE;

    @Comment({"", "Home opened gui message"})
    private String openHomeMessage = "[P] &aOtwarto menu domów.";

    @Comment({"", "Home menu title"})
    private String homeMenuTitle = "&aMenu domów";

    @Comment({"", "Home teleport message"})
    private String homeTeleportMessage = "[P] &aPrzeteleportowano do domu %home%.";

    @Comment({"", "Home teleport sound"})
    private Sound homeTeleportSound = Sound.ENTITY_ENDERMAN_TELEPORT;

    @Comment({"", "Set home message"})
    private String setHomeMessage = "[P] &aUstawiono dom.";

    @Comment({"", "Set home sound"})
    private Sound setHomeSound = Sound.BLOCK_NOTE_BLOCK_PLING;

    @Comment({"", "Set home usage message"})
    private String setHomeUsageMessage = "[P] &cUżycie: /sethome [nazwa]";

    @Comment({"", "Set home updated message"})
    private String setHomeUpdatedMessage = "[P] &aZaktualizowano dom %home%.";

    @Comment({"", "Set home created message"})
    private String setHomeCreatedMessage = "[P] &aUtworzono dom %home%.";

    @Comment({"", "Set home at limit message"})
    private String setHomeAtLimitMessage = "[P] &cOsiągnięto limit domów! Nie możesz dodać więcej. (Limit: %max%)";

    @Comment({"", "Del home message"})
    private String delHomeMessage = "[P] &aUsunięto dom %home%.";

    @Comment({"", "Del home usage message"})
    private String delHomeUsageMessage = "[P] &cUżycie: /delhome [nazwa]";

    @Comment({"", "Del home not found message"})
    private String delHomeNotFoundMessage = "[P] &cNie znaleziono domu %home%.";

    @Comment({"", "Player ignored message"})
    private String messagePlayerIgnoredMessage = "[P] &cNie możesz wysłać wiadomości do gracza %player%.";

    @Comment({"", "Ignoring message"})
    private String messageIgnoringMessage = "[P] &aIgnorujesz gracza %player%.";

    @Comment({"", "Unignoring message"})
    private String messageUnignoringMessage = "[P] &aPrzestałeś ignorować gracza %player%.";

    @Comment({"", "Ignore command usage message"})
    private String ignoreUsageMessage = "[P] &cUżycie: /ignore <gracz>";

    @Comment({"", "MsgToggle enabled command"})
    private String msgToggleEnabledMessage = "[P] &aWłączono wiadomości prywatne.";

    @Comment({"", "MsgToggle disabled command"})
    private String msgToggleDisabledMessage = "[P] &cWyłączono wiadomości prywatne.";

    @Comment({"", "Home menu already teleporting message"})
    private String homeTeleportingMessage = "[P] &cWłaśnie teleportujesz się do domu.";

    @Comment({"", "Home teleporting right now message"})
    private String homeTeleportingRightNowMessage = "[P] &cTeleportacja do domu w trakcie. Nie ruszaj się przez %time% sekund.";

    @Comment({"", "Kick all message"})
    private String kickAllMessage = "[P] &cWyrzucono wszystkich graczy.";

    @Comment({"", "Kick all reason"})
    private String kickAllReason = "&cPrzerwa techniczna";

    @Comment({"", "Hat command wore a hat message"})
    private String woreHatMessage = "[P] &aZałożono czapkę.";

    @Comment({"", "Hat command no hat message"})
    private String noHatMessage = "[P] &cZdjąłeś czapkę.";

    @Comment({"", "Hat command other wore a hat message"})
    private String otherWoreHatMessage = "[P] &aZałożono czapkę dla gracza %player%.";

    @Comment({"", "Hat command other no hat message"})
    private String otherNoHatMessage = "[P] &cZdjęto czapkę dla gracza %player%.";

    @Comment({"", "Hat command player not found message"})
    private String hatPlayerNotFoundMessage = "[P] &cGracz %player% nie został znaleziony.";

    @Comment({"", "Offline Teleport no args message"})
    private String offlineTeleportNoArgsMessage = "[P] &cUżycie: /otp <gracz>";

    @Comment({"", "Offline Teleport player not found message"})
    private String offlineTeleportPlayerNotFoundMessage = "[P] &cGracz %player% nie został znaleziony.";

    @Comment({"", "Offline Teleport no location message"})
    private String offlineTeleportNoLocationMessage = "[P] &cGracz %player% nie posiada zapisanej lokalizacji.";

    @Comment({"", "Offline Teleport success message"})
    private String offlineTeleportSuccessMessage = "[P] &aPrzeteleportowano do ostatniej lokalizacji gracza %player%.";

    @Comment({"", "Gamma command ON"})
    private String gammaOnMessage = "[P] &aWłączono gammę.";

    @Comment({"", "Gamma command OFF"})
    private String gammaOffMessage = "[P] &cWyłączono gammę.";

    @Comment({"", "Bin command opened message"})
    private String binOpenedMessage = "[P] &aOtwarto kosz.";

    @Comment({"", "Bin gui name"})
    private String binGuiName = "&aKosz";

    @Comment({"", "Invsee command usage message"})
    private String invseeUsageMessage = "[P] &cUżycie: /invsee <gracz>";

    @Comment({"", "Invsee player not found message"})
    private String invseePlayerNotFoundMessage = "[P] &cGracz %player% nie został znaleziony.";

    @Comment({"", "Invsee gui name"})
    private String invseeGuiName = "&aEkwipunek gracza %player%";

    @Comment({"", "Invsee already open message"})
    private String invseeAlreadyOpenMessage = "[P] &cGracz %player% ma już otwarty ekwipunek przez innego administratora.";

    @Comment({"", "BanXray usage message"})
    private String banXrayUsageMessage = "[P] &cUżycie: /banxray <gracz> <reszta argumentów z /ban>";

    @Comment({"", "BanXray player not found message"})
    private String banXrayPlayerNotFoundMessage = "[P] &cGracz %player% nie został znaleziony.";

    @Comment({"", "BanXray success message"})
    private String banXraySuccessMessage = "[P] &aWyczyszczono i zbanowano gracza %player%.";

    @Comment({"", "BanXray not successful message"})
    private String banXrayNotSuccessfulMessage = "[P] &cNie udało się wyczyścić i zbanować gracza %player%.";
}
