package DB;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import Entities.Task;
import Utils.Util.TaskListSortColumns;
import Utils.Util.TaskStatuses;
import static DB.ServiceDB.*;

public class TaskDB extends BaseDB {    

    public static boolean add(Task newTask) {
        DBConnect();

        PreparedStatement stmt;
            try { // пробуем отправить SQL запрос
                stmt = connection.prepareStatement("INSERT INTO public.\"tasks\"(\"name\", \"body\", \"created_at\", \"updated_at\", \"deadline\", \"status\"," + 
                        " \"performer_id\", \"autor_id\") VALUES(?, ?, ?, ?, ?, ?, ?, ?)"); // переменной присваиваем результат метода с запросом
                stmt.setString(1, newTask.getName()); // подставляем значение переменной вместо первого знака вопроса
                stmt.setString(2, newTask.getBody());
                stmt.setTimestamp(3, Timestamp.valueOf(newTask.getcreated_at()));
                stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                stmt.setDate(5, Date.valueOf(newTask.getDeadLine()));
                stmt.setString(6, newTask.getStatus());
                stmt.setInt(7, newTask.getPerformer().getId());
                stmt.setInt(8, newTask.getAutor().getId());
                stmt.execute(); // После того как весь запрос составлен, отправляем запрос в бвзу
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Не удалось добавить задачу. Попробуйте еще раз.");
            }

        DBDisconnect();
        return false;
    }

    public static boolean delete(int taskId) {
        boolean answer;
        int delAnswer;
        DBConnect();

        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement("DELETE FROM public.\"tasks\" WHERE \"id\"= ?");
            stmt.setInt(1, taskId);
            delAnswer = stmt.executeUpdate();
            answer = (delAnswer != 0)?true:false;
        } catch (SQLException e) {
            answer = false;
        }
        DBDisconnect();
        return answer;
    }

    public static ResultSet getAllBySort(TaskListSortColumns sort) {
        ResultSet rs = null;
        String sql = "SELECT task.id as task_id, task.name as task_name,  task.status as task_status, task.deadline as task_deadline," + 
                        " performer.nickname as performer_nickname FROM public.\"tasks\" as task LEFT JOIN public.\"users\"" +
                        " as performer ON task.performer_id = performer.id";
        switch (sort) {
            case DEADLINE -> sql += " WHERE task.deadline >= '" + Date.valueOf(LocalDate.now()) + "' ORDER BY task.deadline, task.id";
            case CREATE -> sql += "WHERE task.deadline >= '" + Date.valueOf(LocalDate.now()) +  "' ORDER BY task.created_at, task.id";
            case COMPLETED -> sql += " WHERE task.status = 'completed' AND task.deadline >= '" + Date.valueOf(LocalDate.now()) + "'";
            case NOT_COMPLETED -> sql += " WHERE task.status = 'not_completed' AND task.deadline >= '" + Date.valueOf(LocalDate.now()) + "'";
            case OVERDUE -> sql += " WHERE task.deadline < '" + Date.valueOf(LocalDate.now()) + "' ORDER BY task.deadline, task.id";
        }

        DBConnect();

        Statement stmt;

        try {
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println(sql);
            System.out.println("Не удалось загрузить задачи. Ошибка запроса.");
        }

        DBDisconnect();
        return rs;
    }

    public static ResultSet getOneFull(int taskId) {
        String sql = "SELECT task.id as task_id, task.name as task_name, task.body as task_body, task.status as task_status, task.created_at as task_created_at," +
                            " task.deadline as task_deadline, performer.id as performer_id, performer.nickname as performer_nickname," +
                            " autor.id as autor_id, autor.nickname as autor_nickname FROM public.\"tasks\" as task" + 
                            " LEFT JOIN public.\"users\" as performer ON task.performer_id = performer.id LEFT JOIN public.\"users\" as autor" + 
                            " ON task.autor_id = autor.id WHERE task.id = " + taskId;
                            

        DBConnect();

        Statement stmt;
        ResultSet rs = null;

        try {
             stmt = connection.createStatement();
             rs = stmt.executeQuery(sql);
         } catch (SQLException e) {
             System.out.println("Не удалось загрузить задачу.");
         }

        DBDisconnect();
        return rs;
    }

    public static boolean setStatus(Task task, TaskStatuses status) {

        DBConnect();

        PreparedStatement stmt;
        try {
            connection.setAutoCommit(false);
            stmt = connection.prepareStatement("UPDATE public.\"tasks\" SET \"status\" = ? WHERE \"id\" = ?");
            stmt.setString(1, status.toString().toLowerCase());
            stmt.setInt(2, task.getId());
            stmt.executeUpdate();
            setUpdatedAt(task);
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println("Не удалось создать переменную выражение");
            throw new RuntimeException(e);
        }

        DBDisconnect();

        return true;
    }

    public static boolean update(Task task) {
        DBConnect();

        PreparedStatement stmt;
        try {
            connection.setAutoCommit(false);
            stmt = connection.prepareStatement("UPDATE public.\"tasks\" SET \"name\" = ?, \"body\" = ?, \"deadline\" = ? WHERE \"id\" = ?");
            stmt.setString(1, task.getName());
            stmt.setString(2, task.getBody());
            stmt.setDate(3, Date.valueOf(task.getDeadLine()));
            stmt.setInt(4, task.getId());
            stmt.executeUpdate();
            setUpdatedAt(task);
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println("Не удалось создать переменную выражение");
            throw new RuntimeException(e);
        }
        DBDisconnect();

        return true;
    }
   
}
