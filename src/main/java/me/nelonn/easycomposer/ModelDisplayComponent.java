package me.nelonn.easycomposer;

import com.mojang.math.Transformation;
import me.nelonn.easycomposer.dynamic.FutureDisplayRotation;
import me.nelonn.easycomposer.dynamic.FutureRotation;
import me.nelonn.easycomposer.rotation.Rotation2d;
import me.nelonn.easycomposer.transform.Property;
import me.nelonn.easycomposer.transform.Transform;
import me.nelonn.easycomposer.utility.Adapter;
import me.nelonn.entitycomposer.shaded.bestvecs.ImmVec3d;
import me.nelonn.entitycomposer.shaded.bestvecs.Vec3f;
import me.nelonn.flint.path.Key;
import me.nelonn.flint.path.Path;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Display;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

import java.util.Set;

public class ModelDisplayComponent extends EntityComponent {
    public static class ModelBuilder {
        private Item item;
        private Path model;

        public ModelBuilder(@NotNull Item item) {
            this.item = item;
        }

        public ModelBuilder(@NotNull Key itemId) {
            this(BuiltInRegistries.ITEM.get(new ResourceLocation(itemId.namespace(), itemId.value())));
        }

        public @NotNull ModelBuilder model(Path model) {
            this.model = model;
            return this;
        }

        public @NotNull ItemStack build() {
            ItemStack itemStack = new ItemStack(item);
            itemStack.getOrCreateTag().putString("CustomModel", model.toString());
            return itemStack;
        }
    }

    public static final Set<Property<?>> SUPPORTED_PROPERTIES = Set.of(Property.values());
    private final FutureRotation rotation;
    private final FutureDisplayRotation displayLeftRotation;
    private Vec3f scale;
    private Vec3f translation;

    public ModelDisplayComponent(@NotNull AActor actor, @NotNull String name, @Nullable ItemStack itemStack, @Nullable ItemDisplayContext itemTransform, @Nullable FutureRotation rotation, @Nullable FutureDisplayRotation displayLeftRotation) {
        super(actor, name, new ModelDisplay(actor.getRoot().asEntity().level()));
        ModelDisplay display = getEntity();
        display.setActor(actor);
        display.setItemStack(itemStack != null ? itemStack : ItemStack.EMPTY);
        display.setItemTransform(itemTransform != null ? itemTransform : ItemDisplayContext.THIRD_PERSON_RIGHT_HAND);
        display.setTransformationInterpolationDelay(0);
        display.setTransformationInterpolationDuration(4);
        display.getEntityData().set(Display.DATA_POS_ROT_INTERPOLATION_DURATION_ID, 2);
        this.rotation = rotation;
        this.displayLeftRotation = displayLeftRotation;
    }

    @Override
    public @NotNull ModelDisplay getEntity() {
        return (ModelDisplay) super.getEntity();
    }

    @Override
    public void moveTo(double x, double y, double z) {
        if (Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z)) {
            System.err.println("NaN");
            return;
        }
        getEntity().moveTo(x, y, z);
    }

    public void rotateTo(float pitch, float yaw) {
        getEntity().setYRot(yaw);
        getEntity().setXRot(pitch);
        getEntity().setOldPosAndRot();
        getEntity().setYHeadRot(yaw);
    }

    public void rotateTo(@NotNull Rotation2d rotation) {
        this.rotateTo((float) Math.toDegrees(rotation.pitch()), (float) Math.toDegrees(rotation.yaw()));
    }

    public void setScale(Vec3f scale) {
        this.scale = scale;
    }

    public void setTranslation(Vec3f translation) {
        this.translation = translation;
    }

    @Override
    public void applyTransforms(@NotNull Level world, @NotNull ImmVec3d rootPos, @NotNull Transform transform) {
        super.applyTransforms(world, rootPos, transform);

        Rotation2d rotation = transform.get(Property.ROTATION);
        if (rotation != null) {
            rotateTo(rotation);
        }

        Vec3f translation = transform.get(Property.DISPLAY_TRANSLATION);
        Quaternionf leftRotation = transform.get(Property.DISPLAY_LEFT_ROTATION);
        Vec3f scale = transform.get(Property.DISPLAY_SCALE);
        Quaternionf rightRotation = transform.get(Property.DISPLAY_RIGHT_ROTATION);

        Transformation transformation = new Transformation(Adapter.adapt(translation), leftRotation, Adapter.adapt(scale), rightRotation);

        getEntity().setTransformation(transformation);
    }

    @Override
    public @NotNull Transform baseTransform() {
        Transform transform = super.baseTransform();
        if (rotation != null) {
            transform.set(Property.ROTATION, rotation.apply(getActor().getVariables()));
        }
        if (displayLeftRotation != null) {
            transform.set(Property.DISPLAY_LEFT_ROTATION, displayLeftRotation.apply(getActor().getVariables()));
        }
        if (scale != null) {
            transform.set(Property.DISPLAY_SCALE, scale.mutableCopy());
        }
        if (translation != null) {
            transform.set(Property.DISPLAY_TRANSLATION, translation.mutableCopy());
        }
        return transform;
    }

    @Override
    public @NotNull Set<Property<?>> getSupportedProperties() {
        return SUPPORTED_PROPERTIES;
    }
}
