package me.nelonn.easycomposer;

import me.nelonn.easycomposer.animation.Animation;
import me.nelonn.easycomposer.animation.AnimationType;
import me.nelonn.easycomposer.rotation.MutRotation3d;
import me.nelonn.easycomposer.rotation.Rotation3d;
import me.nelonn.easycomposer.transform.Transform;
import me.nelonn.easycomposer.utility.Utility;
import me.nelonn.easycomposer.variable.FlexibleVariablesMap;
import me.nelonn.easycomposer.variable.VariableKey;
import me.nelonn.easycomposer.variable.VariablesMap;
import me.nelonn.entitycomposer.api.EntityComposer;
import me.nelonn.entitycomposer.api.Root;
import me.nelonn.entitycomposer.api.actor.Actor;
import me.nelonn.entitycomposer.api.actor.ActorType;
import me.nelonn.entitycomposer.shaded.bestvecs.ImmVec3d;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

public abstract class AActor extends Actor {
    public static final VariableKey<Rotation3d> MAIN_ROTATION = new VariableKey<>("main_rotation", Rotation3d.class, Rotation3d.ZERO);
    private final Map<String, AComponent> components = new HashMap<>();
    private final Collection<AComponent> allComponents = Collections.unmodifiableCollection(components.values()); // cache field
    private final Map<AnimationType<?>, Animation> playingAnimations = new HashMap<>();
    protected final MutRotation3d mainRotation = new MutRotation3d(0, 0, 0);
    private double lastTickTime = (double) System.currentTimeMillis() / 1000.0D;
    protected FlexibleVariablesMap variables = new FlexibleVariablesMap();

    public AActor(@NotNull ActorType<?> type, @NotNull Root root) {
        super(type, root);
        variables.add(MAIN_ROTATION, (Supplier<Rotation3d>) this::getMainRotation);
    }

    @Override
    public void assemble(@Nullable CompoundTag nbt) {
        super.assemble(nbt);
        moveTo(getRoot().asEntity().getX(), getRoot().asEntity().getY(), getRoot().asEntity().getZ()); // probably not needed
    }

    public void addComponent(@NotNull AComponent component) {
        String componentName = component.getName();
        if (componentName.equals("root") || componentName.equals("this") || componentName.equals("self")) {
            throw new IllegalArgumentException("You cannot add component with name '" + componentName + "'");
        }
        components.put(componentName, component);
    }

    public @Nullable AComponent removeComponent(@NotNull String name) {
        return components.remove(name.toLowerCase(Locale.ENGLISH));
    }

    public @Nullable AComponent getComponent(@NotNull String name) {
        return components.get(name.toLowerCase(Locale.ENGLISH));
    }

    public <T extends AComponent> @Nullable T getTypedComponent(@NotNull String name) {
        return Utility.safeCast(getComponent(name));
    }

    public @NotNull Collection<AComponent> getAllComponents() {
        return allComponents;
    }

    // mutable copy
    public @NotNull MutRotation3d getMainRotation() {
        return mainRotation.mutableCopy();
    }

    public @NotNull Collection<Animation> getPlayingAnimations() {
        return playingAnimations.values();
    }

    public void playAnimation(@NotNull AnimationType<?> animationType) {
        Animation animation = animationType.create(this, variables);
        playingAnimations.put(animationType, animation);
    }

    public @NotNull VariablesMap getVariables() {
        return variables;
    }

    @Override
    public final void tick() {
        super.tick();
        double current = (double) System.currentTimeMillis() / 1000.0D;
        float deltaTime = (float) (current - this.lastTickTime);
        this.lastTickTime = current;
        tick(deltaTime);
        compose();
    }

    public abstract void tick(float deltaTime);

    @Override
    public void compose() {
        ImmVec3d rootPos = position().toImmutable();

        playingAnimations.values().removeIf(animation -> !animation.isRunning());

        for (AComponent component : getAllComponents()) {
            Transform transform = new Transform();
            if (!playingAnimations.isEmpty()) {
                for (Animation animation : playingAnimations.values()) {
                    try {
                        Transform animationTransform = animation.animate(component);
                        if (animationTransform != null) {
                            transform.add(animationTransform);
                        }
                    } catch (Throwable e) {
                        EntityComposer.get().getLogger().error("Unable to animate component", e);
                    }
                }
            }
            try {
                component.compose(level(), rootPos, transform);
            } catch (Throwable e) {
                EntityComposer.get().getLogger().error("Unable to move component", e);
            }
        }
    }

    @Override
    public void discard() {
        super.discard();
        for (AComponent component : getAllComponents()) {
            try {
                component.onRemove();
            } catch (Throwable e) {
                EntityComposer.get().getLogger().error("Unable to remove component", e);
            }
        }
    }
}
