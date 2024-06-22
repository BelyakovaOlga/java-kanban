package http;

import com.sun.net.httpserver.HttpExchange;
import exception.NotFoundException;
import exception.NotParmException;
import exception.ValidationException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ErrorHandler {
    public void handle(HttpExchange httpExchange, Exception exception) {
        try {
            if (exception instanceof NotFoundException) {
                writeResponse(httpExchange, exception.getMessage(), 404);
            } else if (exception instanceof ValidationException) {
                writeResponse(httpExchange, exception.getMessage(), 406);
            } else if (exception instanceof NotParmException) {
                writeResponse(httpExchange, exception.getMessage(), 400);
            } else {
                exception.printStackTrace();
                writeResponse(httpExchange, exception.getMessage(), 500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeResponse(HttpExchange httpExchange, String responseString, int responseCode) throws IOException {
        try (OutputStream os = httpExchange.getResponseBody()) {
            httpExchange.sendResponseHeaders(responseCode, 0);
            os.write(responseString.getBytes(StandardCharsets.UTF_8));
        }
        httpExchange.close();
    }
}
