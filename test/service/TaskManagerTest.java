package service;
import exception.ValidationException;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class TaskManagerTest<T extends TaskManager>  {
    T manager;
    Task task;
    protected abstract T createManager();
    @BeforeEach
    void init(){
        manager = createManager();
        task = manager.createTask(new Task("Task_1", "DescTask_1", TaskStatus.NEW, LocalDateTime.of(2024,06,06,8,0), Duration.ofMinutes(30)));
    }
    @DisplayName("Нет пересечений самим с собой")
    @Test
    void shouldNotConflictItself() {
        Assertions.assertDoesNotThrow(() -> { manager.updateTask(task);}, "Исключения быть не должно");
    }
    @DisplayName("Выдается ошибка, если есть  пересечение по датам - 1 вариант")
    @Test
    void shouldExceptionAboutConflictPeriod() {
        // 2024.06.06 8:00
        Assertions.assertThrows(ValidationException.class, () -> { manager.createTask(new Task("Task_1", "DescTask_1", TaskStatus.NEW, LocalDateTime.of(2024,06,06,8,0), Duration.ofMinutes(30)));} ,"Исключения с ошибкой о пересечении нет");
    }
    @DisplayName("Выдается ошибка, если есть  пересечение по датам - 2 вариант ")
    @Test
    void shouldExceptionAboutConflictPeriodLeft() {
        // 2024.06.06 7:50
        Assertions.assertThrows(ValidationException.class, () -> { manager.createTask(new Task("Task_1", "DescTask_1", TaskStatus.NEW, LocalDateTime.of(2024,06,06,7,50), Duration.ofMinutes(30)));} ,"Исключения с ошибкой о пересечении нет");
    }
    @DisplayName("Выдается ошибка, если есть  пересечение по датам - 3 вариант")
    @Test
    void shouldExceptionAboutConflictPeriodRight() {
        // 2024.06.06 8:20
        Assertions.assertThrows(ValidationException.class, () -> { manager.createTask(new Task("Task_1", "DescTask_1", TaskStatus.NEW, LocalDateTime.of(2024,06,06,8,20), Duration.ofMinutes(30)));} ,"Исключения с ошибкой о пересечении нет");
    }
    @DisplayName("Проверка приграничных StarDate задач")
    @Test
    void shouldNotConflict() {
        Assertions.assertDoesNotThrow(() ->  { manager.createTask(new Task("Task_1", "DescTask_1", TaskStatus.NEW, LocalDateTime.of(2024,06,06,8,30), Duration.ofMinutes(30)));}, "Исключения быть не должно, одна задача завершится до 8-30, другая стартует в 8-30");
    }
}
