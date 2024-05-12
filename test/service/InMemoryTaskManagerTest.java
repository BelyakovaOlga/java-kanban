package service;
import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryTaskManagerTest {
    TaskManager  taskManager = Manager.getDefault();
    Task task1 = taskManager.createTask(new Task("Task_1", "Desc_1", TaskStatus.NEW));
    Epic epic1 = taskManager.createEpic(new Epic("Epic_1", "EpicDescr_1"));
    SubTask subTask1 = taskManager.createSubTask(new SubTask("SubTask_1", "SubTaskDescr_1", TaskStatus.NEW, epic1.getTaskId()));
    SubTask subTask2 = taskManager.createSubTask(new SubTask("SubTask_2", "SubTaskDescr_2", TaskStatus.NEW, epic1.getTaskId()));
    SubTask subTask3 = taskManager.createSubTask(new SubTask("SubTask_3", "SubTaskDescr_3", TaskStatus.NEW, epic1.getTaskId()));

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
