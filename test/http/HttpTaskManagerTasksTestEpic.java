package http;
import com.google.gson.Gson;

import com.google.gson.GsonBuilder;
import model.Epic;
import model.SubTask;
import model.Task;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTasksTestEpic {
    TaskManager manager = Manager.getDefault();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = BaseHttpHandler.getGson();
    public HttpTaskManagerTasksTestEpic() throws IOException {
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
    @DisplayName("Должен создать эпик")
    public void testAddEpic() throws IOException, InterruptedException {
        Epic  epic = new Epic("Epic1", "EpicDescr1",TaskStatus.NEW,LocalDateTime.now(),Duration.ofMinutes(0));

        String epicJson =  gson.toJson(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Epic> epicList = manager.getAllEpics();

        assertEquals(1, epicList.size(), "Некорректное количество задач");
        assertEquals("EpicDescr1", epicList.get(0).getTaskDescription(), "Некорректное описание эпика");
    }
    @Test
    @DisplayName("Должен обновить эпик")
    public void testUpdateEpic() throws IOException, InterruptedException {
        manager.createEpic(new Epic("Epic1", "EpicDescr1"));
        Epic  epic = new Epic(1,"Epic1", "EpicDescrUpdate",TaskStatus.NEW,LocalDateTime.now(),Duration.ofMinutes(5));
        String epicJson =  gson.toJson(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Epic epicUpd = manager.getEpic(1);
        assertEquals("EpicDescrUpdate", epicUpd.getTaskDescription(), "Задача не обновлена");
    }
}
