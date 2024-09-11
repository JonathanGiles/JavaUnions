package net.jonathangiles.test.union.samples;

import net.jonathangiles.test.union.Union;
import net.jonathangiles.test.union.UnionTypes;

public class Main {

    public static void main(String[] args) {
        Main main = new Main();
        Union union = main.doFoo();

        // This union allows for String, Integer, and Double types
        union.setValue("Hello");

        // we can (attempt to) exhautively consume the union using a switch statement...
        handleUnion(union);

        // ... or we can pass in lambda expressions to consume the union for the types we care about
        union.tryConsume(v -> System.out.println("String value from lambda: " + v), String.class);

        // ... or we can just get the value to the type we expect it to be using the getValue methods
        String value = union.getValue();
        System.out.println("Value (from getValue()): " + value);

        value = union.getValue(String.class);
        System.out.println("Value (from getValue(Class<?> cls)): " + value);

        // Of course, this union supports Integer and Double types as well:
        union.setValue(123);
        handleUnion(union);
        union.setValue(3.14);
        handleUnion(union);

        // This will throw an IllegalArgumentException, as the union does not support the type Long
        try {
            union.setValue(123L);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught exception: " + e.getMessage());
        }
    }

    @UnionTypes({String.class, Integer.class, Double.class})
    public Union doFoo() {
        return Union.ofTypes(String.class, Integer.class, Double.class);
    }

    private static void handleUnion(Union union) {
        // we simply get the value from the Union, and switch on it depending on the type
        switch (union.getValue()) {
            case String s -> System.out.println("String value from switch: " + s);
            case Integer i -> System.out.println("Integer value from switch: " + i);
            case Double d -> System.out.println("Double value from switch: " + d);
            default -> throw new IllegalArgumentException("Unknown type: " + union.getType().getTypeName());
        }
    }
}