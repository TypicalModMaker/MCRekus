package dev.isnow.mcrekus.util;

import dev.isnow.mcrekus.MCRekus;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ReflectionUtil {

    public List<Class<?>> getClasses(final String packageName) throws ClassNotFoundException, IOException {
        final String path = packageName.replace('.', '/');

        final ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
        final ClassLoader pluginClassLoader = MCRekus.class.getClassLoader();
        Thread.currentThread().setContextClassLoader(pluginClassLoader);

        final URL resource = Thread.currentThread().getContextClassLoader().getResource(path);
        final List<Class<?>> classes = new ArrayList<>();

        if (resource != null) {
            if (resource.getProtocol().equals("file")) {
                final File directory = new File(resource.getFile());
                if (directory.exists()) {
                    findClassesInDirectory(directory, packageName, classes);
                }
            } else if (resource.getProtocol().equals("jar")) {
                final String jarPath = resource.getPath().substring(5, resource.getPath().indexOf("!"));
                try (JarFile jarFile = new JarFile(new File(jarPath))) {
                    findClassesInJar(jarFile, path, classes);
                }
            }
        } else {
            RekusLogger.error("Resource is null! " + packageName);
        }

        Thread.currentThread().setContextClassLoader(originalClassLoader);

        return classes;
    }

    private void findClassesInDirectory(final File directory, final String packageName, final List<Class<?>> classes) throws ClassNotFoundException {
        final File[] files = directory.listFiles();
        if (files != null) {
            for (final File file : files) {
                if (file.isDirectory()) {
                    findClassesInDirectory(file, packageName + "." + file.getName(), classes);
                } else if (file.getName().endsWith(".class")) {
                    final String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                    if (!className.contains("$")) {
                        classes.add(Class.forName(className));
                    }
                }
            }
        }
    }


    private void findClassesInJar(final JarFile jarFile, final String path, final List<Class<?>> classes) throws ClassNotFoundException {
        final Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            final JarEntry entry = entries.nextElement();
            final String entryName = entry.getName();
            if (entryName.startsWith(path) && entryName.endsWith(".class") && !entry.isDirectory()) {
                final String className = entryName.replace('/', '.').substring(0, entryName.length() - 6);
                if (!className.contains("$")) {
                    classes.add(Class.forName(className));
                }
            }
        }
    }


    @SuppressWarnings("unchecked")
    public <T> Class<T> getGenericTypeClass(final Object object, final int index) {
        final Type superclass = object.getClass().getGenericSuperclass();
        if (superclass instanceof ParameterizedType parameterizedType) {
            final Type[] typeArguments = parameterizedType.getActualTypeArguments();
            if (index < typeArguments.length && typeArguments[index] instanceof Class<?>) {
                return (Class<T>) typeArguments[index];
            }
        }

        return null;
    }

}
