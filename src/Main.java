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
        Task task = taskManager.createTask(new Task("Task_1", "Desc_1", TaskStatus.NEW));
        Epic epic = taskManager.createEpic(new Epic("Epic_1", "EpicDescr_1"));

        SubTask subTask1 = taskManager.createSubTask(new SubTask("SubTask_1", "SubTaskDescr_1", TaskStatus.NEW, epic.getTaskId()));
        SubTask subTask2 = taskManager.createSubTask(new SubTask("SubTask_2", "SubTaskDescr_2", TaskStatus.NEW, epic.getTaskId()));
        SubTask subTask3 = taskManager.createSubTask(new SubTask("SubTask_3", "SubTaskDescr_3", TaskStatus.NEW, epic.getTaskId()));
        SubTask subTask4 = taskManager.createSubTask(new SubTask("SubTask_4", "SubTaskDescr_4", TaskStatus.NEW, epic.getTaskId()));

        Epic epic2 = taskManager.createEpic(new Epic("Epic_2", "EpicDescr_2"));
        SubTask subTaskE2 = taskManager.createSubTask(new SubTask("SubTask_E2", "SubTaskDescr_E2", TaskStatus.NEW, epic2.getTaskId()));

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());

        System.out.println("Тестируем обновление TaskId: " + task.getTaskId());
        Task taskUpd = taskManager.getTask(task.getTaskId());
        System.out.println("До: " + taskUpd.toString());
        taskManager.updateTask(new Task(taskUpd.getTaskId(), "TaskUpd", "TaskUpdDescr", TaskStatus.IN_PROGRESS));
        System.out.println("После: " + taskManager.getTask(task.getTaskId()).toString());

        System.out.println("Тестируем обновление SubTaskID: " + subTask2.getTaskId());
        SubTask subTaskUpd = taskManager.getSubTask(subTask2.getTaskId());
        System.out.println("До: " + subTaskUpd.toString());
        taskManager.updateSubTask(new SubTask(subTaskUpd.getTaskId(), "ОбновИмяПодзад", "ОбновDescrПодзад", TaskStatus.NEW, subTaskUpd.getEpicId()));
        System.out.println("После: " + taskManager.getSubTask(subTask2.getTaskId()).toString());

        int epicId = subTask2.getEpicId();
        System.out.println("Тестируем Удаление подзадачи: " + subTask2.getTaskId() + "EpicID " + subTask2.getEpicId());
        taskManager.deleteSubTask(subTask2.getTaskId());
        System.out.println(taskManager.getEpic(epicId));

        System.out.println("Тестируем Обновление статуса EpicID: " + epic2.getTaskId());
        System.out.println("до: " + taskManager.getEpic(epic2.getTaskId()));
        taskManager.updateSubTask(new SubTask(subTaskE2.getTaskId(),subTaskE2.getTaskName(),subTaskE2.getTaskDescription(),TaskStatus.DONE,subTaskE2.getEpicId()));
        System.out.println("после: " + taskManager.getEpic(epic2.getTaskId()));


        System.out.println("Тестируем Удаление всех Epics");
        taskManager.deleteEpics();
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println( taskManager.getAllSubTasks());

        System.out.println("История обращений");

        System.out.println(taskManager.getHistory());
        //HistoryManager  historyManager = new InMemoryHistoryManager();


    }
}

