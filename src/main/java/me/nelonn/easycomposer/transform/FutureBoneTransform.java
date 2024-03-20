package me.nelonn.easycomposer.transform;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FutureBoneTransform {
    private final Map<me.nelonn.easycomposer.transform.Property<?>, FuturePropertyTransform> map = new HashMap<>();
    private final Set<me.nelonn.easycomposer.transform.Property<?>> affects = Collections.unmodifiableSet(map.keySet());

    public FutureBoneTransform() {
    }

    public void add(@NotNull FuturePropertyTransform propertyTransform) {
        map.put(propertyTransform.property(), propertyTransform);
    }

    public @NotNull Set<me.nelonn.easycomposer.transform.Property<?>> affects() {
        return affects;
    }

    public @Nullable FuturePropertyTransform getTransform(@NotNull Property<?> property) {
        return map.get(property);
    }
}
