package http;

import com.google.gson.*;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HtppTaskManagerTasksTestHistory {
    TaskManager manager = Manager.getDefault();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = BaseHttpHandler.getGson();
    public HtppTaskManagerTasksTestHistory() throws IOException {
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
    @DisplayName("Должен быть получен список Истории задач")
    public void testGetHistory() throws IOException, InterruptedException {
        manager.createTask( new Task("Test http", "Testing task http",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(5)));
        manager.getTask(1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        List<Task> usersList = gson.fromJson(jsonArray.toString(), new HistoryListTypeToken().getType());

        assertEquals(1, usersList.size(),"Кол-во задач в истории неверно");
        assertEquals("Test http", usersList.get(0).getTaskName(), "Некорректное имя задачи");
    }
}
