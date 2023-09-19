import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class Server {
    public static void start(int port) throws IOException {
        //http server kurma
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        //route kaydetme
        Map<String, BaseRouteHandler> routes = new HashMap<>();
        routes.put("/login", new LoginRouteHandler());
        routes.put("/register", new RegisterRouteHandler());
        routes.put("/accounts", new AccountsRouteHandler());

        server.createContext("/", new Handler(routes));
        server.start();
        System.out.println("HTTP sunucusu " + port + " portunda çalışıyor...");
    }

    static class Handler extends BaseRouteHandler implements HttpHandler {
        private final Map<String, BaseRouteHandler> routes;

        public Handler(Map<String, BaseRouteHandler> routes) {
            this.routes = routes;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestPath = exchange.getRequestURI().getPath();

            BaseRouteHandler handler = routes.get(requestPath);
            if (handler != null) {
                handler.handle(exchange);
            } else {
                respond(exchange, "Böyle bir istek bulunamadı!");
            }
        }

        @Override
        protected int get(HttpExchange exchange) throws IOException {
            return 0;
        }

        @Override
        protected int post(HttpExchange exchange) throws IOException {
            return 0;
        }

        @Override
        protected int patch(HttpExchange exchange) throws IOException {
            return 0;
        }

        @Override
        protected int delete(HttpExchange exchange) throws IOException {
            return 0;
        }
    }
}