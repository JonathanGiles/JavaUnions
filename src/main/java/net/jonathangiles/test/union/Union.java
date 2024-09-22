package net.jonathangiles.test.union;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import java.util.Collections;
import java.util.function.Consumer;

public class Union {
    private final List<Type> types;
    private Object value;
    private Type currentType;

    private Union(Type... types) {
        this.types = Arrays.asList(types);
    }

    public static Union ofTypes(Type... types) {
        return new Union(types);
    }

    public void setValue(Object value) {
        for (Type type : types) {
            if (isInstanceOfType(value, type) || isPrimitiveTypeMatch(value, type)) {
                this.value = value;
                this.currentType = type;
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
        return currentType;
    }

    public <T> T getValue(Class<T> cls) {
        if (cls.isInstance(value)) {
            return cls.cast(value);
        }
        if (isPrimitiveTypeMatch(value, cls)) {
            return (T) value;
        }
        throw new IllegalArgumentException("Value is not of type: " + cls.getName());
    }

    public <T> T getValue(Class<T> cls, Class<?>... genericTypes) {
        return getValue(new ParameterizedTypeImpl(cls, genericTypes));
    }

    public <T> T getValue(ParameterizedTypeImpl type) {
        if (isInstanceOfType(value, type)) {
            return (T) value;
        }
        throw new IllegalArgumentException("Value is not of type: " + type.getTypeName());
    }

    /**
     * This method is used to consume the value of the Union if it is of the expected type.
     *
     * @param consumer A consumer that will consume the value of the Union if it is of the expected type.
     * @param cls The expected type of the value.
     * @return Returns true if the value was consumable by the consumer, and false if it was not.
     * @param <T> The value type expected by the consumer.
     */
    @SuppressWarnings("unchecked")
    public <T> boolean tryConsume(Consumer<T> consumer, Class<T> cls) {
        if (isInstanceOfType(value, cls)) {
            consumer.accept(cls.cast(value));
            return true;
        }
        if (isPrimitiveTypeMatch(value, cls)) {
            consumer.accept((T) value);
            return true;
        }
        return false;
    }

    /**
     * This method is used to consume the value of the Union if it is of the expected type.
     *
     * @param consumer A consumer that will consume the value of the Union if it is of the expected type.
     * @param genericTypes A var-args representation of generic types that are expected by the consumer, for example,
     *                     List<String> would be represented as <pre>List.class, String.class</pre>.
     * @return Returns true if the value was consumable by the consumer, and false if it was not.
     * @param <T> The value type expected by the consumer.
     */
    public <T> boolean tryConsume(Consumer<T> consumer, Class<T> cls, Class<?>... genericTypes) {
        return tryConsume(consumer, new ParameterizedTypeImpl(cls, genericTypes));
    }

    /**
     * This method is used to consume the value of the Union if it is of the expected type.
     *
     * @param consumer A consumer that will consume the value of the Union if it is of the expected type.
     * @param type The expected type of the value.
     * @return Returns true if the value was consumable by the consumer, and false if it was not.
     * @param <T> The value type expected by the consumer.
     */
    @SuppressWarnings("unchecked")
    public <T> boolean tryConsume(Consumer<T> consumer, ParameterizedType type) {
        if (isInstanceOfType(value, type)) {
            consumer.accept((T) value);
            return true;
        }
        return false;
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
                ", type=" + currentType.getTypeName() +
                ", value=" + value +
                '}';
        }
    }

    private boolean isInstanceOfType(Object value, Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            if (pType.getRawType() instanceof Class<?> && ((Class<?>) pType.getRawType()).isInstance(value)) {
                Type[] actualTypeArguments = pType.getActualTypeArguments();
                if (value instanceof Collection<?> c) {
                    return c.stream().allMatch(element ->
                        Arrays.stream(actualTypeArguments)
                            .anyMatch(arg -> isInstanceOfType(element, arg))
                    );
                }
            }
        } else if (type instanceof Class<?> cls) {
            return cls.isInstance(value);
        }
        return false;
    }

    private boolean isPrimitiveTypeMatch(Object value, Type type) {
        if (type instanceof Class<?>) {
            Class<?> cls = (Class<?>) type;
            if (cls.isPrimitive()) {
                if ((cls == int.class && value instanceof Integer) ||
                    (cls == long.class && value instanceof Long) ||
                    (cls == double.class && value instanceof Double) ||
                    (cls == float.class && value instanceof Float) ||
                    (cls == boolean.class && value instanceof Boolean) ||
                    (cls == char.class && value instanceof Character) ||
                    (cls == byte.class && value instanceof Byte) ||
                    (cls == short.class && value instanceof Short)) {
                    return true;
                }
            }
        }
        return false;
    }
}