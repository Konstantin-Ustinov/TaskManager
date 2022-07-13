package DB;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

//import org.jvnet.inflector.Pluralizer;
//import net.java.dev.inflector.*;
import org.javalite.common.*;

import static DB.ServiseDB.*;
import Entities.*;

public class BaseDB {
    public static void setUpdatedAt(Base object) {
        String tableName = object.getClass().toString().toLowerCase(); // получаем назавание класса строкой в нижнем регистре
        // поскольку имя класса содержит в себе и пакеты то ищем последнюю точку в названии и плюсуем 1 что бы встать на следующую позицию
        int indexOfDot = tableName.lastIndexOf(".") + 1; 
        tableName = tableName.substring(indexOfDot, tableName.length()); // берем подстроку только с названием класса
        tableName = Inflector.pluralize(tableName); // преобразовываем в множественное число
        PreparedStatement stmt;
        try { // пробуем отправить SQL запрос
            stmt = connection.prepareStatement("UPDATE public.\"" + tableName + "\" SET \"updated_at\" = ? WHERE \"id\" = ?"); // переменной присваиваем результат метода с запросом
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(2, object.getId());
            stmt.execute(); // После того как весь запрос составлен, отправляем запрос в бвзу
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Не удалось поменять поле udated_at");           
        }
    }
}
