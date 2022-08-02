package me.temez;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Reflections reflections = new Reflections(args[0], new SubTypesScanner(false));
        Set<Class> classSet = new HashSet<>(reflections.getSubTypesOf(Object.class));
        Set<Object> output = new HashSet<>();
        for (Class aClass : classSet) {
            Object object = aClass.getConstructor().newInstance();
            for (Field field : aClass.getDeclaredFields()) {
                if (field.getAnnotation(RandomInt.class) != null) {
                    RandomInt target = field.getAnnotation(RandomInt.class);
                    int min = target.min();
                    int max = target.max();
                    int random;
                    if (min < max) {
                        random = min + new Random().nextInt(max - min);
                    } else {
                        random = max + new Random().nextInt(min - max);
                    }
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                        field.set(object, random);
                        field.setAccessible(false);
                    } else {
                        field.set(object, random);
                    }
                }
            }
            output.add(object);
        }
    }
}
