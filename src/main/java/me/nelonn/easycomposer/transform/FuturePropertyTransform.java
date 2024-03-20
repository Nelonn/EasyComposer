package me.nelonn.easycomposer.transform;

import me.nelonn.easycomposer.variable.VariablesMap;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface FuturePropertyTransform {

    @NotNull me.nelonn.easycomposer.transform.Property<?> property();

    @NotNull Object apply(@NotNull VariablesMap actorProperties);

    static @NotNull FuturePropertyTransform function(@NotNull me.nelonn.easycomposer.transform.Property<?> property, @NotNull Function<VariablesMap, ?> supplier) {
        return new FuturePropertyTransform() {
            @Override
            public @NotNull me.nelonn.easycomposer.transform.Property<?> property() {
                return property;
            }

            @Override
            public @NotNull Object apply(@NotNull VariablesMap actorProperties) {
                return supplier.apply(actorProperties);
            }
        };
    }

    static @NotNull FuturePropertyTransform zero(@NotNull Property<?> property) {
        return function(property, properties -> property.getZero());
    }

}
