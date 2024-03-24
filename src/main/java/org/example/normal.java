package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class normal {

    public static void categoryTable(Connection connection) throws SQLException {
        String sql = "CREATE TABLE CATEGORY(catID int auto_increment PRIMARY KEY, category varchar(255))";
        PreparedStatement stmt =  connection.prepareStatement(sql);
        stmt.executeUpdate();
        insertCategoryTable(connection);
    }

    private static void insertCategoryTable(Connection connection) throws SQLException {
        String sql = "INSERT INTO category (category)SELECT DISTINCT category FROM entries;";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.executeUpdate();
    }

    public  static void apiTable(Connection connection) throws SQLException {
        String sql = "CREATE TABLE API (\n" +
                "    APIID INT AUTO_INCREMENT PRIMARY KEY,\n" +
                "    api VARCHAR(255),\n" +
                "    description TEXT,\n" +
                "    Auth VARCHAR(50),\n" +
                "    https BOOLEAN,\n" +
                "    cors VARCHAR(50),\n" +
                "    link VARCHAR(255),\n" +
                "    catID INT,\n" +
                "    FOREIGN KEY (catID) REFERENCES Category(catID)\n" +
                ");";
        PreparedStatement stmt =  connection.prepareStatement(sql);
        stmt.executeUpdate();
        insertApiTable(connection);
    }

    private static void insertApiTable(Connection connection) throws SQLException {
        String sql = "INSERT INTO api ( api, description, Auth, https, cors, link, catID) \n " +
                " SELECT e.api, e.description, e.Auth, e.https, \n " +
                " e.cors, e.link, c.catID \n" +
                " FROM entries e \n" +
                "JOIN category c on e.category = c.category";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.executeUpdate();
    }
}
