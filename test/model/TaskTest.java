package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.Manager;
class TaskTest {
    Manager manager = new Manager();
    InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) manager.getDefault();
    Task taskNew = new Task("Task_1", "Desc_1", TaskStatus.NEW);
    Task taskNewCreate = inMemoryTaskManager.createTask(taskNew);
    Task taskNewCreate2 = new Task("Task_2", "Desc_2", TaskStatus.NEW);
    @Test
    void checkGetTaskById() {
        int  taskId = taskNew.getTaskId();
        Task taskGetTaskById = inMemoryTaskManager.getTask(taskId);
        Assertions.assertEquals(taskId,taskGetTaskById.getTaskId(), "Поиск Task по TaskId неверный");
    }
    @Test
    void checkEqualsTasksById() {
        int  taskId = taskNew.getTaskId();
        Task taskCheckHashCode = inMemoryTaskManager.getTask(taskId);
        Assertions.assertEquals(taskNew.hashCode(),taskCheckHashCode.hashCode(), "Разные HashCode");
    }
    @Test
    void taskIsCreated(){
        Task testIsCreated = new Task("Task_Create", "Desc_Create", TaskStatus.NEW);
        Task taskTestIsCreated = inMemoryTaskManager.createTask(testIsCreated);
        Assertions.assertNotNull(taskTestIsCreated, "Task не создан");
    }

}