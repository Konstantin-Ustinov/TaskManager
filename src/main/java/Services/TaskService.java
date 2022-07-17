package Services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import DB.TaskDB;
import DB.UserDB;
import Entities.Task;
import Entities.User;
import Utils.Util;
import ConsoleUi.TaskUI;

public class TaskService {

    public static Task getOneFull(int id) {        
        User creator, executor = null;

        // создаем объет ResultSet и инициализируем его ответом из метода
        ResultSet rs = TaskDB.getOneFull(id);
        Task task = null; 

        try {
            if (!rs.next()) {
                TaskUI.showMessage("Не удалось загрузить задачу. Убедитесь в правильности введенного ID.");
                TaskUI.showOneFull(-1);
            } else {
                creator = getUser(rs.getInt("creator_id"));
                executor = getUser(rs.getInt("executor_id"));
                task = new Task(rs.getInt("id"), rs.getString("name"),
                        rs.getString("body"), rs.getString("status"),
                        LocalDateTime.parse(rs.getTimestamp("created_at").toString(), Util.formatterToLocalDateTime),
                        LocalDate.parse(rs.getDate("deadline").toString()), creator, executor);             
            }
        } catch (SQLException e) {
            System.out.println("Объект task не создан.");
        }
        
        return task;
    }

    public static User getUser(int id) {
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
}
