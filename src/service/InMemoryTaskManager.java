package service;

import exception.NotFoundException;
import exception.ValidationException;
import model.Epic;
import model.SubTask;
import model.Task;
import java.util.stream.Collectors;
import java.util.*;
import java.util.stream.Stream;

public class InMemoryTaskManager implements TaskManager {
    protected int counterTask;
    final HashMap<Integer, Task> tasks;
    final HashMap<Integer, Epic> epics;
    final HashMap<Integer, SubTask> subTasks;
    final HistoryManager historyManager;
    final Set<Task>  priorTasks = new TreeSet<Task>(Comparator.comparing(Task::getStartTime));

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
    }

    private int generateTaskId() {
        counterTask++;
        return counterTask;
    }

    @Override
    public Task createTask(Task task) {
        task.setTaskId(generateTaskId());
        tasks.put(task.getTaskId(), task);

        if (checkTaskTime(task)) {
            priorTasks.add(task);
        }
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setTaskId(generateTaskId());
        epics.put(epic.getTaskId(), epic);
        return epic;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        Integer  epicId = subTask.getEpicId();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            throw new NotFoundException("Не найден Epic: " + epicId);
        }
        subTask.setTaskId(generateTaskId());
        subTasks.put(subTask.getTaskId(), subTask);
        epic.addSubTask(subTask);
        epic.calculateEpicFields();
        if (checkTaskTime(subTask)) {
            priorTasks.add(subTask);
        }
        return subTask;
    }

    @Override
    public Task getTask(int taskId) {
        Task task = tasks.get(taskId);
        if (task == null) {
            throw new NotFoundException("Не найден Task: " + taskId);
        }
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpic(int taskId) {
        Epic epic = epics.get(taskId);
        if (epic == null) {
            throw new NotFoundException("Не найден Epic: " + taskId);
        }
        historyManager.add(epic);

        return epic;
    }

    @Override
    public SubTask getSubTask(int taskId) {
        SubTask subTask = subTasks.get(taskId);
        if (subTask == null) {
            throw new NotFoundException("Не найден SubTask: " + taskId);
        }
        historyManager.add(subTasks.get(taskId));
        return subTask;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void deleteTasks() {
        for (int curTaskId : tasks.keySet()) {
            historyManager.remove(curTaskId);
        }
        tasks.clear();
    }

    @Override
    public void deleteEpics() {
        for (int curEpicId : epics.keySet()) {
            historyManager.remove(curEpicId);
        }
        deleteSubTasks();
        epics.clear();
    }

    @Override
    public void deleteSubTasks() {
        for (int curTaskId : subTasks.keySet()) {
            historyManager.remove(curTaskId);
        }
        subTasks.clear();
    }

    @Override
    public void deleteTask(int taskId) {
        Task task = tasks.get(taskId);
        tasks.remove(taskId);
        historyManager.remove(taskId);
        priorTasks.remove(task);
    }

    @Override
    public void deleteEpic(int taskId) {
        Epic epic = epics.get(taskId);
        List<SubTask> subTasksList = epic.getSubTasksList();

        subTasksList.stream()
                .peek(curSubTask -> historyManager.remove(curSubTask.getTaskId()))
                .collect(Collectors.toList());


        if (epics.get(taskId) != null) {
            epic.getSubTasksList().stream()
                            .peek(subTask -> subTasks.remove(subTask.getTaskId()))
                            .collect(Collectors.toList());

            epic.removeSubAllSubTask();
            epics.remove(taskId);
            historyManager.remove(taskId);
        }
    }
    @Override
    public void deleteSubTask(int taskId) {
        SubTask subTask = subTasks.get(taskId);
        Epic epic = epics.get(subTask.getEpicId());

        if (subTask == null) {
            throw new NotFoundException("Не найден Epic:" + subTask.getEpicId());
        }
        subTasks.remove(taskId);
        historyManager.remove(taskId);
        epic.removeSubTask(subTask);
        epic.calculateEpicFields();
        priorTasks.remove(subTask);
    }
    @Override
    public void updateTask(Task task) {
        tasks.put(task.getTaskId(), task);

        if (checkTaskTime(task)) {
            priorTasks.remove(task);
            priorTasks.add(task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getTaskId(),epic);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        Integer epicId = subTask.getEpicId();
        subTasks.put(subTask.getTaskId(),subTask);

        Epic epicUpd = epics.get(epicId);
        if (epicUpd == null) {
            throw new NotFoundException("Не найден Epic: " + epicId);
        }
        epicUpd.calculateEpicFields();

        if (checkTaskTime(subTask)) {
            priorTasks.remove(subTask);
            priorTasks.add(subTask);
        }
    }

    public List<SubTask> getListForEpic(Epic epic) {
        return epic.getSubTasksList();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
    public Set<Task>  getPrioritizedTasks() {

        return priorTasks;
    }
    private boolean checkTaskTime(Task  task) {

        for (Task taskCurrent : priorTasks) {
            if (task.getTaskId() == taskCurrent.getTaskId()) {
                continue;
            }
            if(task.getStartTime().isBefore(taskCurrent.getEndTime())  && task.getEndTime().isAfter(taskCurrent.getStartTime())) {
                throw new ValidationException("Обнаружено пересечение с задаче " + taskCurrent.getTaskId());
            }
        }
        return true;
    }
}
