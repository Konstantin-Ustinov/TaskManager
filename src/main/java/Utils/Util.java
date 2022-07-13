package Utils;

import java.time.format.DateTimeFormatter;

public class Util {
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    // Задаем шаблон Timestamp из БД в LocalDateTime
    public static DateTimeFormatter formatterToLocalDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.n");

    public enum TaskStatuses {COMPLETED, NOT_COMPLETED};
    public enum TaskListSortColumns {CREATE, DEADLINE, COMPLETED, NOT_COMPLETED, OVERDUE};
}
