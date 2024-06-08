package model;
import exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.Manager;

import java.time.Duration;
import java.time.LocalDateTime;

class SubTaskTest {
    Manager manager = new Manager();
    InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) manager.getDefault();
    Epic epicForSubTask = new Epic("Epic_1", "Первый эпик");
    Epic newEpicForSubTask = inMemoryTaskManager.createEpic(epicForSubTask);
    int epicId = newEpicForSubTask.getTaskId();
    @DisplayName("Должна быть создана задача")
    @Test
    void checkSubTaskIsCreated() {
        SubTask subTask = new SubTask("SubTask_1", "SubTask_Descr", TaskStatus.NEW, epicId, LocalDateTime.of(2024,06,06,14,00), Duration.ofHours(4));
        SubTask subtaskCreate = inMemoryTaskManager.createSubTask(subTask);
        Assertions.assertNotNull(subtaskCreate, "SubTask не создан");
    }
    @DisplayName("Подзадача попала в нужный Epic")
    @Test
    void checkSubTaskBelongEpic() {
        SubTask subTaskBelongEpic = new SubTask("SubTask_2", "SubTask_Descr2", TaskStatus.NEW, epicId,LocalDateTime.of(2024,06,012,14,00), Duration.ofHours(1));
        SubTask subTaskCreateBelongEpic = inMemoryTaskManager.createSubTask(subTaskBelongEpic);
        Assertions.assertEquals(subTaskCreateBelongEpic.getEpicId(), epicId, "SubTask создан не в том эпике");
    }
    @DisplayName("Подзадача должна быть удалена и выдано сообщение что подзадачи с таким номером нет")
    @Test
    void checkDeleteSubTask() {
        SubTask subTaskForDel = new SubTask("SubTask_2", "SubTask_Descr2", TaskStatus.NEW, epicId,LocalDateTime.of(2024,05,012,14,00), Duration.ofHours(1));
        SubTask subTaskTestingDel = inMemoryTaskManager.createSubTask(subTaskForDel);
        int taskId = subTaskTestingDel.getTaskId();
        inMemoryTaskManager.deleteSubTask(taskId);
        Assertions.assertThrows(NotFoundException.class, () -> {inMemoryTaskManager.getSubTask(taskId);}, "SubTask не удалился");

    }
    @DisplayName("Epic, под который создается подзадача не существует и должно быть выдано исключение")
    @Test
    void shouldNotExistEpic() {
        int epicIdNotExist = 1000;
        Assertions.assertThrows(NotFoundException.class, () -> { inMemoryTaskManager.createSubTask(new SubTask("SubTask_2", "SubTask_Descr2", TaskStatus.NEW, epicIdNotExist,LocalDateTime.of(2024,05,012,14,00), Duration.ofHours(1)));}, "Исключение не выдано");

    }
    @DisplayName("Проверка что Epic, под который создается подзадача существует и исключение не появляется")
    @Test
    void shouldExistEpic() {
         Assertions.assertDoesNotThrow(() -> { inMemoryTaskManager.createSubTask(new SubTask("SubTask_2", "SubTask_Descr2", TaskStatus.NEW, epicId,LocalDateTime.of(2024,05,012,14,00), Duration.ofHours(1)));}, "Выдано исклчение о несуществующем эпике");
    }
}