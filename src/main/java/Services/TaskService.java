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

    public static ArrayList<Task> getAllBySort(TaskListSortColumns sort) {
        ArrayList<Task> tasks = new ArrayList<>(); // Создаем списочный массив объектов Task
        ResultSet rs = TaskDB.getAllBySort(Main.sort); // создаем объет ResultSet и инициализируем его ответом из метода        

            while (true) {
                try {
                    if (!rs.next()) {
                        break;
                    }
                 // Пока есть записи создаем объеты в цикле
                    User performer = new User(rs.getString("performer_nickname"));
                    tasks.add(new Task(rs.getInt("task_id"), rs.getString("task_name"),
                            rs.getString("task_status"), LocalDate.parse(rs.getDate("task_deadline").toString()), performer));
                } catch (SQLException e) {
                    BaseUI.showMessage("При выбранной сортировке задач нет.");
                 }
            }
        return tasks;
    }

    public static Task getOneFull(int id) {        
        User autor = null, performer = null;

        // создаем объет ResultSet и инициализируем его ответом из метода
        ResultSet rs = TaskDB.getOneFull(id);
        Task task = null; 

        try {
            if (!rs.next()) {
                TaskUI.showMessage("Не удалось загрузить задачу. Убедитесь в правильности введенного ID.");
                TaskUI.showOneFull(-1);
            } else {               
                autor = new User(rs.getInt("autor_id"), rs.getString("autor_nickname"));
                performer = new User(rs.getInt("performer_id"), rs.getString("performer_nickname"));
                task = new Task(rs.getInt("task_id"), rs.getString("task_name"),
                        rs.getString("task_body"), rs.getString("task_status"),
                        LocalDateTime.parse(rs.getTimestamp("task_created_at").toString(), Util.formatterToLocalDateTime),
                        LocalDate.parse(rs.getDate("task_deadline").toString()), autor, performer);             
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
