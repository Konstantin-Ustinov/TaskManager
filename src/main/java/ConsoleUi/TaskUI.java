package ConsoleUi;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.sql.ResultSet;
import java.sql.SQLException;

import Entities.Task;
import DB.TaskDB;
import Utils.Util;
import Utils.Util.TaskListSortColumns;
import TaskManager.TaskManager;

public class TaskUI extends BaseUI {

    public static TaskListSortColumns sort = TaskListSortColumns.DEADLINE;

    public static void mainMenu() {
        String input; 
        boolean isShowMenu = true;       

        System.out.println("Добро пожаловать в Менеджер задач!");

        do {
            showAll(sort);
            showMessage(message);
            message = "";
            System.out.println("_________________");
            System.out.println("Введите комманду:");
            System.out.println(" \"1\" - Показать задачу;");
            System.out.println(" \"2\" - Добравить задачу;");
            System.out.println(" \"3\" - Удалить задачу;");
            System.out.println(" \"4\" - Сортировка по дате Добавления;");
            System.out.println(" \"5\" - Сортировка по дате Завершения (по умолчанию);");
            System.out.println(" \"6\" - Только Выполненные задачи;");
            System.out.println(" \"7\" - Только Невыполненные задачи;");
            System.out.println(" \"8\" - Только Просроченные задачи;");
            System.out.println(" \"0\" - В начало.");

            input = scanner.next();

            switch (input) {
                case "1" -> showOneFull(-1);
                case "2" -> add();
                case "3" -> delete(-1); // аргумент -1 для того чтобы запустить код по вводу ID в методе
                case "4" -> sort = TaskListSortColumns.CREATE;
                case "5" -> sort = TaskListSortColumns.DEADLINE;
                case "6" -> sort = TaskListSortColumns.COMPLETED;
                case "7" -> sort = TaskListSortColumns.NOT_COMPLETED;
                case "8" -> sort = TaskListSortColumns.OVERDUE;
                case "0" -> isShowMenu = false;
                default -> System.out.println("Вы ввели комманду: " + input + "такой комманды нет. Выберете комманду из списка");
            }
        } while (isShowMenu);

    }
    
    public static void add() {
        String taskName;
        String taskBody;
        String taskStatus = "not_completed";
        LocalDateTime createDate = LocalDateTime.now();
        LocalDate deadline = null; // Ставим NULL чтобы запустить цикл по валидации ввода дедлайна

        System.out.println("Добавление задачи \n -----------------");
        System.out.println("Введите название задачи:");

        scanner.nextLine(); //Очищаем поток ввода

        while (true) {
            taskName = scanner.nextLine();
            if (taskName.equals("")) {
                System.out.println("Поле не может быть пустым");
            } else {
                break;
            }
        }

        System.out.println("Введите описание задачи:");

        while (true) {
            taskBody = scanner.nextLine();
            if (taskBody.equals("")) {
                System.out.println("Поле не может быть пустым");
            } else {
                break;
            }
        }

        deadline = enterDeadline();

        Task newTask = new Task(taskName, taskBody, taskStatus, createDate, deadline);
        boolean answer = TaskDB.add(newTask); // Вызываем сатичный метод добавления задачи

        if (answer) {
            message = "Задача успешно добавлена";
        }
    }

    public static void delete(int taskId) {
        String input;
        int id;

        if (taskId == -1) {
            id = enterId();
        } else {
            id = taskId;
        }

        System.out.println("Точно удалить задачу с ID = " + id + "?");
        System.out.println("1 - да; 2 - нет");
        input = scanner.next();
        if (input.equals("1")) {
            if (TaskDB.delete(id)) {
                showMessage("Задача успешно удалена.");
            } else {
                showMessage("Не удалось удалить задачу. Убедитесь в правильности введенного ID.");
                delete(-1);
            }
        }
    }

    public static void showAll(TaskListSortColumns sortIn) {
        String listName = "";

        switch (sortIn) {
            case DEADLINE -> {
                //sort = "deadline";
                listName = "Сортировка по дате Завершения:";
            }
            case CREATE -> {
                listName = "Сортировка по дате Добавления:";
                //sort = "create";
            }
            case COMPLETED -> {
                listName = "Только Выполненные задачи:";
                //sort = "completed";
            }
            case NOT_COMPLETED -> {
                listName = "Только Невыполненные задачи:";
                //sort = "not_completed";
            }
            case OVERDUE -> {
                listName = "Только Просроченные задачи:";
                //sort = "all";
            }
        }

        showMessage(listName);

        ArrayList<Task> tasks = new ArrayList<>(); // Создаем списочный массив объектов Task
        ResultSet rs = TaskDB.getAll(TaskManager.sort); // создаем объет ResultSet и инициализируем его ответом из метода        

            while (true) {
                try {
                    if (!rs.next()) {
                        break;
                    }
                 // Пока есть записи выводим их
                    tasks.add(new Task(rs.getInt("id"), rs.getString("name"),
                            rs.getString("status"), LocalDate.parse(rs.getDate("deadline").toString())));
                } catch (SQLException e) {
                    showMessage("При выбранной сортировке задач нет.");
                 }
            }

            int i = 1;
            for (Task task : tasks) {
                if (i != 1) {
                    System.out.println("------------------");
                }
                System.out.print(i + ") ");
                System.out.println(task.showShortTask());
                i++;
            }
    }

    public static void showOneFull(int taskId) {
        int id;

        if (taskId == -1) {
            id = enterId();
        } else {
            id = taskId;
        }

        // создаем объет ResultSet и инициализируем его ответом из метода
        ResultSet rs = TaskDB.getOneFull(id);
        Task task = null; // Создаем объект

        try {
            if (!rs.next()) {
                showMessage("Не удалось загрузить задачу. Убедитесь в правильности введенного ID.");
                showOneFull(-1);
            } else {
                // Инициализируем объект
                task = new Task(rs.getInt("id"), rs.getString("name"),
                        rs.getString("body"), rs.getString("status"),
                        LocalDateTime.parse(rs.getTimestamp("created_at").toString(), Util.formatterToLocalDateTime),
                        LocalDate.parse(rs.getDate("deadline").toString()));

                System.out.println(task.toString());
                menu(task);
            }
        } catch (SQLException e) {
            System.out.println("Объект task не создан.");
        }
    }

    public static void setStatus(Task task) {

        if (task.getStatus().equals("not_completed")) {
            if(TaskDB.setStatus(task, Util.TaskStatuses.COMPLETED)) {
                message = "Статус изменен на \"Завершена\"";
            } else {
                message = "Статус не изменен.";
            }
        } else  if (task.getStatus().equals("completed")) {
            if (TaskDB.setStatus(task, Util.TaskStatuses.NOT_COMPLETED)) {
                message = "Статус изменен на \"В работе\"";
            } else {
                message = "Статус не изменен.";
            }
        }
    }

    public static void update(Task task) {
        Task changedTask = task;
        String input;
        LocalDate deadline = null;

        System.out.println("Введите новое название:");
        input = scanner.nextLine();
        changedTask.setName(input);

        System.out.println("Введите новое описание:");
        input = scanner.nextLine();
        changedTask.setBody(input);

        deadline = enterDeadline();

        changedTask.setDeadline(deadline);

        if (TaskDB.update(changedTask)) {
            showMessage("Задача успешно изменена!");
        } else {
            showMessage("Задача не была изменена :(");
        }

        showOneFull(changedTask.getId());
    }

    public static LocalDate enterDeadline() {
        String inputDeadline = "";

        do { // цикл по вводу даты дедлайна с валидацией

            System.out.println("Введите дату завершения задачи (День.Месяц.Год) Пример: 31.01.2022:");
            inputDeadline = scanner.nextLine();

            if (inputDeadline.matches("[0-3]\\d\\.[0-1]\\d\\.[2-9]\\d\\d\\d")) { //Проверем дату по регулярному выражению
                return LocalDate.parse(inputDeadline, Util.formatter); // переводим строку в дату
            } else {
                System.out.println("Поле должно быть в формате (День.Месяц.Год) и не может быть пустым");
            }

        } while (true); // выполняем цикл пока не введена правильно дата дедлайна
    }

    public static void menu(Task task) {
        String input;
        boolean showMenu = true;

        while (showMenu) {
            System.out.println("____________");
            System.out.println("Меню задачи:");
            System.out.println(" \"1\" - Изменить задачу;");
            System.out.println(" \"2\" - Удалить задачу;");

            if (task.getStatus().equals("not_completed")) {
                System.out.println(" \"3\" - Завершить задачу;");
            } else if (task.getStatus().equals("completed")){
                System.out.println(" \"3\" - Вернуть задачу в работу;");
            }

            System.out.println(" \"0\" - Вернуться назад.");

            input = scanner.next();

            switch (input) {
                case "1" -> {update(task);
                    showMenu = false;}
                case "2" -> {
                    delete(task.getId());
                    showMenu = false; // После удаления задачи возвращаемся к списку с главным меню
                }
                case "3" -> {setStatus(task);
                    showMenu = false;}
                case "0" -> showMenu = false;
                default -> System.out.println("Вы ввели комманду: " + input
                        + "такой комманды нет. Выберете комманду из списка");
            }
        }
    }
    
    public static int enterId() {
        int taskId = -1;        

        while (taskId == -1) {
            System.out.println("Введите ID задачи:");

            try {
                taskId = scanner.nextInt();
                if (taskId <= 0) {
                    System.out.println("Введите положительное число");
                    taskId = -1;
                }
            } catch (InputMismatchException e) {
                System.out.println("Введите число");
                scanner.next(); // Очищаем поток ввода что бы не было зацикливания
            }
        }
        return taskId;
    }

}