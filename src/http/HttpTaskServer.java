package http;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskStatus;
import service.Manager;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    public static final int port = 8080;
    HttpServer httpServer;
    TaskManager manager;
    Gson gson;

    public HttpTaskServer() {
        this(Manager.getDefault());
    }

    public HttpTaskServer(TaskManager manager) {
        this.manager = manager;
        try {
            this.httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        httpServer.createContext("/tasks", new TaskHandler(manager));
        httpServer.createContext("/subtasks", new SubTaskHandler(manager));
        httpServer.createContext("/epics", new EpicHandler(manager));
        httpServer.createContext("/history", new HistoryHandler(manager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(manager));
    }

    public static void main(String[] args) throws IOException {
        LocalDateTime localDateTimeT1 = LocalDateTime.of(2024, 06, 06, 8, 0);
        LocalDateTime localDateTimeST1 = LocalDateTime.of(2024, 06, 06, 12, 30);

        Duration durationT1 = Duration.ofHours(4);
        Duration durationST1 = Duration.ofMinutes(30);

        TaskManager taskManager = Manager.getDefault();
        Task task1 = taskManager.createTask(new Task("Task_1", "DescTask_1", TaskStatus.NEW, localDateTimeT1, durationT1));
        Epic epic1 = taskManager.createEpic(new Epic("Epic_1", "EpicDescr_1"));
        SubTask subTask1 = taskManager.createSubTask(new SubTask("SubTask_1", "SubTaskDescr_1", TaskStatus.NEW, epic1.getTaskId(), localDateTimeST1, durationST1));

        HttpTaskServer taskServer = new HttpTaskServer(taskManager);
        taskServer.start();
        taskServer.stop();
    }

    public void start() {
        System.out.println("Запустили сервер на порту " + port);
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Остановили сервер на порту " + port);
    }
}
