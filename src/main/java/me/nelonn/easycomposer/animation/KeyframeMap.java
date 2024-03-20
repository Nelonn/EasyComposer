package me.nelonn.easycomposer.animation;

import com.mojang.datafixers.util.Pair;
import me.nelonn.easycomposer.transform.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.TreeMap;

public class KeyframeMap {
    private final TreeMap<Double, me.nelonn.easycomposer.animation.Keyframe> map = new TreeMap<>();

    public void add(@NotNull me.nelonn.easycomposer.animation.Keyframe keyframe) {
        map.put(keyframe.getTime(), keyframe);
    }

    public @Nullable Session bone(@NotNull String boneName) {
        TreeMap<Double, me.nelonn.easycomposer.animation.Keyframe> newMap = new TreeMap<>();
        newMap.putAll(map);
        newMap.values().removeIf(keyframe -> !keyframe.shouldAffectBone(boneName));
        if (newMap.isEmpty()) return null;
        return new Session(boneName, newMap);
    }

    public static class Session {
        private final String boneName;
        private final TreeMap<Double, me.nelonn.easycomposer.animation.Keyframe> map;

        public Session(@NotNull String boneName, @NotNull TreeMap<Double, me.nelonn.easycomposer.animation.Keyframe> map) {
            this.boneName = boneName;
            this.map = map;
        }

        public @Nullable Session property(@NotNull Property<?> property) {
            TreeMap<Double, me.nelonn.easycomposer.animation.Keyframe> newMap = new TreeMap<>();
            newMap.putAll(map);
            newMap.values().removeIf(keyframe -> !keyframe.shouldAffectBoneProperty(boneName, property));
            if (newMap.isEmpty()) return null;
            return new Session(boneName, newMap);
        }

        public @Nullable me.nelonn.easycomposer.animation.Keyframe findKeyframe(double time) {
            return map.get(time);
        }

        public @NotNull Pair<me.nelonn.easycomposer.animation.Keyframe, me.nelonn.easycomposer.animation.Keyframe> findSurroundingKeyframes(double targetTime) {
            Map.Entry<Double, me.nelonn.easycomposer.animation.Keyframe> floorEntry = map.floorEntry(targetTime);
            Map.Entry<Double, Keyframe> ceilingEntry = map.ceilingEntry(targetTime);
            return Pair.of(floorEntry != null ? floorEntry.getValue() : null,
                    ceilingEntry != null ? ceilingEntry.getValue() : null);
        }
    }

}
