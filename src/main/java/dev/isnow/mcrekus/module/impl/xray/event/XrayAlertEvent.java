package dev.isnow.mcrekus.module.impl.xray.event;

import com.alessiodp.oreannouncer.api.events.bukkit.common.BukkitOreAnnouncerAdvancedAlertEvent;
import com.alessiodp.oreannouncer.api.interfaces.BlockLocation;
import com.alessiodp.oreannouncer.api.interfaces.OAPlayer;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.xray.XrayModule;
import dev.isnow.mcrekus.util.DurationUtils;
import dev.isnow.mcrekus.util.RekusLogger;
import java.awt.Color;
import java.io.IOException;
import me.micartey.webhookly.DiscordWebhook;
import me.micartey.webhookly.embeds.EmbedObject;
import me.micartey.webhookly.embeds.Thumbnail;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class XrayAlertEvent extends ModuleAccessor<XrayModule> implements Listener {

    @EventHandler
    public void onXrayAlert(final BukkitOreAnnouncerAdvancedAlertEvent event) {
        if (!event.getBlock().getDisplayName().equals("Ancient Debris")) return;

        final BlockLocation location = event.getLocation();

        if (!location.getWorld().equals("earthsmp_nether")) return;

        final OAPlayer player = event.getPlayer();

        final DiscordWebhook webhook = new DiscordWebhook(getModule().getConfig().getDiscordHookUrl());

        final EmbedObject embed = getEmbed(event.getTotalBlocks(), event.getElapsedTime(), player, location);

        webhook.getEmbeds().add(embed);

        try {
            webhook.execute();
            RekusLogger.debug("Sent webhook!");
        } catch (IOException e) {
            RekusLogger.error("Failed to send webhook!");
            e.printStackTrace();
        }
    }

    private EmbedObject getEmbed(final int totalBlocks, final long elapsedTime, final OAPlayer player, final BlockLocation location) {
        final EmbedObject embed = new EmbedObject();
        embed.setTitle(player.getName());

        embed.setDescription("test");
        embed.setDescription(
                "> Wykopał **" + totalBlocks + "** debrisu w ciągu **" + DurationUtils.formatElapsed(elapsedTime) + "**!\\n" +
                "\\n" +
                "> Kordynaty:\\n" +
                "> X: " + (int) location.getX() + "\\n" +
                "> Y: " + (int) location.getY() + "\\n" +
                "> Z: " + (int) location.getZ() + "\\n" +
                "\\n" +
                "> Komendy:\\n" +
                "> /tp " + player.getName() + "\\n" +
                "> /tp " + (int) location.getX() + " " + (int) location.getY() + " " + (int) location.getZ() + "\\n" +
                "> /banxray " + player.getName() + " 3d\\n" +
                "> /tempban " + player.getName() + " Udowodniono u Ciebie używanie niedozwolonej modyfikacji tekstur - X-ray. 3d"
        );

        embed.setColor(Color.RED);
        embed.setThumbnail(new Thumbnail("https://mc-heads.net/head/" + player.getPlayerUUID()));

        return embed;
    }
}
