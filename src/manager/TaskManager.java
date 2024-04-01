package manager;

import dataModel.Epic;
import dataModel.SubTask;
import dataModel.Task;
import enumTask.TaskStatus;
import enumTask.TaskType;
import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    int counterTask;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, SubTask> subTasks = new HashMap<>();
    HashMap<Integer, TaskType> mapNumberTask = new HashMap<>();
    HashMap<Integer, ArrayList> subTasksEpic = new HashMap<>(); // список подзадач для Эпиков( key: ID of dataModel.Epic)

    public Task createTask(String nameTask, String descriptionTask) {
        counterTask++;
        mapNumberTask.put(counterTask, TaskType.TASK);
        Task task = new Task(counterTask, nameTask, descriptionTask);
        tasks.put(counterTask, task);

        return task;
    }

    public Epic createEpic(String nameTask, String descriptionTask) {
        counterTask++;
        mapNumberTask.put(counterTask, TaskType.EPIC);
        Epic taskEpic = new Epic(counterTask, nameTask, descriptionTask);
        epics.put(counterTask, taskEpic);
        return taskEpic;
    }

    public SubTask createSubTask(String nameTask, String descriptionTask, int epicId) {
        ArrayList<SubTask> subTasksList;
        Epic epic = epics.get(epicId);

        counterTask++;
        mapNumberTask.put(counterTask, TaskType.SUBTASK);
        SubTask subTask = new SubTask(counterTask, nameTask, descriptionTask, epicId);
        subTasks.put(counterTask, subTask);
        subTasksList = subTasksEpic.get(epicId);

        if (subTasksList == null) {
            subTasksList = new ArrayList<>();
        }
        subTasksList.add(subTask);
        subTasksEpic.put(epicId, subTasksList);
        epic.setSubTasksList(subTasksList);
        return subTask;
    }

    public void writeTasks() {
        if (!mapNumberTask.isEmpty()) {
            for (int taskId : mapNumberTask.keySet()) {
                switch (mapNumberTask.get(taskId)) {
                    case TaskType.TASK:
                        System.out.println(tasks.get(taskId).toString());
                        break;
                    case TaskType.EPIC:
                        System.out.println(epics.get(taskId).toString());
                        break;
                    case TaskType.SUBTASK:
                        System.out.println(subTasks.get(taskId).toString());
                        break;
                }
            }
        }
    }

    public Task getTask(int taskId) {
        Task task = null;
        TaskType   taskType = mapNumberTask.get(taskId);
        if (taskType != null) {
            switch (taskType) {
                case TaskType.TASK:
                    task = tasks.get(taskId);
                    break;
                case TaskType.EPIC:
                    task = epics.get(taskId);
                    break;
                case TaskType.SUBTASK:
                    task = subTasks.get(taskId);
                    break;
            }
        }
        return task;
    }
    public void deleteTasks() {
        tasks.clear();
        epics.clear();
        subTasks.clear();
        mapNumberTask.clear();
    }

    public void deleteTask(int taskId) {
        tasks.remove(taskId);
        mapNumberTask.remove(taskId);
    }

    public void deleteEpic(int taskId) {
        ArrayList<SubTask> subTasksList;
        if (epics.get(taskId) != null) {
            subTasksList = subTasksEpic.get(taskId);
            if (subTasksList != null) {
                for (SubTask subTask : subTasksList) {
                    subTasks.remove(subTask.getTaskId());
                }
                subTasksList.clear();
            }
            epics.remove(taskId);
            mapNumberTask.remove(taskId);
        }
    }

    public void deleteSubTask(int taskId) {
        SubTask subTask = subTasks.get(taskId);
        if (subTask != null) {
            Epic epic = epics.get(subTask.epicTaskId);
            ArrayList<SubTask> subTasksList = subTasksEpic.get(subTask.epicTaskId);
            if (subTasksList != null) {
                subTasksList.remove(subTask);
                epic.setSubTasksList(subTasksList);
            }
            subTasks.remove(taskId);
            mapNumberTask.remove(taskId);
        }
    }

    public TaskStatus getStatusEpic(int taskId) {
        ArrayList<SubTask> subTasksList = subTasksEpic.get(taskId);
        TaskStatus status;
        for (SubTask subTask : subTasksList) {
            if (subTask.status != TaskStatus.DONE) {
                return TaskStatus.NEW;
            }
        }
        return TaskStatus.DONE;
    }

    public Task updateTask(int taskId, String nameTask, String descriptionTask, TaskStatus taskStatus) {
        Task task = tasks.get(taskId);

        if (task != null) {
            task.name = nameTask;
            task.description = descriptionTask;
            task.status = taskStatus;
        }
        return task;
    }

    public Epic updateEpic(int taskId, String nameTask, String descriptionTask) {
        Epic epic = (Epic) tasks.get(taskId);

        if (epic != null) {
            epic.name = nameTask;
            epic.description = descriptionTask;
            epic.status = getStatusEpic(taskId);
        }
        return epic;
    }

    public SubTask updateSubTask(int taskId, String nameTask, String descriptionTask, TaskStatus taskStatus) {
        SubTask subTask = subTasks.get(taskId);
        if (subTask != null) {
            subTask.name = nameTask;
            subTask.description = descriptionTask;
            subTask.status = taskStatus;
            Epic epic = epics.get(subTask.epicTaskId);
            updateEpic(subTask.epicTaskId, epic.name, epic.description);
        }
        return subTask;
    }
}
