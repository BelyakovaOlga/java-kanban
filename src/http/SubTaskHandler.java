package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exception.NotFoundException;
import model.Task;
import model.TaskStatus;
import model.SubTask;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
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
        Integer  epicId = Integer.parseInt(valueArrya[0]);
        LocalDateTime startTime = LocalDateTime.parse(valueArrya[5], TaskManager.getDateTimeFormatter());
        Duration duration = Duration.ofMinutes(Long.parseLong(valueArrya[4]));

        if (taskId != 0) {
            subTask = new SubTask(taskId, valueArrya[1], valueArrya[2], TaskStatus.valueOf(valueArrya[3]),epicId, startTime, duration);
            manager.updateSubTask(subTask);
        } else {
            subTask = manager.createSubTask(new SubTask(valueArrya[1], valueArrya[2], TaskStatus.valueOf(valueArrya[3]), epicId,startTime, duration));
        }
        sendText(httpExchange, gson.toJson(subTask));
    }
    /*protected void handlePost(HttpExchange httpExchange) throws IOException {
        Optional<Integer> taskIdOpt = getTaskId(httpExchange);
        Optional<SubTask> taskOpt = Optional.empty();

        if (taskIdOpt.isEmpty()) {
            taskOpt = parseTask(httpExchange.getRequestBody(), 0);
            if (taskOpt.isEmpty()) {
                throw new NotFoundException("Задача не создана");
            }
            sendText(httpExchange, gson.toJson(manager.createTask(taskOpt.get())));
        } else {
            taskOpt = parseTask(httpExchange.getRequestBody(), taskIdOpt.get());
            if (taskOpt.isEmpty()) {
                throw new NotFoundException("Задача не Обновлена");
            }
            manager.updateTask(taskOpt.get());
            sendText(httpExchange, gson.toJson(taskOpt.get()));
        }
    }*/
   /* private Optional<SubTask> parseTask(InputStream bodyInputStream, Integer taskId) throws IOException {
        String[] valueArrya = new String[6];
        String body = new String(bodyInputStream.readAllBytes(), StandardCharsets.UTF_8).replace("{", "").replace("}", "");
        String[] stringArrayBody = body.split(",");

        for (int i = 0; i < stringArrayBody.length; i++) {
            String curSection = stringArrayBody[i];
            String[] sectionArray = curSection.split(":");
            valueArrya[i] = sectionArray[1].replace("\"", "").replace("\n", "").trim();
        }
        LocalDateTime startTime = LocalDateTime.parse(valueArrya[5], TaskManager.getDateTimeFormatter());
        Duration duration = Duration.ofMinutes(Long.parseLong(valueArrya[4]));
        Integer  epicId = Integer.parseInt(valueArrya[0]);
        if (taskId != 0) {
            return Optional.of(new SubTask(taskId, valueArrya[1], valueArrya[2], TaskStatus.valueOf(valueArrya[3]),epicId, startTime, duration));
        }
        return Optional.of(new SubTask(valueArrya[1], valueArrya[2], TaskStatus.valueOf(valueArrya[3]), epicId,startTime, duration));
    }*/
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
