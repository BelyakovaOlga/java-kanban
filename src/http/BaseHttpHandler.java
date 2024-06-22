package http;
import adapter.DurationAdapter;
import adapter.LocalDateTimeAdapter;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import com.google.gson.Gson;
import service.TaskManager;

public class BaseHttpHandler {
    ErrorHandler  errorHandler = new ErrorHandler();
    Gson gson = getGson();
    TaskManager manager;
    static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());

        return gsonBuilder.create();
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }
    protected void handleDelete(HttpExchange httpExchange) throws IOException {
        Optional<Integer> taskIdOpt = getTaskId(httpExchange);
        Integer taskId = taskIdOpt.get();
        delete(taskId);
        sendText(httpExchange, gson.toJson(null));
    }
    protected void delete(Integer taskId) {}
    protected Optional<Integer> getTaskId(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        Integer taskId;

        try {
            if (pathParts.length == 3) {
                taskId = Integer.parseInt(pathParts[2]);
                return Optional.of(taskId);
            }
            return Optional.empty();
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    protected String[] parseTask(InputStream bodyInputStream, Integer taskId) throws IOException {
        String[] valueArrya = new String[7];
        String body = new String(bodyInputStream.readAllBytes(), StandardCharsets.UTF_8).replace("{", "").replace("}", "");
        String[] stringArrayBody = body.split(",");

        for (int i = 0; i < stringArrayBody.length; i++) {
            String curSection = stringArrayBody[i];
            String[] sectionArray = curSection.split(":");
            valueArrya[i] = sectionArray[1].replace("\"", "").replace("\n", "").trim();
        }
        return valueArrya;
    }
}
