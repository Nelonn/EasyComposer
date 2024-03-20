package me.nelonn.easycomposer.transform;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class Transform {
    private final Map<me.nelonn.easycomposer.transform.Property<?>, Object> map = new HashMap<>();

    public <T> @Nullable T getRaw(@NotNull me.nelonn.easycomposer.transform.Property<?> property) {
        return (T) map.get(property);
    }

    public <T> @Nullable T get(@NotNull me.nelonn.easycomposer.transform.Property<T> property) {
        return getRaw(property);
    }

    public <T> void setRaw(@NotNull me.nelonn.easycomposer.transform.Property<?> property, @NotNull T value) {
        map.put(property, value);
    }

    public <T> void set(@NotNull me.nelonn.easycomposer.transform.Property<T> property, @NotNull T value) {
        setRaw(property, value);
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public @NotNull Set<me.nelonn.easycomposer.transform.Property<?>> keySet() {
        return map.keySet();
    }

    public @NotNull Transform copy() {
        Transform transform = new Transform();
        transform.map.putAll(map);
        return transform;
    }

    public void add(@NotNull Transform other) {
        for (Map.Entry<me.nelonn.easycomposer.transform.Property<?>, Object> entry : other.map.entrySet()) {
            me.nelonn.easycomposer.transform.Property<?> key = entry.getKey();
            Object value = entry.getValue();
            if (map.containsKey(key)) {
                Object existing = map.get(key);
                map.put(key, key.getOperators().add(existing, value));
            } else {
                map.put(key, value);
            }
        }
    }

    public void lerp(@NotNull Transform other, float delta) {
        for (Map.Entry<me.nelonn.easycomposer.transform.Property<?>, Object> entry : other.map.entrySet()) {
            Property<?> key = entry.getKey();
            Object value = entry.getValue();

            if (map.containsKey(key)) {
                Object existing = map.get(key);
                map.put(key, key.getOperators().lerp(existing, value, delta));
            } else {
                map.put(key, value);
            }
        }
    }
}
