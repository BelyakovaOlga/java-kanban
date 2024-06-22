package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Epic;
import model.TaskStatus;
import service.TaskManager;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    public EpicHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        try (httpExchange) {
            try {
                String method = httpExchange.getRequestMethod();
                switch (method) {
                    case "POST":
                        handlePost(httpExchange);
                        break;
                    case "GET":
                        handleGet(httpExchange);
                        break;
                    case "DELETE":
                        handleDelete(httpExchange);
                        break;
                }
            } catch (Exception exception) {
                errorHandler.handle(httpExchange, exception);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    protected void handlePost(HttpExchange httpExchange) throws IOException {
        Optional<Integer> taskIdOpt = getTaskId(httpExchange);
        Integer taskId = 0;
        Epic epic;
        if (!taskIdOpt.isEmpty()) {
            taskId = taskIdOpt.get();
        }
        String[] valueArrya = parseTask(httpExchange.getRequestBody(), taskId);

        if (taskId != 0) {
            LocalDateTime startTime = LocalDateTime.parse(valueArrya[5], TaskManager.getDateTimeFormatter());
            Duration duration = Duration.ofMinutes(Long.parseLong(valueArrya[4]));
            epic = new Epic(taskId, valueArrya[1], valueArrya[2], TaskStatus.valueOf(valueArrya[3]), startTime, duration);

            manager.updateEpic(epic);
        } else {
            LocalDateTime startTime = LocalDateTime.parse(valueArrya[4], TaskManager.getDateTimeFormatter());
            Duration duration = Duration.ofMinutes(Long.parseLong(valueArrya[3]));
            epic = manager.createEpic(new Epic(valueArrya[0], valueArrya[1], TaskStatus.valueOf(valueArrya[2]), startTime, duration));
        }
        sendText(httpExchange, gson.toJson(epic));
    }

    protected void delete(Integer taskId) {
        manager.deleteEpic(taskId);
    }

    protected void handleGet(HttpExchange httpExchange) throws IOException {
        Optional<Integer> taskIdOpt = getTaskId(httpExchange);
        if (taskIdOpt.isEmpty()) {
            sendText(httpExchange, gson.toJson(manager.getAllEpics()));
        } else {
            sendText(httpExchange, gson.toJson(manager.getEpic(taskIdOpt.get())));
        }
    }
}
