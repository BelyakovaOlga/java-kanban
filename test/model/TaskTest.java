package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.Manager;
class TaskTest {
    Manager manager = new Manager();
    InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) manager.getDefault();
    Task testTst1 = new Task("Task_1", "Desc_1", TaskStatus.NEW);
    Task taskNew = inMemoryTaskManager.createTask(testTst1);
    Task testTst2 = new Task("Task_2", "Desc_2", TaskStatus.NEW);
    @Test
    void checkGetTaskById() {
        int  taskId = taskNew.getTaskId();
        Task taskGetTaskById = inMemoryTaskManager.getTask(taskId);
        Assertions.assertEquals(taskId,taskGetTaskById.getTaskId(), "Поиск Task по TaskId неверный");
    }
    @Test
    void checkEqualsTasksById() {
        int  taskId = taskNew.getTaskId();
        Task taskCheck = inMemoryTaskManager.getTask(taskId);
        Assertions.assertEquals(taskNew.hashCode(),taskCheck.hashCode(), "Разные HashCode");
    }
    @Test
    void taskIsCreated(){
        Task testTstCreate = new Task("Task_Create", "Desc_Create", TaskStatus.NEW);
        Task taskTstCreate = inMemoryTaskManager.createTask(testTstCreate);
        Assertions.assertNotNull(taskTstCreate, "Task не создан");
    }

}