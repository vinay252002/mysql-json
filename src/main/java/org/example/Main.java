package org.example;

import java.io.FileReader;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/example";
        String username = "root";
        String password = "20020125";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
            JSONObject data = fetchData();
            createTable(connection);
            beginTables(connection, data);
            normal.categoryTable(connection);
            normal.apiTable(connection);
            deleteTable(connection);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void deleteTable(Connection connection) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("drop table entries");
        stmt.executeUpdate();
    }

    public static void createTable(Connection connection) throws SQLException {
        String sql = "CREATE TABLE entries (\n" +
                "    api VARCHAR(255),\n" +
                "    description TEXT,\n" +
                "    Auth VARCHAR(50),\n" +
                "    https BOOLEAN,\n" +
                "    cors VARCHAR(50),\n" +
                "    link VARCHAR(255),\n" +
                "    category VARCHAR(255)\n" +
                ");";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.executeUpdate();
        
    }

    public static JSONObject fetchData() {
        JSONObject json = null;
        try {
            JSONParser parser = new JSONParser();
            json = (JSONObject) parser.parse(new FileReader("C:/Users/boina/Desktop/data.json"));
        } catch (Exception e) {
            System.out.println(e);
        }
        return json;

    }

    public static void beginTables(Connection connection, JSONObject data) {
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonData = (JSONObject) jsonParser.parse(String.valueOf(data));
            JSONArray jsonArray = (JSONArray) jsonData.get("entries");
            beginTableCreation(connection, jsonArray);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void beginTableCreation(Connection connection, JSONArray jsonArray) {
        try {
            //createTable(connection, jsonArray);
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO entries VALUES(? ,? ,? ,? ,? ,? ,? )");
            for (Object object : jsonArray) {
                JSONObject record = (JSONObject) object;
                String api = (String) record.get("API");
                String description = (String) record.get("Description");
                String Auth = (String) record.get("Auth");
                boolean https = (boolean) record.get("HTTPS");
                String cors = (String) record.get("Cors");
                String link = (String) record.get("Link");
                String category = (String) record.get("Category");
                stmt.setString(1, api);
                stmt.setString(2, description);
                stmt.setString(3, Auth);
                stmt.setBoolean(4, https);
                stmt.setString(5, cors);
                stmt.setString(6, link);
                stmt.setString(7, category);
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}