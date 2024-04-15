package model;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.Manager;

class SubTaskTest {
    Manager manager = new Manager();
    InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) manager.getDefault();
    Epic EpicTstST = new Epic("Epic_1","Первый эпик");
    Epic newEpicTstsST = inMemoryTaskManager.createEpic(EpicTstST);
    int  epicId = newEpicTstsST.getTaskId();
    @Test
    void chechSubTaskCreate() {
        SubTask  subTask = new SubTask("SubTask_1", "SubTask_Descr", TaskStatus.NEW, epicId);
        SubTask subtaskCreate = inMemoryTaskManager.createSubTask(subTask);
        Assertions.assertNotNull(subtaskCreate,"SubTask не создан");
    }
    @Test
    void checkSubTaskBelongEpic() {
        SubTask  subTaskBelongEpic = new SubTask("SubTask_2", "SubTask_Descr2", TaskStatus.NEW, epicId);
        SubTask  subTaskCreateBelongEpic = inMemoryTaskManager.createSubTask(subTaskBelongEpic);
        Assertions.assertEquals(subTaskCreateBelongEpic.getEpicId(),epicId, "SubTask создан не в том эпике");
    }
    @Test
    void checkDeleteSubTask() {

        SubTask  subTaskDel = new SubTask("SubTask_2", "SubTask_Descr2", TaskStatus.NEW, epicId);
        SubTask  subTaskCreateDel = inMemoryTaskManager.createSubTask(subTaskDel);
        int taskId = subTaskCreateDel.getTaskId();
        inMemoryTaskManager.deleteSubTask(taskId);
        Assertions.assertNull(inMemoryTaskManager.getTask(taskId),"SubTask не удалился");
    }
}