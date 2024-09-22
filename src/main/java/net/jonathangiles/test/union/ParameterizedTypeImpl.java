package net.jonathangiles.test.union;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class ParameterizedTypeImpl implements ParameterizedType {
    private final Class<?> raw;
    private final Type[] args;

    public ParameterizedTypeImpl(Class<?> raw, Type... args) {
        this.raw = raw;
        this.args = args;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return args;
    }

    @Override
    public Type getRawType() {
        return raw;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }

    @Override
    public String toString() {
        String argsString = Arrays.stream(args)
            .map(Type::getTypeName)
            .collect(Collectors.joining(", "));
        return raw.getTypeName() + "<" + argsString + ">";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParameterizedTypeImpl that = (ParameterizedTypeImpl) o;
        return Objects.equals(raw, that.raw) && Objects.deepEquals(args, that.args);
    }

    @Override
    public int hashCode() {
        return Objects.hash(raw, Arrays.hashCode(args));
    }
}