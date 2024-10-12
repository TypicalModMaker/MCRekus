package dev.isnow.mcrekus.module.impl.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

@CommandAlias("hat|czapka")
@Description("Command to wear a hat")
@CommandPermission("mcrekus.hat")
@SuppressWarnings("unused")
public class HatCommand extends BaseCommand {

    private final ModuleAccessor<EssentialsModule> moduleAccessor = new ModuleAccessor<>(EssentialsModule.class);

    @Default
    @CommandCompletion("@players")
    public void execute(Player player, String[] args) {
        final EssentialsConfig config = moduleAccessor.getModule().getConfig();

        final PlayerInventory inventory = player.getInventory();
        if(args.length == 0) {
            if (inventory.getHelmet() != null) {
                inventory.addItem(inventory.getHelmet());
                inventory.setHelmet(null);
                player.sendMessage(ComponentUtil.deserialize(config.getNoHatMessage()));
            } else {
                inventory.setHelmet(inventory.getItemInMainHand());
                inventory.setItemInMainHand(null);
                player.sendMessage(ComponentUtil.deserialize(config.getWoreHatMessage()));
            }

            return;
        }

        final Player target = player.getServer().getPlayer(args[0]);
        if(target == null) {
            player.sendMessage(ComponentUtil.deserialize(config.getHatPlayerNotFoundMessage(), null, "%player%", args[0]));
            return;
        }

        final PlayerInventory targetInventory = target.getInventory();
        if (targetInventory.getHelmet() != null) {
            inventory.addItem(targetInventory.getHelmet());
            targetInventory.setHelmet(null);
            player.sendMessage(ComponentUtil.deserialize(config.getOtherNoHatMessage(), null, "%player%", target));
        } else {
            targetInventory.setHelmet(inventory.getItemInMainHand());
            inventory.setItemInMainHand(null);
            player.sendMessage(ComponentUtil.deserialize(config.getOtherWoreHatMessage(), null, "%player%", target));
        }

    }
}
