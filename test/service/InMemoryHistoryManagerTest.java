package service;

import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    TaskManager  taskManager = Manager.getDefault();
    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

    Task task1 = taskManager.createTask(new Task("Task_1", "Desc_1", TaskStatus.NEW));
    Task task2 = taskManager.createTask(new Task("Task_2", "Desc_2", TaskStatus.NEW));
    Epic epic1 = taskManager.createEpic(new Epic("Epic_1", "EpicDescr_1"));
    @Test
    @DisplayName("Должен добавлять задачи в Историю")
    void shouldAddHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(epic1);
        assertEquals(historyManager.getHistory(), List.of(task1,task2,epic1),"История задач не содержит все ожидаемые задачи");
    }
    @Test
    @DisplayName("Должен удалять первую задачу из Истории")
    void shouldFirstRemoveHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(epic1);
        historyManager.remove(task1.getTaskId());
        assertEquals(historyManager.getHistory(), List.of(task2,epic1),"История содержит , удаленный Task1");
    }
    @Test
    @DisplayName("Должен удалять последнюю задачу из Истории")
    void shouldLastRemoveHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(epic1);
        historyManager.remove(epic1.getTaskId());
        assertEquals(historyManager.getHistory(), List.of(task1,task2),"История содержит , удаленный Epic");
    }

    @Test
    @DisplayName("Должен удалять задачу внутри Истории")
    void shouldRemoveInTheMiddlOfHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(epic1);
        historyManager.remove(task2.getTaskId());
        assertEquals(historyManager.getHistory(), List.of(task1,epic1),"История содержит , удаленный task");
    }
    @Test
    @DisplayName("Должен удалять задачу, вызванную повторно из Истории, оставлят последний вызванный вариант")
    void shouldRemoveRepeatedTaskHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(epic1);
        historyManager.add(task1);
        assertEquals(historyManager.getHistory(), List.of(task2,epic1,task1),"История содержит , удаленный task");
    }
}