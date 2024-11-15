package dev.isnow.mcrekus.module.impl.model.util;

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.item.ItemStack.Builder;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.Location;
import com.github.retrooper.packetevents.util.Quaternion4f;
import com.github.retrooper.packetevents.util.Vector3f;
import dev.isnow.mcrekus.module.impl.model.parser.impl.Model;
import dev.isnow.mcrekus.module.impl.model.parser.impl.group.Group;
import dev.isnow.mcrekus.module.impl.model.parser.impl.object.DisplayObject;
import dev.isnow.mcrekus.module.impl.model.parser.impl.object.Transformable;
import dev.isnow.mcrekus.module.impl.model.parser.impl.object.type.BlockDisplay;
import dev.isnow.mcrekus.module.impl.model.parser.impl.object.type.TextDisplay;
import dev.isnow.mcrekus.module.impl.model.parser.impl.object.type.item.HeadDisplay;
import dev.isnow.mcrekus.module.impl.model.parser.impl.object.type.item.ItemDisplay;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.RekusLogger;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;
import me.tofaa.entitylib.meta.display.AbstractDisplayMeta;
import me.tofaa.entitylib.meta.display.BlockDisplayMeta;
import me.tofaa.entitylib.meta.display.ItemDisplayMeta;
import me.tofaa.entitylib.meta.display.ItemDisplayMeta.DisplayType;
import me.tofaa.entitylib.meta.display.TextDisplayMeta;
import me.tofaa.entitylib.meta.types.DisplayMeta;
import me.tofaa.entitylib.wrapper.WrapperEntity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

@UtilityClass
public class ModelUtil {


    public List<WrapperEntity> setupModel(final WrapperEntity base, final Model model) {
        final List<WrapperEntity> entities = new ArrayList<>();

        for(final Transformable children : model.getChildren()) {
            RekusLogger.debug("Spawning: " + children.getClass().getSimpleName());
            if(children instanceof Group group) {
                final List<WrapperEntity> groupEntities = spawnGroupChildren(base, group);

                RekusLogger.debug("Spawned: " + group.getClass().getSimpleName());
                entities.addAll(groupEntities);
            } else if (children instanceof DisplayObject displayObject) {
                final WrapperEntity entity = spawnDisplayObject(base, displayObject);

                RekusLogger.debug("Spawned: " + displayObject.getClass().getSimpleName());
                entities.add(entity);
            }
        }

        return entities;
    }


    public List<WrapperEntity> spawnGroupChildren(final WrapperEntity base, final Group group) {
        final List<WrapperEntity> entities = new ArrayList<>();

        for(final Transformable children : group.getChildren()) {
            if(children instanceof Group) {
                final List<WrapperEntity> groupEntities = spawnGroupChildren(base, (Group) children);

                entities.addAll(groupEntities);
            } else if (children instanceof DisplayObject displayObject) {
                final WrapperEntity entity = spawnDisplayObject(base, displayObject);

                entities.add(entity);
            }
        }

        return entities;
    }

    public WrapperEntity spawnDisplayObject(final WrapperEntity base, final DisplayObject displayObject) {
        WrapperEntity entity = null;

        if (displayObject instanceof BlockDisplay blockDisplay) {
            entity = new WrapperEntity(EntityTypes.BLOCK_DISPLAY);

            final BlockDisplayMeta meta = (BlockDisplayMeta) entity.getEntityMeta();
            meta.setBlockId(SpigotConversionUtil.fromBukkitBlockData(blockDisplay.getMaterial().createBlockData(blockDisplay.getMaterialData())).getGlobalId());
        } else if (displayObject instanceof ItemDisplay itemDisplay) {
            entity = new WrapperEntity(EntityTypes.ITEM_DISPLAY);
            final ItemDisplayMeta meta = (ItemDisplayMeta) entity.getEntityMeta();
            meta.setDisplayType(itemDisplay.getDisplayType().convert());

            if(itemDisplay instanceof HeadDisplay headDisplay) {
                meta.setItem(SpigotConversionUtil.fromBukkitItemStack(SkullCreator.itemFromBase64(headDisplay.getTextureValue())));
            } else {
                meta.setItem(new Builder().type(SpigotConversionUtil.fromBukkitItemMaterial(itemDisplay.getItem())).build());
            }
        } else if (displayObject instanceof TextDisplay textDisplay) {
            entity = new WrapperEntity(EntityTypes.TEXT_DISPLAY);

            final TextDisplayMeta meta = (TextDisplayMeta) entity.getEntityMeta();

            Component component = ComponentUtil.deserialize(textDisplay.getText());

            if (textDisplay.isBold()) {
                component = component.decorate(TextDecoration.BOLD);
            }

            if (textDisplay.isItalic()) {
                component = component.decorate(TextDecoration.ITALIC);
            }

            if (textDisplay.isUnderline()) {
                component = component.decorate(TextDecoration.UNDERLINED);
            }

            if (textDisplay.isStrikeThrough()) {
                component = component.decorate(TextDecoration.STRIKETHROUGH);
            }

            if (textDisplay.isObfuscated()) {
                component = component.decorate(TextDecoration.OBFUSCATED);
            }

            meta.setText(component);
            meta.setLineWidth(textDisplay.getLineLength());
            meta.setBackgroundColor(textDisplay.getBackgroundColor().setAlpha((int) (textDisplay.getBackgroundAlpha() * 255)).asARGB());
            meta.setTextOpacity((byte) ((int) (textDisplay.getAlpha() * 255)));
            switch (textDisplay.getAlign()) {
                case LEFT -> meta.setAlignLeft(true);
                case RIGHT -> meta.setAlignRight(true);
            }
        }

        final AbstractDisplayMeta meta = (AbstractDisplayMeta) entity.getEntityMeta();

        meta.setTranslation(displayObject.getTranslation());
        meta.setLeftRotation(displayObject.getLeftRotation());
        meta.setScale(displayObject.getScale());
        meta.setRightRotation(displayObject.getRightRotation());

        meta.setBrightnessOverride(displayObject.getBrightness().getBlock() << 4 | displayObject.getBrightness().getSky() << 20);

        entity.spawn(base.getLocation());
        base.addPassenger(entity);

        return entity;
    }

}
