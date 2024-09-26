package dev.isnow.mcrekus.module.impl.spawn.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import dev.isnow.mcrekus.module.ModuleConfig;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;

@Getter
@Setter
@Configuration
public class SpawnConfig extends ModuleConfig {
    public SpawnConfig() {
        super("config", "Spawn");
    }

    @Comment({"Spawn module configuration (/setspawn and /spawn)", "", "Spawn Location"})
    private RekusLocation spawnLocation = new RekusLocation(Bukkit.getWorlds().getFirst(), 0, 200, 0, 50, 200);

    @Comment({"", "Spawn teleport delay in seconds (how much time player has to wait before teleporting to spawn)", "Set to 0 to disable"})
    private int spawnTeleportDelay = 5;

    @Comment({"", "Spawn teleport cooldown in seconds (how much time player has to wait before teleporting to spawn again)", "Set to 0 to disable"})
    private int spawnTeleportCooldown = 10;

    @Comment({"", "Spawn sound effect"})
    private Sound spawnSound = Sound.ENTITY_ENDER_PEARL_THROW;

    @Comment({"", "Spawn effect"})
    private Effect spawnEffect = Effect.ENDER_SIGNAL;

    @Comment({"", "Teleport to spawn on respawn"})
    private boolean teleportOnRespawn = true;

    @Comment({"", "Teleport to spawn on first join"})
    private boolean teleportOnFirstJoin = true;

    @Comment({"", "Spawn cooldown and delay bypass permission"})
    private String spawnCooldownBypassPermission = "mcrekus.spawn.bypass";

    @Comment({"", " ", "-------------------------", "Spawn Messages", "-------------------------", " ", "", "Successful /setspawn message"})
    private String spawnSetSuccessfullyMessage = "[P] &#22FF00P&#21FF03o&#1FFF06m&#1EFF09y&#1DFF0Dś&#1BFF10l&#1AFF13n&#18FF16i&#17FF19e &#14FF20u&#13FF23s&#12FF26t&#10FF29a&#0FFF2Cw&#0EFF2Fi&#0CFF33o&#0BFF36n&#0AFF39o &#07FF3Fs&#05FF42p&#04FF46a&#03FF49w&#01FF4Cn&#00FF4F.";

    @Comment({"", "/spawn delay message"})
    private String spawnDelayMessage = "[P] &#9D9D9DT&#9C9C9Ce&#9C9C9Cl&#9B9B9Be&#9A9A9Ap&#9A9A9Ao&#999999r&#989898t&#989898a&#979797c&#969696j&#969696a &#949494z&#949494a &#22FF00%time% &#18FF17s&#13FF22e&#0FFF2Dk&#0AFF38u&#05FF44n&#00FF4Fd&#939393! &#919191N&#919191i&#909090e &#8F8F8Fr&#8E8E8Eu&#8D8D8Ds&#8D8D8Dz&#8C8C8Ca&#8B8B8Bj &#8A8A8As&#898989i&#898989ę&#888888!";

    @Comment({"", "Successful /spawn teleport message"})
    private String spawnTeleportedMessage = "[P] &#22FF00P&#21FF02o&#20FF05m&#1FFF07y&#1EFF09ś&#1DFF0Bl&#1CFF0En&#1BFF10i&#1AFF12e &#18FF17p&#17FF19r&#16FF1Bz&#15FF1Dt&#14FF20e&#13FF22l&#12FF24e&#11FF26p&#11FF29o&#10FF2Br&#0FFF2Dt&#0EFF2Fo&#0DFF32w&#0CFF34a&#0BFF36n&#0AFF38o &#08FF3Dn&#07FF3Fa &#05FF44s&#04FF46p&#03FF48a&#02FF4Aw&#01FF4Dn&#00FF4F!";

    @Comment({"", "Cooldown message for /spawn"})
    private String spawnOnCooldownMessage = "[P] &#FF0000P&#FF0101o&#FF0202c&#FF0303z&#FF0404e&#FF0505k&#FF0606a&#FF0707j &#FF0909j&#FF0909e&#FF0A0As&#FF0B0Bz&#FF0C0Cc&#FF0D0Dz&#FF0E0Ee %time% &#FF1010s&#FF1111e&#FF1212k&#FF1313u&#FF1414n&#FF1515d &#FF1717z&#FF1818a&#FF1919n&#FF1A1Ai&#FF1B1Bm &#FF1C1Cz&#FF1D1Dn&#FF1E1Eo&#FF1F1Fw&#FF2020u &#FF2222u&#FF2323ż&#FF2424y&#FF2525j&#FF2626e&#FF2727s&#FF2828z &#FF2A2Ak&#FF2B2Bo&#FF2C2Cm&#FF2D2De&#FF2E2En&#FF2E2Ed&#FF2F2Fy &#FF3131/&#FF3232s&#FF3333p&#FF3434a&#FF3535w&#FF3636n&#FF3737!";

    @Comment({"", "Player move cancel message"})
    private String spawnOnPlayerMoveMessage = "[P] &#FF0000R&#FF0202u&#FF0303s&#FF0505z&#FF0606y&#FF0808ł&#FF0909e&#FF0B0Bś &#FF0E0Es&#FF0F0Fi&#FF1111ę&#FF1212! &#FF1515T&#FF1717e&#FF1818l&#FF1A1Ae&#FF1C1Cp&#FF1D1Do&#FF1F1Fr&#FF2020t&#FF2222a&#FF2323c&#FF2525j&#FF2626a &#FF2929a&#FF2B2Bn&#FF2C2Cu&#FF2E2El&#FF2F2Fo&#FF3131w&#FF3232a&#FF3434n&#FF3535a&#FF3737!";
}
