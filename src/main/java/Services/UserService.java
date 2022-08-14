package Services;

import java.sql.ResultSet;
import java.util.ArrayList;

import DB.UserDB;
import Entities.User;

public class UserService {
    
    public static boolean add(User newUser) {
        return UserDB.add(newUser);
    }

    public static boolean delete(String nickname) {
        return UserDB.delete(nickname);
    }

    public static boolean update(User user) {
        return UserDB.update(user);
    }

    public static User getOneFull(int id) {
        User user = null;
            try {
                ResultSet rs = UserDB.getOneFull(id);
                if (rs.next()) {
                   user = new User(rs.getInt("id"), rs.getString("nickname")); 
                } 
            } catch (Exception e) {
                System.out.println("Объект User не создан.");
            }
    
            return user;
        }
        
    public static User getOneFull(String nickname) {
        User user = null;
            try {
                ResultSet rs = UserDB.getOneFull(nickname);
                if (rs.next()) {
                   user = new User(rs.getInt("id"), rs.getString("nickname")); 
                } 
            } catch (Exception e) {
                System.out.println("Объект User не создан.");
            }
    
            return user;
        }

    public static ArrayList<User> getAll() {
        String field = "id";
        String order = "ASC";

        return getAll(field, order);
        
    }

    public static ArrayList<User> getAll(String field, String order) {

        ArrayList<User> users = new ArrayList<>();

        try {
            ResultSet rs = UserDB.getAll(field, order);
            if (!rs.next()) {
                System.out.println("Пришел пустой ответ из базы.");
            }
            while (rs.next()) {
                users.add(new User(rs.getInt("id"), rs.getString("nickname")));
            }
        } catch (Exception e) {
            System.out.println("Ошибка метода к БД в UserUI");
        }
        return users;
    }
}
