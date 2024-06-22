package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.TaskManager;

import java.io.IOException;

public class PrioritizedHandler  extends BaseHttpHandler implements HttpHandler  {
    public PrioritizedHandler(TaskManager manager) {
        this.manager = manager;
    }
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        try (httpExchange) {
            try {
                String method = httpExchange.getRequestMethod();
                switch (method) {

                    case "GET":
                        handleGet(httpExchange);
                        break;

                }
            } catch (Exception exception) {
                errorHandler.handle(httpExchange, exception);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    protected void handleGet(HttpExchange httpExchange) throws IOException {
        sendText(httpExchange, gson.toJson(manager.getPrioritizedTasks()));
    }
}
