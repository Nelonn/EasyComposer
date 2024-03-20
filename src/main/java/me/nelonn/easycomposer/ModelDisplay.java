package me.nelonn.easycomposer;

import me.nelonn.easycomposer.utility.ActorPart;
import me.nelonn.entitycomposer.api.actor.Actor;
import me.nelonn.entitycomposer.paper.BukkitEntity;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftItemDisplay;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModelDisplay extends Display.ItemDisplay implements ActorPart {
    private final BukkitEntity<ModelDisplay, CraftItemDisplay> bukkitEntity = new BukkitEntity<>(this, CraftItemDisplay::new);
    private Actor actor;

    public ModelDisplay(EntityType<?> type, Level world) {
        super(type, world);
    }

    public ModelDisplay(Level world) {
        this(AllEntities.MODEL_DISPLAY, world);
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    @Override
    public @Nullable Actor getActor() {
        return actor;
    }

    public void setActor(@Nullable Actor actor) {
        this.actor = actor;
    }

    public @NotNull CraftEntity getBukkitEntity() {
        return this.bukkitEntity.getBukkitEntity();
    }

    public @NotNull CraftEntity getBukkitEntityRaw() {
        return this.bukkitEntity.getBukkitEntityRaw();
    }
}
