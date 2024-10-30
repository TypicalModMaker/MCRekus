package dev.isnow.mcrekus.module.impl.essentials.command;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Named;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.Usage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

@Command({"hat", "czapka"})
@Description("Command to wear a hat")
@Permission("mcrekus.hat")
@SuppressWarnings("unused")
public class HatCommand extends ModuleAccessor<EssentialsModule> {

    @Usage
    @Async
    public void execute(final BukkitSource source) {
        final EssentialsConfig config = getModule().getConfig();

        final Player player = source.asPlayer();

        final PlayerInventory inventory = player.getInventory();
        if (inventory.getHelmet() != null) {
            inventory.addItem(inventory.getHelmet());
            inventory.setHelmet(null);
            source.reply(ComponentUtil.deserialize(config.getNoHatMessage()));
        } else {
            inventory.setHelmet(inventory.getItemInMainHand());
            inventory.setItemInMainHand(null);
            source.reply(ComponentUtil.deserialize(config.getWoreHatMessage()));
        }
    }

    @Usage
    @Async
    public void executeOther(final BukkitSource source, @Named("player") final Player target) {
        final EssentialsConfig config = getModule().getConfig();

        final Player player = source.asPlayer();

        final PlayerInventory inventory = player.getInventory();
        final PlayerInventory targetInventory = target.getInventory();

        if (targetInventory.getHelmet() != null) {
            inventory.addItem(targetInventory.getHelmet());
            targetInventory.setHelmet(null);
            source.reply(ComponentUtil.deserialize(config.getOtherNoHatMessage(), null, "%player%", target.getName()));
        } else {
            targetInventory.setHelmet(inventory.getItemInMainHand());
            inventory.setItemInMainHand(null);
            source.reply(ComponentUtil.deserialize(config.getOtherWoreHatMessage(), null, "%player%", target.getName()));
        }
    }
}
