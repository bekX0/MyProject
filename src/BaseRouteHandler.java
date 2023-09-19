import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.io.OutputStream;
import java.sql.SQLException;

public abstract class BaseRouteHandler implements HttpHandler {

    protected void categorizeRequest(HttpExchange exchange) throws IOException, SQLException {
        String requestMethod = exchange.getRequestMethod();
        if(requestMethod.equals("GET")){
            this.get(exchange);
        }else if(requestMethod.equals("POST")){
            this.post(exchange);
        }else if(requestMethod.equals("PATCH")){
            this.patch(exchange);
        }else if(requestMethod.equals("DELETE")){
            this.delete(exchange);
        }
    }
    protected abstract int get(HttpExchange exchange) throws IOException, SQLException;
    protected abstract int post(HttpExchange exchange) throws IOException, SQLException;
    protected abstract int patch(HttpExchange exchange) throws IOException, SQLException;
    protected abstract int delete(HttpExchange exchange) throws IOException, SQLException;



    protected void respond(HttpExchange exchange, String response) throws IOException {
        exchange.sendResponseHeaders(404, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    protected void respond(HttpExchange exchange, JSONObject response) throws IOException {
        exchange.sendResponseHeaders(404, response.toJSONString().getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.toJSONString().getBytes());
        os.close();
    }
    protected void respond(HttpExchange exchange, JSONArray response) throws IOException {
        exchange.sendResponseHeaders(404, response.toJSONString().getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.toJSONString().getBytes());
        os.close();
    }

    protected JSONObject getBodyJson(HttpExchange exchange) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8));
        StringBuilder requestBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }

        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(requestBody.toString());

            return jsonObject;
        } catch (ParseException e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(400, 0); // Hatalı istek durumu

        }

        return new JSONObject();
    }

}
