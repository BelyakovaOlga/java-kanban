package service;

public class Manager {
    public TaskManager getDefault(){
        return new InMemoryTaskManager();
    }
    static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
