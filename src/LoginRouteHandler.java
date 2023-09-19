import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginRouteHandler extends BaseRouteHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            categorizeRequest(exchange);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected int get(HttpExchange exchange) throws IOException, SQLException {
        return 0;
    }

    @Override
    protected int post(HttpExchange exchange) throws IOException, SQLException {
        JSONObject body = getBodyJson(exchange);

        String email = body.get("email").toString();
        String password = body.get("password").toString();

        DatabaseConnection db = new DatabaseConnection();

        try{
            PreparedStatement preparedStatement = db.getConnection().prepareStatement("SELECT password FROM accounts WHERE email=?");
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();
            db.close();

            JSONArray result = db.displayResult(resultSet);
            String dataPassword = (String) ((JSONObject) result.get(0)).get("password");

            JSONObject respond = new JSONObject();

            if (password.equals(dataPassword)){
                respond.put("code", 200);
                respond.put("process", "Successful");
                respond(exchange, respond);
            }else {
                respond.put("code", 404);
                respond.put("process", "Failed");
                respond(exchange, respond);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        return 0;
    }

    @Override
    protected int patch(HttpExchange exchange) throws IOException, SQLException {
        return 0;
    }

    @Override
    protected int delete(HttpExchange exchange) throws IOException, SQLException {
        return 0;
    }
}
