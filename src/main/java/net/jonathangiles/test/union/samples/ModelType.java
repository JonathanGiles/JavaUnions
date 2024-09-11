package net.jonathangiles.test.union.samples;

import net.jonathangiles.test.union.Union;
import net.jonathangiles.test.union.UnionTypes;

// This is an example of a Model class that uses the Union type to allow for multiple types to be stored in a single
// property. This is useful when you have a property that can be one of a few types, but you want to ensure that the
// types are known at compile time, and that you can easily switch on the type of the value.
public class ModelType {
    private Union prop1 = Union.ofTypes(String.class, Integer.class, Double.class);

    @UnionTypes({String.class, Integer.class, Double.class})
    public Union getProp1() {
        return prop1;
    }

    // In this case, because all three values of the Union type are distinct, we can have three setter methods to
    // modify the Union in a type-safe way. If the types were not distinct, we would need to use a single setter method
    // that took an Object type, and then rely on the Union type to ensure that the value was of the correct type.
    // This would be the case (as we see in GenericModelType) where there are multiple types of the same class, such as
    // List<String>, List<Integer>, and List<Float>.
    public void setProp1(String str) {
        prop1.setValue(str);
    }
    public void setProp1(Integer integer) {
        prop1.setValue(integer);
    }
    public void setProp1(Double dbl) {
        prop1.setValue(dbl);
    }

    // This is not necessary, as the above API is more convenient and type-safe
//    public void setProp1(Object obj) {
//        prop1.setValue(obj);
//    }

    public static void main(String[] args) {
        ModelType modelType = new ModelType();
        modelType.setProp1(23);
        System.out.println(modelType.getProp1().getValue(Integer.class));
    }
}