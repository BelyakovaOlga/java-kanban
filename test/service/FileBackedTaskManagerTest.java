package service;

import exception.MExceptionanagerSaveException;
import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class FileBackedTaskManagerTest {
    public FileBackedTaskManagerTest() {
    }
    @Test
    @DisplayName("Должен сохранять задачу в файл")
    void shouldSaveTaskInFile() throws IOException {
        File file = File.createTempFile("tstTask", ".csv");
        TaskManager  taskManager = new FileBackedTaskManager(new InMemoryHistoryManager(),file);
        Task task1 = taskManager.createTask(new Task("Task1", "Desc1", TaskStatus.NEW));
        try {
            String     string = Files.readString(file.toPath());
            String[]   lines = string.split("\n");
            String     line = lines[1];
            String[]   value = line.split(",");
            assertEquals(Integer.parseInt(value[0]), task1.getTaskId(),"ID задачи в файл записано некорректно");
            assertEquals(value[2],task1.getTaskName(), "Наименование задачи в файл записано некорректно");
            assertEquals(value[4],task1.getTaskDescription(), "Описание задачи в файл записана некорректно");
            assertEquals(TaskStatus.valueOf(value[3]),task1.getTaskStatus(), "Статус в файл записана некорректно");

        } catch (IOException e) {
            throw new MExceptionanagerSaveException("Ошибка в файле:" + file.toPath());
        }
    }
    @Test
    @DisplayName("Должен сохранять кол-во строк , равное кол-ву задач")
    void shouldSaveAllTasksInFile() throws IOException {
        File file = File.createTempFile("tstTask", ".csv");
        TaskManager  taskManager = new FileBackedTaskManager(new InMemoryHistoryManager(),file);
        Task     task1 = taskManager.createTask(new Task("Task1", "Desc1", TaskStatus.NEW));
        Epic     epic1 = taskManager.createEpic(new Epic("Epic1", "EpicDescr1"));
        SubTask  subTask1 = taskManager.createSubTask(new SubTask("SubTask1", "SubTaskDescr1", TaskStatus.NEW, epic1.getTaskId()));

        try {
            String   string = Files.readString(file.toPath());
            String[] lines = string.split("\n");
            assertEquals(3, lines.length-1,"Кол-во строк в фале не соответствует кол-ву созданных задач");
        } catch (IOException e) {
            throw new MExceptionanagerSaveException("Ошибка в файле:" + file.toPath());
        }
    }
    @Test
    @DisplayName("Должен восстанавливать задачи из файла")
    void shouldReStoreFromFile() throws IOException {
        File file = File.createTempFile("tstTask", ".csv");
        TaskManager  taskManager = new FileBackedTaskManager(new InMemoryHistoryManager(),file);
        Task task1 = taskManager.createTask(new Task("Task1", "Desc1", TaskStatus.NEW));
        TaskManager  taskManagerReStore = FileBackedTaskManager.loadFromFile(new InMemoryHistoryManager(),file);
        Task taskRestore = taskManagerReStore.getTask(task1.getTaskId());
        assertTask(task1,taskRestore);
    }
    void assertTask(Task taskExpected, Task taskActual) {
        assertEquals(taskExpected.getTaskId(),taskActual.getTaskId(),"Id восстановлен неверно");
        assertEquals(taskExpected.getTaskName(),taskActual.getTaskName(),"Name восстановлен неверно");
        assertEquals(taskExpected.getTaskDescription(),taskActual.getTaskDescription(),"Description восстановлен неверно");
        assertEquals(taskExpected.getTaskStatus(),taskActual.getTaskStatus(),"Status восстановлен неверно");
    }
}
