package Entities;

import static Utils.Util.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Task extends Base {

    private String name;
    private String body;
    private String status;
    private LocalDateTime createDate;
    private LocalDate deadLine;
    private User creator;
    private User executor;

    // Базовый конструктор
    public Task(String name, String status, LocalDate deadline) {
        this.name = name;
        this.status = status;
        this.deadLine = deadline;
    }

    // Конструктор для вывода задач кратким список
    public Task(int id, String name, String status, LocalDate deadline, User executor) {
        this(name, status, deadline);
        this.id = id;
        this.executor = executor;
    }

    // Конструктор для добавления задачи в базу
    public Task(String name, String body, String status, LocalDateTime createDate, LocalDate deadline, User creator, User executor) {
        this(name, status, deadline);
        this.body = body;
        this.createDate = createDate;
        this.creator = creator;
        this.executor = executor;
    }

    // Конструктор для всех полей задачи
    public Task(int id, String name, String body, String status, LocalDateTime createDate, LocalDate deadline, User creator, User executor) {
        this(name, body, status, createDate, deadline, creator, executor);
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getBody() {
        return this.body;
    }

    public String getStatus() {
        return this.status;
    }

    public LocalDateTime getCreateDate() {
        return this.createDate;
    }

    public LocalDate getDeadLine() {
        return this.deadLine;
    }

    public String getCreator() {
        return this.creator.getNickname();
    }

    public String getExecutor() {
        return this.executor.getNickname();
    } 

    public int getCreatorId() {
        return this.creator.getId();
    }

    public int getExecutorId() {
        return this.executor.getId();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadLine = deadline;
    }

    @Override
    public String toString() {
        String executorNickname = (executor != null) ? getExecutor() : "Не назначен";
        String creatorNickname = (creator != null) ? getCreator() : "Не назначен";

        return "ID задачи: " + id +
                "\nНазвание: " + name +
                "\nОписание: " + body +
                "\nДата создания: " + createDate.atZone(ZoneId.systemDefault()).format(formatter) +
                "\nЗавершить до: " + deadLine.format(formatter) +
                "\nСтатус: " + translateStatus() +
                "\nСоздатель: " + creatorNickname + 
                "\nИсполнитель: " + executorNickname;
    }

    public String showShortTask() {
        String executorNickname = (executor != null) ? getExecutor() : "Не назначен";

        return "ID задачи: " + id + "\nНазвание: " + name +
                "; Завершить до: " + deadLine.format(formatter) +
                "; Статус: " + translateStatus() + 
                "; Исполнитель: " + executorNickname;
    }

    String translateStatus() {
        return (this.status.equals("completed")) ? "Завершена" : "В работе";
    }
}
