package dev.isnow.mcrekus.module.impl.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandAlias("skull")
@Description("Command to get skull from texture")
@CommandPermission("mcrekus.skull")
@SuppressWarnings("unused")
public class SkullCommand extends BaseCommand {
    @Default
    public void execute(Player player, String[] args) {
        if (args.length < 1) return;

        player.getInventory().addItem(getSkull(args[0]));
    }

    public ItemStack getSkull(String texture) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        if (texture.isEmpty()) {
            return head;
        }
//
//        final String hash = UUID.randomUUID().toString().replace("-", "");
//        for (int i = 0; i < hash.length(); i+=4) {
//
//        }
//        Bukkit.getUnsafe().modifyItemStack(head, "minecraft:player_head[minecraft:profile={id:[" + hashAsId + "],properties:[{name:\"textures\",\"value\":\"" + texture + "\"}]}]]");

        return head;
    }
}
