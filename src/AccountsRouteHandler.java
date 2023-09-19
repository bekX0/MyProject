import com.sun.net.httpserver.HttpExchange;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountsRouteHandler extends BaseRouteHandler{
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            categorizeRequest(exchange);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected int get(HttpExchange exchange) throws IOException, SQLException { // for get all account data
        DatabaseConnection databaseConnection = new DatabaseConnection();

        Statement statement = databaseConnection.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM accounts");

        JSONArray result = databaseConnection.displayResult(resultSet);
        respond(exchange, result);
        return 0;
    }
    @Override
    protected int post(HttpExchange exchange) throws IOException, SQLException {
        return 0;
    }

    @Override
    protected int patch(HttpExchange exchange) throws IOException, SQLException {
        JSONObject body = getBodyJson(exchange);

        int id = Integer.parseInt(body.get("id").toString());
        String email = body.get("email").toString();
        String password = body.get("password").toString();
        String name = body.get("name").toString();
        String phone = body.get("phone").toString();
        String role = body.get("role").toString();
        String job = body.get("job").toString();

        DatabaseConnection databaseConnection = new DatabaseConnection();

        PreparedStatement getDataStatement = databaseConnection.getConnection().prepareStatement("SELECT * FROM accounts WHERE id=?");
        getDataStatement.setInt(1, id);

        ResultSet resultSet = getDataStatement.executeQuery();
        JSONArray result = databaseConnection.displayResult(resultSet);
        if(result.size() < 1){
                JSONObject respond = new JSONObject();
                respond.put("code", 404);
                respond.put("message", "ID not found!");
                respond(exchange, respond);
                return 0;
        }
        JSONObject resultObject = (JSONObject) result.get(0);
        System.out.println(resultObject.toJSONString());
        //getDataStatement.close();
        String _email = !email.equals("") ? email : resultObject.get("email").toString();
        String _name = !name.equals("") ? name : resultObject.get("name").toString();
        String _password = !password.equals("") ? password : resultObject.get("password").toString();
        String _phone = !phone.equals("") ? phone : resultObject.get("phone") == null ? "null" : resultObject.get("phone").toString();
        String _role = !role.equals("") ? role : resultObject.get("role").toString();
        String _job = !job.equals("") ? job : resultObject.get("job") == null ? "null" : resultObject.get("job").toString();

        PreparedStatement updateDataStatement = databaseConnection.getConnection().prepareStatement("UPDATE accounts SET email=?, password=?, name=?, phone=?, role=?, job=? WHERE id=?");
        updateDataStatement.setString(1, _email);
        updateDataStatement.setString(2, _password);
        updateDataStatement.setString(3, _name);
        updateDataStatement.setString(4, _phone);
        updateDataStatement.setString(5, _role);
        updateDataStatement.setString(6, _job);
        updateDataStatement.setInt(7, id);

        int status = updateDataStatement.executeUpdate();
        JSONObject respond = new JSONObject();
        if(status == 1){
            respond.put("code", 200);
            respond.put("message", "Operation Successful!");
        }else{
            respond.put("code", 404);
            respond.put("message", "There was an error!");
        }
        respond(exchange, respond);
        return 0;
    }
    // yukardaki string declarasyonunda hata var
    @Override
    protected int delete(HttpExchange exchange) throws IOException, SQLException {
        JSONObject body = getBodyJson(exchange);
        int id = Integer.parseInt(body.get("id").toString());
        DatabaseConnection databaseConnection = new DatabaseConnection();
        PreparedStatement preparedStatement = databaseConnection.getConnection().prepareStatement("DELETE FROM accounts WHERE id=?");
        preparedStatement.setInt(1, id);

        int stat = preparedStatement.executeUpdate();

        preparedStatement.close();
        databaseConnection.close();
        // responding
        JSONObject respond = new JSONObject();
        if (stat == 1){
            respond.put("code", 200);
            respond.put("message", "Successful");
        }else{
            respond.put("code", 404);
            respond.put("message", "ID not found!");
        }
        respond(exchange, respond);
        return 0;
    }
}