package service;

import model.Epic;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    Manager manager = new Manager();
    HistoryManager inMemoryHistoryManager =  Manager.getDefaultHistory();
    InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) manager.getDefault();
    Task testTst = new Task("Task_1", "Desc_1", TaskStatus.NEW);
    Epic epicTst = new Epic("Epic_1","Первый эпик");
    @Test
    void checkSizeList() {
        Task taskNew = inMemoryTaskManager.createTask(testTst);
        Epic taskEpic = inMemoryTaskManager.createEpic(epicTst);

        inMemoryTaskManager.getTask(taskNew.getTaskId()); //1
        inMemoryTaskManager.getTask(taskNew.getTaskId()); //2
        inMemoryTaskManager.getTask(taskNew.getTaskId()); //3
        inMemoryTaskManager.getTask(taskNew.getTaskId()); //4
        inMemoryTaskManager.getTask(taskNew.getTaskId()); //5
        inMemoryTaskManager.getTask(taskNew.getTaskId()); //6
        inMemoryTaskManager.getTask(taskNew.getTaskId()); //7
        inMemoryTaskManager.getTask(taskNew.getTaskId()); //8
        inMemoryTaskManager.getTask(taskNew.getTaskId()); //9
        inMemoryTaskManager.getTask(taskNew.getTaskId()); //10
        inMemoryTaskManager.getTask(taskNew.getTaskId()); //11
        inMemoryTaskManager.getEpic(taskEpic.getTaskId());//12

        Assertions.assertEquals(10,inMemoryTaskManager.getHistory().size(),"Размер списка истории таксов превышает 10");
        Assertions.assertEquals(taskEpic.getTaskId(),inMemoryTaskManager.getHistory().get(9).getTaskId(),"Не происходит удаление устаревших записей");
    }

}