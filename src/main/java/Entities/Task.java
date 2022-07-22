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
    private User autor;
    private User performer;

    // Базовый конструктор
    public Task(String name, String status, LocalDate deadline) {
        this.name = name;
        this.status = status;
        this.deadLine = deadline;
    }

    // Конструктор для вывода задач кратким списоком
    public Task(int id, String name, String status, LocalDate deadline, User performer) {
        this(name, status, deadline);
        this.id = id;
        this.performer = performer;
    }

    // Конструктор для добавления задачи в базу
    public Task(String name, String body, String status, LocalDateTime createDate, LocalDate deadline, User autor, User performer) {
        this(name, status, deadline);
        this.body = body;
        this.createDate = createDate;
        this.autor = autor;
        this.performer = performer;
    }

    // Конструктор для всех полей задачи
    public Task(int id, String name, String body, String status, LocalDateTime createDate, LocalDate deadline, User autor, User performer) {
        this(name, body, status, createDate, deadline, autor, performer);
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

    public String getAutorNickname() {
        return this.autor.getNickname();
    }

    public String getPerformerNickname() {
        return this.performer.getNickname();
    } 

    public int getAutorId() {
        return this.autor.getId();
    }

    public int getPerformerId() {
        return this.performer.getId();
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
        String performerNickname = (performer != null && getPerformerNickname() != null) ? getPerformerNickname() : "Не назначен";
        String autorNickname = (autor != null && getAutorNickname() != null) ? getAutorNickname() : "Не назначен";

        return "ID задачи: " + id +
                "\nНазвание: " + name +
                "\nОписание: " + body +
                "\nДата создания: " + createDate.atZone(ZoneId.systemDefault()).format(formatter) +
                "\nЗавершить до: " + deadLine.format(formatter) +
                "\nСтатус: " + translateStatus() +
                "\nСоздатель: " + autorNickname + 
                "\nИсполнитель: " + performerNickname;
    }

    public String showShortTask() {
        String performerNickname = (performer != null && getPerformerNickname() != null) ? getPerformerNickname() : "Не назначен";

        return "ID задачи: " + id + "\nНазвание: " + name +
                "; Завершить до: " + deadLine.format(formatter) +
                "; Статус: " + translateStatus() + 
                "; Исполнитель: " + performerNickname;
    }

    String translateStatus() {
        return (this.status.equals("completed")) ? "Завершена" : "В работе";
    }
}
