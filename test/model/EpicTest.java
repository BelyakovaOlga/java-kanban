package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.Manager;

class EpicTest {
    Manager manager = new Manager();
    InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) manager.getDefault();
    Epic epicNew = new Epic("Epic_1","Первый эпик");
    Epic epicCreateNew = inMemoryTaskManager.createEpic(epicNew);
    @Test
    void checkEqualsEpicById() {
        int  epicId = epicCreateNew.getTaskId();
        Epic epicCheckHashCode = inMemoryTaskManager.getEpic(epicId);
        Assertions.assertEquals(epicCreateNew.hashCode(),epicCheckHashCode.hashCode(),"Разные HashCode");
    }
    @Test
    void epicIsCreated(){
        int  epicId = epicCreateNew.getTaskId();
        Epic epicIsCreated = inMemoryTaskManager.getEpic(epicId);
        Assertions.assertNotNull(epicIsCreated, "Эпик не создан");
    }
    @Test
    void checkChangeFields() {
        int  epicId  = epicCreateNew.getTaskId();
        Epic epicUpd = inMemoryTaskManager.getEpic(epicId);
        String oldName = epicUpd.getTaskName();
        String oldDescr = epicUpd.getTaskDescription();

        inMemoryTaskManager.updateEpic(new Epic(epicUpd.getTaskId(),epicUpd.getTaskName(),"Новый description"));
        Epic epicAfterUpd = inMemoryTaskManager.getEpic(epicId);
        Assertions.assertEquals(oldName,epicAfterUpd.getTaskName());
        Assertions.assertNotEquals(oldDescr,epicAfterUpd.getTaskDescription(),"Описание не поменялось");
    }
}