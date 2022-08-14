package DB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import Entities.User;
import static DB.ServiceDB.*;

public class UserDB extends BaseDB {
    public static boolean add(User newUser) {
        DBConnect();

        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        
        PreparedStatement stmt;
            try { // пробуем отправить SQL запрос                
                stmt = connection.prepareStatement("INSERT INTO public.\"users\"(\"nickname\", \"created_at\", \"updated_at\")" +
                        "VALUES(?, ?, ?)"); // переменной присваиваем результат метода с запросом
                stmt.setString(1, newUser.getNickname()); // подставляем значение переменной вместо первого знака вопроса
                stmt.setTimestamp(2, now);
                stmt.setTimestamp(3, now);
                stmt.execute(); // После того как весь запрос составлен, отправляем запрос в бвзу
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Не удалось добавить пользователя. Попробуйте еще раз.");
            }

        DBDisconnect();
        return false;
    }

    public static boolean delete(String nickname) {
        if (nickname.equals("")) {
            return false;
        }

        boolean answer;
        int delAnswer;
        DBConnect();

        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement("DELETE FROM public.\"users\" WHERE \"nickname\"= ?");
            stmt.setString(1, nickname);
            delAnswer = stmt.executeUpdate();
            answer = (delAnswer != 0)?true:false;
        } catch (SQLException e) {
            answer = false;
        }
        DBDisconnect();
        return answer;
    }

    public static boolean update(User user) {
        DBConnect();

        PreparedStatement stmt;
        try {
            connection.setAutoCommit(false);
            stmt = connection.prepareStatement("UPDATE public.\"users\" SET \"nickname\" = ? WHERE \"id\" = ?");
            stmt.setString(1, user.getNickname());
            stmt.setInt(2, user.getId());            
            stmt.executeUpdate();
            setUpdatedAt(user);
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            return false;
        }
        DBDisconnect();

        return true;
    }

    public static ResultSet getOneFull(String nickname) {      
        DBConnect();

        String sql = "SELECT \"id\", \"nickname\" FROM public.\"users\" WHERE \"nickname\"= '" + nickname + "'";
        Statement stmt;
        ResultSet rs = null;

        try {
             stmt = connection.createStatement();
             rs = stmt.executeQuery(sql);
         } catch (SQLException e) {
             System.out.println("Не удалось загрузить пользователя.");
         }

        DBDisconnect();
        return rs;
    }
    // Перегрузка метода
    public static ResultSet getOneFull(int id) {      
        DBConnect();

        String sql = "SELECT \"id\", \"nickname\" FROM public.\"users\" WHERE \"id\"=" + id;
        Statement stmt;
        ResultSet rs = null;

        try {
             stmt = connection.createStatement();
             rs = stmt.executeQuery(sql);
         } catch (SQLException e) {
             System.out.println("Не удалось загрузить пользователя.");
         }

        DBDisconnect();
        return rs;
    }

    public static ResultSet getAll(String field, String order) {
        
        if (!order.equals("ASC") && !order.equals("DESC")) {
            order = "ASC";
        }

        String sql = "SELECT id, nickname FROM public.\"users\" ORDER BY " + field + " " + order;
        
        DBConnect();
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (Exception e) {
            System.out.println("Не удалось загрузить данные из базы данных.");
        }

        DBDisconnect();
        
        return rs;
    }

}


