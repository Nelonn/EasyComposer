package me.nelonn.easycomposer.animation;

import com.mojang.datafixers.util.Pair;
import me.nelonn.easycomposer.AActor;
import me.nelonn.easycomposer.AComponent;
import me.nelonn.easycomposer.variable.VariablesMap;
import me.nelonn.easycomposer.transform.Property;
import me.nelonn.easycomposer.transform.Transform;
import me.nelonn.easycomposer.utility.ECMath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class KeyframeAnimation extends Animation {
    private final double duration; // seconds, tick = 0.05 s
    private final boolean loop;
    private final KeyframeMap frames;
    private final double startedTime;

    public KeyframeAnimation(@NotNull KeyframeAnimationType type, @NotNull AActor actor, @NotNull VariablesMap properties) {
        super(type, actor, properties);
        this.duration = type.getDuration();
        this.loop = type.isLoop();
        this.frames = type.getFrames();
        this.startedTime = KeyframeAnimation.time();
    }

    public static double time() {
        return ((double) System.currentTimeMillis()) / 1000.0D;
    }

    private double animationTime() {
        return KeyframeAnimation.time() - startedTime;
    }

    @Override
    public boolean isRunning() {
        return loop || (animationTime() <= duration);
    }

    @Override
    public @Nullable Transform animate(@NotNull AComponent bone) {
        double time = animationTime() % duration;

        KeyframeMap.Session session = frames.bone(bone.getName());
        if (session == null) return null;

        Transform result = new Transform();

        for (Property<?> property : bone.getSupportedProperties()) {
            KeyframeMap.Session propertySession = session.property(property);
            if (propertySession == null) continue;

            me.nelonn.easycomposer.animation.Keyframe exactKeyframe = propertySession.findKeyframe(time);
            Object object;
            if (exactKeyframe != null) {
                object = Objects.requireNonNull(exactKeyframe.getBoneTransforms().get(bone.getName())
                        .getTransform(property)).apply(bone.getActor().getVariables());
            } else {
                Pair<me.nelonn.easycomposer.animation.Keyframe, me.nelonn.easycomposer.animation.Keyframe> pair = propertySession.findSurroundingKeyframes(time);

                me.nelonn.easycomposer.animation.Keyframe left = pair.getFirst();
                if (left == null) {
                    left = me.nelonn.easycomposer.animation.Keyframe.zero(bone.getName(), 0, property);
                }
                me.nelonn.easycomposer.animation.Keyframe right = pair.getSecond();
                if (right == null) {
                    right = Keyframe.zero(bone.getName(), duration, property);
                }

                Object leftTransform = left.getBoneTransforms().get(bone.getName()).getTransform(property).apply(bone.getActor().getVariables());
                Object rightTransform = right.getBoneTransforms().get(bone.getName()).getTransform(property).apply(bone.getActor().getVariables());

                float delta = ECMath.delta(time, left.getTime(), right.getTime());

                object = property.getOperators().lerp(leftTransform, rightTransform, delta);
            }

            result.setRaw(property, object);
        }

        if (result.isEmpty()) return null;
        return result;
    }

    public double getDuration() {
        return duration;
    }

    public boolean isLoop() {
        return loop;
    }

    public @NotNull KeyframeMap getFrames() {
        return frames;
    }
}
