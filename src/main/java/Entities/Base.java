package Entities;

import java.time.LocalDateTime;

public class Base {

    int id;
    LocalDateTime created_at, updated_at;

    public int getId() {return id;}
    public LocalDateTime getcreated_at() {return created_at;}
    public LocalDateTime getupdated_at() {return updated_at;}
}
