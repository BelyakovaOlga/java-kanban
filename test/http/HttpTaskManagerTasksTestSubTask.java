package http;

import com.google.gson.Gson;

import model.Epic;
import model.SubTask;
import model.TaskStatus;
import org.junit.jupiter.api.*;
import service.Manager;
import service.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTasksTestSubTask {
    TaskManager manager = Manager.getDefault();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = BaseHttpHandler.getGson();

    public HttpTaskManagerTasksTestSubTask() throws IOException {
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
    @DisplayName("Должен создать подзадачу")
    public void testAddSubTask() throws IOException, InterruptedException {
        manager.createEpic(new Epic("Epic1", "EpicDescr1"));
        SubTask subTask = new SubTask("SubTaskForEpic1", "SubTaskForEpic1 descr",
                TaskStatus.NEW,1, LocalDateTime.now(), Duration.ofMinutes(5));

        String taskJson = gson.toJson(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        ArrayList<SubTask> subTaskList = manager.getEpic(1).getSubTasksList();
        SubTask subTaskForEpic = manager.getSubTask(2);
        assertEquals(1, subTaskList.size(), "Некорректное количество задач");
        assertEquals("SubTaskForEpic1", subTaskForEpic.getTaskName(), "Некорректное имя задачи");
    }
    @Test
    @DisplayName("Ответ от сервера не равен 200, вернет 404")
    public void testErrorNotExistsSubTask() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/1");

        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(),"Должно быть сообещние что такой подзадачи нет");
    }
}
