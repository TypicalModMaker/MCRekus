package dev.isnow.mcrekus.module.impl.xray.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import dev.isnow.mcrekus.module.ModuleConfig;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Sound;

@Getter
@Setter
@Configuration
public class XrayConfig extends ModuleConfig {
    public XrayConfig() {
        super("config", "Xray");
    }

    @Comment({"", "BanXray usage message"})
    private String banXrayUsageMessage = "[P] &cUżycie: /banxray <gracz> <reszta argumentów z /ban>";

    @Comment({"", "BanXray player not found message"})
    private String banXrayPlayerNotFoundMessage = "[P] &cGracz %player% nie został znaleziony.";

    @Comment({"", "BanXray success message"})
    private String banXraySuccessMessage = "[P] &aWyczyszczono i zbanowano gracza %player%.";

    @Comment({"", "BanXray not successful message"})
    private String banXrayNotSuccessfulMessage = "[P] &cNie udało się wyczyścić i zbanować gracza %player%.";

    @Comment({"", "Xray discord hook url"})
    private String discordHookUrl = "https://discord.com/api/webhooks/1298029756511555654/6QEAD0RLl6SoF_KjZV6ZahNkOQN2MlIDK9GqOKOIlKRZk65OZYO8KtkjGPpll-z2zmSJ";
}
