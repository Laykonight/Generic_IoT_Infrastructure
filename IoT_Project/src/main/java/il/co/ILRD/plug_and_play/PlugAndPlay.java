package il.co.ILRD.plug_and_play;

import il.co.ILRD.command_factory.Command;
import il.co.ILRD.command_factory.Factory;
import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public class PlugAndPlay implements Runnable {
    private String methodName;
    private DynamicJarLoader jarLoader;
    private MonitorDir monitorDir;
    private AtomicBoolean isRunning;
    private Factory factory;
    private String directoryPath;

    public PlugAndPlay(String interfaceName, String directoryPath, String methodName, Factory factory) /*throws IOException*/ {
        this.methodName = methodName;
        this.jarLoader = new DynamicJarLoader(interfaceName);
        this.monitorDir = new MonitorDir(directoryPath);
        this.isRunning = new AtomicBoolean(false);
        this.factory = factory;
        this.directoryPath = directoryPath;
    }

    public void start() {
        isRunning.set(true);
        loadExistsJARs();
        new Thread(this);
    }

    private void loadExistsJARs() {
        File plugins = new File(directoryPath);
        for (File jar : Objects.requireNonNull(plugins.listFiles())) {
            addJar(jar);
        }
    }

    public void stop() {
        isRunning.set(false);
    }

    @Override
    public void run() {
        while (this.isRunning.get()) {
            try {

                File file = this.monitorDir.watchPath();
                addJar(file);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void addJar(File file) {
        if (null == file || !file.getAbsolutePath().endsWith(".jar")) {
            // throw new InvalidDataException("Dir path for monitoring error - dir not exist!");
            return;
        }

        ArrayList<Class<?>> classes = this.jarLoader.load(file.getAbsolutePath());

        for (Class<?> clss : classes) {
            addToFactory(clss);
        }
    }

    private void addToFactory(Class<?> clss) {
        Function<JSONObject, Command> function = s -> (mongoManager) -> {
            Method method;
            try {
                method = clss.getMethod(this.methodName);
                System.out.println((String) method.invoke(clss.getName()));

            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                System.out.println(e.getMessage());
            }
        };

        factory.add(clss.getName(), function);
        System.out.println("New IPI method was created");
    }
}
