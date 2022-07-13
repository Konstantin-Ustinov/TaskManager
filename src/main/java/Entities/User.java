package Entities;

public class User extends Base {
    
    private String nickname;

    //Конструктор при добавлении нового пользователя
    public User(String nickname) {
        this.nickname = nickname;
    }

    //Конструктор при чтении пользователя из базы
    public User(int id, String nickname) {
        this(nickname);
        this.id = id;
    }

    public int getId() {return this.id;}
    public String getNickname() {return this.nickname;}

    public void setNickname(String nickname) {this.nickname = nickname;}

    @Override
    public String toString() {
        return "ID: " + this.id + "\nNickname: " + this.nickname;
    }
}
