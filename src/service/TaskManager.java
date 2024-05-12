package service;

import model.Epic;
import model.SubTask;
import model.Task;
import java.util.List;

public interface TaskManager {
    Task createTask(Task task);

    Epic createEpic(Epic epic);

    SubTask createSubTask(SubTask subTask);

    void deleteTasks();

    void deleteEpics();

    void deleteSubTasks();

    void deleteTask(int taskId);

    void deleteEpic(int taskId);

    void deleteSubTask(int taskId);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    Task getTask(int taskId);

    Epic getEpic(int taskId);

    SubTask getSubTask(int taskId);

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<SubTask> getAllSubTasks();

    List<Task> getHistory();
}
