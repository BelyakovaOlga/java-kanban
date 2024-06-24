package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.TaskStatus;
import model.SubTask;
import service.TaskManager;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class SubTaskHandler extends BaseHttpHandler implements HttpHandler {
    public SubTaskHandler(TaskManager manager) {
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
        SubTask subTask;
        if (!taskIdOpt.isEmpty()) {
            taskId = taskIdOpt.get();
        }
        String[] valueArrya = parseTask(httpExchange.getRequestBody(), taskId);
        Integer epicId = Integer.parseInt(valueArrya[0]);
        LocalDateTime startTime = LocalDateTime.parse(valueArrya[5], TaskManager.getDateTimeFormatter());
        Duration duration = Duration.ofMinutes(Long.parseLong(valueArrya[4]));

        if (taskId != 0) {
            subTask = new SubTask(taskId, valueArrya[1], valueArrya[2], TaskStatus.valueOf(valueArrya[3]), epicId, startTime, duration);
            manager.updateSubTask(subTask);
        } else {
            subTask = manager.createSubTask(new SubTask(valueArrya[1], valueArrya[2], TaskStatus.valueOf(valueArrya[3]), epicId, startTime, duration));
        }
        sendText(httpExchange, gson.toJson(subTask));
    }

    protected void delete(Integer taskId) {
        manager.deleteSubTask(taskId);
    }

    protected void handleGet(HttpExchange httpExchange) throws IOException {
        Optional<Integer> taskIdOpt = getTaskId(httpExchange);
        if (taskIdOpt.isEmpty()) {
            sendText(httpExchange, gson.toJson(manager.getAllSubTasks()));
        } else {
            sendText(httpExchange, gson.toJson(manager.getSubTask(taskIdOpt.get())));
        }
    }
}
