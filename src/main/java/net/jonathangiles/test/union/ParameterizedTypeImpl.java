package net.jonathangiles.test.union;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
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
}