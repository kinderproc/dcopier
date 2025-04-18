package com.lightspeed;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

// TODO: add interface and document methods

/**
 * Makes deep copy of object, using reflection. The class doesn't process objects without default constructor.
 * It could be refactored later on to analyze all the available constructors and their parameters. But I guess
 * it's out of scope for this task.
 * <p>
 * It doesn't pay attention to Map, records and arrays also and could be refactored to Singleton. This points to improve.
 **/
public class DeepCopier {

    private final List<Class<?>> WRAPPER_TYPES = List.of(
            Boolean.class,
            Byte.class,
            Short.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class,
            Character.class
    );

    public static void main(String[] args) {
        List<String> books = List.of(
                "Pride and Prejudice ",
                "To Kill a Mockingbird",
                "The Great Gatsby");

        DeepCopier copier = new DeepCopier();
        Man original = new Man("Alex", 40, books);
        Man copy = (Man) copier.createCopy(original);

        System.out.printf("The original object: %s%n", original);
        System.out.printf("The copy: %s%n", copy);
    }

    public Object createCopy(Object original) {

        if (original == null) return null;

        if (original.getClass().isPrimitive() || original instanceof String || WRAPPER_TYPES.contains(original.getClass())) {
            return original;
        }

        Object copy;

        // TODO: get the default constructor, in case of several constructors
        try {
            copy = original.getClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            // TODO: create custom exception (optionally)
            throw new RuntimeException("Can't create new instance of " + original.getClass().getName() + ". Check if class has default public constructor.", e);
        }

        try {
            for (Field field : original.getClass().getDeclaredFields()) {
                field.setAccessible(true);

                if (field.get(original) == null) {
                    field.set(copy, null);
                    continue;
                }

                if (field.getType().isPrimitive() || field.getType().equals(String.class)) {
                    field.set(copy, field.get(original));
                    continue;
                }

                if (field.get(original) instanceof Collection<?> originalCollection) {
                    Collection<Object> copiedCollection = copyCollection(originalCollection);
                    field.set(copy, copiedCollection);
                    continue;
                }

                Object childObj = field.get(original);

                if (childObj == original) {
                    field.set(copy, copy);
                } else {
                    field.set(copy, createCopy(field.get(original)));
                }
            }

            return copy;
        } catch (Exception e) {
            throw new RuntimeException("Can't create copy of " + original.getClass().getName(), e);
        }
    }

    private Collection<Object> copyCollection(Collection<?> original) {
        // TODO: the default implementations could be defined in a flexible way with properties/map and enum
        Collection<Object> copy = switch (original) {
            case Set<?> objects -> new HashSet<>();
            case Queue<?> objects -> new LinkedList<>();
            default -> new ArrayList<>();
        };

        for (Object item : original) {
            copy.add(createCopy(item));
        }

        return copy;
    }

}
