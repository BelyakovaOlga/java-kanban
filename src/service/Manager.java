package service;
import java.io.File;


public class Manager {
    public static TaskManager getDefault() {

        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static TaskManager getFileManager() {

        return new FileBackedTaskManager(getDefaultHistory(), new File("task.csv"));
    }

    public static HistoryManager getDefaultHistory() {

        return new InMemoryHistoryManager();
    }
}






