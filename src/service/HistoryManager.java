package service;
import model.Task;
import java.util.ArrayList;

import java.util.ArrayList;

public interface HistoryManager {
    <T extends Task> void add(T task);
    <T extends Task> ArrayList<Task> getHistory();
}
