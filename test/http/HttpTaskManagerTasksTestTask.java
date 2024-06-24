package http;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import service.Manager;
import service.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTasksTestTask {
    TaskManager manager = Manager.getDefault();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = BaseHttpHandler.getGson();

    public HttpTaskManagerTasksTestTask() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.deleteTasks();
        manager.deleteSubTasks();
        manager.deleteEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    @DisplayName("Должен создать задачу")
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("Test http", "Testing task http",
                        TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(5));

        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Task> tasksFromManager = manager.getAllTasks();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test http", tasksFromManager.get(0).getTaskName(), "Некорректное имя задачи");
    }
    @Test
    @DisplayName("Должен удалить задачу")
    public void testDeleteTask() throws IOException, InterruptedException {
        manager.createTask( new Task("Test http", "Testing task http",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(5)));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");

        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Task> tasksFromManager = manager.getAllTasks();
        assertEquals(0, tasksFromManager.size(), "Задача не удалена");
    }
    @Test
    @DisplayName("Должен получить задачу")
    public void testGetTask() throws IOException, InterruptedException {
        manager.createTask( new Task("Test http", "Testing task http",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(5)));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");

        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        JsonElement jsonElement = JsonParser.parseString(response.body());

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonElement taskIdElement = jsonObject.get("taskId");
        Integer taskId = taskIdElement.getAsInt();
        assertEquals(taskId,1, "Получена задача с некорректным номером");
    }
    @Test
    @DisplayName("Должен обновить задачу")
    public void testUpdateTask() throws IOException, InterruptedException {
        manager.createTask( new Task("Test http", "Testing task http",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(5)));
        Task taskUpdate = new Task(1,"Test http Update", "Testing task http",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        String taskJson = gson.toJson(taskUpdate);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Task task = manager.getTask(1);
        assertEquals("Test http Update", task.getTaskName(), "Задача не обновлена");
    }
}
