package service;

import model.Epic;
import model.SubTask;
import model.Task;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager{
    int counterTask;
    private HashMap<Integer, Task> tasks; // = new HashMap<>();
    private HashMap<Integer, Epic> epics; //  = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks; // = new HashMap<>();
    private final HistoryManager historyManager; //=  Manager.getDefaultHistory();

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
    }
    private int generateTaskId(){
        counterTask++;
        return counterTask;
    }
    @Override
    public Task createTask(Task task) {
        task.setTaskId(generateTaskId());
        tasks.put(task.getTaskId(), task);
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
        Epic  epic = epics.get(subTask.getEpicId());

        subTask.setTaskId(generateTaskId());
        subTasks.put(subTask.getTaskId(), subTask);
        epic.addSubTask(subTask);
        epic.setStatus(epic.calulateEpicStatus());
        return subTask;
    }
    @Override
    public Task getTask(int taskId) {
        historyManager.add(tasks.get(taskId));
        return tasks.get(taskId);
    }
    @Override
    public Epic getEpic(int taskId){
        historyManager.add(epics.get(taskId));
        return epics.get(taskId);
    }
    @Override
    public SubTask getSubTask(int taskId) {
        historyManager.add(subTasks.get(taskId));
        return subTasks.get(taskId);
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
        tasks.clear();
    }
    @Override
    public void deleteEpics() {
        deleteSubTasks();
        epics.clear();
    }
    @Override
    public void deleteSubTasks() {
        subTasks.clear();
    }
    @Override
    public void deleteTask(int taskId) {
        tasks.remove(taskId);
    }
    @Override
    public void deleteEpic(int taskId) {
        ArrayList<SubTask> subTasksList;
        Epic  epic = epics.get(taskId);
        if (epics.get(taskId) != null) {
            epic.removeSubAllSubTask();
            epics.remove(taskId);
        }
    }
    @Override
    public void deleteSubTask(int taskId) {
        SubTask subTask = subTasks.get(taskId);
        Epic    epic = epics.get(subTask.getEpicId());

        if (subTask != null) {
            subTasks.remove(taskId);
            epic.removeSubTask(subTask);
            epic.setStatus(epic.calulateEpicStatus());
        }
    }
    @Override
    public void updateTask(Task task) {
        tasks.put(task.getTaskId(),task);
    }
    @Override
    public void updateEpic(Epic epic) {
        Epic epicUpd = epics.get(epic.getTaskId());
        epicUpd.setName(epic.getTaskName());
        epicUpd.setDescription(epic.getTaskDescription());
    }
    @Override
    public void updateSubTask(SubTask subTask) {
        SubTask subTaskUpd = subTasks.get(subTask.getTaskId());
        subTaskUpd.setName(subTask.getTaskName());
        subTaskUpd.setDescription(subTask.getTaskDescription());
        subTaskUpd.setEpicId(subTask.getEpicId());
        subTaskUpd.setStatus(subTask.getTaskStatus());

        Epic epicUpd = epics.get(subTask.getEpicId());
        epicUpd.setStatus(epicUpd.calulateEpicStatus());
    }
    public List<SubTask> getListForEpic(Epic epic){
        return epic.getSubTasksList();
    }
    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }
}
