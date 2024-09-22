package net.jonathangiles.test.union;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UnionTest {

    @Test
    void testCreateUnionWithMultipleTypes() {
        Union union = Union.ofTypes(String.class, Integer.class, Double.class);
        assertNotNull(union);
        assertEquals(3, union.getTypes().size());
    }

    @Test
    void testSetValueAndGetValue() {
        Union union = Union.ofTypes(String.class, Integer.class, Double.class);
        union.setValue("Hello");
        assertEquals(String.class, union.getType());
        assertEquals("Hello", union.getValue(String.class));

        union.setValue(42);
        assertEquals(Integer.class, union.getType());
        assertEquals(42, union.getValue(Integer.class));

        union.setValue(3.14);
        assertEquals(Double.class, union.getType());
        assertEquals(3.14, union.getValue(Double.class));
    }

    @Test
    void testSetValueWithInvalidType() {
        Union union = Union.ofTypes(String.class, Integer.class, Double.class);
        assertThrows(IllegalArgumentException.class, () -> union.setValue(3.14f));
    }


    @Test
    void testTryConsumeWithValidType() {
        Union union = Union.ofTypes(String.class, Integer.class, Double.class);
        union.setValue(42);
        assertTrue(union.tryConsume(value -> assertEquals(42, value), Integer.class));

        union.setValue("Hello");
        assertTrue(union.tryConsume(value -> assertEquals("Hello", value), String.class));

        union.setValue(3.14);
        assertTrue(union.tryConsume(value -> assertEquals(3.14, value), Double.class));
    }

    @Test
    void testTryConsumeWithInvalidType() {
        Union union = Union.ofTypes(String.class, Integer.class, Double.class);
        union.setValue(42);
        assertFalse(union.tryConsume(value -> fail("Should not consume String"), String.class));
        assertFalse(union.tryConsume(value -> fail("Should not consume Double"), Double.class));
        assertTrue(union.tryConsume(value -> assertEquals(42, value), Integer.class));
    }

    @Test
    void testCreateUnionWithMultipleTypesAutoboxing() {
        Union union = Union.ofTypes(String.class, int.class, double.class);
        assertNotNull(union);
        assertEquals(3, union.getTypes().size());
    }

    @Test
    void testSetValueAndGetValueAutoboxing() {
        Union union = Union.ofTypes(String.class, int.class, double.class);
        union.setValue("Hello");
        assertEquals(String.class, union.getType());
        assertEquals("Hello", union.getValue(String.class));

        union.setValue(42);
        assertEquals(int.class, union.getType());
        assertEquals(42, union.getValue(int.class));

        union.setValue(3.14);
        assertEquals(double.class, union.getType());
        assertEquals(3.14, union.getValue(double.class));
    }

    @Test
    void testSetValueWithInvalidTypeAutoboxing() {
        Union union = Union.ofTypes(String.class, int.class, double.class);
        assertThrows(IllegalArgumentException.class, () -> union.setValue(3.14f));
    }

    @Test
    void testTryConsumeWithValidTypeAutoboxing() {
        Union union = Union.ofTypes(String.class, int.class, double.class);
        union.setValue(42);
        assertEquals(int.class, union.getType());
        assertTrue(union.tryConsume(value -> assertEquals(42, value), int.class));

        union.setValue("Hello");
        assertEquals(String.class, union.getType());
        assertTrue(union.tryConsume(value -> assertEquals("Hello", value), String.class));

        union.setValue(3.14);
        assertEquals(double.class, union.getType());
        assertTrue(union.tryConsume(value -> assertEquals(3.14, value), double.class));
    }

    @Test
    void testTryConsumeWithInvalidTypeAutoboxing() {
        Union union = Union.ofTypes(String.class, int.class, double.class);
        union.setValue(42);
        assertFalse(union.tryConsume(value -> fail("Should not consume String"), String.class));
        assertFalse(union.tryConsume(value -> fail("Should not consume Double"), double.class));
        assertTrue(union.tryConsume(value -> assertEquals(42, value), int.class));
    }


    @Test
    void testCreateUnionWithArrayTypes() {
        Union union = Union.ofTypes(int[].class, float[].class, String[].class);
        assertNotNull(union);
        assertEquals(3, union.getTypes().size());
    }

    @Test
    void testSetValueAndGetValueWithArrayTypes() {
        Union union = Union.ofTypes(int[].class, float[].class, String[].class);

        int[] intArray = {1, 2, 3};
        union.setValue(intArray);
        assertEquals(int[].class, union.getType());
        assertArrayEquals(intArray, union.getValue(int[].class));

        float[] floatArray = {1.1f, 2.2f, 3.3f};
        union.setValue(floatArray);
        assertEquals(float[].class, union.getType());
        assertArrayEquals(floatArray, union.getValue(float[].class));

        String[] stringArray = {"Hello", "World"};
        union.setValue(stringArray);
        assertEquals(String[].class, union.getType());
        assertArrayEquals(stringArray, union.getValue(String[].class));
    }

    @Test
    void testSetValueWithInvalidArrayType() {
        Union union = Union.ofTypes(int[].class, float[].class, String[].class);
        assertThrows(IllegalArgumentException.class, () -> union.setValue(new double[]{1.1, 2.2, 3.3}));
    }

    @Test
    void testTryConsumeWithValidArrayType() {
        Union union = Union.ofTypes(int[].class, float[].class, String[].class);

        int[] intArray = {1, 2, 3};
        union.setValue(intArray);
        assertTrue(union.tryConsume(value -> assertArrayEquals(intArray, value), int[].class));

        float[] floatArray = {1.1f, 2.2f, 3.3f};
        union.setValue(floatArray);
        assertTrue(union.tryConsume(value -> assertArrayEquals(floatArray, value), float[].class));

        String[] stringArray = {"Hello", "World"};
        union.setValue(stringArray);
        assertTrue(union.tryConsume(value -> assertArrayEquals(stringArray, value), String[].class));
    }

    @Test
    void testTryConsumeWithInvalidArrayType() {
        Union union = Union.ofTypes(int[].class, float[].class, String[].class);

        int[] intArray = {1, 2, 3};
        union.setValue(intArray);
        assertFalse(union.tryConsume(value -> fail("Should not consume float[]"), float[].class));
        assertFalse(union.tryConsume(value -> fail("Should not consume String[]"), String[].class));
        assertTrue(union.tryConsume(value -> assertArrayEquals(intArray, value), int[].class));
    }


    @Test
    void testCreateUnionWithParameterizedTypes() {
        Union union = Union.ofTypes(new ParameterizedTypeImpl(List.class, String.class),
            new ParameterizedTypeImpl(List.class, Integer.class),
            new ParameterizedTypeImpl(List.class, Double.class));
        assertNotNull(union);
        assertEquals(3, union.getTypes().size());
    }

    @Test
    void testSetValueAndGetValueWithParameterizedTypes() {
        ParameterizedTypeImpl listOfString = new ParameterizedTypeImpl(List.class, String.class);
        ParameterizedTypeImpl listOfInteger = new ParameterizedTypeImpl(List.class, Integer.class);
        ParameterizedTypeImpl listOfDouble = new ParameterizedTypeImpl(List.class, Double.class);

        Union union = Union.ofTypes(listOfString, listOfInteger, listOfDouble);

        List<String> stringList = List.of("Hello", "World");
        union.setValue(stringList);
        assertEquals(listOfString, union.getType());
        assertEquals(stringList, union.getValue(listOfString));

        List<Integer> intList = List.of(1, 2, 3);
        union.setValue(intList);
        assertEquals(listOfInteger, union.getType());
        assertEquals(intList, union.getValue(listOfInteger));

        List<Double> doubleList = List.of(1.1, 2.2, 3.3);
        union.setValue(doubleList);
        assertEquals(listOfDouble, union.getType());
        assertEquals(doubleList, union.getValue(listOfDouble));
    }

    @Test
    void testSetValueWithInvalidParameterizedType() {
        ParameterizedTypeImpl listOfString = new ParameterizedTypeImpl(List.class, String.class);
        ParameterizedTypeImpl listOfInteger = new ParameterizedTypeImpl(List.class, Integer.class);
        ParameterizedTypeImpl listOfDouble = new ParameterizedTypeImpl(List.class, Double.class);

        Union union = Union.ofTypes(listOfString, listOfInteger, listOfDouble);
        assertThrows(IllegalArgumentException.class, () -> union.setValue(Set.of("Hello", "World")));
    }

    @Test
    void testTryConsumeWithValidParameterizedType() {
        ParameterizedTypeImpl listOfString = new ParameterizedTypeImpl(List.class, String.class);
        ParameterizedTypeImpl listOfInteger = new ParameterizedTypeImpl(List.class, Integer.class);
        ParameterizedTypeImpl listOfDouble = new ParameterizedTypeImpl(List.class, Double.class);

        Union union = Union.ofTypes(listOfString, listOfInteger, listOfDouble);

        List<String> stringList = List.of("Hello", "World");
        union.setValue(stringList);
        assertTrue(union.tryConsume(value -> assertEquals(stringList, value), listOfString));

        List<Integer> intList = List.of(1, 2, 3);
        union.setValue(intList);
        assertTrue(union.tryConsume(value -> assertEquals(intList, value), listOfInteger));

        List<Double> doubleList = List.of(1.1, 2.2, 3.3);
        union.setValue(doubleList);
        assertTrue(union.tryConsume(value -> assertEquals(doubleList, value), listOfDouble));
    }

    @Test
    void testTryConsumeWithInvalidParameterizedType() {
        ParameterizedTypeImpl listOfString = new ParameterizedTypeImpl(List.class, String.class);
        ParameterizedTypeImpl listOfInteger = new ParameterizedTypeImpl(List.class, Integer.class);
        ParameterizedTypeImpl listOfDouble = new ParameterizedTypeImpl(List.class, Double.class);

        Union union = Union.ofTypes(listOfString, listOfInteger, listOfDouble);

        List<String> stringList = List.of("Hello", "World");
        union.setValue(stringList);
        assertFalse(union.tryConsume(value -> fail("Should not consume List<Integer>"), listOfInteger));
        assertFalse(union.tryConsume(value -> fail("Should not consume List<Double>"), listOfDouble));
        assertTrue(union.tryConsume(value -> assertEquals(stringList, value), listOfString));
    }

    @Test
    void testSetValueAndGetValueWithNull() {
        Union union = Union.ofTypes(String.class, Integer.class, Double.class);

        // setValue(null) should throw an exception, check for it
        assertThrows(NullPointerException.class, () -> union.setValue(null));
        assertNull(union.getValue());
    }

    @Test
    void testSetValueAndGetValueWithEmptyCollection() {
        ParameterizedTypeImpl listOfString = new ParameterizedTypeImpl(List.class, String.class);
        Union union = Union.ofTypes(listOfString);

        List<String> emptyList = List.of();
        union.setValue(emptyList);
        assertEquals(listOfString, union.getType());
        assertEquals(emptyList, union.getValue(listOfString));
    }

    @Test
    void testSetValueWithMixedTypeCollection() {
        ParameterizedTypeImpl listOfString = new ParameterizedTypeImpl(List.class, String.class);
        Union union = Union.ofTypes(listOfString);

        List<Object> mixedList = List.of("Hello", 1);
        assertThrows(IllegalArgumentException.class, () -> union.setValue(mixedList));
    }

    @Test
    void testSetValueAndGetValueWithNestedParameterizedType() {
        ParameterizedTypeImpl listOfListOfString = new ParameterizedTypeImpl(List.class, new ParameterizedTypeImpl(List.class, String.class));
        Union union = Union.ofTypes(listOfListOfString);

        List<List<String>> nestedList = List.of(List.of("Hello", "World"));
        union.setValue(nestedList);
        assertEquals(listOfListOfString, union.getType());
        assertEquals(nestedList, union.getValue(listOfListOfString));
    }

    @Test
    void testSetValueAndGetValueWithPrimitiveArray() {
        Union union = Union.ofTypes(int[].class, double[].class);

        int[] intArray = {1, 2, 3};
        union.setValue(intArray);
        assertEquals(int[].class, union.getType());
        assertArrayEquals(intArray, union.getValue(int[].class));

        double[] doubleArray = {1.1, 2.2, 3.3};
        union.setValue(doubleArray);
        assertEquals(double[].class, union.getType());
        assertArrayEquals(doubleArray, union.getValue(double[].class));
    }

    @Test
    void testSetValueAndGetValueWithDeeplyNestedParameterizedType() {
        ParameterizedTypeImpl listOfListOfListOfString = new ParameterizedTypeImpl(List.class, new ParameterizedTypeImpl(List.class, new ParameterizedTypeImpl(List.class, String.class)));
        Union union = Union.ofTypes(listOfListOfListOfString);

        List<List<List<String>>> deeplyNestedList = List.of(List.of(List.of("Hello", "World")));
        union.setValue(deeplyNestedList);
        assertEquals(listOfListOfListOfString, union.getType());
        assertEquals(deeplyNestedList, union.getValue(listOfListOfListOfString));
    }

    @Test
    void testSetValueAndGetValueWithMixedParameterizedTypes() {
        ParameterizedTypeImpl listOfString = new ParameterizedTypeImpl(List.class, String.class);
        ParameterizedTypeImpl setOfString = new ParameterizedTypeImpl(Set.class, String.class);
        Union union = Union.ofTypes(listOfString, setOfString);

        List<String> stringList = List.of("Hello", "World");
        union.setValue(stringList);
        assertEquals(listOfString, union.getType());
        assertEquals(stringList, union.getValue(listOfString));

        Set<String> stringSet = Set.of("Hello", "World");
        union.setValue(stringSet);
        assertEquals(setOfString, union.getType());
        assertEquals(stringSet, union.getValue(setOfString));
    }
}