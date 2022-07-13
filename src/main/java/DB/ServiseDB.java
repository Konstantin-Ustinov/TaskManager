package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ServiseDB {
    private final static String DB_URL = "jdbc:postgresql://localhost:5432/TaskManager";
    private final static String USER = "postgres";
    private final static String PASSWORD = "0123";
    public static Connection connection = null;

    public static void DBConnect() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Не найден драйвер JDBC");
        }

        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Нет подключения к БД по исключению");
        }

        if (connection == null) {
            System.out.println("Не подключено к БД");
        }
    }

    public static void DBDisconnect() {
        if (connection == null) {
            System.out.println("БД не была подключена");
        } else {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("БД не была отключена в исключении");
            }
        }
    }
}
