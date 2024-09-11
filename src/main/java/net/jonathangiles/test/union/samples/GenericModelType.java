package net.jonathangiles.test.union.samples;

import net.jonathangiles.test.union.ParameterizedTypeImpl;
import net.jonathangiles.test.union.Union;

import java.util.List;

// This is an example of a Model class that uses the Union type to allow for multiple types to be stored in a single
// property, with the additional complexity that the types are all generic types (in this case, List<String>, List<Integer>,
// and List<Float>).
public class GenericModelType {
    // We specify that the Union type can be one of three types: List<String>, List<Integer>, or List<Float>.
    private Union fooValuesUnion = Union.ofTypes(
        new ParameterizedTypeImpl(List.class, String.class),
        new ParameterizedTypeImpl(List.class, Integer.class),
        new ParameterizedTypeImpl(List.class, Float.class));

    // we give access to the Union type, so that the value can be modified and retrieved.
    public Union getFooValues() {
        return fooValuesUnion;
    }

    // but our setter methods need to have more complex names, to differentiate them at runtime.
    public void setFooValuesAsStrings(List<String> strValues) {
        fooValuesUnion.setValue(strValues);
    }
    public void setFooValuesAsIntegers(List<Integer> intValues) {
        fooValuesUnion.setValue(intValues);
    }
    public void setFooValuesAsFloats(List<Float> floatValues) {
        fooValuesUnion.setValue(floatValues);
    }

    public static void main(String[] args) {
        GenericModelType model = new GenericModelType();
        model.setFooValuesAsStrings(List.of("Hello", "World"));

        // in this case, it isn't possible to switch over the values easily (as we could in the ModelType class), as the
        // types are all List types (and we would need to inspect the values inside the list to be sure). Instead, we
        // can use the tryConsume method to consume the value if it is of the expected type.
        model.getFooValues().tryConsume(strings -> System.out.println("Strings: " + strings), List.class, String.class);

//        System.out.println(model.getValues());
    }
}