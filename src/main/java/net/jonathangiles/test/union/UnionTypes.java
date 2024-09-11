package net.jonathangiles.test.union;

/**
 * This annotation is used to specify the types that are allowed in a {@link Union} type. It allows for better tooling
 * support to ensure users are properly consuming the types in a {@link Union} type.
 *
 * @see Union
 */
public @interface UnionTypes {
    Class<?>[] value();
}
