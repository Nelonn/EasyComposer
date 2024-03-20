package me.nelonn.easycomposer;

import me.nelonn.easycomposer.transform.Property;
import me.nelonn.easycomposer.transform.RelativePosition;
import me.nelonn.easycomposer.transform.Transform;
import me.nelonn.entitycomposer.shaded.bestvecs.ImmVec3d;
import me.nelonn.entitycomposer.shaded.bestvecs.MutVec3d;
import me.nelonn.entitycomposer.shaded.bestvecs.Vec3d;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Set;

public abstract class AComponent {
    public static final Set<Property<?>> DEFAULT_PROPERTIES = Set.of(Property.POSITION);
    private final AActor actor;
    private final String name;
    private RelativePosition position;

    public AComponent(@NotNull AActor actor, @NotNull String name) {
        this.actor = actor;
        this.name = name.toLowerCase(Locale.ENGLISH);
    }

    public @NotNull AActor getActor() {
        return actor;
    }

    public @NotNull String getName() {
        return name;
    }

    public abstract void moveTo(double x, double y, double z);

    public void onRemove() {
    }

    public void applyTransforms(@NotNull Level world, @NotNull ImmVec3d rootPos, @NotNull Transform transform) {
        MutVec3d pos = rootPos.mutableCopy();
        Vec3d transformPosition = transform.get(Property.POSITION);
        if (transformPosition != null) {
            pos.add(transformPosition);
        }
        moveTo(pos.x(), pos.y(), pos.z());
    }

    public @NotNull Transform baseTransform() {
        Transform transform = new Transform();
        if (position == null) return transform;
        Vec3d positionVec = position.apply(actor.getVariables());
        transform.set(Property.POSITION, positionVec);
        return transform;
    }

    public void compose(@NotNull Level world, @NotNull ImmVec3d rootPos, @NotNull Transform transform) {
        Transform baseTransform = this.baseTransform();
        baseTransform.add(transform);
        applyTransforms(world, rootPos, baseTransform);
    }

    public @Nullable RelativePosition getPosition() {
        return position;
    }

    public void setPosition(@Nullable RelativePosition position) {
        this.position = position;
    }

    public @NotNull Set<Property<?>> getSupportedProperties() {
        return DEFAULT_PROPERTIES;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + '[' + getName() + ']';
    }
}
