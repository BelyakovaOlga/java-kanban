public class Main {
    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();
        // класс для ввода данных для теста
        ConsoleForTest сonsoleForTest = new ConsoleForTest();
        сonsoleForTest.setTaskManager(taskManager);
        сonsoleForTest.run();
    }
}

