package http;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;
import model.TaskStatus;
import service.TaskManager;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

class TaskHandler extends BaseHttpHandler implements HttpHandler {
    public TaskHandler(TaskManager manager) {
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
        Task task;
        if (!taskIdOpt.isEmpty()) {
            taskId = taskIdOpt.get();
        }
        String[] valueArrya = parseTask(httpExchange.getRequestBody(), taskId);


        if (taskId != 0) {
            LocalDateTime startTime = LocalDateTime.parse(valueArrya[5], TaskManager.getDateTimeFormatter());
            Duration duration = Duration.ofMinutes(Long.parseLong(valueArrya[4]));
            task = new Task(taskId, valueArrya[1], valueArrya[2], TaskStatus.valueOf(valueArrya[3]), startTime, duration);
            manager.updateTask(task);
        } else {
            LocalDateTime startTime = LocalDateTime.parse(valueArrya[4], TaskManager.getDateTimeFormatter());
            Duration duration = Duration.ofMinutes(Long.parseLong(valueArrya[3]));
            task = manager.createTask(new Task(valueArrya[0], valueArrya[1], TaskStatus.valueOf(valueArrya[2]), startTime, duration));
        }
        sendText(httpExchange, gson.toJson(task));
    }

    protected void delete(Integer taskId) {
        manager.deleteTask(taskId);
    }

    protected void handleGet(HttpExchange httpExchange) throws IOException {
        Optional<Integer> taskIdOpt = getTaskId(httpExchange);
        if (taskIdOpt.isEmpty()) {
            sendText(httpExchange, gson.toJson(manager.getAllTasks()));
        } else {
            sendText(httpExchange, gson.toJson(manager.getTask(taskIdOpt.get())));
        }
    }
}
