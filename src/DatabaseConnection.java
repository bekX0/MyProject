import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.Properties;
import java.sql.PreparedStatement;

public class DatabaseConnection {
    private Connection connection;

    //default constructer
    public DatabaseConnection(){
        String url = "jdbc:postgresql://localhost/mywork";
        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "78berkin");
        props.setProperty("ssl", "false");

        try {
            this.connection = DriverManager.getConnection(url, props);
        }catch (Exception e){
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    public Connection getConnection(){
        return this.connection;
    }

    //overloaded constructer
    public DatabaseConnection(String user, String password){
        String url = "jdbc:postgresql://localhost/mywork";
        Properties props = new Properties();
        props.setProperty("user", user);
        props.setProperty("password", password);
        props.setProperty("ssl", "false");

        try {
            this.connection = DriverManager.getConnection(url, props);
        }catch (Exception e){
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    // close database method
    public void close() throws SQLException {
        connection.close();
    }

    // select method returns JSONObject
//    public JSONArray select(String query) throws SQLException {
//        Statement statement = connection.createStatement();
//        ResultSet resultSet = statement.executeQuery(query);
//
//        ResultSetMetaData metaData = resultSet.getMetaData();
//        int columnCount = metaData.getColumnCount();
//
//        JSONArray dataObject = new JSONArray();
//
//        while (resultSet.next()){
//            JSONObject object = new JSONObject();
//            for(int i = 1; i < columnCount; i++){
//                object.put(metaData.getColumnName(i), resultSet.getObject(i));
//            }
//            dataObject.add(object);
//        }
//        System.out.println(dataObject.toJSONString());
//        resultSet.close();
//        statement.close();
//
//        return dataObject;
//    }

    public JSONArray displayResult(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        JSONArray resultArray = new JSONArray();
        while (resultSet.next()){
            JSONObject obj = new JSONObject();
            for (int i = 1; i <= columnCount; i++){
                String columnName = metaData.getColumnName(i);
                obj.put(columnName, resultSet.getString(i));
            }
            resultArray.add(obj);
        }
        resultSet.close();
        return resultArray;
    }
}
