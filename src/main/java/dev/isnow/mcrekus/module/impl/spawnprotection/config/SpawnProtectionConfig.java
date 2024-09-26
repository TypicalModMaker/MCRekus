package dev.isnow.mcrekus.module.impl.spawnprotection.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import dev.isnow.mcrekus.module.ModuleConfig;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

@Getter
@Setter
@Configuration
public class SpawnProtectionConfig extends ModuleConfig {
    public SpawnProtectionConfig() {
        super("config", "SpawnProtection");
    }

    @Comment({"Spawn Protection module configuration (WorldGuard Alternative)", "", "Pos1 of the Cuboid location"})
    private RekusLocation pos1 = new RekusLocation(Bukkit.getWorlds().get(0), 0, 0, 0);

    @Comment({"", "Pos2 of the Cuboid location"})
    private RekusLocation pos2 = new RekusLocation(Bukkit.getWorlds().get(0), 100, 100, 100);

    @Comment({"", " ", "-------------------------", "Spawn Protection Messages", "-------------------------", " ", "", "Trying to claim land at spawn message"})
    private String spawnClaimMessage = "[P] &#FF0000N&#FF0202i&#FF0303e &#FF0606m&#FF0808o&#FF0A0Aż&#FF0B0Be&#FF0D0Ds&#FF0F0Fz &#FF1212z&#FF1313a&#FF1515j&#FF1717ą&#FF1818ć &#FF1C1Ct&#FF1D1Du&#FF1F1Ft&#FF2020e&#FF2222j&#FF2424s&#FF2525z&#FF2727e&#FF2828g&#FF2A2Ao &#FF2D2Dt&#FF2F2Fe&#FF3131r&#FF3232e&#FF3434n&#FF3535u&#FF3737.";

    @Comment({"", "Trying to throw an ender pearl message"})
    private String spawnEnderPearlMessage = "[P] &#FF0000N&#FF0101i&#FF0303e &#FF0606m&#FF0707o&#FF0808ż&#FF0A0Ae&#FF0B0Bs&#FF0D0Dz &#FF1010u&#FF1111ż&#FF1212y&#FF1414ć &#FF1717e&#FF1818n&#FF1919d&#FF1B1Be&#FF1C1Cr&#FF1E1Ep&#FF1F1Fe&#FF2020r&#FF2222ł&#FF2323y &#FF2626w &#FF2929t&#FF2A2Ay&#FF2C2Cm &#FF2F2Fm&#FF3030i&#FF3131e&#FF3333j&#FF3434s&#FF3636c&#FF3737u.";

    @Comment({"", "Trying to break a block message"})
    private String spawnBlockBreakMessage = "[P] &#FF0000N&#FF0202i&#FF0404e &#FF0808m&#FF0909o&#FF0B0Bż&#FF0D0De&#FF0F0Fs&#FF1111z &#FF1515w&#FF1717y&#FF1919k&#FF1B1Bo&#FF1C1Cp&#FF1E1Ea&#FF2020ć &#FF2424t&#FF2626e&#FF2828g&#FF2A2Ao &#FF2E2Eb&#FF2F2Fl&#FF3131o&#FF3333k&#FF3535u&#FF3737.";

//    @Comment({"", "Trying to break a block message"})
//    private String spawnBlockBreakMessage = "[P]";

    @Comment({"", "Trying to place a block message"})
    private String spawnBlockPlaceMessage = "[P] &#FF0000N&#FF0202i&#FF0404e &#FF0707m&#FF0909o&#FF0B0Bż&#FF0D0De&#FF0F0Fs&#FF1111z &#FF1414p&#FF1616o&#FF1818s&#FF1A1At&#FF1C1Ca&#FF1D1Dw&#FF1F1Fi&#FF2121ć &#FF2525t&#FF2727e&#FF2828g&#FF2A2Ao &#FF2E2Eb&#FF3030l&#FF3232o&#FF3333k&#FF3535u&#FF3737.";

    @Comment({"", "Trying to interact with a block message"})
    private String spawnBlockInteractMessage = "[P] &#FF0000N&#FF0101i&#FF0303e &#FF0505m&#FF0606o&#FF0808ż&#FF0909e&#FF0A0As&#FF0C0Cz &#FF0E0Ew&#FF0F0Fe&#FF1111j&#FF1212ś&#FF1313ć &#FF1616w &#FF1818i&#FF1A1An&#FF1B1Bt&#FF1C1Ce&#FF1D1Dr&#FF1F1Fk&#FF2020a&#FF2121c&#FF2323j&#FF2424e &#FF2626z &#FF2929t&#FF2A2Ay&#FF2B2Bm &#FF2E2Eb&#FF2F2Fl&#FF3131o&#FF3232k&#FF3333i&#FF3434e&#FF3636m&#FF3737.";

    @Comment({"", "Trying to mount an entity"})
    private String spawnMountEntityMessage = "[P] &#FF0000N&#FF0202i&#FF0404e &#FF0707m&#FF0909o&#FF0B0Bż&#FF0D0De&#FF0F0Fs&#FF1111z &#FF1414w&#FF1616e&#FF1818j&#FF1A1Aś&#FF1C1Cć &#FF1F1Fn&#FF2121a &#FF2525t&#FF2727e&#FF2828n &#FF2C2Co&#FF2E2Eb&#FF3030i&#FF3232e&#FF3333k&#FF3535t&#FF3737.";

    @Comment({"", "Trying to destroy a vehicle"})
    private String spawnDestroyVehicleMessage = "[P] &#FF0000N&#FF0202i&#FF0404e &#FF0707m&#FF0909o&#FF0B0Bż&#FF0D0De&#FF0F0Fs&#FF1111z &#FF1414u&#FF1616s&#FF1818u&#FF1A1An&#FF1C1Cą&#FF1D1Dć &#FF2121t&#FF2323e&#FF2525g&#FF2727o &#FF2A2Ap&#FF2C2Co&#FF2E2Ej&#FF3030a&#FF3232z&#FF3333d&#FF3535u&#FF3737.";
}
