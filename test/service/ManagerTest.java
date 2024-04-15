package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagerTest {
    Manager manager = new Manager();
    @Test
    void chechInitManager() {
        Assertions.assertNotNull(manager.getDefault(), "экземпляры менеджеров не проинициализирован");
    }
}