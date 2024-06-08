import model.TaskStatus;
import service.*;
import model.Task;
import model.Epic;
import model.SubTask;

import java.time.Duration;
import java.time.LocalDateTime;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        LocalDateTime localDateTimeT1 = LocalDateTime.of(2024,06,06,8,0);
        LocalDateTime localDateTimeT2 = LocalDateTime.of(2024,06,06,6,0);
        LocalDateTime localDateTimeST1 = LocalDateTime.of(2024,06,06,12,30);
        LocalDateTime localDateTimeST2 = LocalDateTime.of(2024,06,06,14,00);
        Duration      durationT1 = Duration.ofHours(4);
        Duration      durationT2 = Duration.ofHours(1);
        Duration      durationST1 = Duration.ofMinutes(30);
        Duration      durationST2 = Duration.ofMinutes(60);
        // Обычный менеджер
        //TaskManager  taskManager = Manager.getDefault();
        // Запуск FileManager
        TaskManager taskManager = Manager.getFileManager();
        System.out.println("Создаем задачи разных типов:");

        Task task1 = taskManager.createTask(new Task("Task_1", "DescTask_1", TaskStatus.NEW, localDateTimeT1,durationT1));
        Task task2 = taskManager.createTask(new Task("Task_2", "DescTask_2", TaskStatus.NEW,localDateTimeT2,durationT2));

        Epic epic1 = taskManager.createEpic(new Epic("Epic_1", "EpicDescr_1"));

        SubTask subTask1 = taskManager.createSubTask(new SubTask("SubTask_1", "SubTaskDescr_1", TaskStatus.NEW, epic1.getTaskId(),localDateTimeST1,durationST1));
        SubTask subTask2 = taskManager.createSubTask(new SubTask("SubTask_2", "SubTaskDescr_2", TaskStatus.NEW, epic1.getTaskId(),localDateTimeST2,durationST2));

        taskManager.getEpic(epic1.getTaskId());
        taskManager.getTask(task2.getTaskId());
        taskManager.getTask(task1.getTaskId());
        taskManager.getEpic(epic1.getTaskId());
        taskManager.getSubTask(subTask1.getTaskId());
        taskManager.getSubTask(subTask2.getTaskId());
        taskManager.getSubTask(subTask1.getTaskId());
        System.out.println("История задач:");
        System.out.println(taskManager.getHistory());
        System.out.println("Приоритет задач:");
        System.out.println(taskManager.getPrioritizedTasks());


      File dir = new File("task.csv");
      FileBackedTaskManager taskManager2 = FileBackedTaskManager.loadFromFile(new InMemoryHistoryManager(),dir);
      System.out.println("История задач из Файла:");
      System.out.println(taskManager2.getHistory());
      System.out.println("Приоритет задач:");
      System.out.println(taskManager2.getPrioritizedTasks());
    }
}

