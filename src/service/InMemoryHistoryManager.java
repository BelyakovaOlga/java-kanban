package service;

import model.Task;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager{
    ArrayList<Task>  topLastTasks = new ArrayList<>();
    @Override
    public <T extends Task> void add(T task) {
        if (topLastTasks.size() == 10) {
            topLastTasks.remove(0);
        }
        topLastTasks.add(task);
    }
    @Override
    public <T extends Task> ArrayList<Task> getHistory() {

        return topLastTasks;
    }
}
