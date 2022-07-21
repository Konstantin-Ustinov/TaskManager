package ConsoleUI.UI;

import java.util.ArrayList;

import Entities.User;
import Services.UserService;

public class UserUI extends BaseUI {

    public static void mainMenu() {
        String input;
        boolean isShowMenu = true;

        System.out.println("Добро пожаловать в Менеджер Пользователей!");

        do {
            showMessage(message);
            message = "";
            System.out.println("_________________");
            System.out.println("Введите комманду:");
            System.out.println(" \"1\" - Показать всех Пользователей;");
            System.out.println(" \"2\" - Добавить Пользователя;");
            System.out.println(" \"3\" - Удалить Пользователя;");
            System.out.println(" \"4\" - Изменить Пользователя;");
            System.out.println(" \"0\" - В начало.");

            input = scanner.next();

            switch (input) {
                case "1" -> showAll();
                case "2" -> add();
                case "3" -> delete();               
                case "4" -> update();               
                case "0" -> isShowMenu = false;
                default -> System.out.println("Вы ввели комманду: " + input + "такой комманды нет. Выберете комманду из списка");
            }
        } while (isShowMenu);

    }

    public static void add() {
        String nickname;

        System.out.println("Добавление пользователя \n -----------------");
        
        nickname = enterNickname();

        User newUser = new User(nickname);
        boolean answer = UserService.add(newUser); // Вызываем сатичный метод добавления пользователя

        if (answer) {
            message = "Пользователь успешно добавлен.";
        }
    }
   
    public static void delete() {
        String input;
        String nickname;         

        System.out.println("Удаление пользователя \n -----------------");
        
        nickname = enterNickname();

        System.out.println("Точно удалить пользователя " + nickname + "?");
        System.out.println("1 - да; 2 - нет");
        input = scanner.next();
        if (input.equals("1")) {
            if (UserService.delete(nickname)) {
                showMessage("Пользователь успешно удален.");
            } else {
                showMessage("Не удалось удалить пользователя. Убедитесь в правильности введенного nickname.");
                delete();
            }
        }
    }
   
    public static void update() {
        String nickname;         
        String newNickname;
                

        System.out.println("Изменение пользователя \n -----------------");
        System.out.println("Кому меняем nickname?");
        
        nickname = enterNickname();

        User user = UserService.getUser(nickname);
        
        System.out.println("Новый nickname?");
        
        newNickname = enterNickname();

        user.setNickname(newNickname);

        if (UserService.update(user)) {
            showMessage("Пользователь успешно изменен.");
        } else {
            showMessage("Не удалось изменить пользователя. Убедитесь в правильности введенного nickname.");
            delete();
        }
        
    }
   
    public static void showAll() {

        ArrayList<User> users = UserService.getAll();
        
        int num = 1;

        showMessage("Показаны все пользователи:");
        for (User user:users) {
            System.out.println(num + "). " + user.getNickname());
            num++;
        }
    }

    public static String enterNickname() {
        String nickname;
        //Scanner scan = new Scanner(System.in);
        //scanner.nextLine(); // Очищаем поток ввода
        while (true) {
            System.out.println("Введите nickname:");
            nickname = scanner.nextLine();
            if (nickname.equals("")) {
                System.out.println("Поле не может быть пустым.");
            } else {
                break;
            }
        } 
        return nickname; 
    }
    
}
