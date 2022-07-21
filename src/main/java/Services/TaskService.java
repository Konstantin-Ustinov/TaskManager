package Services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import ConsoleUI.Main;
import ConsoleUI.UI.BaseUI;
import ConsoleUI.UI.TaskUI;
import DB.TaskDB;
import Entities.Task;
import Entities.User;
import Utils.Util;
import Utils.Util.TaskListSortColumns;
import Utils.Util.TaskStatuses;

public class TaskService {

    public static boolean add(Task newTask) {
        return TaskDB.add(newTask);
    }

    public static boolean delete(int id) {
        return TaskDB.delete(id);
    }

    public static ArrayList<Task> getAll(TaskListSortColumns sort) {
        ArrayList<Task> tasks = new ArrayList<>(); // Создаем списочный массив объектов Task
        ResultSet rs = TaskDB.getAll(Main.sort); // создаем объет ResultSet и инициализируем его ответом из метода        

            while (true) {
                try {
                    if (!rs.next()) {
                        break;
                    }
                 // Пока есть записи создаем объеты в цикле
                    User performer = UserService.getUser(rs.getInt("performer_id"));
                    tasks.add(new Task(rs.getInt("id"), rs.getString("name"),
                            rs.getString("status"), LocalDate.parse(rs.getDate("deadline").toString()), performer));
                } catch (SQLException e) {
                    BaseUI.showMessage("При выбранной сортировке задач нет.");
                 }
            }
        return tasks;
    }

    public static Task getOneFull(int id) {        
        User autor, performer = null;

        // создаем объет ResultSet и инициализируем его ответом из метода
        ResultSet rs = TaskDB.getOneFull(id);
        Task task = null; 

        try {
            if (!rs.next()) {
                TaskUI.showMessage("Не удалось загрузить задачу. Убедитесь в правильности введенного ID.");
                TaskUI.showOneFull(-1);
            } else {
                autor = new User(rs.getInt("autorId"), rs.getString("autorNickname"));
                performer = new User(rs.getInt("performerId"), rs.getString("performerNickname"));
                task = new Task(rs.getInt("taskId"), rs.getString("name"),
                        rs.getString("body"), rs.getString("status"),
                        LocalDateTime.parse(rs.getTimestamp("created").toString(), Util.formatterToLocalDateTime),
                        LocalDate.parse(rs.getDate("deadline").toString()), autor, performer);             
            }
        } catch (SQLException e) {
            System.out.println("Объект task не создан.");
        }
        
        return task;
    }

    public static boolean setStatus(Task task, TaskStatuses taskStatutes) {
        return TaskDB.setStatus(task, taskStatutes);
    }

    public static boolean update(Task changedTask) {
        return TaskDB.update(changedTask);
    }

}
