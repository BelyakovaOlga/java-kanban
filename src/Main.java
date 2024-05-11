import model.TaskStatus;
import service.*;
import service.InMemoryHistoryManager;
import service.HistoryManager;
import model.Task;
import model.Epic;
import model.SubTask;

public class Main {
    public static void main(String[] args) {

        TaskManager  taskManager = Manager.getDefault();

        System.out.println("Создаем задачи разных типов:");
        Task task1 = taskManager.createTask(new Task("Task_1", "Desc_1", TaskStatus.NEW));
        Task task2 = taskManager.createTask(new Task("Task_2", "Desc_2", TaskStatus.NEW));
        Epic epic1 = taskManager.createEpic(new Epic("Epic_1", "EpicDescr_1"));

        SubTask subTask1 = taskManager.createSubTask(new SubTask("SubTask_1", "SubTaskDescr_1", TaskStatus.NEW, epic1.getTaskId()));
        SubTask subTask2 = taskManager.createSubTask(new SubTask("SubTask_2", "SubTaskDescr_2", TaskStatus.NEW, epic1.getTaskId()));
        SubTask subTask3 = taskManager.createSubTask(new SubTask("SubTask_3", "SubTaskDescr_3", TaskStatus.NEW, epic1.getTaskId()));

        Epic epic2 = taskManager.createEpic(new Epic("Epic_2", "EpicDescr_2"));

        taskManager.getEpic(epic1.getTaskId());
        taskManager.getTask(task1.getTaskId());
        taskManager.getEpic(epic2.getTaskId());
        taskManager.getEpic(epic1.getTaskId());
        taskManager.getSubTask(subTask1.getTaskId());
        taskManager.getSubTask(subTask2.getTaskId());
        taskManager.getSubTask(subTask3.getTaskId());
        taskManager.getSubTask(subTask1.getTaskId());
        taskManager.deleteEpic(epic1.getTaskId());

        System.out.println(taskManager.getHistory());
    }
}

