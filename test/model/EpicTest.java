package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.Manager;

class EpicTest {
    Manager manager = new Manager();
    InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) manager.getDefault();
    Epic EpicTst = new Epic("Epic_1","Первый эпик");
    Epic newEpic = inMemoryTaskManager.createEpic(EpicTst);
    @Test
    void checkEqualsEpicById() {
        int  epicId = newEpic.getTaskId();
        Epic epicCheck = inMemoryTaskManager.getEpic(epicId);
        Assertions.assertEquals(EpicTst.hashCode(),epicCheck.hashCode(),"Разные HashCode");
    }
    @Test
    void epicIsCreated(){
        int  epicId = newEpic.getTaskId();
        Epic epicTst = inMemoryTaskManager.getEpic(epicId);
        Assertions.assertNotNull(epicTst, "Эпик не создан");
    }
    @Test
    void checkChangeFields() {
        int  epicId  = newEpic.getTaskId();
        Epic epicUpd = inMemoryTaskManager.getEpic(epicId);
        String oldName = epicUpd.getTaskName();
        String oldDescr = epicUpd.getTaskDescription();

        inMemoryTaskManager.updateEpic(new Epic(epicUpd.getTaskId(),epicUpd.getTaskName(),"новый descr"));
        Epic epicAfterUpd = inMemoryTaskManager.getEpic(epicId);
        Assertions.assertEquals(oldName,epicAfterUpd.getTaskName());
        Assertions.assertNotEquals(oldDescr,epicAfterUpd.getTaskDescription(),"Описание не поменялось");
    }
    @Test
    void checkEpicIsCreated(){
        int  epicId = newEpic.getTaskId();
        Epic epicTst = inMemoryTaskManager.getEpic(epicId);
        Assertions.assertNotNull(epicTst, "Эпик не создан");
    }
}