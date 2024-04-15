import model.TaskStatus;
import service.Manager;
import service.InMemoryTaskManager;
import model.Task;
import model.Epic;
import model.SubTask;

public class Main {
    public static void main(String[] args) {

        Manager manager = new Manager();
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) manager.getDefault();
        System.out.println("Создаем задачи разных типов:");
        Task task = inMemoryTaskManager.createTask(new Task("Task_1", "Desc_1", TaskStatus.NEW));
        Epic epic = inMemoryTaskManager.createEpic(new Epic("Epic_1", "EpicDescr_1"));

        SubTask subTask1 = inMemoryTaskManager.createSubTask(new SubTask("SubTask_1", "SubTaskDescr_1", TaskStatus.NEW, epic.getTaskId()));
        SubTask subTask2 = inMemoryTaskManager.createSubTask(new SubTask("SubTask_2", "SubTaskDescr_2", TaskStatus.NEW, epic.getTaskId()));
        SubTask subTask3 = inMemoryTaskManager.createSubTask(new SubTask("SubTask_3", "SubTaskDescr_3", TaskStatus.NEW, epic.getTaskId()));
        SubTask subTask4 = inMemoryTaskManager.createSubTask(new SubTask("SubTask_4", "SubTaskDescr_4", TaskStatus.NEW, epic.getTaskId()));

        Epic epic2 = inMemoryTaskManager.createEpic(new Epic("Epic_2", "EpicDescr_2"));
        SubTask subTaskE2 = inMemoryTaskManager.createSubTask(new SubTask("SubTask_E2", "SubTaskDescr_E2", TaskStatus.NEW, epic2.getTaskId()));

        System.out.println(inMemoryTaskManager.getAllTasks());
        System.out.println(inMemoryTaskManager.getAllEpics());

        System.out.println("Тестируем обновление TaskId: " + task.getTaskId());
        Task taskUpd = inMemoryTaskManager.getTask(task.getTaskId());
        System.out.println("До: " + taskUpd.toString());
        inMemoryTaskManager.updateTask(new Task(taskUpd.getTaskId(), "TaskUpd", "TaskUpdDescr", TaskStatus.IN_PROGRESS));
        System.out.println("После: " + inMemoryTaskManager.getTask(task.getTaskId()).toString());

        System.out.println("Тестируем обновление SubTaskID: " + subTask2.getTaskId());
        SubTask subTaskUpd = inMemoryTaskManager.getSubTask(subTask2.getTaskId());
        System.out.println("До: " + subTaskUpd.toString());
        inMemoryTaskManager.updateSubTask(new SubTask(subTaskUpd.getTaskId(), "ОбновИмяПодзад", "ОбновDescrПодзад", TaskStatus.NEW, subTaskUpd.getEpicId()));
        System.out.println("После: " + inMemoryTaskManager.getSubTask(subTask2.getTaskId()).toString());

        int epicId = subTask2.getEpicId();
        System.out.println("Тестируем Удаление подзадачи: " + subTask2.getTaskId() + "EpicID " + subTask2.getEpicId());
        inMemoryTaskManager.deleteSubTask(subTask2.getTaskId());
        System.out.println(inMemoryTaskManager.getEpic(epicId));

        System.out.println("Тестируем Обновление статуса EpicID: " + epic2.getTaskId());
        System.out.println("до: " + inMemoryTaskManager.getEpic(epic2.getTaskId()));
        inMemoryTaskManager.updateSubTask(new SubTask(subTaskE2.getTaskId(),subTaskE2.getTaskName(),subTaskE2.getTaskDescription(),TaskStatus.DONE,subTaskE2.getEpicId()));
        System.out.println("после: " + inMemoryTaskManager.getEpic(epic2.getTaskId()));


        System.out.println("Тестируем Удаление всех Epics");
        inMemoryTaskManager.deleteEpics();
        System.out.println(inMemoryTaskManager.getAllTasks());
        System.out.println(inMemoryTaskManager.getAllEpics());
        System.out.println( inMemoryTaskManager.getAllSubTasks());

        System.out.println("История обращений");

        System.out.println(inMemoryTaskManager.getHistory());
    }
}

