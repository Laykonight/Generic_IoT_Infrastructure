package il.co.ILRD.plug_and_play;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public class DynamicJarLoader {
    private final String interfaceName;

    DynamicJarLoader(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public ArrayList<Class<?>> load(String pathToJar) {
        ArrayList<Class<?>> classes = new ArrayList<>();
        try (JarFile jarFile = new JarFile(pathToJar)) {
            ;
            // Obtains an enumeration of all entries (files and directories) in the JAR file
            /*Enumeration<JarEntry> */
            Enumeration<JarEntry> enumeration = jarFile.entries();

            // constructs a 'URL[]' array with a single element,
            // representing the URL to the JAR file.
            // This URL will be used to create a 'URLClassLoader'
            /*URL[] */
            URL[] urls = new URL[]{new URL("jar:file:" + jarFile.getName() + "!/")};


            // new 'URLClassLoader' using the urls array.
            // This class loader is capable of loading classes from the JAR file
            URLClassLoader classLoader = URLClassLoader.newInstance(urls);

            //  iterates through the entries in the JAR file
            while (enumeration.hasMoreElements()) {
                JarEntry entry = enumeration.nextElement();

                // retrieve className
                String className = entry.getName();
                if (className.endsWith(".class")) { // if the entry is class
                    try {
                        // trims the ".class" extension
                        className = className.substring(0, className.length() - 6);

                        // replace '/' with '.' -> convert it into package format.
                        className = className.replace('/', '.');

                        // try to load the class into currentClass
                        // if can't load 'ClassNotFoundException' is thrown
                        Class<?> currentClass = classLoader.loadClass(className);

                        // get all the interfaces that class implements
                        Class<?>[] interfaces = currentClass.getInterfaces();

                        // Check if the loaded class implements the specified interface
                        for (Class<?> clss : interfaces) {
                            if (this.interfaceName.equals(clss.getSimpleName())) {
                                classes.add(currentClass);
                                break;// No need to continue checking interfaces
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        System.out.println("Class " + className + " was not found " + e);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return classes;
    }
}
