package ConsoleUI;

import java.util.*;

import ConsoleUI.UI.BaseUI;
import ConsoleUI.UI.TaskUI;
import ConsoleUI.UI.UserUI;
import Utils.Util.TaskListSortColumns;

public class Main {
    public static TaskListSortColumns sort = TaskListSortColumns.DEADLINE;
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        String input;        

        System.out.println("Добро пожаловать!");

        do {
            BaseUI.showMessage(BaseUI.message);
            BaseUI.message = "";
            System.out.println("_________________");
            System.out.println("Выберете, что вы хотите сделать:");
            System.out.println(" \"1\" - Открыть Менеджер Задач;");
            System.out.println(" \"2\" - Открыть Менеджер Пользователей;");
            System.out.println(" \"0\" - Выход.");

            input = scanner.next();

            switch (input) {
                case "1" -> TaskUI.mainMenu();
                case "2" -> UserUI.mainMenu();                
                case "0" -> System.exit(0);
                default -> System.out.println("Вы ввели комманду: " + input + "такой комманды нет. Выберете комманду из списка");
            }
        } while (true);

    }
}
