package dev.isnow.mcrekus.module.impl.timeshop.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import dev.isnow.mcrekus.module.ModuleConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
public class TimeShopConfig extends ModuleConfig {
    public TimeShopConfig() {
        super("config", "TimeShop");
    }

    @Comment({"", "Time shop menu title"})
    private String timeShopMenuTitle = "       >> <gradient:#15FB08:#1BCB00><bold><tinify>SKLEP ZA CZAS</tinify></gradient><reset> <<";

    @Comment({"", "Time shop open message"})
    private String timeShopOpenMessage = "[P] &aOtworzono sklep z czasem!";

}
