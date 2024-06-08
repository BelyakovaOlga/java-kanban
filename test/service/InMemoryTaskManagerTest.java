package service;
import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager>{
    TaskManager  taskManager = Manager.getDefault();
    Task task1 = taskManager.createTask(new Task("Task_1", "Desc_1", TaskStatus.NEW,LocalDateTime.of(2024,01,1,14,00), Duration.ofHours(1)));
    Epic epic1 = taskManager.createEpic(new Epic("Epic_1", "EpicDescr_1"));
    SubTask subTask1 = taskManager.createSubTask(new SubTask("SubTask_1", "SubTaskDescr_1", TaskStatus.NEW, epic1.getTaskId(), LocalDateTime.of(2024,01,12,14,00), Duration.ofHours(1)));
    SubTask subTask2 = taskManager.createSubTask(new SubTask("SubTask_2", "SubTaskDescr_2", TaskStatus.NEW, epic1.getTaskId(),LocalDateTime.of(2024,01,11,14,00), Duration.ofHours(1)));
    SubTask subTask3 = taskManager.createSubTask(new SubTask("SubTask_3", "SubTaskDescr_3", TaskStatus.NEW, epic1.getTaskId(),LocalDateTime.of(2024,01,10,14,00), Duration.ofHours(1)));
    @Override
    public InMemoryTaskManager createManager() {
        return new InMemoryTaskManager(new InMemoryHistoryManager());
    }
    @Test
    @DisplayName("Должен удалять из истории все подзадачи Epic,при удалении Epic")
    void sholdEpicAndAllSubTask(){
        taskManager.getTask(task1.getTaskId());
        taskManager.getSubTask(subTask1.getTaskId());
        taskManager.getSubTask(subTask3.getTaskId());
        taskManager.deleteEpic(epic1.getTaskId());
        assertEquals(taskManager.getHistory(), List.of(task1),"История содержит , подзадачи, удаленного epic");
    }
    @Test
    @DisplayName("Должен очищать Историю при удалении всех задач")
    void shouldClearHistory() {
        taskManager.getTask(task1.getTaskId());
        taskManager.getSubTask(subTask1.getTaskId());
        taskManager.getSubTask(subTask3.getTaskId());
        taskManager.getEpic(epic1.getTaskId());
        taskManager.deleteEpic(epic1.getTaskId());
        taskManager.deleteEpics();
        taskManager.deleteTasks();
        assertEquals(taskManager.getHistory(), List.of(),"История Не пустая, после удаления всех задач, подзадач и эпиков");
    }
}
