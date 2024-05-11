package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.Manager;
@DisplayName("Задача")
class TaskTest {
    Manager manager = new Manager();
    InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) manager.getDefault();
    Task taskNew = new Task("Task_1", "Desc_1", TaskStatus.NEW);
    Task taskNewCreate = inMemoryTaskManager.createTask(taskNew);
    Task taskNewCreate2 = new Task("Task_2", "Desc_2", TaskStatus.NEW);

    @Test
    @DisplayName("Должна совпадать со своей копией")
    void  shouldEqualsWithCopy() {
        Task taskOrig = new Task("Task","Задача",TaskStatus.NEW);
        Task taskCopy = new Task("Task","Задача",TaskStatus.NEW);
        assertEqualsTask(taskOrig,taskCopy, "Эпики должны совпадать");
    }
    private static void assertEqualsTask(Task taskOrig, Task taskCopy, String message){
        Assertions.assertEquals(taskOrig.getTaskId(),taskCopy.getTaskId(),"Id");
        Assertions.assertEquals(taskOrig.getTaskName(),taskCopy.getTaskName(),"Name");
    }
    @Test
    @DisplayName("Должен искать задачу по ее Id")
    void checkGetTaskById() {
        int  taskId = taskNew.getTaskId();
        Task taskGetTaskById = inMemoryTaskManager.getTask(taskId);
        Assertions.assertEquals(taskId,taskGetTaskById.getTaskId(), "Поиск Task по TaskId неверный");
    }
    @Test
    @DisplayName("Должен создавать новую задачу")
    void shouldCreatTask(){
        Task testIsCreated = new Task("Task_Create", "Desc_Create", TaskStatus.NEW);
        Task taskTestIsCreated = inMemoryTaskManager.createTask(testIsCreated);
        Assertions.assertNotNull(taskTestIsCreated, "Task не создан");
    }

}