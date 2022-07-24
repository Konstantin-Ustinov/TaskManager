package ConsoleUI.UI;

import java.util.Scanner;

public class BaseUI {        
    public static String message = "";
    public static Scanner scanner = new Scanner(System.in);

    public static void showMessage(String message) {
        if (!message.equals("")) {
            for (int i = 0; i < message.length() + 4; i++) {
                System.out.print("-");
            }

            System.out.println("\n| " + message + " |");

            for (int i = 0; i < message.length() + 4; i++) {
                System.out.print("-");
            }
            System.out.print("\n");
        }
    }

}
