package il.co.ILRD.plug_and_play;

import java.io.*;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

public class MonitorDir {
    private String monitoredDirPath;

            public MonitorDir(String monitoredDirPath) {
                this.monitoredDirPath = monitoredDirPath;
            }

            public File watchPath() {// todo return list of Files
                Path path = Paths.get(monitoredDirPath);

                FileSystem fileSystem = path.getFileSystem();

                try {
                    WatchService watchService = fileSystem.newWatchService();
                    path.register(watchService, ENTRY_CREATE);

                    WatchKey key;
                    do {
                        key = watchService.take();

                        // iterates through events (file system changes in the monitored directory)
                        for (WatchEvent<?> watchEvent : key.pollEvents()) {
                            // the file or directory that triggered the event
                            Path newFile = (Path) watchEvent.context();
                            // retrieve the absolut path
                            Path absolutPath = path.resolve(newFile);
                            // Converts to File Object
                            File file = absolutPath.toFile();

                            // ensures that the file is fully available for reading before proceeding
                            while (!file.canRead()) ;

                            return file;
                        }
                    } while (key.reset());
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
}
