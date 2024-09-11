package net.jonathangiles.test.union;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import java.util.Collections;
import java.util.function.Consumer;

public class Union {
    private final List<Type> types;
    private Object value;

    private Union(Type... types) {
        this.types = Arrays.asList(types);
    }

    public static Union ofTypes(Type... types) {
        return new Union(types);
    }

    public void setValue(Object value) {
        for (Type type : types) {
            if (isInstanceOfType(value, type)) {
                this.value = value;
                return;
            }
        }
        throw new IllegalArgumentException("Invalid type: " + value.getClass().getName());
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue() {
        return (T) value;
    }

    public List<Type> getTypes() {
        return Collections.unmodifiableList(types);
    }

    public Type getType() {
        return value != null ? value.getClass() : null;
    }

    public <T> T getValue(Class<T> cls) {
        if (cls.isInstance(value)) {
            return cls.cast(value);
        }
        throw new IllegalArgumentException("Value is not of type: " + cls.getName());
    }

    public <T> void tryConsume(Consumer<T> consumer, Class<T> cls) {
        if (isInstanceOfType(value, cls)) {
            consumer.accept(cls.cast(value));
        }
    }

    public <T> void tryConsume(Consumer<T> consumer, Class<T> cls, Class<?>... genericTypes) {
        tryConsume(consumer, new ParameterizedTypeImpl(cls, genericTypes));
    }

    @SuppressWarnings("unchecked")
    public <T> void tryConsume(Consumer<T> consumer, ParameterizedType type) {
        if (isInstanceOfType(value, type)) {
            consumer.accept((T) value);
        }
    }

    @Override
    public String toString() {
        if (value == null) {
            return "Union{" +
                "types=" + types +
                ", value=null" +
                '}';
        } else {
            return "Union{" +
                "types=" + types +
                ", type=" + value.getClass().getName() +
                ", value=" + value +
                '}';
        }
    }

    private boolean isInstanceOfType(Object value, Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            if (pType.getRawType() instanceof Class<?> && ((Class<?>) pType.getRawType()).isInstance(value)) {
                Type[] actualTypeArguments = pType.getActualTypeArguments();
                if (value instanceof List<?> list) {
                    if (!list.isEmpty()) {
                        Object firstElement = list.get(0);
                        return Arrays.stream(actualTypeArguments)
                            .anyMatch(arg -> ((Class<?>) arg).isInstance(firstElement));
                    }
                }
            }
        } else if (type instanceof Class<?> cls) {
            return cls.isInstance(value);
        }
        return false;
    }
}