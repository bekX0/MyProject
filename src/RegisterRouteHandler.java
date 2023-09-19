import com.sun.net.httpserver.HttpExchange;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterRouteHandler extends BaseRouteHandler{

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            categorizeRequest(exchange);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected int get(HttpExchange exchange) throws IOException {
        return 0;
    }

    @Override
    protected int post(HttpExchange exchange) throws IOException, SQLException {
        JSONObject body = getBodyJson(exchange);

        String email = body.get("email").toString();
        String password = body.get("password").toString();
        String rePassword = body.get("re_password").toString();
        String name = body.get("name").toString();

        //respond object
        JSONObject respond = new JSONObject();

        // password check
        if(password.equals(rePassword)){
            DatabaseConnection db = new DatabaseConnection();

            //query settings
            PreparedStatement preparedStatement = db.getConnection().prepareStatement("INSERT INTO accounts (email, password, role, name) VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, "Basic");
            preparedStatement.setString(4, name);

            int affectedRowsCount = preparedStatement.executeUpdate();

            preparedStatement.close();
            db.close();

            if(affectedRowsCount < 1){
                respond.put("code", 404);
                respond.put("process", "Failed");
            }else {
                respond.put("code", 200);
                respond.put("process", "Successful");
            }

        }else {
            respond.put("code", 404);
            respond.put("process", "Failed");
            respond(exchange, respond);
            //? respond objesi BaseHandler içinde protected olarak tutulabilir (sürekli açılıyor)
        }
        respond(exchange, respond);
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
