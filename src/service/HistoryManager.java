package service;
import model.Task;
import java.util.List;

import java.util.ArrayList;

public interface HistoryManager {
    void add(Task task);
    void remove(int id);
    List<Task> getHistory();
}
