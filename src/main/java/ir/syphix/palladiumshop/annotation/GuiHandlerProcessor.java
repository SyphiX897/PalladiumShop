package ir.syphix.palladiumshop.annotation;

import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.List;

public class GuiHandlerProcessor {

    public static void process() {
        Reflections reflections = new Reflections("ir.syphix.palladiumshop");
        List<Class<?>> annotatedClasses =reflections.getTypesAnnotatedWith(GuiHandler.class).stream().toList();

        for (Class<?> annotatedClass : annotatedClasses) {
            createNewInstance(annotatedClass);
        }
    }

    private static void createNewInstance(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
