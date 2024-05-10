package me.nelonn.easycomposer;

import me.nelonn.easycomposer.utility.ActorPart;
import me.nelonn.entitycomposer.api.actor.Actor;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ModelDisplay extends Display.ItemDisplay implements ActorPart {
    private Actor actor;

    public ModelDisplay(EntityType<?> type, Level world) {
        super(type, world);
    }

    public ModelDisplay(Level world) {
        this(EntityType.ITEM_DISPLAY, world);
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    @Override
    public @Nullable Actor getActor() {
        return actor;
    }

    public void setActor(@Nullable Actor actor) {
        this.actor = actor;
    }
}
